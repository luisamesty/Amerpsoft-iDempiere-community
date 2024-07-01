package org.amerp.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

import org.amerp.amnmodel.MAMN_Concept_Types;
import org.amerp.amnmodel.MAMN_Concept_Types_Proc;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Deferred;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.LoanPeriods;
import org.compiere.model.MClient;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class AMNPayrollProcessPayrollDeferred {

	// PUBLIC VARS
		static String Msg_Value="";
		static CLogger log = CLogger.getCLogger(AMNPayrollProcessPayrollDeferred.class);

	/**
	 * VerifyPeriodsAvailable:
	 * 	 Returns perido avaible after enter date p_AMN_FirstPeriod_ID
	 * @param ctx
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_FirstPeriod_ID
	 * @param trxName
	 * @return
	 */
	public static int VerifyPeriodsAvailable (Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Payroll_ID, int p_AMN_FirstPeriod_ID, String trxName) {
		
		String sql="";
		Integer retValue = 0;
		MAMN_Period amnperiod = new MAMN_Period(Env.getCtx(), p_AMN_FirstPeriod_ID, null);
		Timestamp PeriodFirtsDate = amnperiod.getAMNDateEnd();
		//
		sql ="SELECT  COUNT(*) " +
				"from adempiere.amn_period " +
				"where amn_process_id = ? " +
				"and amn_contract_id = ? " +
				"and amndateend >= '" + PeriodFirtsDate +"' " 
				;
		//log.warning("sql:"+sql);
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Process_ID);
            pstmt1.setInt (2, p_AMN_Contract_ID);
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				retValue = (Integer) rsod1.getInt(1);
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = 0;
	    }
		finally
		{
			DB.close(rsod1, pstmt1);
			rsod1 = null; pstmt1 = null;
		}
		//
		if (retValue == null)
			retValue = 0;
		return retValue;
	}
		
	/**
	 * VerifyDeferredDetailLines
	 * Description: Verify Records on Table AMN_PAyroll_Deferred 
	 * @param ctx
	 * @param p_AMN_Payroll_ID
	 * @param trxName
	 * return int 0: means no lines  N: means has N lines
	 */

	public static int VerifyDeferredDetailLines (Properties ctx, int p_AMN_Payroll_ID,  String trxName) {
					
		String sql="";
		int reccount = 0;
		//
		sql ="SELECT COUNT(*) " + 
			"FROM adempiere.amn_payroll_deferred " + 
			"WHERE amn_payroll_id = ? "
			;
		PreparedStatement pstmt1 = null;
		ResultSet rsprd = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Payroll_ID);
            rsprd = pstmt1.executeQuery();
			if (rsprd.next())
			{
				reccount =rsprd.getInt(1);
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsprd, pstmt1);
			rsprd = null; pstmt1 = null;
		}
		return reccount;
	}
		
	/**
	 * VerifyPayrollDetailLines
	 * Description: Verify Records on Table AMN_PAyroll_Deferred 
	 * @param ctx
	 * @param p_AMN_Payroll_ID
	 * @param trxName
	 * @return return int 1: means 1 lines  N: means has N lines	 
	 */
	
	public static boolean VerifyPayrollDetailLines (Properties ctx, int p_AMN_Payroll_ID, String trxName) {
					
		String sql="";
		int reccount = 0;
		boolean retValue = false;
		//
		sql ="SELECT COUNT(*) " + 
			"FROM adempiere.amn_payroll_detail " + 
			"WHERE amn_payroll_id = ? "
			;
		PreparedStatement pstmt1 = null;
		ResultSet rsprd = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Payroll_ID);
            rsprd = pstmt1.executeQuery();
			if (rsprd.next())
			{
				reccount =rsprd.getInt(1);
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsprd, pstmt1);
			rsprd = null; pstmt1 = null;
		}
		if ( reccount == 0 ) {
			retValue = true;
		} else if ( reccount == 1 ) {
			// Verifiy if AMN_Payroll_Types_proc is LOAN Concept
			MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
			MAMN_Contract amncontract = new MAMN_Contract(Env.getCtx(), amnpayroll.getAMN_Contract_ID(), null);
			MAMN_Process amnprocess = new MAMN_Process(Env.getCtx(), amnpayroll.getAMN_Process_ID(), null);
			int AMN_Concept_Types_ID = MAMN_Concept_Types.sqlGetAMNConceptTypesPRESTAMODB(amncontract.getAD_Client_ID());
		    int AMN_Concept_Types_Proc_ID = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcPRESTAMODB(AMN_Concept_Types_ID, amnprocess.getAMN_Process_ID());
		    MClient client = MClient.get(Env.getCtx());
		    // Verify AMN_Concept_Types_Proc_ID for LOAN
		    MAMN_Payroll_Detail amnpayrolldetail = MAMN_Payroll_Detail.findAMNPayrollDetailbyAMNPayroll(Env.getCtx(), client.getLocale(), 
					p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID);
			if (amnpayrolldetail == null) {
				retValue = false;				// NOT Found AMN_Concept_Types_Proc_ID for LOAN
				//log.warning("reccount:"+reccount+ "  false   p_AMN_Payroll_ID:"+p_AMN_Payroll_ID+"  AMN_Concept_Types_Proc_ID:"+AMN_Concept_Types_Proc_ID);
			} else {
				retValue = true;				// Found AMN_Concept_Types_Proc_ID for LOAN
				//log.warning("reccount:"+reccount+ "  true   p_AMN_Payroll_ID:"+p_AMN_Payroll_ID+"  AMN_Concept_Types_Proc_ID:"+AMN_Concept_Types_Proc_ID);
			}
		} else if ( reccount > 1 ) {
			retValue = false;
		} else {
			retValue = false;
		}
		//log.warning("reccount:"+reccount);
		return retValue;
	}
	
	/**
	 * CreatePayrollDeferredDetailLines 
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Employee_ID
	 * @param p_AMN_Concept_Types_Proc_DB_ID
	 * @param p_AMN_Concept_Types_Proc_CR_ID
	 * @param p_LoanAmount
	 * @param p_LoanQuotaNo
	 * @param p_AMN_FirstPeriod_ID
	 * @param p_LoanDescription
	 * @return
	 */


	@SuppressWarnings("null")
	public static LoanPeriods[] CreatePayrollDeferredDetailLines 
		(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID,int p_AMN_Payroll_ID, int p_AMN_Employee_ID,
				int p_AMN_Concept_Types_Proc_DB_ID, int p_AMN_Concept_Types_Proc_CR_ID,
				BigDecimal p_LoanAmount, int p_LoanQuotaNo, int p_AMN_FirstPeriod_ID,
				String p_LoanDescription, String trxName) {
		
	    int AMN_Period_ID=0;
	    String sql="";
		// MAMN_Contract
		MAMN_Contract amncontract = new MAMN_Contract(Env.getCtx(),p_AMN_Contract_ID,null);		
		// MAMN_Period
		MAMN_Period amnperiod = new MAMN_Period(Env.getCtx(), p_AMN_FirstPeriod_ID, null);
		Timestamp PeriodFirtsDate = amnperiod.getAMNDateEnd();
		//log.warning("PeriodFirtsDate:"+PeriodFirtsDate);
	    //LoanPeriods[] periodList = new LoanPeriods[100];
	    ArrayList<LoanPeriods> periodList = new ArrayList<LoanPeriods>();
	    LoanPeriods loanPeriodData =  new LoanPeriods (AMN_Period_ID, null, PeriodFirtsDate, p_LoanAmount);
	    // 
	    //int AMN_Concept_Types_ID = MAMN_Concept_Types.sqlGetAMNConceptTypesPRESTAMOCR(amncontract.getAD_Client_ID());
	    //int AMN_Concept_Types_Proc_ID = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcPRESTAMOCR(AMN_Concept_Types_ID, p_AMN_Process_ID);
	    MAMN_Concept_Types_Proc amnconcepttypesprocCR = new MAMN_Concept_Types_Proc(Env.getCtx(), p_AMN_Concept_Types_Proc_CR_ID, null);
//log.warning("....p_AMN_Concept_Types_Proc_DB_ID="+p_AMN_Concept_Types_Proc_DB_ID+" ....p_AMN_Concept_Types_Proc_DB_ID="+p_AMN_Concept_Types_Proc_DB_ID);
	    MAMN_Payroll_Deferred amnpayrolldeferred = new MAMN_Payroll_Deferred(Env.getCtx(), 0, null);
		// Cuota Calculation
		double DLoanAmount = p_LoanAmount.doubleValue();
		double DCuotaAmount = 0.00;
		BigDecimal BDCuotaAmount = BigDecimal.valueOf(0);
		BigDecimal BDLoanAmount = BigDecimal.valueOf(0);
		BigDecimal BDLastCuotaAmount = BigDecimal.valueOf(0);
		BigDecimal BDLastCuotaAmountDiff = BigDecimal.valueOf(0);
		if (p_LoanQuotaNo > 0) {
			DCuotaAmount = DLoanAmount / p_LoanQuotaNo;
			BDCuotaAmount = BigDecimal.valueOf(DCuotaAmount).setScale(2, RoundingMode.HALF_DOWN);
			//BDCuotaAmount = p_LoanAmount.divide(BigDecimal.valueOf(p_LoanQuotaNo)).setScale(2, RoundingMode.HALF_DOWN);
			// LAST QUOTE ROUNDED
			BDLoanAmount =  BDCuotaAmount.multiply(BigDecimal.valueOf(p_LoanQuotaNo)) ;
			BDLastCuotaAmountDiff = BDLoanAmount.subtract(p_LoanAmount);
			if ( BDLastCuotaAmountDiff.compareTo(BigDecimal.valueOf(0)) == -1 ) {
				BDLastCuotaAmount = BDCuotaAmount.subtract(BDLastCuotaAmountDiff);
			} else {
				BDLastCuotaAmount = BDCuotaAmount.subtract(BDLastCuotaAmountDiff);
			}
		}
		//log.warning("p_LoanAmount:"+p_LoanAmount+"  BDCuotaAmount"+BDCuotaAmount+"  BDLastCuotaAmount"+BDLastCuotaAmount+"  BDLastCuotaAmountDiff"+BDLastCuotaAmountDiff);
		//
		sql ="SELECT  amn_period_id " +
				"from adempiere.amn_period " +
				"where amn_process_id = ? " +
				"and amn_contract_id = ? " +
				"and amndateend >= '" + PeriodFirtsDate +"' " +
				"ORDER BY amndateini ASC"
				;
		//log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID:"+p_AMN_Contract_ID);
		//log.warning("sql:"+sql+ "  DLoanAmount:"+DLoanAmount+"  DCuotaAmount"+DCuotaAmount);
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Process_ID);
            pstmt1.setInt (2, p_AMN_Contract_ID);
            //pstmt1.setTimestamp(3, PeriodFirtsDate);
            int nPeriods = 0;
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				AMN_Period_ID = rsod1.getInt(1);
				amnperiod = new MAMN_Period(Env.getCtx(), AMN_Period_ID, sql);
				// SET LAS QUOTE
				if (nPeriods == p_LoanQuotaNo - 1)
					BDCuotaAmount = BDLastCuotaAmount;
				//  CREATE MAMN_Payroll Detail
				amnpayrolldeferred.createAmnPayrollDeferred(
						ctx, Env.getLanguage(Env.getCtx()).getLocale(),
						amncontract.getAD_Client_ID(), amncontract.getAD_Org_ID(),  p_AMN_Process_ID, p_AMN_Contract_ID,
						p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_CR_ID, AMN_Period_ID, BDCuotaAmount,
						amnconcepttypesprocCR.getValue(), amnconcepttypesprocCR.getName(), amnconcepttypesprocCR.getDescription(), trxName );
				// Verify nPeriods
				nPeriods = nPeriods +1;
				loanPeriodData = new  LoanPeriods ();
				loanPeriodData.setLoanCuotaNo(nPeriods);
				loanPeriodData.setCuotaAmount(BDCuotaAmount);
				loanPeriodData.setPeriodDate(amnperiod.getAMNDateEnd());
				loanPeriodData.setPeriodValue(amnperiod.getValue());
				periodList.add(loanPeriodData);
				// log.warning("....nPeriods:"+nPeriods+" Date:"+amnperiod.getAMNDateEnd()+"  Period:"+amnperiod.getValue());
				if (nPeriods >= p_LoanQuotaNo)
					break;
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsod1, pstmt1);
			rsod1 = null; pstmt1 = null;
		}
		// COPY TO NEWW ARRAY AND RETURN
		int sizeres = periodList.size();
		// log.warning("  sizeres:"+sizeres);
		LoanPeriods[] new_periodList = new LoanPeriods[sizeres];
		// Create dls		
		periodList.toArray(new_periodList);
//// TRAZA2
//    	for (int i=0 ; i < sizeres; i++) {
//    		loanPeriodData = new_periodList[i];
//log.warning("Traza2:..LoanQuotaNo:"+loanPeriodData.getLoanCuotaNo()+" Periodo:"+loanPeriodData.getPeriodValue()+"  Date:"+loanPeriodData.getPeriodDate().toString().substring(0,10));
// // TRAZA2
//    	}
   		return new_periodList;
		
	}
	
	

}
