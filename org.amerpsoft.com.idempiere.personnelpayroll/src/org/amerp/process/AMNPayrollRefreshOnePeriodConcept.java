package org.amerp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Concept_Types_Proc;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.amerp.amnutilities.AmerpPayrollCalc;
import org.amerp.amnutilities.AmerpPayrollCalcArray;
import org.amerp.amnutilities.PayrollScriptEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * Recalc One Period but Updating previous a Given Concept.
 * 
 */
public class AMNPayrollRefreshOnePeriodConcept extends SvrProcess{

	static CLogger log = CLogger.getCLogger(AMNPayrollRefreshOnePeriod.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Concept_Types_ID = 0;
	String Employee_Name,AMN_Process_Value="";
	String sql="";
	private String MessagetoShow = "";
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
    @Override
    protected void prepare() {
	    // TODO Auto-generated method stub
    	//log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
			// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
			// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
			if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Period_ID"))
				p_AMN_Period_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Concept_Types_ID"))
				p_AMN_Concept_Types_ID = para.getParameterAsInt();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
		//log.warning("...........Parametros...................");
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
    }


    /* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
    @Override
    protected String doIt() throws Exception {
	    // 
		AmerpPayrollCalcArray amerpPayrollCalcArray = new AmerpPayrollCalcArray();
	    String sql="";
	    String AMN_Process_Value="NN";
	    String Payroll_Name="";
	    String Msg_Value="";
	    int AMN_Payroll_ID=0;
		sql = "SELECT amn_process_value FROM amn_process WHERE amn_process_id=?" ;
		AMN_Process_Value = DB.getSQLValueString(null, sql, p_AMN_Process_ID).trim();
		// Message Value Init
		Msg_Value=Msg_Value+(Msg.getMsg(Env.getCtx(), "Process")+":"+AMN_Process_Value.trim())+" \n";
		IProcessUI processMonitor = Env.getProcessUI(getCtx());
		// ARRAY DOCS FOR EMPLOYEE - CONTRACT
		sql = "SELECT \n" + 
				"amnpr.amn_payroll_id, \n" + 
				"amnpr.amn_employee_id, \n" + 
				"amnpr.name as payroll_name  \n" + 
				"FROM  amn_payroll as amnpr \n" + 
				"WHERE amnpr.amn_period_id=? \n" + 
				"ORDER BY payroll_name"
				;        		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Period_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Payroll_ID= rs.getInt(1);
				Payroll_Name = rs.getString(3).trim();
				Msg_Value=(Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+Payroll_Name).trim()+" \r\n";
				MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, null);
				MAMN_Employee employee = new MAMN_Employee(getCtx(), amnpayroll.getAMN_Employee_ID(), null);
				//  PROCESS MAMN_Payroll (DOCUMENT HEADER)
				if (processMonitor != null) {
					MessagetoShow = Msg.translate(Env.getCtx(), "AMN_Payroll_ID")+":"+String.format("%-20s", amnpayroll.getDocumentNo().trim()).replace(' ', '_') +"-"+
					String.format("%-20s", employee.getValue().trim()).replace(' ', '_') +"-" +
					String.format("%-40s", employee.getName().trim()).replace(' ', '_') ;;
					processMonitor.statusUpdate(MessagetoShow);
				}
				//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
				//log.warning("MessagetoShow:"+MessagetoShow);
				if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
				{	
					// Recreate Payroll Line
					MAMN_Concept_Types_Proc amnc = new MAMN_Concept_Types_Proc(getCtx(), 0, null);
					// Default Reference for Process
					int AMN_Concept_Types_Proc_ID = amnc.sqlGetAMNConceptTypesProc_FromConcept(p_AMN_Concept_Types_ID, p_AMN_Process_ID);
					MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(Env.getCtx(), 0, null);
					amnpayrolldetail.createAmnPayrollDetail(getCtx(), null, amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID(), p_AMN_Process_ID, p_AMN_Contract_ID, AMN_Payroll_ID, AMN_Concept_Types_Proc_ID, get_TrxName());
					// Recalculates
			    	amerpPayrollCalcArray.PayrollEvaluationArrayCalculate(getCtx(), AMN_Payroll_ID);
				} else {
					Msg_Value=Msg_Value+" ** ALREADY PROCESSED - CAN'T BE RECALCULATED ** "+
							Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+Payroll_Name+" \r\n";
				}
				addLog(Msg_Value);
			}				
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return Msg_Value;
    }

}
