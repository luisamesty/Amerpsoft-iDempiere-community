package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;

import org.adempiere.util.IProcessUI;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionType;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;

public class MAMN_Payroll_Historic extends X_AMN_Payroll_Historic {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7140722534706225206L;
	
	/**	Cache							*/
	private static CCache<Integer,MAMN_Payroll_Historic> s_cache = new CCache<Integer,MAMN_Payroll_Historic>(Table_Name, 10);
	
	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Historic.class);

	public MAMN_Payroll_Historic(Properties ctx, int AMN_Payroll_Historic_ID,
			String trxName) {
		super(ctx, AMN_Payroll_Historic_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	public MAMN_Payroll_Historic(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * createAmnPayrollHistoric
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Employee_ID
	 * @param p_ValueFrom
	 * @param p_ValueTo	
	 * @param trxName
	 * @return boolean
	 */
	public boolean createAmnPayrollHistoric(Properties ctx, Locale locale, 
			int p_AMN_Employee_ID, Timestamp p_ValueFrom, Timestamp p_ValueTo, String trxName) {
		
		Integer Currency_ID = 0;
		Integer CU_Currency_ID = 0;
		Integer ConversionType_ID = MConversionType.TYPE_SPOT;
		Integer AcctSchema_ID = 0;
		Timestamp StartDate=null;
		Timestamp EndDate=null;
		String Period_Value = "" ;
		String Period_Name = "Nombre del Período" ;
		String AMN_Period_YYYYMM ="";
		BigDecimal Salary_Base, Salary_Integral, Salary_Vacation;
		BigDecimal Salary_Utilities_NV, Salary_Utilities,Salary_Utilities_NN;
		BigDecimal Salary_Socialbenefits, Salary_Socialbenefits_NN, Salary_Socialbenefits_NV, Salary_Socialbenefits_NU ;
		BigDecimal Salary_Socialbenefits_Updated ;
		BigDecimal Salary_Utilities_Updated ;
		// GET Employee 
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
		// log.warning("Parameters...p_AMN_Employee_ID:"+p_AMN_Employee_ID+"  p_ValueFrom:"+p_ValueFrom+"  p_ValueTo:"+p_ValueTo);
		// Default Account Schema
		MClientInfo info = MClientInfo.get(Env.getCtx(), amnemployee.getAD_Client_ID(), null); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		AcctSchema_ID= as.getC_AcctSchema_ID();
		if (locale == null)
		{
			MClient client = MClient.get(ctx);
			locale = client.getLocale();
		}
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(ctx).getLocale();
   		// Default Currency  for Contract
   		Currency_ID = AmerpUtilities.defaultAMNContractCurrency(amnemployee.getAMN_Contract_ID());
   		if (Currency_ID == null )
   			Currency_ID = AmerpUtilities.defaultAcctSchemaCurrency(amnemployee.getAD_Client_ID());	
   		// Default ConversionType for Contract
   		ConversionType_ID = AmerpUtilities.defaultAMNContractConversionType(amnemployee.getAMN_Contract_ID());
   		if (ConversionType_ID == null)	
   			ConversionType_ID = MConversionType.TYPE_SPOT;
		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		// ************************************************************
    	// * Get HSTORIC SALARY FROM VIEW (amn_employee_salary_hist_v)
    	// ************************************************************
		String sql = "" +
	    		 "SELECT " + 
	    		 	"value,name, " +
	    		 	"amn_period_yyyymm,  " +
	    		 	"startdate, enddate,  " +
	    		 	"salary_base,  " +
	    		 	"salary_integral,  " +
	    		 	"salary_vacation,  " +
	    		 	"salary_utilities,  " +
	    		 	"salary_utilities_nn,  " +
	    		 	"salary_utilities_nv,  " +
	    		 	"salary_socialbenefits,  " +
	    		 	"salary_socialbenefits_nn,  " +
	    		 	"salary_socialbenefits_nv,  " +
	    		 	"salary_socialbenefits_nu, " +
	    		 	"cu_currency_id, " +
	    		 	"c_conversiontype_id "+
	    		 	"FROM adempiere.amn_employee_salary_hist_v " +
	    		 	"WHERE amn_employee_id = ? AND c_acctschema_id = ?";
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try
	    {
	            pstmt = DB.prepareStatement(sql, null);
	            pstmt.setInt(1, p_AMN_Employee_ID);
	            pstmt.setInt(2, AcctSchema_ID);
	            rs = pstmt.executeQuery();
	            while (rs.next())
	            {
	            	Period_Value= rs.getString(1).trim();
	                Period_Name = rs.getString(2).trim();
	                AMN_Period_YYYYMM = rs.getString(3).trim();
	                StartDate		= rs.getTimestamp(4);
	                EndDate			=rs.getTimestamp(5);
	                Salary_Base 	= rs.getBigDecimal(6);
	                Salary_Integral = rs.getBigDecimal(7);
	                Salary_Vacation = rs.getBigDecimal(8);
	                Salary_Utilities= rs.getBigDecimal(9);
	                Salary_Utilities_NN= rs.getBigDecimal(10);
	                Salary_Utilities_NV= rs.getBigDecimal(11);
	        		Salary_Socialbenefits= rs.getBigDecimal(12);
	        		Salary_Socialbenefits_NN= rs.getBigDecimal(13);
	        		Salary_Socialbenefits_NV= rs.getBigDecimal(14); 
	        		Salary_Socialbenefits_NU = rs.getBigDecimal(15);
	        		CU_Currency_ID= rs.getInt(16);
	        		ConversionType_ID= rs.getInt(17);
	        		//log.warning("Cicle...AMN_Period_YYYYMM"+AMN_Period_YYYYMM+" StartDate:"+StartDate+"  EndDate:"+EndDate);
	        		//log.warning("AMN_Period_YYYYMM"+AMN_Period_YYYYMM);
	        		if (p_AMN_Employee_ID !=0 && StartDate != null &&  EndDate!=null) {
	        			// Verify if Exists
	        			MAMN_Payroll_Historic amnpayrollhistoric = MAMN_Payroll_Historic.findAMNPayrollHistoricbyDates(
	        					ctx, locale, p_AMN_Employee_ID, StartDate,  EndDate, CU_Currency_ID);
	        			if (amnpayrollhistoric == null) {
	        				//log.warning(".....(NEW).AMN_Employee_ID:"+p_AMN_Employee_ID+"  Period_YYYYMM:"+AMN_Period_YYYYMM+" StartDate:"+StartDate+"  EndDate:"+EndDate);
							amnpayrollhistoric = new  MAMN_Payroll_Historic(ctx, getAMN_Payroll_Historic_ID(), get_TrxName());
		        			amnpayrollhistoric.setAMN_Employee_ID(p_AMN_Employee_ID);
		        			amnpayrollhistoric.setAD_Client_ID(amnemployee.getAD_Client_ID());
		        			amnpayrollhistoric.setAD_Org_ID(amnemployee.getAD_Org_ID());
		        			amnpayrollhistoric.setValue(Period_Value);
		        			amnpayrollhistoric.setName(Period_Name);
		        			amnpayrollhistoric.setAMN_Period_YYYYMM(AMN_Period_YYYYMM);
		        			amnpayrollhistoric.setValidFrom(StartDate);
		        			amnpayrollhistoric.setValidTo(EndDate);
		        			amnpayrollhistoric.setSalary_Base(Salary_Base);
		        			amnpayrollhistoric.setSalary_Integral(Salary_Integral);
		        			amnpayrollhistoric.setSalary_Socialbenefits(Salary_Socialbenefits);
		        			// Updated Values for Salary_Socialbenefits
		        			amnpayrollhistoric.setSalary_Socialbenefits_Updated(Salary_Socialbenefits);
		        			amnpayrollhistoric.setSalary_Socialbenefits_NN(Salary_Socialbenefits_NN);
		        			amnpayrollhistoric.setSalary_Socialbenefits_NU(Salary_Socialbenefits_NU);
		        			amnpayrollhistoric.setSalary_Socialbenefits_NV(Salary_Socialbenefits_NV);
		        			amnpayrollhistoric.setSalary_Utilities(Salary_Utilities);
		        			// Updated Values for Salary_Utilities
		        			amnpayrollhistoric.setSalary_Utilities_Updated(Salary_Utilities);
		        			amnpayrollhistoric.setSalary_Utilities_NN(Salary_Utilities_NN);
		        			amnpayrollhistoric.setSalary_Utilities_NV(Salary_Utilities_NV);
		        			amnpayrollhistoric.setSalary_Vacation(Salary_Vacation);
		        			// Currency
		        			amnpayrollhistoric.setC_Currency_ID(CU_Currency_ID);
		        			amnpayrollhistoric.setC_ConversionType_ID(ConversionType_ID);
		        			// SAVES NEW
		        			amnpayrollhistoric.save(get_TrxName());
		        		} else {
		        			// Verifiy Updated Value
		        			Salary_Socialbenefits_Updated=amnpayrollhistoric.getSalary_Socialbenefits_Updated();
		        			Salary_Utilities_Updated=amnpayrollhistoric.getSalary_Utilities_Updated();
		        			//log.warning(".....(UPDATE).AMN_Employee_ID:"+p_AMN_Employee_ID+"  Period_YYYYMM:"+AMN_Period_YYYYMM+" StartDate:"+StartDate+"  EndDate:"+EndDate);
							amnpayrollhistoric.setAD_Client_ID(amnemployee.getAD_Client_ID());
							amnpayrollhistoric.setAD_Org_ID(amnemployee.getAD_Org_ID());;
		        			amnpayrollhistoric.setValue(Period_Value);
		        			amnpayrollhistoric.setName(Period_Name);
		        			amnpayrollhistoric.save(trxName);
		        			amnpayrollhistoric.setAMN_Period_YYYYMM(AMN_Period_YYYYMM);
		        			amnpayrollhistoric.setValidFrom(StartDate);
		        			amnpayrollhistoric.setValidTo(EndDate);
		        			amnpayrollhistoric.setSalary_Base(Salary_Base);
		        			amnpayrollhistoric.setSalary_Integral(Salary_Integral);
		        			// Verify is Null or Zero Salary_Socialbenefits_Updated
	        				if (Salary_Socialbenefits_Updated.compareTo(new BigDecimal("0.00")) == 0 
	        						|| Salary_Socialbenefits_Updated == null) {
	        					// Updated Value
			        			amnpayrollhistoric.setSalary_Socialbenefits_Updated(Salary_Socialbenefits);
	        				}
		        			// Verify is Null or Zero Salary_Utilities_Updated
	        				if (Salary_Utilities_Updated.compareTo(new BigDecimal("0.00")) == 0 
	        						|| Salary_Utilities_Updated == null) {
	        					// Updated Value
			        			amnpayrollhistoric.setSalary_Utilities_Updated(Salary_Utilities);
	        				}
		        			amnpayrollhistoric.setSalary_Socialbenefits(Salary_Socialbenefits);
		        			amnpayrollhistoric.setSalary_Socialbenefits_NN(Salary_Socialbenefits_NN);
		        			amnpayrollhistoric.setSalary_Socialbenefits_NU(Salary_Socialbenefits_NU);
		        			amnpayrollhistoric.setSalary_Socialbenefits_NV(Salary_Socialbenefits_NV);
		        			amnpayrollhistoric.setSalary_Utilities(Salary_Utilities);
		        			amnpayrollhistoric.setSalary_Utilities_NN(Salary_Utilities_NN);
		        			amnpayrollhistoric.setSalary_Utilities_NV(Salary_Utilities_NV);
		        			amnpayrollhistoric.setSalary_Vacation(Salary_Vacation);
		        			// Currency
		        			amnpayrollhistoric.setC_Currency_ID(CU_Currency_ID);
		        			amnpayrollhistoric.setC_ConversionType_ID(ConversionType_ID);
		        			// SAVES UPDATES
		        			amnpayrollhistoric.save(get_TrxName());
		        		}
	        		}
	            }
	    }
	    catch (SQLException e)
	    {
	    	Period_Name = "Error: Nombre del Período" ;
	    }
	    finally
	    {
	            DB.close(rs, pstmt);
	            rs = null; pstmt = null;
	    }
		//amnpayroll.saveEx(trxName);	//	Creates AMNPayroll Control
		if (processMonitor != null)
		{
			processMonitor.statusUpdate(String.format("%-15s","Historic Rec1").replace(' ', '_')+
			Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
			String.format("%-50s",amnemployee.getValue()+"_"+amnemployee.getName().trim()).replace(' ', '_')+
			Msg.getElement(Env.getCtx(), "AMN_Payroll_Historic_ID")+": "+
			AMN_Period_YYYYMM);

			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					Period_Value+"-"+Period_Name);
		}

		return true;
		
	}	//	createAmnPayrollDetail

	/**
	 * resetSocialbenefitsUpdatedValue
	 * Reset Socialbenefits Updated Value with Original Socialbenefits Value
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Employee_ID
	 * @param p_ValueFrom
	 * @param p_ValueTo	
	 * @return boolean
	 * SocialBenefitValueUpdated is taken from SocialBenefitValue field
	 */
	public boolean resetSocialbenefitsUpdatedValue(Properties ctx, Locale locale, 
			int p_AMN_Employee_ID, Timestamp p_ValueFrom, Timestamp p_ValueTo, int CU_Currency_ID, String trxName) {
		
		BigDecimal Salary_Socialbenefits ;
		BigDecimal Salary= BigDecimal.valueOf(0);		
		// GET Employee 
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
		// Get Actual Salary
		Salary = amnemployee.getSalary();
		// log.warning("Parameters...p_AMN_Employee_ID:"+p_AMN_Employee_ID+"  p_ValueFrom:"+p_ValueFrom+"  p_ValueTo:"+p_ValueTo);
		if (locale == null)
		{
			MClient client = MClient.get(ctx);
			locale = client.getLocale();
		}
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(ctx).getLocale();
  		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
		amnpayrollhistoric= MAMN_Payroll_Historic.findAMNPayrollHistoricbyDates(
				ctx, locale, p_AMN_Employee_ID, p_ValueFrom,  p_ValueTo, CU_Currency_ID);
		//log.warning("........p_AMN_Employee_ID:"+p_AMN_Employee_ID+"  p_ValueFrom:"+p_ValueFrom+"  p_ValueTo:"+p_ValueTo);
		if (amnpayrollhistoric != null) {
			Salary_Socialbenefits=amnpayrollhistoric.getSalary_Socialbenefits();
			//log.warning(".....(UPDATE).AMN_Employee_ID:"+p_AMN_Employee_ID+"   Salary_Socialbenefits:"+Salary_Socialbenefits+"  p_ValueFrom:"+p_ValueFrom+"  p_ValueTo:"+p_ValueTo);
			// SAVES UPDATES
			// Updated Values for Salary_Socialbenefits
			amnpayrollhistoric.setSalary_Socialbenefits_Updated(Salary_Socialbenefits);
			amnpayrollhistoric.save(get_TrxName());
	    }
		//amnpayroll.saveEx(trxName);	//	Creates AMNPayroll Control
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(String.format("%-15s","Social Benefit").replace(' ', '_')+
					Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					String.format("%-50s",amnemployee.getValue()+"_"+amnemployee.getName().trim()).replace(' ', '_')+
					"");
		}
		return true;
	}	//	updateSalaryAmnPayrollHistoric

	/**
	 * resetUtilitiesUpdatedValue
	 * Reset Utilities Updated Value with Original Utility Value
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Employee_ID
	 * @param p_ValueFrom
	 * @param p_ValueTo	
	 * @return boolean
	 * SocialBenefitValueUpdated is taken from SocialBenefitValue field
	 */
	public boolean resetUtilitiesUpdatedValue(Properties ctx, Locale locale, 
			int p_AMN_Employee_ID, Timestamp p_ValueFrom, Timestamp p_ValueTo, int CU_Currency_ID, String trxName) {

		BigDecimal Salary_Utilities ;
		BigDecimal Salary= BigDecimal.valueOf(0);		
		// GET Employee 
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
		// Get Actual Salary
		Salary = amnemployee.getSalary();
		// log.warning("Parameters...p_AMN_Employee_ID:"+p_AMN_Employee_ID+"  p_ValueFrom:"+p_ValueFrom+"  p_ValueTo:"+p_ValueTo);
		if (locale == null)
		{
			MClient client = MClient.get(ctx);
			locale = client.getLocale();
		}
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(ctx).getLocale();
  		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
		amnpayrollhistoric= MAMN_Payroll_Historic.findAMNPayrollHistoricbyDates(
				ctx, locale, p_AMN_Employee_ID, p_ValueFrom,  p_ValueTo, CU_Currency_ID);
		if (amnpayrollhistoric != null) {
			//log.warning(".....(UPDATE).AMN_Employee_ID:"+p_AMN_Employee_ID+"   Salary:"+Salary+"  p_ValueFrom:"+p_ValueFrom+"  p_ValueTo:"+p_ValueTo);
			Salary_Utilities=amnpayrollhistoric.getSalary_Utilities();
			// SAVES UPDATES
			// Updated Values for Salary_Socialbenefits
			amnpayrollhistoric.setSalary_Utilities_Updated(Salary_Utilities);
			amnpayrollhistoric.save(get_TrxName());
	    }
		//amnpayroll.saveEx(trxName);	//	Creates AMNPayroll Control
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(String.format("%-15s","Utilities").replace(' ', '_')+
					Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					String.format("%-50s",amnemployee.getValue()+"_"+amnemployee.getName().trim()).replace(' ', '_')+
					"");
		}

		return true;
		
	}	//	updateSalaryAmnPayrollHistoric

	
	/**
	 * updateSalaryAmnPayrollHistoric
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Employee_ID
	 * @param p_ValueFrom
	 * @param p_ValueTo
	 * @param CU_Currency_ID
	 * @param trxName
	 * @return
	 */
	public boolean updateSalaryAmnPayrollHistoric(Properties ctx, Locale locale, 
			int p_AMN_Employee_ID, Timestamp p_ValueFrom, Timestamp p_ValueTo, int CU_Currency_ID, String trxName) {
		

		BigDecimal Salary= BigDecimal.valueOf(0);	
		BigDecimal SalaryPR= BigDecimal.valueOf(0);
		BigDecimal SalaryHistoric= BigDecimal.valueOf(0);	
		
		// GET Employee 
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
		// Get Actual Salary from Employee Table
		Salary = amnemployee.getSalary();
		// Get Salary FROM PAyroll Detail  IF NULL Get Salary from Employee Table
		MAMN_Payroll_Detail amnparyolldetail = new MAMN_Payroll_Detail(ctx, 0, null);
		amnparyolldetail = getAMNPAyrollDetailSalaryBaseSB(
				ctx, locale, p_AMN_Employee_ID, p_ValueFrom,  p_ValueTo, CU_Currency_ID);
		if (amnparyolldetail != null)
			SalaryPR = amnparyolldetail.getQtyValue();
		else
			SalaryPR = Salary;
		// log.warning("Parameters...p_AMN_Employee_ID:"+p_AMN_Employee_ID+"  p_ValueFrom:"+p_ValueFrom+"  p_ValueTo:"+p_ValueTo);
		if (locale == null)
		{
			MClient client = MClient.get(ctx);
			locale = client.getLocale();
		}
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(ctx).getLocale();
  		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
		amnpayrollhistoric= MAMN_Payroll_Historic.findAMNPayrollHistoricbyDates(
				ctx, locale, p_AMN_Employee_ID, p_ValueFrom,  p_ValueTo, CU_Currency_ID);
//log.warning(".....(UPDATE).amnpayrollhistoric:"+amnpayrollhistoric);
		if (amnpayrollhistoric != null) {
//log.warning(".....(UPDATE).AMN_Employee_ID:"+p_AMN_Employee_ID+"   Salary:"+Salary+"  p_ValueFrom:"+p_ValueFrom+"  p_ValueTo:"+p_ValueTo+"  CU_Currency_ID="+CU_Currency_ID);
			// Compare Historic Salary with New Payroll Salary SalaryPR
			if (amnpayrollhistoric.getSalary() != null)
				SalaryHistoric = amnpayrollhistoric.getSalary();
			if (SalaryHistoric.compareTo(BigDecimal.ZERO) == 0)
				amnpayrollhistoric.setSalary(SalaryPR);
			else
				if (SalaryPR.compareTo(SalaryHistoric) != 0)
					amnpayrollhistoric.setSalary(SalaryPR);
				else
					amnpayrollhistoric.setSalary(SalaryHistoric);
			// SAVES UPDATES
			amnpayrollhistoric.save(get_TrxName());
	    }
		//amnpayroll.saveEx(trxName);	//	Creates AMNPayroll Control
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(String.format("%-15s","Historic Rec2").replace(' ', '_')+
					Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					String.format("%-50s",amnemployee.getValue()+"_"+amnemployee.getName().trim()).replace(' ', '_')+
					"");
		}

		return true;
		
	}	//	updateSalaryAmnPayrollHistoric

	/**	
	 * findAMNPayrollHistoricbyDates
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Employee_ID
	 * @param p_ValidFrom
	 * @param p_ValidTo
	 * @return MAMN_Payroll_Historic
	 */
	public static MAMN_Payroll_Historic findAMNPayrollHistoricbyDates(Properties ctx, Locale locale, 
			int p_AMN_Employee_ID, Timestamp p_ValidFrom, Timestamp p_ValidTo, int CU_Currency_ID) {

		MAMN_Payroll_Historic retValue = null;	
		// CALCULATE FIRST DAY AND END DAY OF HOISTORIC PERIOD
		Timestamp firstDayofMonth=null;
		Timestamp endDayofMonth=null;
		GregorianCalendar refcal = new GregorianCalendar();
		// refcal Value to p_ValidTo
		refcal.setTime(p_ValidTo);
		//firstDayofMonth
		refcal.set(Calendar.DAY_OF_MONTH, 1);
		refcal.set(Calendar.HOUR_OF_DAY, 0);
		refcal.set(Calendar.MINUTE, 0);
		refcal.set(Calendar.SECOND, 0);
		refcal.set(Calendar.MILLISECOND, 0);
		firstDayofMonth = new Timestamp(refcal.getTimeInMillis());
		//endDayofMonth
		refcal.add (Calendar.MONTH, 1);
		refcal.add (Calendar.DAY_OF_YEAR, -1);
		endDayofMonth = new Timestamp(refcal.getTimeInMillis());
		//log.warning("p_ValidTo:"+p_ValidTo+"  firstDayofMonth:"+firstDayofMonth+"  endDayofMonth:"+endDayofMonth);
		//
		String sql = "SELECT * " + 
				"FROM amn_payroll_historic as pyh " + 
				"WHERE pyh.validfrom >= ? AND pyh.validto <= ? " + 
				"AND pyh.amn_employee_id=? AND pyh.c_currency_id = ?"			
			;        
		PreparedStatement pstmt = null;
		ResultSet rsh = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setTimestamp(1, firstDayofMonth);
            pstmt.setTimestamp(2, endDayofMonth);
            pstmt.setInt (3, p_AMN_Employee_ID);
            pstmt.setInt (4, CU_Currency_ID);
            rsh = pstmt.executeQuery();
			while (rsh.next())
			{
				MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, rsh, null);
//				Integer key = new Integer(amnpayrollhistoric.getAMN_Payroll_Historic_ID());
				Integer key = amnpayrollhistoric.getAMN_Payroll_Historic_ID();
				s_cache.put (key, amnpayrollhistoric);
				//log.warning("Verify...:AMN_Payroll_Historic_ID():"+amnpayrollhistoric.getAMN_Payroll_Historic_ID()+" ValidFrom:"+amnpayrollhistoric.getValidFrom()+"  ValidTo:"+amnpayrollhistoric.getValidTo());
				if (amnpayrollhistoric.isActive()  ) { 
					//&& amnpayrollhistoric.getValidFrom() == p_ValidFrom && amnpayrollhistoric.getValidTo() == p_ValidTo
					 retValue = amnpayrollhistoric;
				}
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rsh, pstmt);
			rsh = null; pstmt = null;
		}
		//log.warning(" After...sql:"+sql+" retValue:"+retValue);
		return retValue;
	}
	
	/**
	 * getAMNPAyrollDetailSalaryBaseSB  GET Salary SB From AMN_Payroll Table (Historic)
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Employee_ID
	 * @param p_ValidFrom
	 * @param p_ValidTo
	 * @param CU_Currency_ID
	 * @return
	 */
	public MAMN_Payroll_Detail getAMNPAyrollDetailSalaryBaseSB(Properties ctx, Locale locale, 
			int p_AMN_Employee_ID, Timestamp p_ValidFrom, Timestamp p_ValidTo, int CU_Currency_ID) {
		
		MAMN_Payroll_Detail retValue=null;
		// CALCULATE FIRST DAY AND END DAY OF HOISTORIC PERIOD
		Timestamp firstDayofMonth=null;
		Timestamp endDayofMonth=null;
		GregorianCalendar refcal = new GregorianCalendar();
		// refcal Value to p_ValidTo
		refcal.setTime(p_ValidTo);
		//firstDayofMonth
		refcal.set(Calendar.DAY_OF_MONTH, 1);
		refcal.set(Calendar.HOUR_OF_DAY, 0);
		refcal.set(Calendar.MINUTE, 0);
		refcal.set(Calendar.SECOND, 0);
		refcal.set(Calendar.MILLISECOND, 0);
		firstDayofMonth = new Timestamp(refcal.getTimeInMillis());
		//endDayofMonth
		refcal.add (Calendar.MONTH, 1);
		refcal.add (Calendar.DAY_OF_YEAR, -1);
		endDayofMonth = new Timestamp(refcal.getTimeInMillis());
		//log.warning("p_ValidTo:"+p_ValidTo+"  firstDayofMonth:"+firstDayofMonth+"  endDayofMonth:"+endDayofMonth);
		//
		String sql = "SELECT AMN_Payroll_Detail_ID, MAX(amountcalculated) as salarymax FROM( " + 
				"	SELECT " + 
				"	pro.AMN_Employee_ID,pro.C_Currency_ID, prd.AMN_Payroll_Detail_ID, prd.amountcalculated " + 
				"	FROM AMN_Payroll pro " + 
				"	LEFT JOIN AMN_Payroll_Detail prd ON prd.AMN_Payroll_ID = pro.AMN_Payroll_ID " + 
				"	WHERE pro.AMN_Employee_ID = ? AND pro.C_Currency_ID = ? " + 
				"	AND pro.InvDateIni >= ? AND pro.InvDateEnd <= ? " + 
				"	AND prd.Value='SB' AND pro.isActive='Y' " + 
				") as MaxSalary " + 
				" GROUP BY AMN_Payroll_Detail_ID "+
				" ORDER BY salarymax DESC";        
		PreparedStatement pstmt = null;
		ResultSet rsh = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Employee_ID);
            pstmt.setInt (2, CU_Currency_ID);
            pstmt.setTimestamp(3, firstDayofMonth);
            pstmt.setTimestamp(4, endDayofMonth);

            rsh = pstmt.executeQuery();
			if (rsh.next())
			{
				Integer key = rsh.getInt(1);
				if (key != null  ) { 
					MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(ctx, rsh.getInt(1), null);
					 retValue = amnpayrolldetail;
				}
			} else {
				retValue = null;
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rsh, pstmt);
			rsh = null; pstmt = null;
		}
//log.warning(" After...sql:"+sql+"\r\n retValue:"+retValue);
		return retValue;	
	}
	
}
