/******************************************************************************
 * Copyright (C) 2016 Luis Amesty                                             *
 * Copyright (C) 2016 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
  *****************************************************************************/
package org.amerp.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Concept_Types_Proc;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnmodel.X_AMN_Process;
import org.amerp.amnutilities.AmerpMsg;
import org.amerp.amnutilities.AmerpPayrollCalc;
import org.amerp.amnutilities.LoanPeriods;
import org.compiere.model.MClient;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNPayrollProcessPayrollDeferredOneEmployee extends SvrProcess {
	
	static CLogger log = CLogger.getCLogger(AMNPayrollProcessPayrollDeferredOneEmployee.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Payroll_ID = 0;
	private int p_AMN_Employee_ID = 0;
	private int p_AMN_Concept_Types_Proc_DB_ID=0;	// Debit Loan Concepty Types Process
	private int p_AMN_Concept_Types_Proc_CR_ID=0;	// Credit Loan Concepty Types Process
	private BigDecimal p_LoanAmount = BigDecimal.valueOf(0);
	private int p_LoanQuotaNo = 0;
	private int p_AMN_FirstPeriod_ID = 0;
	private String Msg_Header ="";
	private String p_LoanDescription="Descripción Pago Diferido";
	

	String Employee_Name,AMN_Process_Value="";
	String sql="";
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	   	ProcessInfoParameter[] paras = getParameter();
			for (ProcessInfoParameter para : paras)
			{
				String paraName = para.getParameterName();
				// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
				// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
				// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
				if (paraName.equals("AMN_Contract_ID"))
					p_AMN_Contract_ID =  para.getParameterAsInt();
				else if (paraName.equals("AMN_Period_ID"))
					para.getParameterAsInt();
				else if (paraName.equals("AMN_Payroll_ID"))
					p_AMN_Payroll_ID = para.getParameterAsInt();
				else if (paraName.equals("AMN_Employee_ID"))
					p_AMN_Employee_ID = para.getParameterAsInt();
				else if (paraName.equals("LoanAmount"))
					p_LoanAmount = para.getParameterAsBigDecimal();
				else if (paraName.equals("LoanQuotaNo"))
					p_LoanQuotaNo = para.getParameterAsInt();
				else if (paraName.equals("AMN_Process_ID"))
					p_AMN_Process_ID = para.getParameterAsInt();
				else if (paraName.equals("AMN_Concept_Types_Proc_DB_ID"))
					p_AMN_Concept_Types_Proc_DB_ID = para.getParameterAsInt();
				else if (paraName.equals("AMN_Concept_Types_Proc_CR_ID"))
					p_AMN_Concept_Types_Proc_CR_ID = para.getParameterAsInt();
				else if (paraName.equals("AMN_FirstPeriod_ID"))
					p_AMN_FirstPeriod_ID = para.getParameterAsInt();
				else if (paraName.equals("LoanDescription"))
					p_LoanDescription = para.getParameterAsString();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
			}
	}

	@Override
	protected String doIt() throws Exception {

		boolean okProcess = false;
		String retValue="";
//		List<LoanPeriods> periodList = new ArrayList<LoanPeriods>();
//		LoanPeriods loanPeriodData = null;
	    LoanPeriods loanPeriodData = new LoanPeriods(0, null, null, p_LoanAmount);
	    LoanPeriods[] periodList = new LoanPeriods[100];
		
		int rRecPayrollDeferred=0;
		int rRecPeriodAvailable=0;
		boolean bRecPayrollDetail=false;
		boolean bRecPeriodAvailable=false;
//		// AMN_Process_ID for Process_Value="NN"
//		int AMN_Process_ID_NN = ((X_AMN_Process)new Query(getCtx(),X_AMN_Process.Table_Name,"Value='NN'",null).first()).getAMN_Process_ID();
//		// AMN_Process_ID for Process_Value="NU"
//		int AMN_Process_ID_NU = ((X_AMN_Process)new Query(getCtx(),X_AMN_Process.Table_Name,"Value='NU'",null).first()).getAMN_Process_ID();
//		// AMN_Process_ID for Process_Value="NV"
//		int AMN_Process_ID_NV = ((X_AMN_Process)new Query(getCtx(),X_AMN_Process.Table_Name,"Value='NV'",null).first()).getAMN_Process_ID();
		// AMN_Process_ID for Process_Value="PO"
//		int AMN_Process_ID_PO = ((X_AMN_Process)new Query(getCtx(),X_AMN_Process.Table_Name,"Value='PO'",null).first()).getAMN_Process_ID();
		//log.warning("AMN_Process_ID_PO:"+AMN_Process_ID_PO);
		// 
		MAMN_Contract amncontract = new MAMN_Contract(getCtx(), p_AMN_Contract_ID, null); 
		MAMN_Process amnprocessde = new MAMN_Process(getCtx(), p_AMN_Process_ID, null); 		
		MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, null); 
		MAMN_Employee amnemployee = new MAMN_Employee(getCtx(), p_AMN_Employee_ID, null); 
		MAMN_Period amnperiod = new MAMN_Period(getCtx(), p_AMN_FirstPeriod_ID, null);
		MAMN_Concept_Types_Proc amnconcepttypesDB = new MAMN_Concept_Types_Proc(getCtx(), p_AMN_Concept_Types_Proc_DB_ID, null);
		MAMN_Concept_Types_Proc amnconcepttypesCR = new MAMN_Concept_Types_Proc(getCtx(), p_AMN_Concept_Types_Proc_CR_ID, null);
		// AMN_Process_ID for Process_Value="PO"
		int AMN_Process_ID_PO = ((X_AMN_Process)new Query(getCtx(),X_AMN_Process.Table_Name,"Value='PO' AND AD_Client_ID="+amnemployee.getAD_Client_ID(),null).first()).getAMN_Process_ID();
		// MAMN_Process for PO Process
		MAMN_Process amnprocesspo = new MAMN_Process(getCtx(), AMN_Process_ID_PO, null); 
log.warning("p_AMN_Concept_Types_Proc_DB_ID:"+p_AMN_Concept_Types_Proc_DB_ID+" "+amnconcepttypesDB.getName());
log.warning("p_AMN_Concept_Types_Proc_CR_ID:"+p_AMN_Concept_Types_Proc_CR_ID+" "+amnconcepttypesCR.getName());
		// Message Header
		Msg_Header = Msg.getElement(getCtx(), "AMN_Process_ID")+":"+amnprocesspo.getValue().trim()+"-"+amnprocesspo.getName().trim();
	    addLog(Msg_Header);
	    Msg_Header = Msg.getElement(getCtx(), "AMN_Contract_ID")+":"+amncontract.getValue().trim()+"-"+amncontract.getName().trim();
	    addLog(Msg_Header);
	    Msg_Header = Msg.getElement(getCtx(), "AMN_Payroll_ID")+":"+amnpayroll.getValue().trim();
	    addLog(Msg_Header);
	    Msg_Header =amnpayroll.getName().trim();
	    addLog(Msg_Header);
	    Msg_Header = Msg.getElement(getCtx(), "AMN_Employee_ID")+":"+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim();
	    addLog(Msg_Header);
	    // Parameter Header
	    Msg_Header = AmerpMsg.getParameterMsg(getCtx(), "LoanAmount")+": "+p_LoanAmount;
	    addLog(Msg_Header);
	    Msg_Header = AmerpMsg.getParameterMsg(getCtx(), "LoanQuotaNo")+": "+p_LoanQuotaNo;
	    addLog(Msg_Header);
	    Msg_Header = AmerpMsg.getParameterMsg(getCtx(), "AMN_Process_ID")+": "+amnprocessde.getValue().trim()+"-"+amnprocessde.getName().trim();
	    addLog(Msg_Header);
	    Msg_Header = Msg.getElement(getCtx(), "AMN_Concept_Types_Proc_ID")+" (DB): "+amnconcepttypesDB.getValue().trim()+"-"+amnconcepttypesDB.getName().trim();
	    addLog(Msg_Header);
	    Msg_Header = AmerpMsg.getParameterMsg(getCtx(), "AMN_Concept_Types_Proc_ID")+" (CR): "+amnconcepttypesCR.getValue().trim()+"-"+amnconcepttypesCR.getName().trim();
	    addLog(Msg_Header);
	    Msg_Header = AmerpMsg.getParameterMsg(getCtx(), "AMN_FirstPeriod_ID")+": "+amnperiod.getValue().trim()+	    		"  ";
	    addLog(Msg_Header);
	    Msg_Header = AmerpMsg.getParameterMsg(getCtx(), "Description")+": "+p_LoanDescription;
	    addLog(Msg_Header);
	    Msg_Header = amnperiod.getName().trim()+"  "+Msg.getMsg(getCtx(), "Date")+":"+amnperiod.getAMNDateEnd().toString().substring(0,10);
	    addLog(Msg_Header);
	    // Verify if Lines already introduced on AMN_Payroll_Deferred
	    rRecPayrollDeferred=AMNPayrollProcessPayrollDeferred.VerifyDeferredDetailLines(getCtx(),p_AMN_Payroll_ID, get_TrxName());
	    // Verify if Lines already introduced on AMN_Payroll_Detail
	    // TRUE: Correct AMN_Concept_Types_Proc FALSE: INCorrect AMN_Concept_Types_Pro
	    bRecPayrollDetail=AMNPayrollProcessPayrollDeferred.VerifyPayrollDetailLines(getCtx(),p_AMN_Payroll_ID, get_TrxName());
	    // Verify Number of Period Available after Date Entered as initial Payment
	    rRecPeriodAvailable = (int) AMNPayrollProcessPayrollDeferred.VerifyPeriodsAvailable(
	    		getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Payroll_ID,amnperiod.getAMN_Period_ID(), get_TrxName());
	    if (rRecPeriodAvailable < p_LoanQuotaNo) {
			Msg_Header = "********************** ERROR ********************** \r\n "+
					AmerpMsg.getParameterMsg(getCtx(), "LoanQuotaNo")+": ("+p_LoanQuotaNo+") \r\n "+
					"  No. "+Msg.getElement(getCtx(), "AMN_Period_ID")+"(s) = (" + rRecPeriodAvailable +")"+" \r\n "+
					"********************** ERROR ******************** \r\n ";
			addLog(Msg_Header);
			bRecPeriodAvailable = false;
			okProcess = false;
	    } else {
			Msg_Header = " \r\n "+AmerpMsg.getParameterMsg(getCtx(), "LoanQuotaNo")+": ("+p_LoanQuotaNo+") \r\n "+
					"  No. "+Msg.getElement(getCtx(), "AMN_Period_ID")+"(s) = (" + rRecPeriodAvailable+")"+" \r\n ";
			addLog(Msg_Header);
			bRecPeriodAvailable = true;
			okProcess = true;
	    }
		// Verifiy if AMN_Concept_Types_proc is LOAN Concept DB Debit
		if (p_AMN_Concept_Types_Proc_DB_ID == 0)  {
			Msg_Header = "****** ERROR ****** \r\n "+AmerpMsg.getParameterMsg(getCtx(), "AMN_Concept_Types_Proc_ID")+" (DB): "+p_AMN_Concept_Types_Proc_DB_ID;
			addLog(Msg_Header);
			okProcess = false;
		}
		if (p_AMN_Concept_Types_Proc_CR_ID == 0)  {
			Msg_Header = "****** ERROR ****** \r\n "+AmerpMsg.getParameterMsg(getCtx(), "AMN_Concept_Types_Proc_ID")+" (CR): "+p_AMN_Concept_Types_Proc_CR_ID;
			addLog(Msg_Header);
			okProcess = false;
		}
	    if (rRecPayrollDeferred == 0  &&  bRecPayrollDetail && bRecPeriodAvailable ) {
	    	// Create AMN_Payroll_Deferred  LInes
	    	periodList = AMNPayrollProcessPayrollDeferred.CreatePayrollDeferredDetailLines 
	    			(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Payroll_ID,  p_AMN_Employee_ID, 
	    					p_AMN_Concept_Types_Proc_DB_ID, p_AMN_Concept_Types_Proc_CR_ID,
	    					 p_LoanAmount, p_LoanQuotaNo, p_AMN_FirstPeriod_ID, p_LoanDescription, get_TrxName()) ;
			//  CREATE OR VERIFY MAMN_Payroll Detail
	    	MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(getCtx(), 0, null);
		    // CREATE MAMN_Payroll Detail
		    amnpayrolldetail.createAmnPayrollDetail(getCtx(), Env.getLanguage(getCtx()).getLocale(),
					amncontract.getAD_Client_ID(), amncontract.getAD_Org_ID(),  AMN_Process_ID_PO, p_AMN_Contract_ID,
					p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_DB_ID, get_TrxName());
			//  UPDATE MAMN_Payroll Detail
			amnpayrolldetail.updateAmnPayrollDetail(getCtx(), Env.getLanguage(getCtx()).getLocale(),
					amncontract.getAD_Client_ID(), amncontract.getAD_Org_ID(), AMN_Process_ID_PO, p_AMN_Contract_ID,
					p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_DB_ID, p_LoanAmount, get_TrxName());
			//  UPDATE MAMN_Payroll Detail Description with p_LoanDescription			
			amnpayrolldetail.updateAmnPayrollDetailDescription(getCtx(), Env.getLanguage(getCtx()).getLocale(),
					amncontract.getAD_Client_ID(), amncontract.getAD_Org_ID(), AMN_Process_ID_PO, p_AMN_Contract_ID,
					p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_DB_ID, p_LoanDescription, get_TrxName());
			// RECALC
			AmerpPayrollCalc.PayrollEvaluationArrayCalculate(getCtx(), p_AMN_Payroll_ID);
			//  Title
	    	Msg_Header ="Lin  "+Msg.getElement(getCtx(), "AMN_Period_ID")+":     "+Msg.getElement(getCtx(), "AmountQuota")+":  "+Msg.getMsg(getCtx(), "Date")+":";
	    			addLog(Msg_Header);
	    	// Show AMN_Payroll_Deferred  LInes
	    	for (int i=0 ; i < p_LoanQuotaNo; i++) {
	    		loanPeriodData = periodList[i];
	    		//log.warning("Muestra:....p_LoanQuotaNo:"+p_LoanQuotaNo+" Periodo:"+loanPeriodData.getPeriodValue()+"  Date:"+loanPeriodData.getPeriodDate().toString().substring(0,10));
	    		Msg_Header = loanPeriodData.getLoanCuotaNo() +"-"+
	    				loanPeriodData.getPeriodValue() + "    " +
	    				loanPeriodData.getCuotaAmount() + "    " +
	    				loanPeriodData.getPeriodDate().toString().substring(0,10);
	    		addLog(Msg_Header);
	    	}
	    	Msg_Header = "OK";
	    	addLog(Msg_Header);
			okProcess = true;
		} else {
			if (rRecPayrollDeferred > 0 ) {
				Msg_Header = "****** ERROR ****** "+AmerpMsg.getParameterMsg(getCtx(), "LoanQuotaNo")+": "+rRecPayrollDeferred;
				Msg_Header = Msg_Header + " "+Msg.getMsg(getCtx(), "Records"); 
				addLog(Msg_Header);
				Msg_Header = " ******* "+Msg.getMsg(getCtx(), "DeleteAll")+ "****** "; 
				addLog(Msg_Header);
			} 
			if ( ! bRecPayrollDetail) {
				Msg_Header = "****** ERROR ****** ";
				addLog(Msg_Header);		
				Msg_Header = Msg.getElement(getCtx(), "AMN_Payroll_Detail_ID")+": ";
				addLog(Msg_Header);		
			}
			okProcess = false;
		}
	    // Return
		if ( okProcess ) 
			retValue= "OK";
	    else	    	
	    	retValue= "";
		return retValue;
	}

}
