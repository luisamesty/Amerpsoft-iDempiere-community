package org.amerp.process;

import java.util.logging.Level;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MCurrency;
import org.compiere.model.X_C_ValidCombination;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMFRebuildANewClientSchema extends SvrProcess{

	private int p_AD_Client_ID = 0;
	private int	p_SourceAcctSchema_ID = 0;
	private int	p_TargetAcctSchema_ID = 0;
	private int SourceCurrency_ID=0;
	private int TargetCurrency_ID=0;
	private String p_SetupSchema="N";
	private String p_ValidCombinations="N";
	private String p_BankAccounting = "N";
	private String p_BPartners ="N";
	private String p_Material ="N";
	private String p_MaterialCost="N";
	
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	   	//log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			if (paraName.equals("AD_Client_ID")) 
				p_AD_Client_ID =  para.getParameterAsInt();
			else if (paraName.equals("SourceAcctSchema_ID")) 
				p_SourceAcctSchema_ID =  para.getParameterAsInt();
			else if (paraName.equals("TargetAcctSchema_ID")) 
				p_TargetAcctSchema_ID =  para.getParameterAsInt();
			else if (paraName.equals("ValidCombinations")) 
				p_ValidCombinations =  para.getParameterAsString();
			else if (paraName.equals("SetupSchema")) 
				p_SetupSchema =  para.getParameterAsString();
			else if (paraName.equals("Material")) 
				p_Material =  para.getParameterAsString();
			else if (paraName.equals("MaterialCost")) 
				p_MaterialCost =  para.getParameterAsString();
			else if (paraName.equals("BPartners")) 
				p_BPartners =  para.getParameterAsString();
			else if (paraName.equals("BankAccounting")) 
				p_BankAccounting =  para.getParameterAsString();			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
//log.warning("...........Parametros.........AD_Client_ID="+p_AD_Client_ID+"  p_SourceAcctSchema_ID"+p_SourceAcctSchema_ID+"  p_TargetAcctSchema_ID"+p_TargetAcctSchema_ID);
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
//		BigDecimal updatedRecs = BigDecimal.ZERO;
//		BigDecimal createdRecs = BigDecimal.ZERO;
//		BigDecimal errorRecs = BigDecimal.ZERO;
//		BigDecimal updatedTotalRecs = BigDecimal.ZERO;
//		BigDecimal createdTotalRecs = BigDecimal.ZERO;
//		BigDecimal errorTotalRecs = BigDecimal.ZERO;
		// AMRAmerpProcessMsg.sqlGetADProcessTRL  GET PROCESS NAME
		String  Msg_Header = AMFAmerpProcessMsg.sqlGetADProcessTRL(this.getProcessInfo().getAD_Process_ID(), Env.getAD_Language(Env.getCtx()))+"\r\n";
		// Status UPDATE
		this.statusUpdate(Msg_Header.trim()+": "+Msg.translate(Env.getCtx(), "Processing"));		
		String MessagetoShow = "";
		addLog(Msg_Header);
		// Client and AcctSchema
		MClient mc = new MClient(Env.getCtx(), p_AD_Client_ID, null);
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_AD_Client_ID, null);
		MCurrency curr = new MCurrency(Env.getCtx(), info.getC_Currency_ID(), null);
		// Client Name 
		Msg_Header = Msg.translate(Env.getCtx(), "AD_Client_ID")+":"+mc.getName();
		addLog(Msg_Header);
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		// default AcctSchema 
		Msg_Header = Msg.translate(Env.getCtx(), "C_AcctSchema_ID")+" "+
				 Msg.translate(Env.getCtx(), "default")+
				 ":"+as.getName();
		addLog(Msg_Header);
		// default Currency
		Msg_Header = Msg.translate(Env.getCtx(), "C_Currency_ID")+" "+
				 Msg.translate(Env.getCtx(), "default")+
				 ":"+curr.getISO_Code()+"-"+curr.getCurSymbol()+"-"+curr.getDescription();
		addLog(Msg_Header);
		// Verify Parameters
		MAcctSchema source = MAcctSchema.get(Env.getCtx(), p_SourceAcctSchema_ID, null);
		if (source.get_ID() == 0)
			throw new AdempiereSystemError("NotFound Source C_AcctSchema_ID=" + p_SourceAcctSchema_ID);
		MAcctSchema target = new MAcctSchema (Env.getCtx(), p_TargetAcctSchema_ID, get_TrxName());
		if (target.get_ID() == 0)
			throw new AdempiereSystemError("NotFound Target C_AcctSchema_ID=" + p_TargetAcctSchema_ID);		
		// Verify Source AcctSchema
		if (p_SourceAcctSchema_ID != info.getC_AcctSchema1_ID()) {
			throw new AdempiereSystemError("Default C_AcctSchema_ID=" + info.getC_AcctSchema1_ID()+
					" Differs from source received = "+ p_SourceAcctSchema_ID);		
		}
		// Currencies
		SourceCurrency_ID=source.getC_Currency_ID();
		MCurrency scurr = new MCurrency(Env.getCtx(), SourceCurrency_ID, null);
		TargetCurrency_ID=target.getC_Currency_ID();
		MCurrency tcurr = new MCurrency(Env.getCtx(), TargetCurrency_ID, null);

		// Source Received
		Msg_Header = Msg.translate(Env.getCtx(), "C_AcctSchema_ID")+" "+
				 Msg.translate(Env.getCtx(), "From")+
				 ":"+source.getName();
		addLog(Msg_Header);
		Msg_Header = Msg.translate(Env.getCtx(), "C_Currency_ID")+" "+
				 Msg.translate(Env.getCtx(), "From")+
				 ":"+scurr.getISO_Code()+"-"+scurr.getCurSymbol()+"-"+scurr.getDescription();
		addLog(Msg_Header);
		// Destination Received
		Msg_Header = Msg.translate(Env.getCtx(), "C_AcctSchema_ID")+" "+
				 Msg.translate(Env.getCtx(), "To")+
				 ":"+target.getName();
		addLog(Msg_Header);
		Msg_Header = Msg.translate(Env.getCtx(), "C_Currency_ID")+" "+
				 Msg.translate(Env.getCtx(), "To")+
				 ":"+tcurr.getISO_Code()+"-"+tcurr.getCurSymbol()+"-"+tcurr.getDescription();
		addLog(Msg_Header);
		
		// ***********************************************************
		// Accounting: C_ValidCombination Table Class Setup
		// ***********************************************************
		if (p_ValidCombinations.compareToIgnoreCase("Y") == 0 ) {
			// Status UPDATE
			this.statusUpdate(X_C_ValidCombination.Table_Name +": "+Msg.translate(Env.getCtx(), "Processing"));
			AMRRebuildValidCombinations valcomb = new AMRRebuildValidCombinations();
			// Reset Counters
			valcomb.resetCounters();
			// Account Schemas
			valcomb.setSourceAcctSchema_ID(p_SourceAcctSchema_ID);
			valcomb.setTargetAcctSchema_ID(p_TargetAcctSchema_ID);
			valcomb.setSourceAS(source);
			valcomb.setTargetAS(target);
			// AD_Client_ID
			valcomb.setAD_Client_ID(p_AD_Client_ID);
			valcomb.setAD_Org_ID(0);
			valcomb.setM_client(mc);
			valcomb.setM_pi(getProcessInfo());  // Set Process Info
			valcomb.setTargetCurr(tcurr);
			// ---------------------------
			// Call dupC_ValidCombination
			// ---------------------------
			valcomb.dupC_ValidCombination();
			// C_ValidCombination Return Messages
			MessagetoShow = MAccount.Table_Name+":";
			addLog(MessagetoShow);
			MessagetoShow = Msg.translate(Env.getCtx(), "C_ValidCombination_ID");
			addLog(MessagetoShow);	
			addLog(valcomb.getInfo());
			// ---------------------------
			//log.warning("...dupValidCombinations");
		}
		// ***********************************************************
		// Account Schema Definition Tables
		// ***********************************************************
		if (p_SetupSchema.compareToIgnoreCase("Y") == 0 ) {
			// Status UPDATE
			this.statusUpdate(MAcctSchema.Table_Name +": "+Msg.translate(Env.getCtx(), "Processing"));
			AMRRebuildSetup.createClientSchemaStructure(p_AD_Client_ID, p_SourceAcctSchema_ID, p_TargetAcctSchema_ID);
			addLog(AMRRebuildSetup.getInfo());
			//log.warning("...createClientSchemaStructure");
		}
		// ***********************************************************
		// MATERIAL
		// Product, Cost, Warehouse Tables Class Setup
		// ***********************************************************
		if (p_Material.compareToIgnoreCase("Y") == 0 ) {
			// Status UPDATE
			this.statusUpdate(Msg.translate(Env.getCtx(), "M_Product_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			AMRRebuildMaterial recpro = new AMRRebuildMaterial();
			recpro.setM_pi(getProcessInfo());  // Set Process Info
			// Account Schemas
			recpro.setSourceAcctSchema_ID(p_SourceAcctSchema_ID);
			recpro.setTargetAcctSchema_ID(p_TargetAcctSchema_ID);
			recpro.setSourceAS(source);
			recpro.setTargetAS(target);
			// Currencies
			recpro.setSourceCurrency_ID(SourceCurrency_ID);
			recpro.setTargetCurrency_ID(TargetCurrency_ID);
			recpro.setSourceCurr(scurr);
			recpro.setTargetCurr(tcurr);
			recpro.setConversionRates(Env.getCtx(), SourceCurrency_ID, TargetCurrency_ID, getTrx());
			// AD_Client_ID
			recpro.setAD_Client_ID(p_AD_Client_ID);
			recpro.setM_client(mc);
			// ***********************************************************
			// M_Product_Acct, M_Product_Category_Acct, M_Warehouse_Acct
			// ***********************************************************
			// Call duplicate Product Tables
			// Reset Counters
			recpro.resetCounters();
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "M_Product_Category_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recpro.dupM_Product_Category_Acct();
			// ---------------------------
			addLog(recpro.getInfo());
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "M_Product_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recpro.dupM_Product_Acct();
			// ---------------------------
			addLog(recpro.getInfo());
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "M_Warehouse_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recpro.dupM_Warehouse_Acct();
			// ---------------------------
			addLog(recpro.getInfo());
		}
		// ***********************************************************
		// MATERIALCOST
		// Product Cost
		// ***********************************************************
		if (p_MaterialCost.compareToIgnoreCase("Y") == 0 ) {
			// Status UPDATE
			this.statusUpdate(Msg.translate(Env.getCtx(), "M_Product_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			AMRRebuildMaterial recpro = new AMRRebuildMaterial();
			recpro.setM_pi(getProcessInfo());  // Set Process Info
			// Account Schemas
			recpro.setSourceAcctSchema_ID(p_SourceAcctSchema_ID);
			recpro.setTargetAcctSchema_ID(p_TargetAcctSchema_ID);
			recpro.setSourceAS(source);
			recpro.setTargetAS(target);
			// Currencies
			recpro.setSourceCurrency_ID(SourceCurrency_ID);
			recpro.setTargetCurrency_ID(TargetCurrency_ID);
			recpro.setSourceCurr(scurr);
			recpro.setTargetCurr(tcurr);
			recpro.setConversionRates(Env.getCtx(), SourceCurrency_ID, TargetCurrency_ID, getTrx());
			// AD_Client_ID
			recpro.setAD_Client_ID(p_AD_Client_ID);
			recpro.setM_client(mc);
			// ***********************************************************
			// (M_Cost, M_CostDetail, M_CostHistory, M_CostHistoryPeriod)
			// ***********************************************************
			// Call duplicate Product Tables
			// Reset Counters
			recpro.resetCounters();
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "M_Product_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recpro.dupM_Product_Cost();
			// ---------------------------
			addLog(recpro.getInfo());
		}
		// ***********************************************************
		// BUSINESS PARTNERS
		// C_BP_Grouo, C_BP_Customer, C_BP_Vendor, C_BP_Employee
		//		Tables Class Setup
		// ***********************************************************
		if (p_BPartners.compareToIgnoreCase("Y") == 0 ) {
			AMRRebuildBPartners recbpa = new AMRRebuildBPartners();
			recbpa.setM_pi(getProcessInfo());  // Set Process Info
			// Account Schemas
			recbpa.setSourceAcctSchema_ID(p_SourceAcctSchema_ID);
			recbpa.setTargetAcctSchema_ID(p_TargetAcctSchema_ID);
			recbpa.setSourceAS(source);
			recbpa.setTargetAS(target);
			// Currencies
			recbpa.setSourceCurrency_ID(SourceCurrency_ID);
			recbpa.setTargetCurrency_ID(TargetCurrency_ID);
			recbpa.setSourceCurr(scurr);
			recbpa.setTargetCurr(tcurr);
			// AD_Client_ID
			recbpa.setAD_Client_ID(p_AD_Client_ID);
			recbpa.setM_client(mc);
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "C_BP_Group_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recbpa.dupC_BP_Group_Acct();
			addLog(recbpa.getInfo());
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "C_BPartner_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recbpa.dupC_BPartner_Acct();
			addLog(recbpa.getInfo());
			// ---------------------------
		}		
		
		// ***********************************************************
		// BANK, ACCOUNTING ASSET PROJECT
		// C_BP_Grouo, C_BP_Customer, C_BP_Vendor, C_BP_Employee
		//		Tables Class Setup
		// ***********************************************************
		if (p_BankAccounting.compareToIgnoreCase("Y") == 0 ) {
			AMRRebuildBankAccounting recbaa = new AMRRebuildBankAccounting();
			recbaa.setM_pi(getProcessInfo());  // Set Process Info
			// Account Schemas
			recbaa.setSourceAcctSchema_ID(p_SourceAcctSchema_ID);
			recbaa.setTargetAcctSchema_ID(p_TargetAcctSchema_ID);
			recbaa.setSourceAS(source);
			recbaa.setTargetAS(target);
			// Currencies
			recbaa.setSourceCurrency_ID(SourceCurrency_ID);
			recbaa.setTargetCurrency_ID(TargetCurrency_ID);
			recbaa.setSourceCurr(scurr);
			recbaa.setTargetCurr(tcurr);
			// AD_Client_ID
			recbaa.setAD_Client_ID(p_AD_Client_ID);
			recbaa.setM_client(mc);
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "C_Charge_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recbaa.dupC_Charge_Acct();
			addLog(recbaa.getInfo());
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "C_Tax_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recbaa.dupC_Tax_Acct();
			addLog(recbaa.getInfo());
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "C_BankAccount_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recbaa.dupC_BankAccount_Acct();
			addLog(recbaa.getInfo());
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "A_Asset_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recbaa.dupA_Asset_Acct();
			addLog(recbaa.getInfo());
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "A_Asset_Group_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recbaa.dupA_Asset_Group_Acct();
			addLog(recbaa.getInfo());
			// ---------------------------
			this.statusUpdate(Msg.translate(Env.getCtx(), "C_Project_ID") +": "+Msg.translate(Env.getCtx(), "Processing"));
			recbaa.dupC_Project_Acct();
			addLog(recbaa.getInfo());
			// ---------------------------
		}
		return null;
	}

	private String getTrx() {
		// TODO Auto-generated method stub
		return null;
	}
	

}


// M_Cost Return Messages
//String MessagetoShow = X_M_Cost.Table_Name+":"+Msg.translate(Env.getCtx(), "Records")+" "+Msg.translate(Env.getCtx(), "Created");
////addLog(0, null, recpro.getInsertM_Cost(), "@Created@ @M_Cost_ID@");
//addLog(MessagetoShow+"(s) "+recpro.getInsertM_Cost());
//MessagetoShow = X_M_Cost.Table_Name+":"+Msg.translate(Env.getCtx(), "Records")+" "+Msg.translate(Env.getCtx(), "Updated");
////addLog(0, null, recpro.getUpdateM_Cost(), "@Updated@ @M_Cost_ID@");
//addLog(MessagetoShow+"(s) "+recpro.getUpdateM_Cost());
//MessagetoShow = X_M_Cost.Table_Name+":"+Msg.translate(Env.getCtx(), "Records")+" "+Msg.translate(Env.getCtx(), "Error");
////addLog(0, null, recpro.getErrorM_Cost(), "@Error@ @M_Cost_ID@");
//addLog(MessagetoShow+"(s) "+recpro.getErrorM_Cost());
// **********************************************
// Product Category (M_Product_Category)
// **********************************************
////	Insert new Product Category
//sql = new StringBuilder("INSERT INTO M_Product_Category_Acct ")
//	.append("(M_Product_Category_ID, C_AcctSchema_ID,")
//	.append(" AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,")
//	.append(" P_Revenue_Acct, P_Expense_Acct, P_CostAdjustment_Acct, P_InventoryClearing_Acct, P_Asset_Acct, P_CoGs_Acct,")
//	.append(" P_PurchasePriceVariance_Acct, P_InvoicePriceVariance_Acct, P_AverageCostVariance_Acct,")
//	.append(" P_TradeDiscountRec_Acct, P_TradeDiscountGrant_Acct," )
//	.append(" P_RateVariance_Acct, P_LandedCostClearing_Acct) ")
//	.append(" SELECT p.M_Product_Category_ID, acct.C_AcctSchema_ID,")
//	.append(" p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,")
//	.append(" acct.P_Revenue_Acct, acct.P_Expense_Acct, acct.P_CostAdjustment_Acct, acct.P_InventoryClearing_Acct, acct.P_Asset_Acct, acct.P_CoGs_Acct,")
//	.append(" acct.P_PurchasePriceVariance_Acct, acct.P_InvoicePriceVariance_Acct, acct.P_AverageCostVariance_Acct,")
//	.append(" acct.P_TradeDiscountRec_Acct, acct.P_TradeDiscountGrant_Acct,")
//	.append(" acct.P_RateVariance_Acct, acct.P_LandedCostClearing_Acct ") 
//	.append("FROM M_Product_Category p")
//	.append(" INNER JOIN C_AcctSchema_Default acct ON (p.AD_Client_ID=acct.AD_Client_ID) ")
//	.append("WHERE acct.C_AcctSchema_ID=").append(p_C_AcctSchema_ID)
//	.append(" AND NOT EXISTS (SELECT * FROM M_Product_Category_Acct pa ")
//		.append("WHERE pa.M_Product_Category_ID=p.M_Product_Category_ID")
//		.append(" AND pa.C_AcctSchema_ID=acct.C_AcctSchema_ID)");
//created = DB.executeUpdate(sql.toString(), get_TrxName());
//addLog(0, null, new BigDecimal(created), "@Created@ @M_Product_Category_ID@");

