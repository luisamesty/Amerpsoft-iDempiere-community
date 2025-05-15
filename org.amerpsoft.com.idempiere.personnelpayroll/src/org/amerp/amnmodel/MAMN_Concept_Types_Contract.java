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

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author luisamesty
 *
 */
public class MAMN_Concept_Types_Contract extends X_AMN_Concept_Types_Contract{
	
	private static final long serialVersionUID = -3745499356420726029L;

	/**
	 * @param p_ctx
	 * @param AMN_Concept_Types_Contract_ID
	 * @param p_trxName
	 */
    public MAMN_Concept_Types_Contract(Properties p_ctx, int AMN_Concept_Types_Contract_ID, String p_trxName) {
	    super(p_ctx, AMN_Concept_Types_Contract_ID, p_trxName);
	    // 
    }
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MAMN_Concept_Types_Contract(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // 
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

        if (p_AMNConcept_Types_Contract_ID <= 0) {
            return false;
        }

        // Trim de los parámetros una vez
        String contractValue = p_ContractValue != null ? p_ContractValue.trim() : "";
        String value = p_Value != null ? p_Value.trim() : "";
        String name = p_Name != null ? p_Name.trim() : "";

        // Cargar el registro existente
        MAMN_Concept_Types_Contract actc = new MAMN_Concept_Types_Contract(p_ctx, p_AMNConcept_Types_Contract_ID, null);
        
        if (actc.get_ID() <= 0) { // Verificar si el registro existe realmente en la BD
            return false;
        }

        // Construcción de valores formateados
        actc.setValue(contractValue + p_CalcOrder);
        actc.setName(String.format("%s-%d-%s", contractValue, p_CalcOrder, name));
        actc.setDescription(String.format("%s-%d-%s-%s", contractValue, p_CalcOrder, value, name));

        // Guardar y verificar si se guardó correctamente
        return actc.save();
    }

}
