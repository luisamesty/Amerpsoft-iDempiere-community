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
package org.amerp.amncallouts;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author luisamesty
 *
 */
public class AMN_Period_callout implements IColumnCallout {

	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
	String Period_Value="",Period_Name="",Period_Description="";
	Timestamp DateIni,DateEnd;
	String Process_Value="", Contract_Value="";
	int AMN_Process_ID=0;
	int AMN_Contract_ID=0;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
    	AMN_Process_ID=  (int) p_mTab.getValue(MAMN_Period.COLUMNNAME_AMN_Process_ID) ;
    	AMN_Contract_ID=  (int) p_mTab.getValue(MAMN_Period.COLUMNNAME_AMN_Contract_ID) ;
    	if (p_mTab.getValue(MAMN_Period.COLUMNNAME_AMN_Process_ID) != null) {
	    	MAMN_Process amnprocess = new MAMN_Process(Env.getCtx(), AMN_Process_ID, null);
	    	Process_Value=amnprocess.getValue()+"-";
	    }
    	if (p_mTab.getValue(MAMN_Period.COLUMNNAME_AMN_Contract_ID) != null) {
	    	MAMN_Contract amncontract = new MAMN_Contract(Env.getCtx(), AMN_Contract_ID, null);
	    	Contract_Value=amncontract.getValue()+"_";
	    }
    	// TODO Auto-generated method stub
		if (p_mTab.getValue(MAMN_Period.COLUMNNAME_AMNDateIni) != null )
		{
			// *****************************************************
			// * amndateini
			// ******************************************************
			DateIni =  (Timestamp) p_mTab.getValue(MAMN_Period.COLUMNNAME_AMNDateIni);
		}
		
		if (p_mTab.getValue(MAMN_Period.COLUMNNAME_AMNDateEnd) != null )
		{
			// *****************************************************
			// * amndateend
			// ******************************************************
			DateEnd = (Timestamp) p_mTab.getValue(MAMN_Period.COLUMNNAME_AMNDateEnd);
		}
		if (p_mTab.getValue(MAMN_Period.COLUMNNAME_AMNDateIni) != null  && 
				p_mTab.getValue(MAMN_Period.COLUMNNAME_AMNDateEnd) != null ) {
			Period_Value = Process_Value+Contract_Value+dateFormat.format(DateIni)+"_"+dateFormat.format(DateEnd);
//			Period_Name="Period From:"+dateFormat.format(DateIni)+" To:"+dateFormat.format(DateEnd);
			Period_Name=Msg.getElement(Env.getCtx(), "AMN_Period_ID")+" "+
					Msg.getMsg(Env.getCtx(), "From")+":"+dateFormat.format(DateIni)+" "+
					Msg.getMsg(Env.getCtx(), "to")+":"+dateFormat.format(DateEnd);
//			Period_Description="Periodo Desde:"+dateFormat.format(DateIni)+" Hasta:"+dateFormat.format(DateEnd);
			Period_Description=Msg.getElement(Env.getCtx(), "AMN_Period_ID")+" "+
					Msg.getMsg(Env.getCtx(), "From")+":"+dateFormat.format(DateIni)+" "+
					Msg.getMsg(Env.getCtx(), "to")+":"+dateFormat.format(DateEnd);
			p_mTab.setValue("Value",Period_Value);
			p_mTab.setValue("Name",Period_Name);
			p_mTab.setValue("Description",Period_Description);
		}

	    return null;
    }

}
