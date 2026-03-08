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
package org.amerp.fin.factories;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.amerp.amfmodel.MAMF_ElementValue;
import org.compiere.model.PO;
import org.compiere.util.Env;

/**
 * @author luisamesty
 *
 */
public class AMFModelfactory implements IModelFactory{

	/* (non-Javadoc)
	 * @see org.adempiere.base.IModelFactory#getClass(java.lang.String)
	 */
    @Override
    public Class<?> getClass(String p_tableName) {
	    // 
		// Class

		// MAMF_ElementValue
		if(p_tableName.equalsIgnoreCase(MAMF_ElementValue.Table_Name))
			return MAMF_ElementValue.class;
    	return null;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IModelFactory#getPO(java.lang.String, int, java.lang.String)
	 */
    @Override
    public PO getPO(String p_tableName, int Record_ID, String p_trxName) {
    	// Record_ID

		// MAMF_ElementValue
		if(p_tableName.equalsIgnoreCase(MAMF_ElementValue.Table_Name))
			return new MAMF_ElementValue(Env.getCtx(),Record_ID, p_trxName);
    	return null;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IModelFactory#getPO(java.lang.String, java.sql.ResultSet, java.lang.String)
	 */
    @Override
    public PO getPO(String p_tableName, ResultSet p_rs, String p_trxName) {
    	// ResultSet
  
		// MAMF_ElementValue
		if(p_tableName.equalsIgnoreCase(MAMF_ElementValue.Table_Name))
			return new MAMF_ElementValue(Env.getCtx(),p_rs, p_trxName); 
	    return null;
    }

}
