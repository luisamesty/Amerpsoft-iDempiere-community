package org.amerp.amnmodel;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.amerp.workflow.amwmodel.MAMW_WF_Node;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MAMN_Leaves  extends X_AMN_Leaves implements DocAction, DocOptions {

	private static final long serialVersionUID = -7169854017581281267L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Leaves.class);
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;
	/* Is reactivating isRecativating  **/
	private boolean		isReactivating = false;
	/** Persistent Object			*/
	private PO					m_po = null;
	
	public static final String AMN_Leaves_None = "--";
	public static final String AMN_Leaves_Draft = "DR"; 		//	DR Draft - Borrador	Emitida por el Usuario, o en condición de Devuelta
	public static final String AMN_Leaves_Supervisor = "SU"; 	//	SU  Revisión Supervisor	En revisión por el Supervisor del Trabajador
	public static final String AMN_Leaves_Rejected = "SR"; 		//	SR Solicitud Rechazada	Rechazada por el Supervisor del Trabajador
	public static final String AMN_Leaves_HHRR = "RH"; 			//	RH Revisión Recursos Humanos	En Revisión por Recursos Humanos
	public static final String AMN_Leaves_HHRR_Rejected = "RR";	//	RR Rechazada RRHH	Rechazada por Recursos Humanos
	public static final String AMN_Leaves_Filed = "AR";			//	AR Archivo.	Archivo
	public static final String AMN_Leaves_Complete = "CO";		// 	CO Completada	Completada
	public static final String AMN_Leaves_Close = "CL";			// 	CL Closed 
		
	/** Drafted = DR */
	private static final String DOCSTATUS_Drafted = "DR";

	private static final String DOCACTION_Prepare = "PR";

	private static final String DOCACTION_Close = "CL";
	
	public MAMN_Leaves(Properties ctx, int AMN_Leaves_ID, String trxName) {
		super(ctx, AMN_Leaves_ID, trxName);
		// 
	}
	
	public MAMN_Leaves(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	/**
	 * createLeaveRecord
	 * @param ctx
	 * @param p_C_DocTypeTarget_ID
	 * @param p_AMN_Employee_ID
	 * @param p_AMN_Leaves_types_ID
	 * @param leaveDays
	 * @param p_AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static MAMN_Leaves createLeaveRecord(Properties ctx, 
			MAMN_Leaves newLeave, String trxName) {
	

		MAMN_Leaves leave = new MAMN_Leaves(ctx, 0, trxName);
		leave.setC_DocTypeTarget_ID(newLeave.getC_DocTypeTarget_ID());
		leave.setAMN_Employee_ID(newLeave.getAMN_Employee_ID());
		leave.setAMN_Leaves_Types_ID(newLeave.getAMN_Leaves_Types_ID());
		leave.setDateFrom(newLeave.getDateFrom());
		leave.setDateTo(newLeave.getDateTo());
		leave.setDaysTo(newLeave.getDaysTo());
		leave.setAMN_Payroll_ID(newLeave.getAMN_Payroll_ID());
		leave.setProcessed(false);
		leave.saveEx();
		log.info("Leave record created successfully: " + leave.get_ID());
		return leave;
	}

	/**
	 * createLeaveFromPayroll
	 * @param ctx
	 * @param payrollID
	 * @param startDate
	 * @param daysTo
	 * @param trxName
	 * @return
	 */
	public static MAMN_Leaves createLeaveFromPayroll(Properties ctx, int p_AMN_Payroll_ID, Timestamp startDate, BigDecimal daysTo, String trxName) {
	        
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(), null);
		MAMN_Shift shift = new MAMN_Shift();

		int AMN_Shift_ID = amnemployee.getAMN_Shift_ID();
		if (AMN_Shift_ID == 0)
			AMN_Shift_ID = shift.sqlGetDefaultAMN_Shift_ID(amnpayroll.getAD_Client_ID());
		boolean isSaturdayBusinessDay = shift.isSaturdayBusinessDay(amnemployee.getAMN_Shift_ID());

		int AMN_Leaves_Types_ID = MAMN_Leaves_Types.getLeavesTypesID(ctx, trxName);
		MAMN_Leaves_Types leavetype = new MAMN_Leaves_Types(Env.getCtx(), AMN_Leaves_Types_ID, null);
		String DaysMode = leavetype.getDaysMode();

		Timestamp AMNLeavesEndDate;
		if (DaysMode.compareToIgnoreCase("B") == 0) {
			AMNLeavesEndDate = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, startDate, daysTo,
					amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
		} else {
			AMNLeavesEndDate = MAMN_NonBusinessDay.getNextCalendarDay(startDate, daysTo,
					amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
		}

		if (daysTo == null || daysTo.compareTo(BigDecimal.ZERO) <= 0) {
			log.warning("No leave days available");
			return null;
		}

		MAMN_Leaves leave = new MAMN_Leaves(ctx, 0, trxName);
		leave.setC_DocTypeTarget_ID(MAMN_Leaves.getPayrollLeavesDocTypeID(ctx, trxName));
		leave.setAMN_Employee_ID(amnpayroll.getAMN_Employee_ID());
		leave.setAMN_Leaves_Types_ID(AMN_Leaves_Types_ID);
		leave.setDateFrom(startDate);
		leave.setDaysTo(daysTo);
		leave.setDateTo(AMNLeavesEndDate);
		leave.setAMN_Payroll_ID(p_AMN_Payroll_ID);
		leave.setDescription(amnpayroll.getDescription());
		leave.setProcessed(false);
		leave.setAMW_WorkFlow_ID(getLeavesWorkFlowID());
		leave.saveEx();
		log.info("Leave record created successfully: " + leave.get_ID());
		return leave;

	}
	 
	/**
	 * getPayrollLeavesDocTypeID
	 * @param ctx
	 * @param trxName
	 * @return
	 */
	public static int getPayrollLeavesDocTypeID(Properties ctx, String trxName) {
        String sql = "SELECT DISTINCT C_DocType_ID FROM C_DocType WHERE AD_Client_ID = ? AND name = 'Payroll Leaves'";
        int docTypeID = 0;
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setInt(1, Env.getAD_Client_ID(ctx));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    docTypeID = rs.getInt("C_DocType_ID");
                }
            }
        } catch (Exception e) {
            log.severe("Error retrieving C_DocType_ID: " + e.getMessage());
        }
        return docTypeID;
    }
	
	/**
	 * Obtiene el AMW_WorkFlow_ID asociado a la tabla AMN_Leaves.
	 * @return AMW_WorkFlow_ID o 0 si no se encuentra.
	 */
	public static int getLeavesWorkFlowID() {
	    String sql = "SELECT DISTINCT AMW_WorkFlow_ID " +
	                "FROM AMW_WorkFlow " +
	                "WHERE AD_Table_ID = (SELECT AD_Table_ID FROM AD_Table WHERE TableName=?)";

	    int workflowID = DB.getSQLValueEx(null, sql, "AMN_Leaves"); // "AMN_Leaves" es el nombre de la tabla

	    // Si no se encuentra un workflow, devuelve 0
	    return workflowID > 0 ? workflowID : 0;
	}
	
	/**
	 * Busca todos los registros en la tabla AMN_Leaves basados en AMN_Payroll_ID.
	 * @param AMN_Payroll_ID El ID de la nómina (AMN_Payroll_ID) a buscar.
	 * @return Una lista de instancias de MAMN_Leaves que coinciden con el AMN_Payroll_ID.
	 */
	public static List<MAMN_Leaves> findAllByPayrollID(int AMN_Payroll_ID) {
	    String sql = "SELECT AMN_Leaves_ID FROM AMN_Leaves WHERE AMN_Payroll_ID = ?";
	    List<MAMN_Leaves> leavesList = new ArrayList<>();

	    // Ejecuta la consulta y obtiene todos los AMN_Leaves_ID
	    int[] AMN_Leaves_IDs = DB.getIDsEx(null, sql, AMN_Payroll_ID);

	    // Crea instancias de MAMN_Leaves para cada ID encontrado
	    for (int AMN_Leaves_ID : AMN_Leaves_IDs) {
	        if (AMN_Leaves_ID > 0) {
	            MAMN_Leaves amnLeaves = new MAMN_Leaves(Env.getCtx(), AMN_Leaves_ID, null);
	            leavesList.add(amnLeaves);
	        }
	    }

	    return leavesList;
	}
	
	@Override
	public boolean processIt(String action) throws Exception {
		log.warning("===============processIt================================");
		if (isReactivating) {
			setProcessed(false);
			setDocStatus(DOCSTATUS_Drafted);
			isReactivating = false;
			return true;
		}
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (action, getDocAction());
	}

	@Override
		public boolean reActivateIt() {
		log.warning("===============reActivateIt================================");
		isReactivating= true;
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
		
		//MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
		setProcessed(false);
		setIsApproved(false);
		setDocStatus(DOCSTATUS_Drafted);
		setDocAction(DOCACTION_Prepare);
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		return true;
		}


	@Override
	public String prepareIt() {
		log.warning("===============prepareIt================================");
		return DocAction.STATUS_InProgress;
	}

	@Override
	public boolean unlockIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean approveIt() {
		log.warning("===============approveIt================================");
		setIsApproved(true);
		return false;
	}

	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String completeIt() {
		log.warning("===============completeIt================================");
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		if (log.isLoggable(Level.INFO)) log.info(toString());
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		//
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}

	@Override
	public boolean voidIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		//
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}

	@Override
	public String getDocumentNo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoc_User_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stubo
		return null;
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {
    	//log.warning("=====================customizeValidActions===========================");
    	if (options == null)
    		throw new IllegalArgumentException("Option array parameter is null");
    	if (docAction == null)
    		throw new IllegalArgumentException("Doc action array parameter is null");

    	// If a document is drafted or invalid, the users are able to complete, prepare or void
    	if (docStatus.equals(DocumentEngine.STATUS_Drafted) || docStatus.equals(DocumentEngine.STATUS_Invalid)) {
    		options[index++] = DocumentEngine.ACTION_Complete;
    		options[index++] = DocumentEngine.ACTION_Prepare;
    		options[index++] = DocumentEngine.ACTION_Void;

    		// If the document is already completed, we also want to be able to reactivate or void it instead of only closing it
    	} else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
    		options[index++] = DocumentEngine.ACTION_Void;
    		options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
    		options[index++] = DocumentEngine.ACTION_Reverse_Correct;
    		options[index++] = DocumentEngine.ACTION_ReActivate;
    	}

    	return index;

	}

	@Override
	public String getDocAction() {
		// 
		return this.getDocAction();
	}
	
	private void setDocAction(String docactionPrepare) {
		// 
		
	}
	
	/**
	 * getAMWWorkFlowNodeFromDocStatusSQL
	 *  Returns Work Fflow Node from a given Work Flow and Node Value DR,CO,CL...
	 * @param AMW_WorkFlow_ID
	 * @param DocStatus
	 * @return
	 */
	public MAMW_WF_Node getAMWWorkFlowNodeFromDocStatusSQL (int AMW_WorkFlow_ID, String DocStatus )
	{
		MAMW_WF_Node retValue = null;
		String sql= "SELECT DISTINCT *  "+
				" FROM AMw_WF_Node "+  
				" WHERE AMW_WorkFlow_ID=? "+ 
				" AND Value = '"+ DocStatus + "' " +
				" ORDER BY SeqNo ASC " ;
		ArrayList<MAMW_WF_Node> list = new ArrayList<MAMW_WF_Node>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, AMW_WorkFlow_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MAMW_WF_Node(getCtx(), rs, get_TrxName());
				//log.warning("MAMW_WF_Node Name:"+line.getName());
			}
		} 
		catch (Exception e)
		{
			log.severe(e.getMessage());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		return retValue;
	}
	
}
