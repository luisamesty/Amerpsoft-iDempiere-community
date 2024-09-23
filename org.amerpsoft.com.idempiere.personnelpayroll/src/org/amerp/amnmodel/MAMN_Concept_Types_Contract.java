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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author luisamesty
 *
 */
public class MAMN_Concept_Types_Contract extends X_AMN_Concept_Types_Contract{
	/**
	 * @param p_ctx
	 * @param AMN_Concept_Types_Contract_ID
	 * @param p_trxName
	 */
    public MAMN_Concept_Types_Contract(Properties p_ctx, int AMN_Concept_Types_Contract_ID, String p_trxName) {
	    super(p_ctx, AMN_Concept_Types_Contract_ID, p_trxName);
	    // TODO Auto-generated constructor stub
    }
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MAMN_Concept_Types_Contract(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // TODO Auto-generated constructor stub
    }

    
    /**
     * updateAMNConcept_Types_Contract
     * @param p_AMNConcept_Types_Contract_ID
     * @param p_Value
     * @param p_Name
     * @param p_Description
     * @return
     */
    public boolean updateAMNConcept_Types_Contract(int p_AMNConcept_Types_Contract_ID, 
    		String p_ContractValue, String p_Value, String p_Name, int p_CalcOrder) {

    	MAMN_Concept_Types_Contract actc = new MAMN_Concept_Types_Contract(p_ctx, p_AMNConcept_Types_Contract_ID, null);
    	if (actc != null) {
    		
    		actc.setValue(p_ContractValue.trim()+p_CalcOrder);
    		actc.setName(p_ContractValue.trim()+'-'+p_CalcOrder+"-"+p_Name.trim());
    		actc.setDescription(p_ContractValue.trim()+'-'+p_CalcOrder+"-"+p_Value.trim()+'-'+p_Name.trim());
    		actc.save();
        	return true;
    	}
       	return false;
    }

}
