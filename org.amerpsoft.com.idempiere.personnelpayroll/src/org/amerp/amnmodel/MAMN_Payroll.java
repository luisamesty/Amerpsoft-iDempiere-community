/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 ******************************************************************************/
package org.amerp.amnmodel;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.TaxNoExemptFoundException;
import org.adempiere.util.IProcessUI;
import org.amerp.amnutilities.AmerpDateUtils;
import org.amerp.amnutilities.AmerpPayrollCalcUtilDVFormulas;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;

public class MAMN_Payroll extends X_AMN_Payroll implements DocAction, DocOptions{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2845671218224515322L;
	
	/**	Cache							*/
	private static CCache<Integer,MAMN_Payroll> s_cache = new CCache<Integer,MAMN_Payroll>(Table_Name, 10);

	static CLogger log = CLogger.getCLogger(MAMN_Payroll.class);

	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**	Invoice Lines			*/
	private MAMN_Payroll_Detail[]	m_payrolldetail;
	/**	Invoice Lines			*/
	private MAMN_Payroll_Detail[]	m_payrolldetail2;
	/** Standard Constructor */
	public MAMN_Payroll(Properties ctx, int AMN_Payroll_ID, String trxName) {
		super(ctx, AMN_Payroll_ID, trxName);
		if (AMN_Payroll_ID == 0)
		{
	   		//Default Values
			setDocStatus (DOCSTATUS_Drafted);		//	Draft
			setDocAction (DOCACTION_Complete);
			setIsActive(true);
			setIsPrinted(false);
			setIsApproved(false);
			setProcessed(false);
			setPosted(false);
			setIsPaid(false);		
		}
	}

	/** Load Constructor */
	public MAMN_Payroll(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * Get Payroll from Cache
	 * @param ctx context
	 * @param MAMN_Payroll id
	 * @return MAMMN_Payroll
	 */
	public static MAMN_Payroll get (Properties ctx, int p_MAMN_Payroll_ID)
	{
		if (p_MAMN_Payroll_ID <= 0)
			return null;
		//
		Integer key = new Integer(p_MAMN_Payroll_ID);
		MAMN_Payroll retValue = (MAMN_Payroll) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MAMN_Payroll (ctx, p_MAMN_Payroll_ID, null);
		if (retValue.getAMN_Payroll_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get

	
	/**
	 * findByAMNPayroll
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Period_ID
	 * @param p_AMN_Employee_ID
	 * @return MAMN_Payroll
	 */
	public static MAMN_Payroll findByAMNPayroll(Properties ctx, Locale locale, 
				int p_AMN_Process_ID,  int p_AMN_Contract_ID,
				int p_AMN_Period_ID, int p_AMN_Employee_ID) {
				
		MAMN_Payroll retValue = null;
		String sql = "SELECT * "
			+ "FROM amn_payroll "
			+ "WHERE amn_process_id=?"
			+ " AND amn_contract_id=?"
			+ " AND amn_period_id=?"
			+ " AND amn_employee_id=?"
			;        
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Process_ID);
            pstmt.setInt (2, p_AMN_Contract_ID);
            pstmt.setInt (3, p_AMN_Period_ID);
            pstmt.setInt (4, p_AMN_Employee_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, rs, null);
				Integer key = new Integer(amnpayroll.getAMN_Period_ID());
				s_cache.put (key, amnpayroll);
				if (amnpayroll.isActive())
					retValue = amnpayroll;
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
		return retValue;
	}
	
	/**
	 * updateAMNPayroll
	 * Special Updates Header Variables
	 * Depending on Process Value 
	 * @param ctx
	 * @param p_AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public boolean updateAMNPayroll(Properties ctx, String AMN_Process_Value,   int p_AMN_Payroll_ID, String trxName) {
		
		// PROCESS BUSINESSLOGIC LOGIC
	    // Process depending
		if (AMN_Process_Value.equalsIgnoreCase("NN") ||
				AMN_Process_Value.equalsIgnoreCase("TI") ) {
			// ************************
			// Process NN an TI	
			// ************************		
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
			// ************************
			// Process NV VACATION	
			// ************************
			// Data from AMN_Employee - AMN_Shift (isSaturdayBusinessDay )
			MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, trxName);
			MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(), trxName);
			Timestamp employeeIncomeDate=amnemployee.getincomedate();
			MAMN_Shift shift = null;
			if (amnemployee.getAMN_Shift_ID() != 0)
				shift = new MAMN_Shift(ctx, amnemployee.getAMN_Shift_ID(), trxName);
			else 
			  shift = new MAMN_Shift();
			boolean isSaturdayBusinessDay = shift.isSaturdayBusinessDay(shift.getAMN_Shift_ID());
			// Calculations from Dates
			Timestamp receiptDateIni = amnpayroll.getInvDateIni();
			// Get receipt DaysVacaction and Set Elapsed time (-1)
			BigDecimal DaysVacation = AmerpPayrollCalcUtilDVFormulas.DV_VACACION(ctx, p_AMN_Payroll_ID, trxName);
			Integer elapsedDaysVacation = (Integer) amnpayroll.getDaysVacation();
			if (elapsedDaysVacation != null && elapsedDaysVacation > 0) {
				DaysVacation = BigDecimal.valueOf(amnpayroll.getDaysVacation());
				elapsedDaysVacation = elapsedDaysVacation -1;
			}
			BigDecimal DaysVacationCollective = BigDecimal.valueOf(amnpayroll.getDaysVacationCollective());
			// Get receipt DaysVacactionCollective and Set Elapsed time (-1)
			Integer elapsedDaysVacationCollective = (Integer) amnpayroll.getDaysVacationCollective();
			if(elapsedDaysVacationCollective != null && elapsedDaysVacationCollective >0 )
				elapsedDaysVacationCollective = elapsedDaysVacationCollective -1;
			// Receipt App Calc
			// Application Day 15 Days 
			Timestamp receiptDateApplication = MAMN_NonBusinessDay.getPreviusCalendarDay(receiptDateIni,  BigDecimal.valueOf(14), amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
			if (!MAMN_NonBusinessDay.isBusinessDay(isSaturdayBusinessDay, receiptDateApplication, amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID() )) {
				receiptDateApplication = MAMN_NonBusinessDay.getPreviusBusinessDay(isSaturdayBusinessDay, receiptDateApplication,  BigDecimal.ONE, amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
			}
			// Receipt Date
			Timestamp receiptDateReceipt = MAMN_NonBusinessDay.getPreviusBusinessDay(isSaturdayBusinessDay, receiptDateIni,  BigDecimal.ONE, amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
			// Dates
			LocalDateTime localDTEmployee = employeeIncomeDate.toLocalDateTime();
			// Last work aniversary since Receipt Ini
			LocalDateTime lastWorkAniversary = amnemployee.getLastWorkAnniversary(receiptDateIni).toLocalDateTime();
			// REEntry Dates (legal and Real)
			Timestamp receiptDateEnd = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, receiptDateIni,  BigDecimal.valueOf(elapsedDaysVacation), amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
			Timestamp receiptDateReEntry = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, receiptDateEnd,  BigDecimal.ONE, amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
			Timestamp receiptDateReEntryReal = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, receiptDateIni,  BigDecimal.valueOf(elapsedDaysVacation).subtract(BigDecimal.valueOf(elapsedDaysVacationCollective)), amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
			int dia = localDTEmployee.getDayOfMonth();
			int mes = localDTEmployee.getMonthValue();
			// Subtract -1 to lastWorkAniversary to gete begining year
			int anio = lastWorkAniversary.getYear()-1;
			// Crear fecha1 usando LocalDate
	        LocalDate fecha1 = LocalDate.of(anio, mes, dia);
	        Timestamp vacationPeriodIni = Timestamp.valueOf(fecha1.atStartOfDay());
	        // Crear fecha2, un año más tarde
	        LocalDate fecha2 = fecha1.plusYears(1);
	        Timestamp vacationPeriodEnd = Timestamp.valueOf(fecha2.atStartOfDay());
	        // Construir el String con el rango
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String PayrollDescription=AmerpUtilities.truncate(amnemployee.getValue()+"- "+
					 Msg.translate(ctx, "from") + " "+ fecha1.format(formatter) + " " +
					 Msg.translate(ctx, "to") + " " + fecha2.format(formatter),255);	
			// Update AMN_Payroll (HEADER VALUES)
            String sql = "UPDATE AMN_Payroll "
            		+ " set DaysVacation="+DaysVacation+","
            		+ " DaysVacationCollective='"+DaysVacationCollective+"',"
            		+ " description='"+PayrollDescription+"',"
            		+ " InvDateEnd='"+receiptDateEnd+"',"
            		+ " DateReEntry='"+receiptDateReEntry+"',"
            		+ " DateReEntryReal='"+receiptDateReEntryReal+"',"
            		+ " RefDateIni='"+vacationPeriodIni+"',"
            		+ " RefDateEnd='"+vacationPeriodEnd+"',"
            		+ " DateApplication='"+receiptDateApplication+"',"
            		+ " InvDateRec='"+receiptDateReceipt+"',"
					+ " month="+mes+","
					+ " year="+anio
					+ " where amn_payroll_id ="+p_AMN_Payroll_ID;
            DB.executeUpdateEx(sql, null);
            
		} else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
			// ************************
			// Process NP
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NU")) { 
			// ************************
			// Process NU
			// ************************
			SimpleDateFormat nuformat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
			MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, trxName);
			MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(), trxName);
			MAMN_Period amnperiod = new MAMN_Period(ctx, amnpayroll.getAMN_Period_ID(), trxName);
			Timestamp refDateIni = amnperiod.getRefDateIni();
			Timestamp refDateEnd = amnperiod.getRefDateEnd();
			String PayrollDescription = AmerpUtilities.truncate(
				    amnemployee.getValue() + "- " +
				    Msg.translate(ctx, "from") + " " + nuformat.format(amnperiod.getRefDateIni()) + " " +
				    Msg.translate(ctx, "to") + " " + nuformat.format(amnperiod.getRefDateEnd()), 255);
			// Updates Header
			String sql = "UPDATE AMN_Payroll "
			           + "SET description = ?, "
			           + "RefDateIni = ?, "
			           + "RefDateEnd = ? "
			           + "WHERE amn_payroll_id = ?";

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
			    pstmt = DB.prepareStatement(sql, null);
			    pstmt.setString(1, PayrollDescription);
			    pstmt.setTimestamp(2, refDateIni);
			    pstmt.setTimestamp(3, refDateEnd);
			    pstmt.setInt(4, p_AMN_Payroll_ID);
			    pstmt.executeUpdate();
			} catch (Exception e) {
			   log.warning("Error en la actualización: " + e.getMessage());
			} finally {
			    DB.close(pstmt);
			}
	 
		} else if (AMN_Process_Value.equalsIgnoreCase("PL")) { 
			// ************************
			// Process PL
			// ************************
			// Update AMN_Payroll (HEADER VALUES)
			SimpleDateFormat plformat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
			MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, trxName);
			MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(), trxName);
			MAMN_Period amnperiod = new MAMN_Period(ctx, amnpayroll.getAMN_Period_ID(), trxName);
			MAMN_Process amnprocess = new MAMN_Process(ctx, amnpayroll.getAMN_Process_ID(), trxName);
			Timestamp refDateIni = amnperiod.getRefDateIni();
			Timestamp refDateEnd = amnperiod.getRefDateEnd();
			String PayrollDescription = AmerpUtilities.truncate(
				    amnemployee.getValue() + " - " +
				    amnprocess.getName().trim()+ " " + 
				    Msg.getElement(ctx, "DateTo") + " " + plformat.format(amnpayroll.getInvDateEnd()), 255);
			// Updates Header
			String sql = "UPDATE AMN_Payroll "
			           + "SET description = ?, "
			           + "RefDateIni = ?, "
			           + "RefDateEnd = ? "
			           + "WHERE amn_payroll_id = ?";

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
			    pstmt = DB.prepareStatement(sql, null);
			    pstmt.setString(1, PayrollDescription);
			    pstmt.setTimestamp(2, refDateIni);
			    pstmt.setTimestamp(3, refDateEnd);
			    pstmt.setInt(4, p_AMN_Payroll_ID);
			    pstmt.executeUpdate();
			} catch (Exception e) {
			   log.warning("Error en la actualización: " + e.getMessage());
			} finally {
			    DB.close(pstmt);
			}
		} else {
			
		}
		return true;
	}
	
	/**
	 * createAmnPayroll:
	 * Creates AMN_payroll Record
	 * @param ctx
	 * @param locale
	 * @param int p_AMN_Process_ID	Payroll Process
	 * @param int p_AMN_Contract_ID	Payroll Contract
	 * @param int p_AMN_Period_ID
	 * @param int p_AMN_Payroll_Lot_ID
	 * @param int p_AMN_Employee_ID
	 * @param int p_AMN_Payroll_ID
	 * @return MAMN_Period
	 */
	public boolean createAmnPayroll(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID, int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID, 
			Timestamp p_DateAcct, Timestamp p_InvDateIni, Timestamp p_InvDateEnd, Timestamp p_RefDateIni, Timestamp p_RefDateEnd, 
			String trxName) {
		
		Integer Currency_ID = 0;
		Integer ConversionType_ID = MConversionType.TYPE_SPOT;
		// Default Local Currency for Client
		Integer m_defaultCurrency_ID = Env.getContextAsInt(ctx, "$C_Currency_ID");
		String Process_Value = "",Contract_Value="",Employee_Value="",Payroll_Value="",Period_Value="";
		String Employee_Name="",Payroll_Name="";
		String PayrollDescription="";
		String DocumentNo="";
		Integer DocType_ID;
		// AMN_Contract Attributes Variables
		BigDecimal PayRollDays;
		Integer AcctDow;
		Integer InitDow;
		// Rest of Variables
		GregorianCalendar cal = new GregorianCalendar();
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
		MAMN_Process amnprocess = new MAMN_Process(ctx, p_AMN_Process_ID, null);
    	MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
    	MAMN_Period amnperiod = new MAMN_Period(ctx, p_AMN_Period_ID, null);
    	MAMN_Contract amncontract = new MAMN_Contract(ctx, p_AMN_Contract_ID, null);
    	// AMN_Period Cache
    	Process_Value=amnprocess.getValue().trim();
   		Contract_Value=amncontract.getValue().trim();
   		Employee_Value=amnemployee.getValue().trim();
   		Employee_Name=amnemployee.getName().trim();
   		Period_Value=amnperiod.getValue().trim();
   		if (p_AD_Org_ID == 0 && amnemployee.getAD_OrgTo_ID()!=0)
   			p_AD_Org_ID = amnemployee.getAD_OrgTo_ID();
   		// Default Currency  for Contract
   		Currency_ID = AmerpUtilities.defaultAMNContractCurrency(p_AMN_Contract_ID);
   		// Default ConversionType for Contract
   		ConversionType_ID = AmerpUtilities.defaultAMNContractConversionType(p_AMN_Contract_ID);
   		DocumentNo=DocumentNo+Employee_Value+01;
    	Payroll_Value=AmerpUtilities.truncate((Process_Value+"-"+Contract_Value+"-"+Employee_Value+"-"+Period_Value),39);
		Payroll_Name=AmerpUtilities.truncate((Process_Value+"-"+Contract_Value+"-"+Employee_Name),59);
		PayrollDescription=AmerpUtilities.truncate((Process_Value+"-"+Contract_Value+"-"+Employee_Name+"-"+Period_Value),255);		
    	PayRollDays = amncontract.getPayRollDays();
    	AcctDow = Integer.parseInt(amncontract.getAcctDow().trim());	    	
    	InitDow = Integer.parseInt(amncontract.getInitDow().trim());	 
    	// C_Doctype_ID
    	String sql = "select c_doctype_id from c_doctype WHERE ad_client_id="+p_AD_Client_ID+"  AND docbasetype='HRP' " ;
    	DocType_ID = (Integer) DB.getSQLValue(null, sql);
    	// Verify if Seven
    	if (PayRollDays.equals(BigDecimal.valueOf(7))) {
			if (InitDow <= AcctDow ) {
				cal.setTime(amnperiod.getAMNDateIni());
				cal.add(Calendar.DAY_OF_YEAR,  AcctDow - InitDow );
			} else {
				cal.setTime(amnperiod.getAMNDateEnd());
				cal.add(Calendar.DAY_OF_YEAR, AcctDow - InitDow );
			}		
    	} else {
    		cal.setTime(amnperiod.getAMNDateEnd());
    	}
 		//log.warning("................Values in MAMN_Payroll ...................");
		//log.warning(" p_AMN_Period_ID:"+p_AMN_Period_ID+"  p_AMN_Contract_ID:"+p_AMN_Contract_ID
		//		+"  p_AMN_Process_ID:"+p_AMN_Process_ID
		//		+"  p_AMN_Employee_ID:"+p_AMN_Employee_ID +"  C_Activity_ID:"+amnemployee.getC_Activity_ID());
		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		MAMN_Payroll amnpayroll = null;
		// Verify if p_AMN_Payroll_ID is in parameter and != 0
		//log.warning("p_AMN_Payroll_ID="+p_AMN_Payroll_ID);
		//log.warning("Currency_ID:"+Currency_ID+" p_AMN_Period_ID:"+p_AMN_Period_ID+"  p_AMN_Contract_ID:"+p_AMN_Contract_ID+"  p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Employee_ID:"+p_AMN_Employee_ID);
		if (p_AMN_Payroll_ID == 0) {
			amnpayroll = MAMN_Payroll.findByAMNPayroll(ctx, locale, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID);
		} else {
			amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, trxName);
		}
		if (amnpayroll == null)
		{
			//log.warning("................Values in MAMN_Payroll (NUEVO)...................");
			amnpayroll = new MAMN_Payroll(getCtx(), 0, get_TrxName());
			amnpayroll.setAD_Client_ID(p_AD_Client_ID);
			amnpayroll.setAD_Org_ID(p_AD_Org_ID);
			amnpayroll.setAMN_Process_ID(p_AMN_Process_ID);
			amnpayroll.setAMN_Contract_ID(p_AMN_Contract_ID);
			amnpayroll.setAMN_Employee_ID(p_AMN_Employee_ID);
			amnpayroll.setAMN_Period_ID(p_AMN_Period_ID);
			amnpayroll.setAMN_Payroll_Lot_ID(p_AMN_Payroll_Lot_ID);
			amnpayroll.setAMN_Department_ID(amnemployee.getAMN_Department_ID());
			amnpayroll.setAMN_Location_ID(amnemployee.getAMN_Location_ID());
			amnpayroll.setAMN_Jobtitle_ID(amnemployee.getAMN_Jobtitle_ID());
			amnpayroll.setAMN_Jobstation_ID(amnemployee.getAMN_Jobstation_ID());
			amnpayroll.setC_Activity_ID(amnemployee.getC_Activity_ID());
			amnpayroll.setC_Project_ID(amnemployee.getC_Project_ID());
			amnpayroll.setC_Campaign_ID(amnemployee.getC_Campaign_ID());
			amnpayroll.setC_SalesRegion_ID(amnemployee.getC_SalesRegion_ID());
			amnpayroll.setIsActive(true);
	    	if (p_DateAcct != null ) {
	    		amnpayroll.setDateAcct(p_DateAcct);
	    	} else {
				amnpayroll.setDateAcct(amnperiod.getAMNDateEnd());
	    	}
	    	if (p_InvDateIni != null ) {
	    		amnpayroll.setInvDateIni(p_InvDateIni);
	    	} else {
	    		amnpayroll.setInvDateIni(amnperiod.getAMNDateIni());
	    	}
	    	if (p_InvDateEnd != null ) {
				amnpayroll.setInvDateEnd(p_InvDateEnd);
	    	} else {
				amnpayroll.setInvDateEnd(amnperiod.getAMNDateEnd());
	    	}
	    	if (p_RefDateIni != null ) {
	    		amnpayroll.setRefDateEnd(p_RefDateIni);
	    	} else {
	    		amnpayroll.setRefDateEnd(amnperiod.getRefDateEnd());
	    	}
	    	if (p_RefDateEnd != null  ) {
	    		amnpayroll.setRefDateIni(p_RefDateEnd);
	    	} else {
	    		amnpayroll.setRefDateIni(amnperiod.getRefDateIni());
	    	}
			amnpayroll.setC_DocType_ID(0);
			amnpayroll.setC_DocTypeTarget_ID(DocType_ID);
			amnpayroll.setValue(Payroll_Value);
			amnpayroll.setName(Payroll_Name);
			amnpayroll.setDescription(PayrollDescription);
			amnpayroll.setAmountCalculated(BigDecimal.valueOf(0.00));
			amnpayroll.setAmountAllocated(BigDecimal.valueOf(0.00));
			amnpayroll.setAmountDeducted(BigDecimal.valueOf(0.00));
			amnpayroll.setAmountNetpaid(BigDecimal.valueOf(0.00));
			amnpayroll.setC_Currency_ID(Currency_ID);
			amnpayroll.setC_ConversionType_ID(ConversionType_ID);
			amnpayroll.setC_Currency_ID_To(m_defaultCurrency_ID);
			amnpayroll.setIsActive(true);
			amnpayroll.setIsPrinted(false);
			amnpayroll.setIsApproved(false);
			amnpayroll.setProcessed(false);
			amnpayroll.setPosted(false);
			amnpayroll.setIsPaid(false);
			amnpayroll.setDocAction("CO");
			amnpayroll.setDocStatus(DOCSTATUS_Drafted);
			amnpayroll.setDocumentNo("");
			// SAVES NEW
			//amnpayroll.saveNew_getID();
			//log.warning("get_TrxName()="+get_TrxName());
			amnpayroll.save(get_TrxName());
		} else 	{
			//log.warning("................Values in MAMN_Payroll (UPDATE)...................");
			//log.warning(" p_AMN_Period_ID:"+p_AMN_Period_ID+"  p_AMN_Contract_ID:"+p_AMN_Contract_ID+"  p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Employee_ID:"+p_AMN_Employee_ID);
			amnpayroll.setValue(Payroll_Value);
			amnpayroll.setName(Payroll_Name);
			amnpayroll.setDescription(PayrollDescription);
	    	if (p_DateAcct != null ) {
	    		amnpayroll.setDateAcct(p_DateAcct);
	    	} else {
				amnpayroll.setDateAcct(amnperiod.getAMNDateEnd());
	    	}
	    	if (p_InvDateIni != null ) {
	    		amnpayroll.setInvDateIni(p_InvDateIni);
	    	} else {
	    		amnpayroll.setInvDateIni(amnperiod.getAMNDateIni());
	    	}
	    	if (p_InvDateEnd != null ) {
				amnpayroll.setInvDateEnd(p_InvDateEnd);
	    	} else {
				amnpayroll.setInvDateEnd(amnperiod.getAMNDateEnd());
	    	}
	    	if (p_RefDateIni != null ) {
	    		amnpayroll.setRefDateEnd(p_RefDateIni);
	    	} else {
	    		amnpayroll.setRefDateEnd(amnperiod.getRefDateEnd());
	    	}
	    	if (p_RefDateEnd != null  ) {
	    		amnpayroll.setRefDateIni(p_RefDateEnd);
	    	} else {
	    		amnpayroll.setRefDateIni(amnperiod.getRefDateIni());
	    	}
			amnpayroll.setAMN_Department_ID(amnemployee.getAMN_Department_ID());
			amnpayroll.setAMN_Location_ID(amnemployee.getAMN_Location_ID());
			amnpayroll.setAMN_Jobtitle_ID(amnemployee.getAMN_Jobtitle_ID());
			amnpayroll.setAMN_Jobstation_ID(amnemployee.getAMN_Jobstation_ID());
			amnpayroll.setC_Activity_ID(amnemployee.getC_Activity_ID());
			amnpayroll.setC_Project_ID(amnemployee.getC_Project_ID());
			amnpayroll.setC_Campaign_ID(amnemployee.getC_Campaign_ID());
			amnpayroll.setC_SalesRegion_ID(amnemployee.getC_SalesRegion_ID());
			amnpayroll.setC_Currency_ID(Currency_ID);
			amnpayroll.setC_ConversionType_ID(ConversionType_ID);
			amnpayroll.setC_Currency_ID_To(m_defaultCurrency_ID);
			//log.warning("get_TrxName()="+get_TrxName());
			amnpayroll.save(get_TrxName());
		}
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(ctx, "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(String.format("%-15s","Receipt ").replace(' ', '_')+
				Msg.getElement(ctx, "AMN_Employee_ID")+": "+
				String.format("%-50s",amnemployee.getValue()+"-"+amnemployee.getName()).replace(' ', '_')+
				Msg.getElement(ctx, "AMN_Concept_Types_ID")+": "+
				String.format("%-50s",Payroll_Value+"-"+Payroll_Name).replace(' ', '_'));
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control

		return true;
		
	}	//	createAmnPayroll

	/**
	 * Procesa AMN_Payroll y C_Invoice si aplica.
	 *
	 * @param ctx        Contexto de la aplicación.
	 * @param amnpayroll Objeto de nómina AMN_Payroll.
	 * @param amnprocess Objeto de proceso AMN_Process.
	 * @param trxName    Nombre de la transacción.
	 * @return Mensaje de resultado del proceso.
	 */
	public String processAMN_Payroll(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocess, String trxName) {
	    StringBuilder msgBuilder = new StringBuilder(amnpayroll.getSummary()).append("\n");
	    boolean okprocess = false, okinvoice = false, okcreditmemo = false;
	    MInvoice minvoice = null;
	    String returnMsg ="";
	    
	    if (amnprocess.isDocControlled()) {
	        // Procesar factura (Invoice)
	    	// Si no esta definido el tipo de document NO Procede 
	        returnMsg =Msg.translate(ctx, "Process")+": "+Msg.getElement(ctx, MAMN_Process.COLUMNNAME_C_DocTypeTarget_ID)+"\r\n";
	    	if (amnprocess.getC_DocTypeTarget_ID() != 0) {
	            minvoice = processInvoice(ctx, amnpayroll, amnprocess, returnMsg, trxName);
	            okinvoice = (minvoice != null);
	        } else {
	        	msgBuilder.append(Msg.getElement(ctx, MAMN_Process.COLUMNNAME_C_DocTypeTarget_ID)+
	        			Msg.translate(ctx,"NotFound")+"\r\n" );
	        	okinvoice=false;
	        }

	        // Procesar nota de crédito (Credit Memo)
	        // Si no esta definido el tipo de documento SI Procede y continua
	        returnMsg =Msg.translate(ctx, "Process")+": "+Msg.getElement(ctx, MAMN_Process.COLUMNNAME_C_DocTypeCreditMemo_ID)+"\r\n";
	        if (amnprocess.getC_DocTypeCreditMemo_ID() != 0) {
	        	okcreditmemo = processCreditMemos(ctx, amnpayroll, amnprocess, returnMsg, trxName);
	            if (okcreditmemo) {
	            	msgBuilder.append(returnMsg+" OK "+"\r\n");
	            } else {
	            	MDocType mdoctype = new MDocType(ctx, amnprocess.getC_DocTypeCreditMemo_ID(), trxName);
	            	msgBuilder.append(returnMsg+Msg.translate(ctx,mdoctype.getName())+"\r\n" );
	            }
	        } else {
	        	msgBuilder.append(returnMsg+" OK"+"\r\n");
	        	okcreditmemo = true;
	        }
	    }

	    // ** ACTUALIZAR SALARIO SI PROCESO ES "NN" **
	    // ** PROCESS AMN_PAYROLL DOCUMENT 			**
        returnMsg =Msg.translate(ctx, "Process")+" "+
        		Msg.getElement(ctx, MAMN_Payroll.COLUMNNAME_AMN_Payroll_ID)+"\r\n";
	    if (!amnprocess.isDocControlled() || ( amnprocess.isDocControlled() && okinvoice && okcreditmemo)) {
	        try {
	            if ("NN".equalsIgnoreCase(amnprocess.getAMN_Process_Value())) {
	                msgBuilder.append(updateSalaryHistoric(ctx, amnpayroll, trxName));
	            }
	            okprocess = amnpayroll.processIt(MAMN_Payroll.DOCACTION_Complete);
//				if (MClient.isClientAccountingQueue()) {
//					DocumentEngine.postImmediate(Env.getCtx(), getAD_Client_ID(), amnpayroll.get_Table_ID(), amnpayroll.getAMN_Payroll_ID(), true, trxName);
//				}
	        } catch (Exception e) {
	            msgBuilder.append(" ** ERROR: ").append(e.getMessage()).append(" **\r\n");
	        }
	    }

	    msgBuilder.insert(0, okprocess ? " ** "+Msg.translate(ctx, "Success").trim()+" **\r\n" : " ** "+Msg.translate(ctx, "Failed").trim() +"**\r\n");
	    msgBuilder.insert(0, returnMsg);
	    return msgBuilder.toString();
	}	

	private MInvoice processInvoice(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocess, String returnMsg, String trxName) {
        
		MAMN_Invoice minvoice = null;

        // Buscar factura existente en `AMN_Payroll_Docs`
        MAMN_Payroll_Docs existingInvoiceDoc = MAMN_Payroll_Docs.getFirstByPayrollAndDocTypeID(
                ctx, amnpayroll.getAMN_Payroll_ID(), amnprocess.getC_DocTypeTarget_ID(), trxName);

        if (existingInvoiceDoc != null) {
        	minvoice = new MAMN_Invoice(ctx, existingInvoiceDoc.getC_Invoice_ID() , trxName);
            if (minvoice != null && (minvoice.isComplete() || minvoice.isPaid())) {
                log.warning("Factura ya procesada o con asignaciones.");
                returnMsg = returnMsg + Msg.translate(ctx, "InvoiceProcessed") + minvoice.getDocumentNo() ;
                return null;
            }
        } else {
            minvoice = (MAMN_Invoice) new MAMN_Invoice(ctx, 0, trxName);
            returnMsg = returnMsg + Msg.getElement(ctx, "C_Invoice_ID").trim() + " "+Msg.translate(ctx, "New").trim();
        }

        // Configurar la factura
        minvoice.setGrandTotal(amnpayroll.getAmountNetpaid());
        minvoice.setC_DocType_ID(amnprocess.getC_DocTypeTarget_ID());
        minvoice.setC_DocTypeTarget_ID(amnprocess.getC_DocTypeTarget_ID());
        minvoice = (MAMN_Invoice) createCInvoiceDoc(ctx, amnpayroll, minvoice, getChargeProcess(amnprocess), trxName);

        // Completar factura
        if (minvoice.getDocStatus().equals(DocAction.STATUS_Drafted)) {
            minvoice.processIt(DocAction.STATUS_Completed);
        }

        // Crear o actualizar `AMN_Payroll_Docs`
        int conceptTypesID = MAMN_Concept_Types.sqlGetAMNConceptTypesSB(amnpayroll.getAD_Client_ID());
        MAMN_Payroll_Docs.createOrUpdate(ctx, amnpayroll.getAMN_Payroll_ID(), conceptTypesID, minvoice, trxName);

        return minvoice;
    }

    private boolean processCreditMemos(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocess, String returnMsg, String trxName) {
        
    	List<MAMN_Concept_Types> concepts = MAMN_Concept_Types.getFilteredConcepts("B", "C", amnprocess.getAMN_Process_ID());

        if (concepts.isEmpty()) {
        	// Retorna true porque no encuentra conceptos definidos
            return true;
        }

        Map<Integer, MAMN_Payroll_Detail> payrollDetails = MAMN_Payroll_Detail.findPayrollDetailsByConcepts(ctx, amnpayroll.getAMN_Payroll_ID(), concepts);
        if (payrollDetails.isEmpty()) {
        	// Retorna true porque no encuentra conceptos en el recibo
            return true;
        }

        for (MAMN_Payroll_Detail detail : payrollDetails.values()) {
            int conceptTypesID = new MAMN_Concept_Types_Proc(ctx, detail.getAMN_Concept_Types_Proc_ID(), trxName).getAMN_Concept_Types_ID();

            MAMN_Payroll_Docs existingCreditMemo = MAMN_Payroll_Docs.getFirstByPayrollAndConceptTypesID(ctx, amnpayroll.getAMN_Payroll_ID(), conceptTypesID, trxName);
            MAMN_Invoice creditMemo = (existingCreditMemo != null) ? new MAMN_Invoice(ctx, existingCreditMemo.getC_Invoice_ID(), trxName) : new MAMN_Invoice(ctx, 0, trxName);

            if (creditMemo != null && (creditMemo.isComplete() || creditMemo.isPaid())) {
                log.warning("Nota de crédito ya procesada o con asignaciones.");
                returnMsg = returnMsg + "Nota de crédito ya procesada o con asignaciones."+"\r\n";
                return false;
            }

            creditMemo.setGrandTotal(detail.getAmountDeducted());
            creditMemo.setC_DocType_ID(amnprocess.getC_DocTypeCreditMemo_ID());
            creditMemo.setC_DocTypeTarget_ID(amnprocess.getC_DocTypeCreditMemo_ID());
            creditMemo = (MAMN_Invoice) createCInvoiceDoc(ctx, amnpayroll, creditMemo, getChargeProcess(amnprocess), trxName);

            if (creditMemo.getDocStatus().equals(DocAction.STATUS_Drafted)) {
                creditMemo.processIt(DocAction.STATUS_Completed);
            }

            MAMN_Payroll_Docs.createOrUpdate(ctx, amnpayroll.getAMN_Payroll_ID(), conceptTypesID, creditMemo, trxName);
        }

        return true;
    }

    private String getChargeProcess(MAMN_Process amnprocess) {
        MDocType mdoctype = new MDocType(getCtx(), amnprocess.getC_DocTypeTarget_ID(), get_TrxName());

        switch (mdoctype.getDocBaseType()) {
            case "API":
            case "ARI":
                return "NN";
            case "ARC":
                return "PO";
            default:
                return "NN";
        }
    }
    
    /**
     * createCInvoiceDoc
     * Create CInvoice Document (Headers and lines)
     * @param ctx
     * @param amnpayroll
     * @param mdoctype
     * @param amnprocess
     * @param P_Msg_Value
     * @param trxName
     * @return
     */
    private MInvoice createCInvoiceDoc( Properties ctx, MAMN_Payroll amnpayroll, MInvoice minvoice, String chargeProcess,  String trxName) {
		

		// Invoice Header
    	createCInvoiceHdr(getCtx(), amnpayroll,  minvoice,  trxName);
		// Creates Invoice Lines 
		createCInvoiceLines(getCtx(), minvoice, amnpayroll, chargeProcess, trxName);
		log.warning("Invoice:"+minvoice.getDocumentNo().trim() +"  AD_Org_ID:"+ minvoice.getAD_Org_ID()+" "+ minvoice.getDescription().trim() );
    	return minvoice;
    }
    
    
    /**
	 * createCInvoice: 
	 * Create Payroll Invoice Header
	 * @param ctx
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param p_AMN_Payroll_ID
	 * @param minvoice
	 * @param trxName
	 * @return
	 */
	private int createCInvoiceHdr(Properties ctx, MAMN_Payroll amnpayroll, MInvoice minvoice, String trxName) {
		int retValue = 0;
		Integer Currency_ID = 0;
		Integer ConversionType_ID = MConversionType.TYPE_SPOT;
		String Contract_Value="";
		String Employee_Value="";
		String Payroll_Value="";
		String Employee_Name="";
		String Payroll_Name="";
		String PayrollDescription="";
		String DocumentNo="";
		MAMN_Process amnprocess = new MAMN_Process(ctx, amnpayroll.getAMN_Process_ID(), trxName);
    	MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(), trxName);
    	MAMN_Period amnperiod = new MAMN_Period(ctx, amnpayroll.getAMN_Period_ID(), trxName);
    	MAMN_Contract amncontract = new MAMN_Contract(ctx, amnpayroll.getAMN_Contract_ID(), trxName);
    	MBPartner billBp = null;
    	MBPartner empBp = new MBPartner(ctx, amnemployee.getC_BPartner_ID(), trxName);
    	// Default Org Location Location
    	MOrgInfo oi = MOrgInfo.get( amnemployee.getAD_OrgTo_ID(), trxName);
    	int Default_C_BPartner_Location_ID = oi.getC_Location_ID();
    	// Get employe  BPartner for Invoice	
    	if (amnemployee.getBill_BPartner_ID() != 0) {
    		billBp = new MBPartner(ctx, amnemployee.getBill_BPartner_ID(), trxName);
       		Contract_Value="BP";
       		Employee_Value=billBp.getValue().trim();
       		Employee_Name=billBp.getName().trim();
    	} else {
    		billBp = new MBPartner(ctx, amnemployee.getC_BPartner_ID(), trxName);
       		Contract_Value=amncontract.getValue().trim();
       		Employee_Value=amnemployee.getValue().trim();
       		Employee_Name=amnemployee.getName().trim();
    	}
    	// AMN_Period Cache
   		// Default Currency  for Contract
   		Currency_ID = AmerpUtilities.defaultAMNContractCurrency(amncontract.getAMN_Contract_ID());
   		// Default ConversionType for Contract
   		ConversionType_ID = AmerpUtilities.defaultAMNContractConversionType(amncontract.getAMN_Contract_ID());
   		DocumentNo=DocumentNo+Employee_Value+01;
    	Payroll_Value=AmerpUtilities.truncate((amnprocess.getValue().trim()+"-"+Contract_Value+"-"+Employee_Value+"-"+amnperiod.getValue().trim()),39);
		Payroll_Name=AmerpUtilities.truncate((amnprocess.getValue().trim()+"-"+Contract_Value+"-"+Employee_Name),59);
		PayrollDescription=AmerpUtilities.truncate((amnprocess.getValue().trim()+"-"+Contract_Value+"-"+Employee_Name+"-"+amnperiod.getValue().trim()),255);		
    	// Get Default Price List
		MPriceList pl = new MPriceList(getCtx(), 0, null);
		pl = MPriceList.getDefault(getCtx(), false);
		if (pl == null)
			pl = new MPriceList(getCtx(), 1000000, null);
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		// CReates Invoice Header
		if (trxName == null) {
			trxName = Trx.createTrxName("PayrollInvoice");
		}
		amnpayroll.set_TrxName(trxName);
		// C_Invoice
		minvoice.setAD_Org_ID(amnemployee.getAD_OrgTo_ID());
		minvoice.setDescription(PayrollDescription);
		minvoice.setC_Activity_ID(amnemployee.getC_Activity_ID());
		minvoice.setC_Project_ID(amnemployee.getC_Project_ID());
		minvoice.setC_Currency_ID(Currency_ID);
		minvoice.setC_ConversionType_ID(ConversionType_ID);
		minvoice.setDateAcct(amnpayroll.getDateAcct());
		minvoice.setDateInvoiced(amnpayroll.getDateAcct());
		minvoice.setC_Currency_ID(amnpayroll.getC_Currency_ID());
		minvoice.setC_ConversionType_ID(amnpayroll.getC_ConversionType_ID());
		if (amnemployee.getBill_BPartner_ID() != 0) {
			minvoice.setC_BPartner_ID(amnemployee.getBill_BPartner_ID());
			minvoice.setM_PriceList_ID(billBp.getPO_PriceList_ID());
		} else {
			minvoice.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
			minvoice.setM_PriceList_ID(empBp.getPO_PriceList_ID());
		}
		if (billBp.getPrimaryC_BPartner_Location_ID() != 0)
			minvoice.setC_BPartner_Location_ID(billBp.getPrimaryC_BPartner_Location_ID());
		else
		minvoice.setC_BPartner_Location_ID(Default_C_BPartner_Location_ID);
		minvoice.setSalesRep_ID(billBp.getSalesRep_ID());
		minvoice.setIsSOTrx(false);
		// Save C_Invoice header
		minvoice.save(trxName);
		// 
		if (minvoice.getC_BPartner_Location_ID() == 0) {
			minvoice.setC_BPartner_Location_ID(Default_C_BPartner_Location_ID);
			minvoice.save(trxName);

		}
		retValue = minvoice.getC_Invoice_ID();
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(ctx, "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(String.format("%-15s","Receipt ").replace(' ', '_')+
				Msg.getElement(ctx, "AMN_Employee_ID")+": "+
				String.format("%-50s",amnemployee.getValue()+"-"+amnemployee.getName()).replace(' ', '_')+
				Msg.getElement(ctx, "AMN_Concept_Types_ID")+": "+
				String.format("%-50s",Payroll_Value+"-"+Payroll_Name).replace(' ', '_'));
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control

		return retValue;
		
	}	//	createCInvoice

	/**
	 * createCInvoiceLines:
	 * Creates Payroll Invoice Line
	 * @param ctx
	 * @param invoice
	 * @param p_AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	private boolean createCInvoiceLines(Properties ctx, MInvoice invoice, MAMN_Payroll  amnpayroll, String chargeProcess, String trxName) {
		
		boolean retValue = false;
		MInvoiceLine invlin = null;
		
		// Verify if previouss line
		if (invoice != null) {
			MInvoiceLine[] invlines = invoice.getLines();
			if (invlines.length == 0 ) {
				invlin  = new MInvoiceLine(ctx, 0, trxName);
			}  else {
				deleteInvoiceLines(ctx, invoice,  trxName);
				invlin  = new MInvoiceLine(ctx, 0, trxName);
			}
		} else {
			return retValue;
		}
		// Default Exent Tax
		int C_Tax_ID = getExemptTax(ctx, amnpayroll.getAD_Client_ID(), trxName);
		// Workforce from AMN_payroll --ª AMN_Jobtitle
		MAMN_Jobtitle jobtitle = new MAMN_Jobtitle(ctx, amnpayroll.getAMN_Jobtitle_ID(), trxName);
		// 
		MAMN_Charge amncha = new MAMN_Charge(ctx, 0, trxName);
		// Search Charge for chargeProcess
		Integer searchProcessID = MAMN_Process.sqlGetAMNProcessIDFromName(chargeProcess, amnpayroll.getAD_Client_ID());
		int C_Charge_ID = amncha.findC_Charge_ID(ctx, searchProcessID, jobtitle.getWorkforce());
		// 
		invlin.setInvoice(invoice);
		invlin.setC_Invoice_ID(invoice.getC_Invoice_ID());
		invlin.setLine(10);
		// Tax Line and BAse
		invlin.setQty(BigDecimal.ONE);
		invlin.setPrice(invoice.getGrandTotal());
		invlin.setPriceActual(invoice.getGrandTotal());
		invlin.setLineTotalAmt(invoice.getGrandTotal());
		invlin.setTaxAmt(BigDecimal.ZERO);
		invlin.setC_Tax_ID(C_Tax_ID);
		invlin.setPriceList(invoice.getGrandTotal());
		// Other line attributes
		invlin.setC_Charge_ID(C_Charge_ID);
		invlin.setDescription(invoice.getDescription());
		invlin.setAD_Org_ID(invoice.getAD_Org_ID());
		invlin.setC_Activity_ID(invoice.getC_Activity_ID());
		invlin.setC_Project_ID(invoice.getC_Project_ID());
		invlin.saveEx(trxName);
		retValue=true;
		// 
		return retValue;
		
	}	//	createCInvoice
	
	
	
	private MInvoice createCInvoiceCreditMemoDoc()  {
		
		MInvoice mcreditmemo = null;
		return mcreditmemo;
		
	}

	/**
	 * deleteInvoiceLines
	 * When Lines ere generated remove them from credit memo
	 * @param ctx
	 * @param cm
	 * @param trxName
	 */
	private void deleteInvoiceLines(Properties ctx, MInvoice inv, String trxName) {
			
			MInvoiceLine[] invlines = inv.getLines();
			for( MInvoiceLine invlin : invlines) {
				// Delete
				invlin.deleteEx(true);
			}
	}
	
	/**
	 * updateSalaryHistoric
	 * Updates SalaryHistoric
	 * @param ctx
	 * @param amnpayroll
	 * @param trxName
	 * @return
	 */
	private String updateSalaryHistoric (Properties ctx, MAMN_Payroll amnpayroll,  String trxName) {
		
		MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, trxName);
		MAMN_Period amnperiod = new MAMN_Period(getCtx(), amnpayroll.getAMN_Period_ID(), trxName); 
		String Msg_Value= amnpayrollhistoric.updateSalaryAmnPayrollHistoric(ctx, null, amnpayroll.getAMN_Employee_ID(), 
	    		amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), amnpayroll.getC_Currency_ID(), trxName)+"\r\n";
		return Msg_Value;
	}

	/**
	     * reactivateAMN_Payroll
	     * Tractivate AMN_Payroll and C_Invoice if apply
	     * @param ctx
	     * @param amnpayroll
	     * @param amnprocess
	     * @param trxName
	     * @return
	     */
	    public String reactivateAMN_Payroll(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocess, String trxName) {
	    
	    	StringBuilder msgBuilder = new StringBuilder(amnpayroll.getSummary()).append("\n");
	    	boolean okprocess = false;
	    	boolean okinvoice = false;
	    	boolean okcreditmemo = false;
	    	MInvoice minvoice = null;
	    	String returnMsg ="";

	    	if (amnprocess.isDocControlled()) {
		        // Reactivar factura (Invoice)
		    	// Si no esta definido el tipo de document NO Procede 
		        returnMsg =Msg.translate(ctx, "Reactivate")+": "+Msg.getElement(ctx, MAMN_Process.COLUMNNAME_C_DocTypeTarget_ID)+"\r\n";
		    	if (amnprocess.getC_DocTypeTarget_ID() != 0) {
		    		minvoice = reactivateInvoice(ctx, amnpayroll, amnprocess, returnMsg, trxName);
		            okinvoice = (minvoice != null);
		    	} else {
		        	msgBuilder.append(Msg.getElement(ctx, MAMN_Process.COLUMNNAME_C_DocTypeTarget_ID)+
		        			Msg.translate(ctx,"NotFound")+"\r\n" );
		        	okinvoice=false;
		        }

		        // Reactivar nota de crédito (Credit Memo)
		        // Si no esta definido el tipo de documento SI Procede y continua
		        returnMsg =Msg.translate(ctx, "Reactivate")+": "+Msg.getElement(ctx, MAMN_Process.COLUMNNAME_C_DocTypeCreditMemo_ID)+"\r\n";
		        if (amnprocess.getC_DocTypeCreditMemo_ID() != 0) {
		        	okcreditmemo = reactivateCreditMemos(ctx, amnpayroll, amnprocess, returnMsg, trxName);
		            if (okcreditmemo) {
		            	msgBuilder.append(returnMsg+" OK "+"\r\n");
		            } else {
		            	MDocType mdoctype = new MDocType(ctx, amnprocess.getC_DocTypeCreditMemo_ID(), trxName);
		            	msgBuilder.append(returnMsg+Msg.translate(ctx,mdoctype.getName())+"\r\n" );
		            }
		        } else {
		        	msgBuilder.append(returnMsg+" OK"+"\r\n");
		        	okcreditmemo = true;
		        }
		    }
	    	// Set AMN_Payroll C_Doctype in case NULL
		    if (this.getC_DocType_ID() != 0)
		    	this.setC_DocType_ID(this.getC_DocType_ID());
		    else 
		    	this.setC_DocType_ID(this.getC_DocTypeTarget_ID());
			// 
			okprocess = amnpayroll.reActivateIt();
			amnpayroll.saveEx();
			msgBuilder.insert(0, okprocess ? " ** "+Msg.translate(ctx, "Success").trim()+" **\r\n" : " ** "+Msg.translate(ctx, "Failed").trim() +"**\r\n");
			msgBuilder.insert(0, returnMsg);
			return msgBuilder.toString();
	    	
	    }

	    
		private MInvoice reactivateInvoice(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocess, String returnMsg, String trxName) {

			MInvoice minvoice = null;

	        // Buscar factura existente en `AMN_Payroll_Docs`
	        MAMN_Payroll_Docs existingInvoiceDoc = MAMN_Payroll_Docs.getFirstByPayrollAndDocTypeID(
	                ctx, amnpayroll.getAMN_Payroll_ID(), amnprocess.getC_DocTypeTarget_ID(), trxName);

	        if (existingInvoiceDoc != null) {
	            minvoice = MInvoice.get(ctx, existingInvoiceDoc.getC_Invoice_ID());
	            if (minvoice != null && !minvoice.isPaid()) {
	                log.warning("Factura procesada o con asignaciones.");
	                if (amnpayroll.reActivateCInvoice(minvoice,  trxName)) {
	                	returnMsg = returnMsg + " ** "+Msg.translate(ctx, "Success").trim()+" **\r\n" +
	                		Msg.translate(ctx, "DocReactivated") + minvoice.getDocumentNo() ;
	                } else {
	                	returnMsg = returnMsg + " ** "+Msg.getMsg(Env.getCtx(), "PocessFailed")+" **  "+
	                				Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+minvoice.getDocumentNo()+" \r\n";
	                }
	            } else {
	            	minvoice = null; 
	            }
	        } else {
	        	minvoice = null; 
	            returnMsg = returnMsg + Msg.getElement(ctx, "C_Invoice_ID").trim() + " "+Msg.translate(ctx, "New").trim();
	        }

	        return minvoice;
	    }
	    
		private boolean reactivateCreditMemos(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocess, String returnMsg, String trxName) {
	        
			// Busca Conceptos de tipo Saldo (Prestamos)
	    	List<MAMN_Concept_Types> concepts = MAMN_Concept_Types.getFilteredConcepts("B", "C", amnprocess.getAMN_Process_ID());
	        if (concepts.isEmpty()) {
	        	// Retorna true porque no encuentra conceptos definidos
	        	returnMsg ="No hay conceptos de prestamos";
	            return true;
	        }

	        // Busca Lineas de recibos de los conceptos de prestamos
	        Map<Integer, MAMN_Payroll_Detail> payrollDetails = MAMN_Payroll_Detail.findPayrollDetailsByConcepts(ctx, amnpayroll.getAMN_Payroll_ID(), concepts);
	        if (payrollDetails.isEmpty()) {
	        	// Retorna true porque no encuentra conceptos en el recibo
	        	returnMsg ="No hay lineas de conceptos de prestamos en el recibo";
	            return true;
	        }

	        // Busca Documentos en AMN_Payroll_Docs
	        List<MAMN_Payroll_Docs> amnpdocs = MAMN_Payroll_Docs.getByPayrollID(ctx, amnpayroll.getAMN_Payroll_ID(), trxName);
	        if (amnpdocs.isEmpty()) {
	        	// Retorna true porque no encuentra documentos asociados al recibo
	        	returnMsg ="No hay documentos asociados al recibo";
	            return true;
	        }

	        for (MAMN_Payroll_Docs doc : amnpdocs) {

	        	if (doc.getC_DocType_ID() == amnprocess.getC_DocTypeCreditMemo_ID()) {
		            MAMN_Payroll_Docs existingCreditMemo = MAMN_Payroll_Docs.getFirstByPayrollAndConceptTypesID(ctx, amnpayroll.getAMN_Payroll_ID(), doc.getAMN_Concept_Types_ID(), trxName);
		            MInvoice creditMemo = (existingCreditMemo != null) ? MInvoice.get(ctx, existingCreditMemo.getC_Invoice_ID()) : new MInvoice(ctx, 0, trxName);
	
		            if (creditMemo != null && !creditMemo.isPaid()) {
		                log.warning("Credit Memo procesada o con asignaciones.");
		                if (amnpayroll.reActivateCInvoice(creditMemo,  trxName)) {
		                	returnMsg = returnMsg + " ** "+Msg.translate(ctx, "Success").trim()+" **\r\n" +
		                		Msg.translate(ctx, "DocReactivated") + creditMemo.getDocumentNo() ;
		                } else {
		                	returnMsg = returnMsg + " ** "+Msg.getMsg(Env.getCtx(), "PocessFailed")+" **  "+
		                				Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+creditMemo.getDocumentNo()+" \r\n";
		                }
		            } else {
		            	returnMsg = returnMsg + " ** "+Msg.getMsg(Env.getCtx(), "PocessFailed")+" **  "+
                				Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+creditMemo.getDocumentNo()+" \r\n";
		            }
	        	}
	           
	        }

	        return true;
	    }
		
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//log.warning("................AMN_Payroll Before Save ...................");
		Timestamp DateAcct =  this.getDateAcct();
		String errorMess="";
		MAMN_Period amnperiod = new MAMN_Period(getCtx(), this.getAMN_Period_ID(), null);
		//log.warning("DateIni="+amnperiod.getAMNDateIni()+"  DateEnd="+amnperiod.getAMNDateEnd());
		if ( (DateAcct.compareTo(amnperiod.getAMNDateIni()) >= 0) && (DateAcct.compareTo(amnperiod.getAMNDateEnd()) <= 0) ) {
			return true;
		} else {
			errorMess = "***("+ Msg.getMsg(getCtx(), "invalid")+" "+ DateAcct.toString().substring(0, 10)+") ***  "+
					Msg.translate(Env.getCtx(), "DateAcct")+" "+
					Msg.getMsg(Env.getCtx(), "From")+":"+amnperiod.getAMNDateIni().toString().substring(0, 10)+" "+
					Msg.getMsg(Env.getCtx(), "to")+":"+amnperiod.getAMNDateEnd().toString().substring(0, 10);
			log.saveError("Error", errorMess);
			return false;	
		}
	}
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return true;
	}

	/**
	 * getAMNPayrollDays (BigDecimal PayrollDays)
	 * @param p_AMN_Payroll_ID	Payroll_ID
	 */
	public BigDecimal getAMNPayrollDays (int p_AMN_Payroll_ID)
	
	{
		BigDecimal PayrollDays = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, null);
		// PayrollDays
		//log.warning("p_AMN_Payroll_ID="+p_AMN_Payroll_ID+"  fechas INI="+amnpayroll.getInvDateIni()+"  End="+amnpayroll.getInvDateEnd());	
		PayrollDays = AmerpDateUtils.getDaysElapsed(amnpayroll.getInvDateIni(),amnpayroll.getInvDateEnd());
		//log.warning("payrollDays="+PayrollDays);
		PayrollDays = PayrollDays.setScale(0,BigDecimal.ROUND_UP);
		//log.warning("payrollDays rounded="+PayrollDays);
    	//
    	return PayrollDays;	
	}
	
		
	/**
	 * sqlGetAMNPayrollDetailNoLines (int Employee_ID)
	 * @param p_AMN_Payroll_ID	Payroll_ID
	 */
	public static int sqlGetAMNPayrollDetailNoLines (int p_AMN_Payroll_ID, String trxName)
	
	{
		String sql;
		int AMNPayrollDetailNoLines = 0;
		// AMN_Employee_ID
    	sql = "select count(*) from adempiere.amn_payroll_detail where AMN_Payroll_ID=?" ;
    	AMNPayrollDetailNoLines = DB.getSQLValue(trxName, sql, p_AMN_Payroll_ID);
    	return AMNPayrollDetailNoLines;	
	}
	
	
	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#processIt(java.lang.String)
	 */
    @Override
    public boolean processIt(String p_action) throws Exception {
//log.warning("Processing Action=" + p_action + " - DocStatus=" + getDocStatus() + " - DocAction=" + getDocAction());    	
    	m_processMsg = null;
    	boolean m_retVal = true;
    	DocumentEngine engine = new DocumentEngine(this, getDocStatus());
    	if (getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
		{
			m_retVal = true;
		} else {
			m_processMsg = "Process MSG: " + getProcessMsg();
			setProcessed(true);
			m_retVal = engine.processIt(p_action, getDocAction());
			if (!m_retVal) {
				setProcessed(false);
			}
			DocumentEngine.postImmediate(Env.getCtx(), getAD_Client_ID(), get_Table_ID(), getAMN_Payroll_ID(), true, get_TrxName());
		}
    	return m_retVal;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#unlockIt()
	 */
    @Override
    public boolean unlockIt() {
//log.warning("===============unlockIt================================");
	    return true;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#invalidateIt()
	 */
    @Override
    public boolean invalidateIt() {
//log.warning("===============invalidateIt================================");
	    return true;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#prepareIt()
	 */
    @Override
    public String prepareIt() {
//log.warning("===============prepareIt================================");
    	setC_DocType_ID(getC_DocTypeTarget_ID());
    	return DocAction.STATUS_InProgress;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#approveIt()
	 */
    @Override
    public boolean approveIt() {
//log.warning("=================approveIt==============================");
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#rejectIt()
	 */
    @Override
    public boolean rejectIt() {
//log.warning("================rejectIt===============================");
	    return true;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#completeIt()
	 */
    @Override
    public String completeIt() {
        if (!m_justPrepared) {
            String status = prepareIt();
            if (!DocAction.STATUS_InProgress.equals(status))
                return status;
        }

        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
        if (m_processMsg != null)
            return DocAction.STATUS_Invalid;

        if (!isApproved())
            approveIt();

        String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
        if (valid != null) {
            m_processMsg = valid;
            return DocAction.STATUS_Invalid;
        }

        setDefiniteDocumentNo();

        // Procesamiento de lógica interna
        MClientInfo info = MClientInfo.get(getCtx(), getAD_Client_ID(), null); 
        MAcctSchema as = MAcctSchema.get(getCtx(), info.getC_AcctSchema1_ID(), null);
        int p_currency = getC_Currency_ID() == 0 ? as.getC_Currency_ID() : getC_Currency_ID();

        MAMN_Employee_Salary.updateAMN_Employee_Salary(
            getCtx(), Env.getLanguage(getCtx()).getLocale(), 
            getAD_Client_ID(), getAD_Org_ID(), 
            p_currency, getAMN_Payroll_ID()
        );

        // ✅ Contabilizar este documento (nómina) ignorando CLIENT_ACCOUNTING
        DocumentEngine.postImmediate(
            getCtx(), getAD_Client_ID(), get_Table_ID(), get_ID(), true, get_TrxName()
        );

        // ✅ Contabilizar y completar las facturas asociadas
        String sql = "SELECT C_Invoice_ID FROM AMN_Payroll_Docs WHERE AMN_Payroll_ID=?";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName())) {
            pstmt.setInt(1, get_ID());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MInvoice invoice = new MInvoice(getCtx(), rs.getInt(1), get_TrxName());
                    if (!invoice.isProcessed() || !DocAction.STATUS_Completed.equals(invoice.getDocStatus())) {
                        invoice.processIt(DocAction.ACTION_Complete);
                        invoice.saveEx();
                    }
                    DocumentEngine.postImmediate(
                        getCtx(), invoice.getAD_Client_ID(), invoice.get_Table_ID(), invoice.get_ID(), true, get_TrxName()
                    );
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al procesar facturas asociadas", e);
            m_processMsg = e.getMessage();
            return DocAction.STATUS_Invalid;
        }

        // Finalizar
        setProcessed(true);
        setDocStatus(DocAction.STATUS_Completed);
        setDocAction(DocAction.ACTION_None);

        return DocAction.STATUS_Completed;
    }
	
	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			if (this.getProcessedOn().signum() == 0) {
				setDateAcct(new Timestamp (System.currentTimeMillis()));
			}
		}
		if (dt.isOverwriteSeqOnComplete()) {
			if (this.getProcessedOn().signum() == 0) {
				String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
				if (value != null)
					setDocumentNo(value);
			}
		}
	}

    
	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#voidIt()
	 */
    @Override
    public boolean voidIt() {
//log.warning("=================voidIt==============================");
	    return true;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#closeIt()
	 */
    @Override
    public boolean closeIt() {
//log.warning("==================closeIt=============================");
	    return true;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#reverseCorrectIt()
	 */
    @Override
    public boolean reverseCorrectIt() {
//log.warning("==================reverseCorrectIt==============================");
	    return true;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#reverseAccrualIt()
	 */
    @Override
    public boolean reverseAccrualIt() {
//log.warning("======================reverseAccrualIt==========================");
 	    return true;
    }


    /** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		//log.warning("======================reActivateIt==========================");
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
		
		// teo_sarca - FR [ 1776045 ] Add ReActivate action to GL Journal
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
		MFactAcct.deleteEx(MAMN_Payroll.Table_ID, get_ID(), get_TrxName());
		setPosted(false);
		setProcessed(false);
		setDocAction("CL");
		setDocStatus("DR");
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		//  MAcctSchema Select Client Default 
		MClientInfo info = MClientInfo.get(Env.getCtx(), getAD_Client_ID(), null); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		// Currency	
		// AMN_Employee_salary EMPLOYEEHISTORIC TABLE
		int p_currency =  0 ; //Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		// Verify if Receipt currency is set
		Integer receiptcurrency = getC_Currency_ID();
		if (receiptcurrency == null)
			p_currency =  as.getC_Currency_ID(); 
		else
			p_currency = getC_Currency_ID();		// AMN_Employee_salary EMPLOYEEHISTORIC TABLE RESET VALUES
		//log.warning("AMN_Payroll_ID():"+getAMN_Payroll_ID());
		MAMN_Employee_Salary.resetAMN_Employee_Salary(Env.getCtx(), Env.getLanguage(Env.getCtx()).getLocale(), 
				getAD_Client_ID(), getAD_Org_ID(), 
				p_currency, getAMN_Payroll_ID());
				
		return true;
	}	//	reActivateIt
	
	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getSummary()
	 */
    @Override
    public String getSummary() {
//log.warning("=====================getSummary===========================");
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		//	: Allocation = 123.00 Deduction = 20.00 (#1)
		sb.append(": ").
//			append(Msg.translate(getCtx(),"Allocation")).append("=").append(getAmountAllocated())
			append("Allocation =").append(getAmountAllocated())
			.append(" Deduction =").append(getAmountDeducted());
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
//log.warning(sb.toString());
		return sb.toString();
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getDocumentInfo()
	 */
    @Override
    public String getDocumentInfo() {
//log.warning("=====================getDocumentInfo===========================");
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#createPDF()
	 */
    @Override
    public File createPDF() {
    	//log.warning("=====================createPDF===========================");
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getProcessMsg()
	 */
    @Override
    public String getProcessMsg() {
    	//log.warning("=====================getProcessMsg===========================");
	    return null;
    }


	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		//log.warning("=====================setProcessed===========================");
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		StringBuilder set = new StringBuilder("SET Processed='")
		.append((processed ? "Y" : "N"))
		.append("' WHERE AMN_Payroll_ID=").append(getAMN_Payroll_ID());
		
		StringBuilder msgdb = new StringBuilder("UPDATE AMN_Payroll ").append(set);
		DB.executeUpdate(msgdb.toString(), get_TrxName());

	}	//	setProcessed

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getDoc_User_ID()
	 */
    @Override
    public int getDoc_User_ID() {
	    // 
	    return 0;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getApprovalAmt()
	 */
    @Override
    public BigDecimal getApprovalAmt() {
	    //
    	return BigDecimal.ZERO;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocOptions#customizeValidActions(java.lang.String, java.lang.Object, java.lang.String, java.lang.String, int, java.lang.String[], java.lang.String[], int)
	 */
    @Override
    public int customizeValidActions(String p_docStatus, Object p_processing, String p_orderType, String p_isSOTrx, int AD_Table_ID,
            String[] p_docAction, String[] p_options, int p_index) {
  	    // TODO Auto-generated method stub
    	//log.warning("=====================customizeValidActions===========================");
    	if (p_options == null)
    		throw new IllegalArgumentException("Option array parameter is null");
    	if (p_docAction == null)
    		throw new IllegalArgumentException("Doc action array parameter is null");

    	// If a document is drafted or invalid, the users are able to complete, prepare or void
    	if (p_docStatus.equals(DocumentEngine.STATUS_Drafted) || p_docStatus.equals(DocumentEngine.STATUS_Invalid)) {
    		p_options[p_index++] = DocumentEngine.ACTION_Complete;
    		p_options[p_index++] = DocumentEngine.ACTION_Prepare;
    		p_options[p_index++] = DocumentEngine.ACTION_Void;

    		// If the document is already completed, we also want to be able to reactivate or void it instead of only closing it
    	} else if (p_docStatus.equals(DocumentEngine.STATUS_Completed)) {
    		p_options[p_index++] = DocumentEngine.ACTION_Void;
    		p_options[p_index++] = DocumentEngine.ACTION_ReActivate;
    	}

    	return p_index;
    }

	/**	Lines						*/
	//private MAMN_Payroll_Detail[]	m_lines = null;
	
    /**
	 * 	Get Payroll Lines of AMN_Payroll
	 * 	@param whereClause starting with AND
	 * 	@return Payroll Detail lines
	 */
	private MAMN_Payroll_Detail[] getLines (String whereClause)
	{
		String whereClauseFinal = "AMN_Payroll_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MInvoiceLine> list = new Query(getCtx(), I_AMN_Payroll_Detail.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getAMN_Payroll_ID())
										.setOrderBy(I_AMN_Payroll_Detail.COLUMNNAME_CalcOrder)
										.list();
		return list.toArray(new MAMN_Payroll_Detail[list.size()]);
	}	//	getLines
	
	/**************************************************************************
	 * 	Get Payroll Detail Lines
	 * 	@param requery requery
	 *	@return Array of Payroll Detail lines
	 */
	public MAMN_Payroll_Detail[] getLines (boolean requery)
	{
//log.warning("=====================MAMN_Payroll_Detail[] getLines===========================");
		String sql= "SELECT * "+
				"FROM AMN_Payroll_Detail  as pde "+
				"LEFT JOIN AMN_Concept_Types_Proc as ctp ON (ctp.AMN_Concept_Types_Proc_ID = pde.AMN_Concept_Types_Proc_ID) "+
				"LEFT JOIN AMN_Concept_Types as cty ON (cty.AMN_Concept_Types_ID = ctp.AMN_Concept_Types_ID) "+
				"WHERE amountcalculated <> 0  "+
				"AND cty.optmode <> 'R' "+
				"AND AMN_Payroll_ID=?  "+
				"ORDER BY pde.CalcOrder ASC ";
		ArrayList<MAMN_Payroll_Detail> list = new ArrayList<MAMN_Payroll_Detail>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAMN_Payroll_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MAMN_Payroll_Detail line = new MAMN_Payroll_Detail(getCtx(), rs, get_TrxName());
				list.add (line);
				//log.warning("PayrollDetail Name:"+line.getName());
			}
		} 
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		m_payrolldetail = new MAMN_Payroll_Detail[list.size ()];
		list.toArray (m_payrolldetail);
		
		return m_payrolldetail;
	}	//	getLines
	
	/**************************************************************************
	 * 	Get First Payroll Detail Lines
	 * 	@param requery requery
	 *	@return Array of Payroll Detail lines
	 */
	public MAMN_Payroll_Detail[] getFirstLine (boolean requery)
	{
//log.warning("=====================MAMN_Payroll_Detail[] getLines===========================");
		String sql= "SELECT * "+
				"FROM AMN_Payroll_Detail  as pde "+
				"LEFT JOIN AMN_Concept_Types_Proc as ctp ON (ctp.AMN_Concept_Types_Proc_ID = pde.AMN_Concept_Types_Proc_ID) "+
				"LEFT JOIN AMN_Concept_Types as cty ON (cty.AMN_Concept_Types_ID = ctp.AMN_Concept_Types_ID) "+
				"WHERE amountcalculated <> 0  "+
				"AND cty.optmode <> 'R' "+
				"AND AMN_Payroll_ID=?  "+
				"ORDER BY pde.CalcOrder ASC ";
		ArrayList<MAMN_Payroll_Detail> list = new ArrayList<MAMN_Payroll_Detail>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAMN_Payroll_ID());
			rs = pstmt.executeQuery ();
			// ONLY FIRTS LINE
			if (rs.next ())
			{
				MAMN_Payroll_Detail line = new MAMN_Payroll_Detail(getCtx(), rs, get_TrxName());
				list.add (line);
				//log.warning("PayrollDetail Name:"+line.getName());
			}
		} 
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		m_payrolldetail = new MAMN_Payroll_Detail[list.size ()];
		list.toArray (m_payrolldetail);
		
		return m_payrolldetail;
	}	//	getLines
	
	/**
	 * 	Get Payroll Detail Lines
	 * 	@return Array of Payroll Detail lines
	 */
	public MAMN_Payroll_Detail[] getLines()
	{
		return getLines(false);
	}	//	getLines

	/**************************************************************************
	 * 	Get Payroll Reference Only Detail Lines
	 * 	@param requery requery
	 *	@return Array of Payroll Detail lines
	 */
	public MAMN_Payroll_Detail[] getReferenceLines (boolean requery)
	{

		String sql= "SELECT * "+
				"FROM AMN_Payroll_Detail  as pde "+
				"LEFT JOIN AMN_Concept_Types_Proc as ctp ON (ctp.AMN_Concept_Types_Proc_ID = pde.AMN_Concept_Types_Proc_ID) "+
				"LEFT JOIN AMN_Concept_Types as cty ON (cty.AMN_Concept_Types_ID = ctp.AMN_Concept_Types_ID) "+
				"WHERE amountcalculated <> 0  "+
				"AND cty.optmode = 'R' "+
				"AND AMN_Payroll_ID=?  "+
				"ORDER BY pde.CalcOrder ASC ";
		ArrayList<MAMN_Payroll_Detail> list = new ArrayList<MAMN_Payroll_Detail>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAMN_Payroll_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MAMN_Payroll_Detail line = new MAMN_Payroll_Detail(getCtx(), rs, get_TrxName());
				list.add (line);
				//log.warning("PayrollDetail Name:"+line.getName());
			}
		} 
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		m_payrolldetail = new MAMN_Payroll_Detail[list.size ()];
		list.toArray (m_payrolldetail);
		
		return m_payrolldetail;
	}	//	getReferenceLines
	
	/**************************************************************************
	 * 	Get Payroll FirstReferenceLine Only Detail Line
	 * 	@param requery requery
	 *	@return Array of Payroll Detail lines
	 */
	public MAMN_Payroll_Detail[] getFirstReferenceLine (boolean requery)
	{

		int DefAMN_Concept_Types_Proc_ID = 0;
		String sql= "SELECT * "+
				"FROM AMN_Payroll_Detail  as pde "+
				"LEFT JOIN AMN_Concept_Types_Proc as ctp ON (ctp.AMN_Concept_Types_Proc_ID = pde.AMN_Concept_Types_Proc_ID) "+
				"LEFT JOIN AMN_Concept_Types as cty ON (cty.AMN_Concept_Types_ID = ctp.AMN_Concept_Types_ID) "+
				"WHERE amountcalculated <> 0  "+
				"AND cty.optmode = 'R' "+
				"AND AMN_Payroll_ID=?  "+
				"ORDER BY pde.CalcOrder ASC "
				;
		ArrayList<MAMN_Payroll_Detail> list = new ArrayList<MAMN_Payroll_Detail>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAMN_Payroll_ID());
			rs = pstmt.executeQuery ();
			// ONLY FIRTS LINE
			if (rs.next ())
			{
				MAMN_Payroll_Detail line = new MAMN_Payroll_Detail(getCtx(), rs, get_TrxName());
				list.add (line);
				//log.warning("PayrollDetail Name:"+line.getName());
			}
		} 
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		m_payrolldetail = new MAMN_Payroll_Detail[list.size ()];
		if (list.size() > 0 ){
			list.toArray (m_payrolldetail);
		}
		else {
			MAMN_Payroll_Detail linedef = new MAMN_Payroll_Detail(getCtx(), 0, null);
			MAMN_Concept_Types_Proc amnc = new MAMN_Concept_Types_Proc(getCtx(), 0, null);
			// Default Reference for Process
			DefAMN_Concept_Types_Proc_ID = amnc.sqlGetAMNConceptTypesProcFirstReference(getAMN_Process_ID());
			linedef.createAmnPayrollDetail(getCtx(), Env.getLanguage(Env.getCtx()).getLocale(),
					getAD_Client_ID(), getAD_Org_ID(),  getAMN_Process_ID(), getAMN_Contract_ID(),
					getAMN_Payroll_ID(), DefAMN_Concept_Types_Proc_ID, get_TrxName());
			list.add (linedef);
			list.toArray (m_payrolldetail);
		}
		return m_payrolldetail;
	}	//	getFirstReferenceLine
	
	/**************************************************************************
	 * 	Get Payroll Detail Lines where p_AMN_Payroll_ID
	 *  Return All Lines including References
	 * 	@param int p_AMN_Payroll_ID
	 *	@return Array of Payroll Detail lines
	 */
	public MAMN_Payroll_Detail[] getPayrollLinesAll (int p_AMN_Payroll_ID)
	{
		//
		String sql= "SELECT * "+
				"FROM AMN_Payroll_Detail  as pde "+
				"LEFT JOIN AMN_Concept_Types_Proc as ctp ON (ctp.AMN_Concept_Types_Proc_ID = pde.AMN_Concept_Types_Proc_ID) "+
				"LEFT JOIN AMN_Concept_Types as cty ON (cty.AMN_Concept_Types_ID = ctp.AMN_Concept_Types_ID) "+
				"WHERE amountcalculated <> 0  "+
				"AND AMN_Payroll_ID=?  "+
				"ORDER BY pde.CalcOrder ASC ";
		ArrayList<MAMN_Payroll_Detail> list = new ArrayList<MAMN_Payroll_Detail>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, p_AMN_Payroll_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MAMN_Payroll_Detail linepr = new MAMN_Payroll_Detail(getCtx(), rs, get_TrxName());
				list.add (linepr);
				//log.warning("PayrollDetail Name:"+linepr.getName());
			}
		} 
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		m_payrolldetail2 = new MAMN_Payroll_Detail[list.size ()];
		list.toArray (m_payrolldetail2);
		
		return m_payrolldetail2;
	}	//	getLines
	
	/**
	 * getExemptTax
	 * @param ctx
	 * @param AD_Client_ID
	 * @param trxName
	 * @return
	 */
	public static int getExemptTax (Properties ctx, int AD_Client_ID, String trxName)
	{
		final String sql = "SELECT t.C_Tax_ID "
			+ " FROM C_Tax t "
			+ " WHERE t.ad_client_id = ? AND t.taxindicator ='Exenta' AND t.isactive='Y' AND t.name LIKE '%Exenta Compras%' "
			+ " ORDER BY t.Rate DESC ";
		int C_Tax_ID = DB.getSQLValueEx(trxName, sql, AD_Client_ID);
		if (log.isLoggable(Level.FINE)) log.fine("getExemptTax - TaxExempt=Y - C_Tax_ID=" + C_Tax_ID);
		if (C_Tax_ID <= 0)
		{
			throw new TaxNoExemptFoundException(AD_Client_ID);
		}
		else
		{
			return C_Tax_ID;
		}
	}	//	getExemptTax
	
	/**
	 * reActivateCInvoice
	 * 	Re-activate Payroll CInvoice
	 * 	@return false
	 */
	public boolean reActivateCInvoice(MInvoice invoice, String trxName)
	{

		// Reactivate HEADER
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), invoice.getC_DocType_ID(), invoice.getAD_Org_ID());
		MFactAcct.deleteEx(MInvoice.Table_ID, invoice.getC_Invoice_ID(), trxName);
		// Invoice
		invoice.setDocAction("CL");
		invoice.setDocStatus("DR");
		invoice.setProcessed(false);
		invoice.setPosted(false);
		invoice.save(trxName);

		return true;

	}	//	reActivateIt
	
	/**
	 * sqlGet C_AllocationLine_C_Payment (int p_C_Invoice_ID)
	 * @param p_C_Invoice_ID	Invoice ID
	 * Verify if Invoice has C_Allocationline or C_Payment Records associated
	 */
	public String C_AllocationLine_C_Payment (int p_C_Invoice_ID, String trxName)
	
	{
		String sql;
		int C_AllocationLine_ID=0;
		int C_Payment_ID=0;
		String retValue="";
		// LCO_InvoiceWHDocLines
    	sql = "SELECT DISTINCT C_AllocationLine_ID FROM C_AllocationLine WHERE C_Invoice_ID=?" ;
    	C_AllocationLine_ID = DB.getSQLValue(null, sql, p_C_Invoice_ID);	
//log.warning("sql:"+sql+"  p_C_Invoice_ID:"+p_C_Invoice_ID+"  C_AllocationLine_ID:"+C_AllocationLine_ID);
		if (C_AllocationLine_ID > 0) {
			retValue=Msg.getElement(Env.getCtx(),"C_AllocationLine_ID")+":"+C_AllocationLine_ID;
		   	sql = "SELECT DISTINCT C_Payment_ID FROM C_AllocationLine WHERE C_Invoice_ID=?" ;
    		C_Payment_ID = DB.getSQLValue(null, sql, p_C_Invoice_ID);	
//log.warning("sql:"+sql+"  p_C_Invoice_ID:"+p_C_Invoice_ID+"  C_Payment_ID:"+C_Payment_ID);
    		if (C_Payment_ID > 0) {
    			MPayment mpayment = new MPayment(getCtx(), C_Payment_ID, null);
    			retValue=retValue+"  "+Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"
    					+ mpayment.getDocumentNo()+"-"+mpayment.getDescription();
    		}
		} else {
			//retValue=Msg.getElement(Env.getCtx(),"C_AllocationLine_ID")+": ** NO TIENE **";
			retValue="OK";
		}
    	return retValue;	
	}
}
