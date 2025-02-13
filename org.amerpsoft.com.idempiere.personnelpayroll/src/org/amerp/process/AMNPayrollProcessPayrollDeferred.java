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
import org.amerp.amnmodel.MAMN_Employee;
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
	 * @param ctx
	 * @param amnpayroll
	 * @param amnprocessde
	 * @param amnemployee
	 * @param amnconcepttypesDB
	 * @param amnconcepttypesCR
	 * @param p_LoanAmount
	 * @param p_LoanQuotaNo
	 * @param p_AMN_FirstPeriod_ID
	 * @param p_LoanDescription
	 * @param trxName
	 * @return
	 */

	@SuppressWarnings("null")
	public static ArrayList<LoanPeriods> CreatePayrollDeferredDetailLines 
		(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocessde, MAMN_Employee amnemployee,
				MAMN_Concept_Types_Proc amnconcepttypesDB ,	MAMN_Concept_Types_Proc amnconcepttypesCR, 
				BigDecimal p_LoanAmount, int p_LoanQuotaNo, int p_AMN_FirstPeriod_ID,
				String p_LoanDescription, String trxName) {
		
		// MAMN_Contract
		MAMN_Contract amncontract = new MAMN_Contract(ctx, amnpayroll.getAMN_Contract_ID(),trxName);	
		int PayrollDaysInt =amncontract.getPayRollDays().intValue();
		// 
	    MAMN_Payroll_Deferred amnpayrolldeferred = new MAMN_Payroll_Deferred(ctx, 0, trxName);
		// Precision from Contract Variable
		int roundingMode = 2;
		roundingMode = amncontract.getStdPrecision();
		// MAMN_Period
		MAMN_Period amnperiod = new MAMN_Period(Env.getCtx(), p_AMN_FirstPeriod_ID, trxName);
		// Creates Period List
		LoanPeriods loanPeriodData =  new LoanPeriods();
	    ArrayList<LoanPeriods> periodList = (ArrayList<LoanPeriods>) loanPeriodData.generateLoanPeriods(amnperiod.getAMNDateEnd(), p_LoanQuotaNo, PayrollDaysInt); //new ArrayList<LoanPeriods>();
	    // Cuota Calculation
		double DLoanAmount = p_LoanAmount.doubleValue();
		double DCuotaAmount = 0.00;
		BigDecimal BDCuotaAmount = BigDecimal.valueOf(0);
		BigDecimal BDLoanAmount = BigDecimal.valueOf(0);
		BigDecimal BDLastCuotaAmount = BigDecimal.valueOf(0);
		BigDecimal BDLastCuotaAmountDiff = BigDecimal.valueOf(0);
		if (p_LoanQuotaNo > 0) {
			DCuotaAmount = DLoanAmount / p_LoanQuotaNo;
			BDCuotaAmount = BigDecimal.valueOf(DCuotaAmount).setScale(roundingMode, RoundingMode.HALF_DOWN);
			//BDCuotaAmount = p_LoanAmount.divide(BigDecimal.valueOf(p_LoanQuotaNo)).setScale(2, RoundingMode.HALF_DOWN);
			// LAST QUOTE ROUNDED
			BDLoanAmount =  BDCuotaAmount.multiply(BigDecimal.valueOf(p_LoanQuotaNo)) ;
			BDLastCuotaAmountDiff = BDLoanAmount.subtract(p_LoanAmount);
			if ( BDLastCuotaAmountDiff.compareTo(BigDecimal.valueOf(0)) == -1 ) {
				BDLastCuotaAmount = BDCuotaAmount.subtract(BDLastCuotaAmountDiff);
			} else {
				BDLastCuotaAmount = BDCuotaAmount.subtract(BDLastCuotaAmountDiff);
			}
			log.warning("Prestamo:"+p_LoanDescription);
			for (int i = 0; i < periodList.size(); i++) {
				loanPeriodData = periodList.get(i);
			    String cuota = (i + 1) + " / " + periodList.size();
			    if (i == periodList.size() - 1) {
			    	BDCuotaAmount = BDLastCuotaAmount;
			    } 
		        // Acción normal para los demás registros
				loanPeriodData.setLoanCuotaNo(i+1);
				loanPeriodData.setCuotaAmount(BDCuotaAmount);
				loanPeriodData.setPeriodValue(cuota);
				// Dummy amnperiod if not found()
			    amnperiod = MAMN_Period.findPeriodByDates(amnprocessde.getAMN_Process_ID(), amnpayroll.getAMN_Contract_ID(), loanPeriodData.getPeriodDateIni(), loanPeriodData.getPeriodDateEnd(), trxName);
				if (amnperiod == null) {
					amnperiod = new MAMN_Period(ctx, 0 , trxName);
					amnperiod.setAMNDateIni(loanPeriodData.getPeriodDateIni());
					amnperiod.setAMNDateEnd(loanPeriodData.getPeriodDateEnd());
					amnperiod.setAMN_Period_ID(0);
				}
				// CREATE MAMN_Payroll Deferred
			    amnpayrolldeferred.createAmnPayrollDeferred(ctx, amnpayroll, amnprocessde, amnconcepttypesCR, amnperiod, loanPeriodData, p_LoanAmount, trxName );
				// Verify nPeriods
		        log.warning("Cuota No: " + loanPeriodData.getLoanCuotaNo() +
		                ", Periodo: " + loanPeriodData.getPeriodValue() +
		                ", Inicio de Período: " + loanPeriodData.getPeriodDateIni() +
		                ", Fin de Período: " + loanPeriodData.getPeriodDateEnd() +
		                ", Monto: " + loanPeriodData.getCuotaAmount());
			}
		}
		return periodList;
		
	}
	
	

}
