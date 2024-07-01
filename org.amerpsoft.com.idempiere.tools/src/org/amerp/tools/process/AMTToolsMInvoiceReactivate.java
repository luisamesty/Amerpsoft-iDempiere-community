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
 ******************************************************************************/
package org.amerp.tools.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.amerp.tools.amtmodel.AMTToolsMCostDetail;
import org.amerp.tools.amtmodel.AMTToolsMInvoice;
import org.compiere.model.MBPartner;
import org.compiere.model.MCharge;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MProduct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMTToolsMInvoiceReactivate extends SvrProcess {
	
	int p_C_Invoice_ID=0;
	String Msg_Value="";
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
				if (paraName.equals("C_Invoice_ID"))
					p_C_Invoice_ID =  para.getParameterAsInt();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
			}	 
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub

		MInvoice minvoice = new MInvoice(getCtx(), p_C_Invoice_ID, null);
		
//log.warning("------------------C_Invoice_ID:"+p_C_Invoice_ID+"  DocumentNo:"+minvoice.getDocumentNo());
		MBPartner mbpartner = new MBPartner(getCtx(), minvoice.getC_BPartner_ID(), null);
		Msg_Value=Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+minvoice.getDocumentNo();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"DateAcct")+":"+minvoice.getDateAcct()+Msg.getElement(Env.getCtx(),"DateInvoiced")+minvoice.getDateInvoiced();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"Description")+":"+minvoice.getDescription();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"C_BPartner_ID")+":"+mbpartner.getValue().trim()+"-"+mbpartner.getName().trim();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"DocStatus")+":"+minvoice.getDocStatus();
		addLog(Msg_Value);
		// INVOICE HEADER
		AMTToolsMInvoice amtminvoice = new AMTToolsMInvoice(getCtx(), p_C_Invoice_ID, null);
		// VERIFY DOC STATUS
		if (minvoice.DOCSTATUS_Drafted.equals(minvoice.getDocStatus())
				// || minvoice.DOCSTATUS_Invalid.equals(minvoice.getDocStatus())
				// || minvoice.DOCSTATUS_InProgress.equals(minvoice.getDocStatus())
				//|| minvoice.DOCSTATUS_Approved.equals(minvoice.getDocStatus())
				//|| minvoice.DOCSTATUS_NotApproved.equals(minvoice.getDocStatus()) 
				)
		{
			Msg_Value=Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+" **** ESTA SIN COMPLETAR ****";
			addLog(Msg_Value);
		} else {
//log.warning("------------------C_Invoice_ID:"+p_C_Invoice_ID+"  Verify if SERVICES ONLY");

			// Verify if only Services
			boolean isService = true;
			boolean isWHDoc = true;
			boolean isAllocPay = true;
			int M_Product_ID = 0;	
			int C_Charge_ID = 0;
			int C_InvoiceLine_ID =0;
			//
			sql ="SELECT  " + 
				" c_invoiceline_id,  " + 
				" m_product_id, c_charge_id " + 
				"FROM c_invoiceline " + 
				"WHERE c_invoice_id = ? " 
				;
			PreparedStatement pstmt1 = null;
			ResultSet rsod1 = null;
			// VERIFY SERVICE
			try
			{
				pstmt1 = DB.prepareStatement(sql, null);
	            pstmt1.setInt (1, p_C_Invoice_ID);
	            rsod1 = pstmt1.executeQuery();
				while (rsod1.next())
				{
					M_Product_ID = rsod1.getInt(2);
					C_Charge_ID = rsod1.getInt(3);
//log.warning("-----M_Product_ID:"+M_Product_ID+"-----C_Charge_ID:"+C_Charge_ID+"  Verify Product-Charge");
					// Verify if Charge
					if(M_Product_ID == 0 && C_Charge_ID > 0) {
						MCharge mcharge = new MCharge(getCtx(), C_Charge_ID, null);
						Msg_Value=Msg.getElement(Env.getCtx(),"C_Charge_ID")+":"+mcharge.getName()+" **OK CHARGE**";
						addLog(Msg_Value);							
					}
					// Verify if Product is Service
					if (M_Product_ID > 0  && C_Charge_ID == 0) {
						MProduct mproduct = new MProduct(getCtx(), M_Product_ID, null);
						if (mproduct.getProductType().compareToIgnoreCase("S") != 0 )  {
							Msg_Value=Msg.getElement(Env.getCtx(),"M_Product_ID")+":"+mproduct.getValue()+"-"+mproduct.getName().trim()+" ** NOT SERVICE **";
							addLog(Msg_Value);
							isService = false ;
						} else {
							Msg_Value=Msg.getElement(Env.getCtx(),"M_Product_ID")+":"+mproduct.getValue()+"-"+mproduct.getName().trim()+" **OK SERVICE**";
							addLog(Msg_Value);							
						}
					}
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
			// ******** VERIFY RELATIONS TO INVOICE IN DB **********
			// VERIFY IF NOT Withholding Doc Generated on LCO_InvoiceWHDoc
			int LCO_InvoiceWHDocLines_ID=amtminvoice.sqlGetLCO_InvoiceWHDocLines(p_C_Invoice_ID, get_TrxName());
			String AllocPay = amtminvoice.C_AllocationLine_C_Payment(p_C_Invoice_ID, get_TrxName());
			if (AllocPay.compareToIgnoreCase("OK")==0) 
				isAllocPay = false;
			else
				isAllocPay = true;
			// IF SERVICE AND NOT Withholding Doc Generated
			if (LCO_InvoiceWHDocLines_ID <= 0) 
				isWHDoc=false;
			// REACTIVATE IF EVERYTHING IS OK
			if (isService && isWHDoc == false && isAllocPay == false) {
				try
				{
					pstmt1 = DB.prepareStatement(sql, null);
		            pstmt1.setInt (1, p_C_Invoice_ID);
		            rsod1 = pstmt1.executeQuery();
					while (rsod1.next())
					{
						C_InvoiceLine_ID 	 = rsod1.getInt(1);
						MInvoiceLine line = new MInvoiceLine(getCtx(), C_InvoiceLine_ID, null);
//log.warning("-----M_Product_ID:"+M_Product_ID+"  Update Product  c_invoiceline_id:"+line.getC_InvoiceLine_ID());
			//			line.setProcessed(false);
			//			line.saveEx();
						AMTToolsMCostDetail.deleteCostDetail(C_InvoiceLine_ID, get_TrxName());
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
				// DELETE LCO_InvoiceWithholding
				amtminvoice.deleteLCO_InvoiceWithholding(p_C_Invoice_ID, get_TrxName());
				// UPDATE REVERSAL_ID IF EXISTS
				amtminvoice.updateC_Invoice_Reversal_ID(p_C_Invoice_ID, get_TrxName());
				// DELETE C_InvoiceTax Lines where Taxes are  Withholding lines ONLY
				// amtminvoice.deleteC_InvoiceTaxWHLines(p_C_Invoice_ID, get_TrxName());
				// REACTIVATE INVOICE HEADER
				amtminvoice.reActivateIt( get_TrxName());
				
				// DELETE C_InvoiceTax Lines where Taxes are  Withholding lines ONLY
				amtminvoice.deleteC_InvoiceTaxWHLines(p_C_Invoice_ID, get_TrxName());
				// 
				Msg_Value=Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+" **** ESTA REACTIVADA ****";
				addLog(Msg_Value);
			} else {
				if (isService = false ) {
					Msg_Value=Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+" **** NO SON SERVICIOS ****";
					addLog(Msg_Value);
				}
				if (isWHDoc == true)  {
					Msg_Value=Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+" **** TIENE COMPROBANTE DE RETENCION GENERADO****";
					addLog(Msg_Value);
					Msg_Value= Msg.getElement(Env.getCtx(),"DocumentNo")+":"+amtminvoice.sqlGetLCO_InvoiceWHDoc_DocumentNo(p_C_Invoice_ID, get_TrxName());
					addLog(Msg_Value);
				}
				if (isAllocPay == true)  {
					Msg_Value=Msg.getElement(Env.getCtx(),"C_Invoice_ID")+":"+" **** TIENE ASIGNACION DE PAGOS****";
					addLog(Msg_Value);
					Msg_Value= AllocPay.trim();
					addLog(Msg_Value);
				}	
			}
		}
		return null;
	}


}
