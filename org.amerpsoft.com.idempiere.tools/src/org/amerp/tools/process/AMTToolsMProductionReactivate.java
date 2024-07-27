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

import java.sql.Timestamp;
import java.util.logging.Level;

import org.amerp.tools.amtmodel.AMTToolsMProduction;
import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMTToolsMProductionReactivate extends SvrProcess {
	
	int p_M_Production_ID=0;
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
				if (paraName.equals("M_Production_ID"))
					p_M_Production_ID =  para.getParameterAsInt();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
			}	 
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub

		AMTToolsMProduction mproduction = new AMTToolsMProduction(getCtx(), p_M_Production_ID, null);
		Timestamp MovementDate = mproduction.getMovementDate();
		MProduct mproduct = new MProduct(getCtx(), mproduction.getM_Product_ID(), null);
//log.warning("------------------C_Invoice_ID:"+p_C_Invoice_ID+"  DocumentNo:"+minvoice.getDocumentNo());
		Msg_Value=Msg.getElement(Env.getCtx(),"M_Production_ID")+":"+mproduction.getDocumentNo();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"MovementDate")+":"+mproduction.getMovementDate();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"M_product_ID")+":"+mproduct.getValue().trim()+
				"-"+mproduct.getName().trim();
		addLog(Msg_Value);
		if (mproduction.getName() != null) {
			Msg_Value=Msg.getElement(Env.getCtx(),"Name")+":"+mproduction.getName();
			addLog(Msg_Value);
		}
		if (mproduction.getDescription() != null) {
			Msg_Value=Msg.getElement(Env.getCtx(),"Description")+":"+mproduction.getDescription().trim();
			addLog(Msg_Value);
		}
		Msg_Value=Msg.getElement(Env.getCtx(),"DocStatus")+":"+mproduction.getDocStatus();
		addLog(Msg_Value);
		// VERIFY DOC STATUS
		if (mproduction.DOCSTATUS_Drafted.equals(mproduction.getDocStatus())
				// || minvoice.DOCSTATUS_Invalid.equals(minvoice.getDocStatus())
				// || minvoice.DOCSTATUS_InProgress.equals(minvoice.getDocStatus())
				//|| minvoice.DOCSTATUS_Approved.equals(minvoice.getDocStatus())
				//|| minvoice.DOCSTATUS_NotApproved.equals(minvoice.getDocStatus()) 
				)
		{
			Msg_Value=Msg.getElement(Env.getCtx(),"M_Production_ID")+":"+" **** ESTA SIN COMPLETAR ****";
			addLog(Msg_Value);
		} else {
//log.warning("------------------M_Production_ID:"+p_M_Production_ID);
			// REACTIVATE IF EVERYTHING IS OK
			//AMTMProduction amtmproduction = new AMTMProduction(getCtx(), p_M_Production_ID, null);
			int no=0;
			// DELETE M_CostHistory
			no=mproduction.deleteM_CostHistory(p_M_Production_ID, get_TrxName());
			Msg_Value=Msg.getElement(Env.getCtx(),"M_CostHistory_ID")+" "+
				"No "+Msg.translate(Env.getCtx(),"Records")+" "+no+" "+
				Msg.translate(Env.getCtx(),"Deleted");
			addLog(Msg_Value);
			// DELETE M_CostDetail
			no=mproduction.deleteM_CostDetail(p_M_Production_ID, get_TrxName());
			Msg_Value=Msg.getElement(Env.getCtx(),"M_CostDetail_ID")+" "+
				"No "+Msg.translate(Env.getCtx(),"Records")+" "+no+" "+
				Msg.translate(Env.getCtx(),"Deleted");
			addLog(Msg_Value);
			// DELETE MTransaction
			no=mproduction.deleteM_Transaction(p_M_Production_ID, get_TrxName());
			Msg_Value=Msg.getElement(Env.getCtx(),"M_Transaction_ID")+" "+
				"No "+Msg.translate(Env.getCtx(),"Records")+" "+no+" "+
				Msg.translate(Env.getCtx(),"Deleted");
			addLog(Msg_Value);
			// REACTIVATE M_Production
			if (!mproduction.reActivateIt(p_M_Production_ID, get_TrxName()))
				Msg_Value=Msg.getElement(Env.getCtx(),"M_Production_ID")+":"+" **** ESTA REACTIVADA ****";
			else
				Msg_Value=Msg.getElement(Env.getCtx(),"M_Production_ID")+":"+" **** ERROR ****";
			// 
			addLog(Msg_Value);
			// UPDATE M_Storageonhand QtyOnHand (Reverse Values)
			// ADD M_StorageOnHand Recs AS IT IS VOIDING Production
			no=mproduction.reverseM_StorageonHand(p_M_Production_ID, MovementDate, get_TrxName());
			Msg_Value=Msg.getElement(Env.getCtx(),"QtyOnHand")+" "+
					"No "+Msg.translate(Env.getCtx(),"Records")+" "+no+" "+
					Msg.translate(Env.getCtx(),"Updated");
			addLog(Msg_Value);

		
		}
		return null;
	}


}
