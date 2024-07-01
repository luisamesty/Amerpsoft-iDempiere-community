package org.amerp.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Schema;
import org.compiere.acct.Doc_GLJournal;
import org.compiere.acct.Doc_InOut;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionType;
import org.compiere.model.MDocType;
import org.compiere.model.MGLCategory;
import org.compiere.model.MInOut;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalLine;
import org.compiere.model.MPeriod;
import org.compiere.model.MProductCategoryAcct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNPayrollProvision   extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNPayrollProvision.class);
	private int p_C_Period_ID = 0;
	private int p_AD_Client_ID = 0;
	private String p_AMN_ProvisionType = "P";
	private int p_AD_Org_ID = 0;
	private int C_Period_ID = 0;
	private int Target_Currency_ID = 0;
	private int AMN_Employee_ID = 0;
	private int AMN_Schema_ID = 0;
	private int C_BPartner_ID = 0;
	private int p_TargetAcctSchema_ID = 0;
	private int C_AcctSchema_ID=0;
	private BigDecimal p_AMNProvisionFactor = BigDecimal.ZERO;
	private String Employee_Value ="";
	private String Employee_Name="";
	private String WorkForce ="";
	private String Msg_WorkForce="";
	private int PR_DocType_ID = 0;
	private int GL_DocType_ID = 0;
	private int C_ValidCombination_ID = 0;	// Valid Combination Provision Expense Account 
	private int C_VC_Provision_ID = 0; 	// Valid Combination Provision CxP Account
	private int Account_ID = 0;
	private String p_DocumentNo="";
	private String DocStatus ="'CO','CL','RE'";
	private int GL_Journal_ID=0;
	private BigDecimal provisionAmount = BigDecimal.ZERO;
	private String irpp ="";
	int lineNo = 10;
	private MJournal mjournal = null;
	private int	m_GL_Jounal_Window_ID = -1;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	   	//log.warning("........Here I'm in the prerare() - method");		
			ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			if (paraName.equals("AD_Client_ID")) 
				p_AD_Client_ID =  para.getParameterAsInt();
			else if (paraName.equals("AD_Org_ID")) 
				p_AD_Org_ID =  para.getParameterAsInt();
			else if (paraName.equals("TargetAcctSchema_ID")) 
				p_TargetAcctSchema_ID =  para.getParameterAsInt();
			else if (paraName.equals("C_Period_ID")) 
				p_C_Period_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_ProvisionType")) 
				p_AMN_ProvisionType =  para.getParameterAsString();
			else if (paraName.equals("AMNProvisionFactor")) 
				p_AMNProvisionFactor =  para.getParameterAsBigDecimal();
			else if (paraName.equals("DocumentNo")) 
				p_DocumentNo =  para.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
	}

	@Override
	protected String doIt() throws Exception {

		// Process Monitor
		IProcessUI processMonitor = Env.getProcessUI(getCtx());
		String Message="";
		int Employee_Count = 0 ;
		int EmployeeNo = 0;
		int Percent = 0;
		// TODO Auto-generated method stub
		MPeriod mperiod = new MPeriod(Env.getCtx(),p_C_Period_ID, null);
		Timestamp DateIni = mperiod.getStartDate();
		Timestamp DateEnd = mperiod.getEndDate();
		SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
		//  MAcctSchema Select Client Default 
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_AD_Client_ID, null); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		// TARGET C_AcctSchema_ID 
		if (p_TargetAcctSchema_ID >= 0)
			C_AcctSchema_ID = p_TargetAcctSchema_ID;
		else
			C_AcctSchema_ID = as.getC_AcctSchema_ID();
		MAcctSchema astarget = MAcctSchema.get (Env.getCtx(), C_AcctSchema_ID, null);
		// TARGET Currency
		if (astarget != null)
			Target_Currency_ID = astarget.getC_Currency_ID();
		// Default GL Category
		MGLCategory glcat =  MGLCategory.getDefault(Env.getCtx(), null);
		// GL_Journal - GL_JournalLine		
		GL_DocType_ID = MDocType.getDocType(Doc_GLJournal.DOCTYPE_GLJournal);
		// AMN_Schema_ID
    	String sql = "select distinct amn_schema_id from amn_schema "+
    			"WHERE ad_client_id="+p_AD_Client_ID +
    			" AND c_acctschema_id="+C_AcctSchema_ID;
    	AMN_Schema_ID = (Integer) DB.getSQLValue(null, sql);
    	//log.warning("C_AcctSchema_ID="+C_AcctSchema_ID+"  AMN_Schema_ID="+AMN_Schema_ID);
    	// MAMN_Schema Default Valid Combination Table
		MAMN_Schema amnschema = new MAMN_Schema(Env.getCtx(), AMN_Schema_ID, null);
		// Tipo de Provisión
		if (p_AMN_ProvisionType.compareToIgnoreCase("S")== 0 ||
			p_AMN_ProvisionType.compareToIgnoreCase("V")== 0 ||
			p_AMN_ProvisionType.compareToIgnoreCase("U")== 0) {
			addLog( Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+dt1.format(DateEnd)+
					":"+" ** OPCION PROVISION VALIDA **   ("+p_AMN_ProvisionType+ ")");
		} else {
			addLog( Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+dt1.format(DateEnd)+
					":"+" ** OPCION PROVISION NO-VALIDA **");
			addLog(Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+
					":"+" *** Seleccionar (S) para Prestaciones Sociales (V) Para Vacaiones **");
			return null;
		}
		// Factor
		BigDecimal factor = (p_AMNProvisionFactor);
		if (factor.compareTo(BigDecimal.ZERO) <= 0)
			factor = BigDecimal.ZERO;
		else
			factor=factor.divide(BigDecimal.valueOf(100));
		//log.warning(" factor="+factor+"  payamt"+payamt+"   amtacctcr"+amtacctcr);					
		// IF factor = 0 then Do Nothing
		if (factor.compareTo(BigDecimal.ZERO) <= 0) {
			addLog( Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+dt1.format(DateEnd));
			addLog(Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+
					":"+" ELFACTOR MAYOR QUE CERO **");
			return null;
		}
		// Generate GL_Journal record Verify IF EXISTS
		// FIND findGLJournalByDocumentNo
		mjournal = findGLJournalByDocumentNo(Env.getCtx(), p_AD_Client_ID, p_DocumentNo);
		if (mjournal != null ) {
			// Document Header
			addLog( Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+mjournal.getDocumentNo().trim()+
					" ("+Msg.translate(Env.getCtx(), "DocStatus")+":"+mjournal.getDocStatus()+") " +
					Msg.translate(Env.getCtx(), "Date")+":"+
					String.format("%-20s", mjournal.getDateAcct()).replace(' ', '_'));
			addLog(Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+mjournal.getDocumentNo().trim()+
					":"+" **** YA EXISTE UN DOCUMENTO CON ESE NUMERO ****");
			addLog(Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+mjournal.getDocumentNo().trim()+
					":"+" **** DEBE REACTIVAR Y ELIMINAR ****");
			return null;
		} else {
			// * Create a GL Journal
			// conversion type = default
			mjournal = new MJournal(getCtx(), 0, get_TrxName());
			mjournal.setAD_Org_ID(p_AD_Org_ID);
			mjournal.setC_Currency_ID(as.getC_Currency_ID());
	        mjournal.setGL_Category_ID(glcat.getGL_Category_ID());
			mjournal.setControlAmt(Env.ZERO);
			mjournal.setDateAcct(DateEnd);
			mjournal.setC_DocType_ID(GL_DocType_ID);
			mjournal.setC_Period_ID(p_C_Period_ID);
			mjournal.setDateDoc(DateEnd);
			// Prestacion - Vacation
			if (p_AMN_ProvisionType.compareToIgnoreCase("S")== 0)
				mjournal.setDescription("Provisión Nómina Prestaciones Periodo "+dt1.format(DateIni)+" al "+dt1.format(DateEnd));
			else if  (p_AMN_ProvisionType.compareToIgnoreCase("V")== 0)
				mjournal.setDescription("Provisión Nómina Vacaciones Periodo "+dt1.format(DateIni)+" al "+dt1.format(DateEnd));
			else if  (p_AMN_ProvisionType.compareToIgnoreCase("U")== 0)
				mjournal.setDescription("Provisión Nómina Utilidades Periodo "+dt1.format(DateIni)+" al "+dt1.format(DateEnd));
			mjournal.setDocumentNo(p_DocumentNo);
			mjournal.setPostingType("A");
			mjournal.setC_AcctSchema_ID(C_AcctSchema_ID);
			mjournal.setC_ConversionType_ID(MConversionType.getDefault(as.getAD_Client_ID()));
			mjournal.saveEx();
			//
			GL_Journal_ID= mjournal.getGL_Journal_ID();
		}
			
		// Generate GL_JournalLine
		// MMS Material MatShipment		
		// 
    	// PR_DocType_ID
    	String sql1 = "select c_doctype_id from c_doctype WHERE ad_client_id="+p_AD_Client_ID+"  AND docbasetype='HRP' " ;
    	PR_DocType_ID = (Integer) DB.getSQLValue(null, sql1);
    	
		MDocType pr_doctype = new MDocType(Env.getCtx(),PR_DocType_ID,null);
		String PR_DocTypeName = pr_doctype.getPrintName(Env.getAD_Language(Env.getCtx())).trim();
		// Shipments Count
		DocStatus ="'CO','CL'";
		// Get Active employees
		Employee_Count= MAMN_Employee.getEmployeeCount(p_AD_Client_ID, p_AD_Org_ID, "A");
		// log.warning("Employee_Count:"+Employee_Count);
		// Total Provision
		provisionAmount = BigDecimal.ZERO;
		// Show 
		addLog( PR_DocTypeName+"  No:"+Employee_Count);
		// SQL
		String sqls = "SELECT  " +
				" paydet.ad_client_id,  " +
				" paydet.ad_org_id, " +
				" paydet.workforce, " +
				" paydet.c_period_id, " +
				" paydet.amn_employee_id, " +
				" paydet.value_emp, " +
				" paydet.empleado, " +
				" COALESCE(sum(paydet.amountallocated) - sum(paydet.amountdeducted),0) as payamt, " +
				" COALESCE(paydone.paydone,0) as paydone, " +
				" COALESCE(sum(paydet.amountallocated) - sum(paydet.amountdeducted) - paydone.paydone,0) as paydiff, " +
				" paydet.c_currency_id, " +
				" paydet.c_conversiontype_id , " +
				" COALESCE(sum(paydet.amountallocated_src) - sum(paydet.amountdeducted_src), 0) AS payamt_src, " +
				" COALESCE(paydone.paydone_src, 0) AS paydone_src, " +
				" COALESCE(sum(paydet.amountallocated_src) - sum(paydet.amountdeducted_src) - paydone.paydone_src, 0) AS paydiff_src " +
				" FROM ( " +
				" 	SELECT  " +
				" 	      pyr.ad_client_id,  " +
				" 	      pyr.ad_org_id, " +
				" 	      c_prd.c_period_id,  " +
				" 	      emp.amn_employee_id,  " +
				" 	      emp.value as value_emp,  " +
				" 	      emp.name as empleado,  " +
				" 	      COALESCE(jtt.workforce,jtt.workforce,'A') as workforce,  " +
				" 	      pyr_d.value as detail_value,  " +
				" 	      qtyvalue as cantidad,  " +
				" 	      pyr_d.amountallocated AS amountallocated_src, " +
				" 	      pyr_d.amountdeducted AS amountdeducted_src, " +
				" 	      pyr_d.amountcalculated AS amountcalculated_src, " +
				" 	      currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, "+Target_Currency_ID+", pyr.dateacct , pyr.c_conversiontype_id, "+p_AD_Client_ID+", "+p_AD_Org_ID+") " +
				" 	      AS amountallocated, " +
				" 	      currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, "+Target_Currency_ID+", pyr.dateacct , pyr.c_conversiontype_id, "+p_AD_Client_ID+", "+p_AD_Org_ID+") " +
				" 	      AS amountdeducted, " +
				" 	      currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, "+Target_Currency_ID+", pyr.dateacct , pyr.c_conversiontype_id, "+p_AD_Client_ID+", "+p_AD_Org_ID+") " +
				" 	      AS amountcalculated, " +
				" 	      pyr.c_currency_id, " +
				" 	      pyr.c_conversiontype_id " + 
				" 	FROM amn_payroll as pyr " +
				" 	LEFT JOIN amn_payroll_detail as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id) " +
				" 	LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id) " +
				" 	LEFT JOIN amn_concept_types	as cty ON ((cty.amn_concept_types_id= ctp.amn_concept_types_id)) " +
				" 	LEFT JOIN amn_process as prc ON (prc.amn_process_id= ctp.amn_process_id) " +
				" 	INNER JOIN amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id) " +
				" 	LEFT JOIN amn_jobtitle as jtt ON (pyr.amn_jobtitle_id= jtt.amn_jobtitle_id) " +
				" 	LEFT JOIN amn_jobtitle as jtt2 ON (emp.amn_jobtitle_id= jtt2.amn_jobtitle_id) " +
				" 	LEFT JOIN amn_period as prd ON (prd.amn_period_id= pyr.amn_period_id) " +
				" 	LEFT JOIN c_period as c_prd ON (c_prd.c_period_id= prd.c_period_id) " +
				" 	WHERE prc.value ='NN' AND cty.optmode <> 'R' " ;
				// Tipo de Provisión Prestaciones Sociales
				if (p_AMN_ProvisionType.compareToIgnoreCase("S")== 0 ) {
					// Provison Prestaciones Sociales
					sqls = sqls + "   AND cty.prestacion ='Y' " ;
				// Tipo de Provisión Vacaciones
				} else if (p_AMN_ProvisionType.compareToIgnoreCase("V")== 0 ) {
					// Provison Vacations
					sqls = sqls + "   AND cty.vacacion ='Y' " ;
				// Tipo de Provisión Utilidades
				} else if (p_AMN_ProvisionType.compareToIgnoreCase("U")== 0 ) {
					sqls = sqls + "   AND cty.utilidad ='Y' " ;
				}				
		sqls = sqls +
				"   AND pyr.AD_Client_ID = " + p_AD_Client_ID +
				"   AND pyr.AD_Org_ID = " + p_AD_Org_ID +
				" 	AND c_prd.C_Period_ID = " + p_C_Period_ID +
				" ) as paydet " +
				" LEFT JOIN ( " +
				" 	SELECT  " +
				" 	amn_employee_id, " +
				" 	COALESCE(sum(amountallocated) - sum(amountdeducted),0) as paydone, " +
				"	COALESCE(sum(amountallocated_src) - sum(amountdeducted_src), 0) AS paydone_src " +
				" 	 FROM ( " +
				" 		SELECT  " +
				" 		      pyr.ad_client_id,  " +
				" 		      pyr.ad_org_id , " +
				" 		      c_prd.c_period_id, c_prd.startdate, c_prd.enddate, " +
				" 		      emp.amn_employee_id, emp.value as value_emp, emp.name as empleado,  " +
				" 		      jtt.workforce, " +
				" 		      pyr_d.value as detail_value,  " +
				" 		      qtyvalue as cantidad,  " +
				" 		      pyr_d.amountallocated AS amountallocated_src, " +
				" 		      pyr_d.amountdeducted AS amountdeducted_src, " +
				" 		      pyr_d.amountcalculated AS amountcalculated_src, " +
				" 		      currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, "+Target_Currency_ID+", pyr.dateacct , pyr.c_conversiontype_id, "+p_AD_Client_ID+", "+p_AD_Org_ID+") " +
				" 		      AS amountallocated, " +
				" 		      currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, "+Target_Currency_ID+", pyr.dateacct , pyr.c_conversiontype_id, "+p_AD_Client_ID+", "+p_AD_Org_ID+") " +
				" 		      AS amountdeducted, " +
				" 		      currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, "+Target_Currency_ID+", pyr.dateacct , pyr.c_conversiontype_id, "+p_AD_Client_ID+", "+p_AD_Org_ID+") " +
				" 		      AS amountcalculated, " +
				" 		      pyr.c_currency_id, " +
				" 		      pyr.c_conversiontype_id " +
				" 		FROM amn_payroll as pyr " +
				" 		LEFT JOIN amn_payroll_detail as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id) " +
				" 		LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id) " +
				" 		LEFT JOIN amn_concept_types	as cty ON ((cty.amn_concept_types_id= ctp.amn_concept_types_id)) " +
				" 		LEFT JOIN amn_process as prc ON (prc.amn_process_id= ctp.amn_process_id) " +
				" 		INNER JOIN amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id) " +
				" 		LEFT JOIN amn_jobtitle as jtt ON (pyr.amn_jobtitle_id= jtt.amn_jobtitle_id) " +
				" 		LEFT JOIN amn_period as prd ON (prd.amn_period_id= pyr.amn_period_id) " +
				" 		LEFT JOIN c_period as c_prd ON (c_prd.c_period_id= prd.c_period_id) " +
				" 		WHERE prc.value ='NP' AND cty.optmode != 'R' " +
				"       AND pyr.AD_Client_ID = " +p_AD_Client_ID +
				"       AND pyr.AD_Org_ID = " + p_AD_Org_ID +
				" 		AND c_prd.C_Period_ID = " + p_C_Period_ID +
				" 	) as paydet " +
				" 	GROUP BY amn_employee_id " +
				" ) as paydone ON (paydone.amn_employee_id = paydet.amn_employee_id ) " +
				" GROUP BY ad_client_id, ad_org_id, workforce, c_period_id, paydet.amn_employee_id, value_emp,empleado, paydone.paydone, paydet.c_currency_id, " +
				" paydet.c_conversiontype_id, paydone_src " +
				" ORDER BY paydet.workforce, paydet.value_emp " ;
		//log.warning("sql="+sqls);
		//log.warning("IO_DocType_ID="+IO_DocType_ID+"  p_AD_Client_ID="+p_AD_Client_ID+"  p_AD_Org_ID="+p_AD_Org_ID);
		PreparedStatement pstmts = null;
		ResultSet rss = null;
		try
		{			
			pstmts = DB.prepareStatement(sqls, null);

			rss = pstmts.executeQuery();
			while (rss.next())
			{
				WorkForce = rss.getString(3);
				C_Period_ID= rss.getInt(4);
				AMN_Employee_ID= rss.getInt(5);
				Employee_Value =  rss.getString(6).trim();
				Employee_Name =  rss.getString(7).trim();
				BigDecimal payamt = rss.getBigDecimal(8);
				BigDecimal paydone = rss.getBigDecimal(9);
				BigDecimal paydiff = rss.getBigDecimal(10);
				int Pay_Currency_ID =  rss.getInt(11);
				int Pay_ConversionType_ID = rss.getInt(12);
				BigDecimal payamt_src = rss.getBigDecimal(13);
				BigDecimal paydone_src = rss.getBigDecimal(14);
				BigDecimal paydiff_src = rss.getBigDecimal(15);
				// C_BPartner_ID
				MAMN_Employee emp = new MAMN_Employee(Env.getCtx(), AMN_Employee_ID, null);
				C_BPartner_ID = emp.getC_BPartner_ID();
				//log.warning("GL_Journal_ID="+GL_Journal_ID+"  Employee="+Employee_Value+" "+Employee_Name+ " M_Product_Category_ID_ID="+C_Period_ID+"  WorkForce="+WorkForce+
				//		"  payamt="+payamt+"  paydone"+paydone+"  C_BPartner_ID="+C_BPartner_ID);
				// Count ON EmployeeNo
				EmployeeNo=EmployeeNo+1;
				// Percentage Monitor
				Percent =  (EmployeeNo * 100 /(Employee_Count));

				// Valid Combination
				if (WorkForce == null) {
					WorkForce="A";
					Msg_WorkForce= " ** Revisar Cargo Trabajador **  Colocado 'A' Por defecto ";
				} else {
					if (WorkForce.compareToIgnoreCase("A") == 0)
						Msg_WorkForce ="Administración"; 
					else if (WorkForce.compareToIgnoreCase("D") == 0)
						Msg_WorkForce ="Mano de Obra Directa"; 
					else if (WorkForce.compareToIgnoreCase("I") == 0)
						Msg_WorkForce ="Mano de Obra Indirecta"; 
					else if (WorkForce.compareToIgnoreCase("S") == 0)
						Msg_WorkForce ="Ventas"; 
					else if (WorkForce.compareToIgnoreCase("M") == 0)
						Msg_WorkForce ="Directores"; 
				}
				// Provision Account
				// Tipo de Provisión Prestaciones Sociales
				if (p_AMN_ProvisionType.compareToIgnoreCase("S")== 0 ) {
					// Provison Prestaciones Sociales
					C_VC_Provision_ID = amnschema.getAMN_P_Provision_Social();	//1000088;
					// Expenses Accounts Prestaciones Sociales
					if (WorkForce.compareToIgnoreCase("A") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_SocBen_AW_Expense(); 	//1000083;
					else if (WorkForce.compareToIgnoreCase("D") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_SocBen_DW_Expense(); 	//1000977;
					else if (WorkForce.compareToIgnoreCase("I") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_SocBen_IW_Expense(); 	//1000978;
					else if (WorkForce.compareToIgnoreCase("S") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_SocBen_SW_Expense(); 	//1000976;
					else if (WorkForce.compareToIgnoreCase("M") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_SocBen_MW_Expense(); 	//1000972;
				// Tipo de Provisión Vacaciones
				} else if (p_AMN_ProvisionType.compareToIgnoreCase("V")== 0 ) {
					// Provison Vacations
					C_VC_Provision_ID = amnschema.getAMN_P_Provision_Vacation();	//1000088;
					// Expenses Accounts Vacations
					if (WorkForce.compareToIgnoreCase("A") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Vacation_AW_Expense(); 	//1000083;
					else if (WorkForce.compareToIgnoreCase("D") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Vacation_DW_Expense(); 	//1000977;
					else if (WorkForce.compareToIgnoreCase("I") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Vacation_IW_Expense(); 	//1000978;
					else if (WorkForce.compareToIgnoreCase("S") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Vacation_SW_Expense(); 	//1000976;
					else if (WorkForce.compareToIgnoreCase("M") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Vacation_MW_Expense(); 	//1000972;
				// Tipo de Provisión Utilidades
				} else if (p_AMN_ProvisionType.compareToIgnoreCase("U")== 0 ) {
					// Provison Utilidades
					C_VC_Provision_ID = amnschema.getAMN_P_Provision_Utilities();	//1000088;
					// Expenses Accounts Utiilidades
					if (WorkForce.compareToIgnoreCase("A") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Utilities_AW_Expense(); 	//1000083;
					else if (WorkForce.compareToIgnoreCase("D") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Utilities_DW_Expense(); 	//1000977;
					else if (WorkForce.compareToIgnoreCase("I") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Utilities_IW_Expense(); 	//1000978;
					else if (WorkForce.compareToIgnoreCase("S") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Utilities_SW_Expense(); 	//1000976;
					else if (WorkForce.compareToIgnoreCase("M") == 0)
						C_ValidCombination_ID = amnschema.getAMN_P_Utilities_MW_Expense(); 	//1000972;
				}
				//log.warning("C_ValidCombination_ID="+C_ValidCombination_ID+"  C_VC_Provision_ID="+C_VC_Provision_ID);
				// Pay Amount 
				if (payamt.compareTo(BigDecimal.ZERO) != 0) {
					// ------------------------
					
					// CREATE GL Line
					// Account Combination
					// MAccount (Default Value )
					MAccount defExp = new MAccount(Env.getCtx(), C_ValidCombination_ID , null);
					Account_ID = defExp.getAccount_ID();
					// MAccount (GET OR CREATE )
					MAccount vcExp = MAccount.get(getCtx(), p_AD_Client_ID,
							p_AD_Org_ID, as.getC_AcctSchema_ID(), Account_ID,
							0, 0, emp.getC_BPartner_ID(),
							0, 0, 0,
							emp.getC_SalesRegion_ID(), emp.getC_Project_ID(), emp.getC_Campaign_ID(),
							emp.getC_Activity_ID(), 0, 0, 0,
							0,
							get_TrxName());
					//
					vcExp.setAD_Org_ID(emp.getAD_Org_ID());
					vcExp.setC_BPartner_ID(emp.getC_BPartner_ID());
					vcExp.setC_SalesRegion_ID(emp.getC_SalesRegion_ID());
					vcExp.setC_Project_ID(emp.getC_Project_ID());
					vcExp.setC_Campaign_ID(emp.getC_Campaign_ID());
					vcExp.setC_Activity_ID(emp.getC_Activity_ID());
					// ROUND 2 DECIMALS
					BigDecimal payamtrd = payamt.multiply(factor).setScale(2, RoundingMode.CEILING);
					BigDecimal payamtrd_src = payamt_src.multiply(factor).setScale(2, RoundingMode.CEILING);
					// CREATE JOURNAL LINE DEBIT
					if (p_AMN_ProvisionType.compareToIgnoreCase("S")== 0 ) {
						irpp = createJournalLine(mjournal, lineNo, as, Employee_Value.trim()+"-"+Employee_Name.trim(),  
								"Provisión Prestaciones Sociales al "+dt1.format(DateEnd)+" "+Msg_WorkForce, DateEnd, Target_Currency_ID, Pay_ConversionType_ID, vcExp, 
								payamtrd_src, BigDecimal.ZERO, payamtrd, BigDecimal.ZERO, C_ValidCombination_ID);
					} else if (p_AMN_ProvisionType.compareToIgnoreCase("V")== 0 ) {
						irpp = createJournalLine(mjournal, lineNo, as, Employee_Value.trim()+"-"+Employee_Name.trim(),  
								"Provisión Vacaciones al "+dt1.format(DateEnd)+" "+Msg_WorkForce, DateEnd, Target_Currency_ID, Pay_ConversionType_ID, vcExp,
								payamtrd_src, BigDecimal.ZERO, payamtrd, BigDecimal.ZERO, C_ValidCombination_ID);
					} else if (p_AMN_ProvisionType.compareToIgnoreCase("U")== 0 ) {
						irpp = createJournalLine(mjournal, lineNo, as, Employee_Value.trim()+"-"+Employee_Name.trim(),  
								"Provisión Utilidades al "+dt1.format(DateEnd)+" "+Msg_WorkForce, DateEnd, Target_Currency_ID, Pay_ConversionType_ID, vcExp,
								payamtrd_src, BigDecimal.ZERO,payamtrd, BigDecimal.ZERO, C_ValidCombination_ID);
					}
					lineNo += 10;
					provisionAmount= provisionAmount.add(payamtrd);
					MAccount defCxp = new MAccount(Env.getCtx(), C_VC_Provision_ID , null);
					Account_ID = defCxp.getAccount_ID();
					// MAccount (GET OR CREATE )
					MAccount vcCxp = MAccount.get(getCtx(), p_AD_Client_ID,
							p_AD_Org_ID, as.getC_AcctSchema_ID(), Account_ID,
							0, 0, emp.getC_BPartner_ID(),
							0, 0, 0,
							emp.getC_SalesRegion_ID(), emp.getC_Project_ID(), emp.getC_Campaign_ID(),
							emp.getC_Activity_ID(), 0, 0, 0,
							0,
							get_TrxName());
					//
					//Account_ID = vcCxp.getAccount_ID();
					vcCxp.setC_BPartner_ID(C_BPartner_ID);
					vcCxp.setAD_Org_ID(p_AD_Org_ID);
					vcCxp.setAD_Org_ID(emp.getAD_Org_ID());
					vcCxp.setC_BPartner_ID(emp.getC_BPartner_ID());
					vcCxp.setC_SalesRegion_ID(emp.getC_SalesRegion_ID());
					vcCxp.setC_Project_ID(emp.getC_Project_ID());
					vcCxp.setC_Campaign_ID(emp.getC_Campaign_ID());
					vcCxp.setC_Activity_ID(emp.getC_Activity_ID());
					// CREATE JOURNAL LINE CREDIT
					if (p_AMN_ProvisionType.compareToIgnoreCase("S")== 0 ) {
						irpp = createJournalLine(mjournal, lineNo, as, Employee_Value.trim()+"-"+Employee_Name.trim(),  
								"Provisión Prestaciones Sociales al "+dt1.format(DateEnd)+" "+Msg_WorkForce, DateEnd, Target_Currency_ID, Pay_ConversionType_ID, vcCxp ,
								 BigDecimal.ZERO, payamtrd_src, BigDecimal.ZERO, payamtrd, C_VC_Provision_ID);
					} else if (p_AMN_ProvisionType.compareToIgnoreCase("V")== 0 ) {
						irpp = createJournalLine(mjournal, lineNo, as, Employee_Value.trim()+"-"+Employee_Name.trim(),  
								"Provisión Vacaciones al "+dt1.format(DateEnd)+" "+Msg_WorkForce, DateEnd, Target_Currency_ID, Pay_ConversionType_ID, vcCxp ,
								BigDecimal.ZERO, payamtrd_src, BigDecimal.ZERO, payamtrd, C_VC_Provision_ID);
					} else if (p_AMN_ProvisionType.compareToIgnoreCase("U")== 0 ) {
						irpp = createJournalLine(mjournal, lineNo, as, Employee_Value.trim()+"-"+Employee_Name.trim(),  
								"Provisión Utilidades al "+dt1.format(DateEnd)+" "+Msg_WorkForce, DateEnd, Target_Currency_ID, Pay_ConversionType_ID, vcCxp,
								BigDecimal.ZERO, payamtrd_src, BigDecimal.ZERO, payamtrd, C_VC_Provision_ID);
					}
					lineNo += 10;
					// 
					// Process Monitor
					if (processMonitor != null)
					{
						Message = Msg.translate(Env.getCtx(), "AMN_Employee_ID")+":"+Employee_Value.trim()+"-"+Employee_Name.trim()+
								" ("+String.format("%-5s",Percent)+ "% ) "+PR_DocTypeName+":"+ EmployeeNo+"/"+Employee_Count +") " +
								Msg.translate(Env.getCtx(), "Date")+":"+dt1.format(DateEnd)+
								Msg.translate(Env.getCtx(), "WorkForce")+":" + WorkForce + Msg_WorkForce;
						processMonitor.statusUpdate(Message);
					}
					// Document 
					addLog( PR_DocTypeName+":"+" Provisión del Mes ("+ EmployeeNo+"/"+Employee_Count+") " +
							Msg.translate(Env.getCtx(), "Date")+":"+
							dt1.format(DateEnd)+" "+
							Msg.translate(Env.getCtx(), "WorkForce")+":" + WorkForce + Msg_WorkForce);
					addLog("   "+irpp);
				}
				// Pay paydone (Provision already done by Prestacion Receopts PR) 
				if (paydone.compareTo(BigDecimal.ZERO) != 0) {
					// ----------------TO BE IMPLEMENTED  -----
//					provisionAmount= provisionAmount.add(paydone);
//					// CREATE GL Line
//					// Account Combination
//
//					// CREATE JOURNAL LINE
//					String irpp = createJournalLine(mjournal, lineNo, as, Employee_Value.trim()+"-"+Employee_Name.trim(),  
//							"Provisión del Mes "+dt1.format(DateEnd), DateEnd, C_Currency_ID, Account_ID, C_BPartner_ID, 
//							BigDecimal.ZERO, paydone.multiply(factor), BigDecimal.ZERO, paydone.multiply(factor), C_ValidCombination_ID, factor);
//					lineNo += 10;
//					// Document 
//					addLog( PR_DocTypeName+":"+" Provisión del Mes ("+ EmployeeNo+"/"+Employee_Count+") " +
//							Msg.translate(Env.getCtx(), "Date")+":"+
//							dt1.format(DateEnd));
//					addLog("   "+irpp);
				}
			}
			// MJournal Header Add Total 
			mjournal.setControlAmt(provisionAmount);
			mjournal.saveEx();
		}
		catch (Exception ee)
		{
			log.log(Level.SEVERE, sqls, ee);
		}
		finally
		{
			DB.close(rss, pstmts);
			rss = null;
			pstmts = null;
		}
		return null;
	}

	
	/**
	 *  findGLJournalByDocumentNo
	 * @param ctx
	 * @param locale
	 * @param AD_Client_ID
	 * @param DocumentNo
	 * @return
	 */
	
	public static MJournal findGLJournalByDocumentNo(Properties ctx,
				int AD_Client_ID, String DocumentNo) {
				
		MJournal retValue = null;
		String sql = "SELECT * "
			+ " FROM GL_Journal "
			+ " WHERE DocumentNo='"+DocumentNo+"' " 
			+ " AND AD_Client_ID=?"
			;        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, AD_Client_ID);

			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MJournal(Env.getCtx(), rs, null);
				//log.warning("  C_Period_ID:"+rs.getInt(1)+line.getName());
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//log.warning("  DocumentNo="+DocumentNo+"  retValue="+retValue);
		return retValue;
	}
	


	/**
	 *  createJournalLine
	 * @param mjournal
	 * @param lineNo
	 * @param as
	 * @param Employee_description
	 * @param Pay_description
	 * @param DateAcct
	 * @param C_Currency_ID
	 * @param ma
	 * @param amtsourcedr
	 * @param amtsourcecr
	 * @param amtacctdr
	 * @param amtacctcr
	 * @param C_ValidCombination_ID
	 * @param factor
	 * @return
	 */
	public String createJournalLine (MJournal mjournal, int lineNo, MAcctSchema as,  
		String Employee_description, String Pay_description, Timestamp DateAcct,
		int C_Currency_ID,  int C_ConversionType_ID, MAccount ma, BigDecimal amtsourcedr, BigDecimal amtsourcecr,
		BigDecimal amtacctdr, BigDecimal amtacctcr, int C_ValidCombination_ID) {

		String retValue ="";
		// Create the journal lines
		MJournalLine mjournalline = new MJournalLine(mjournal);
		// Fill Journal Lines Attributes
		mjournalline.setLine(lineNo);
		mjournalline.setDescription(Pay_description.trim()+ " "+Msg.translate(Env.getCtx(),"AMN_Employee_ID") +":"+
				Employee_description.trim());
		mjournalline.setC_ValidCombination_ID(ma);	
		//
		mjournalline.setC_Currency_ID(C_Currency_ID);
		mjournalline.setC_ConversionType_ID(C_ConversionType_ID);
		mjournalline.setAmtSourceDr(amtacctdr);
		mjournalline.setAmtSourceCr(amtacctcr);
		mjournalline.setAmtAcctDr(amtacctdr);
		mjournalline.setAmtAcctCr(amtacctcr);
		mjournalline.saveEx();
		retValue = mjournalline.getDescription();
	    return retValue;
	}

}


