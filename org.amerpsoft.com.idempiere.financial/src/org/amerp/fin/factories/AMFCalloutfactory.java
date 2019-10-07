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

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;

/**
 * @author luisamesty
 *
 */
public class AMFCalloutfactory implements IColumnCalloutFactory {

	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCalloutFactory#getColumnCallouts(java.lang.String, java.lang.String)
	 */
    @Override
    public IColumnCallout[] getColumnCallouts(String p_tableName, String p_columnName) {
	    // TODO Auto-generated method stub
    	
		List<IColumnCallout> list = new ArrayList<IColumnCallout>();

//		// *********************************
//		// TableRef: amf_acctbaldetail
//		// *********************************
//		if (p_tableName.equalsIgnoreCase(I_AMF_acctbaldetail.Table_Name)) {
//			// FieldRef: c_year_id
//			if (p_columnName.equalsIgnoreCase(I_AMF_acctbaldetail.COLUMNNAME_C_Year_ID))
//				list.add(new AMF_acctbaldetail_callout());
//		}
		
		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
    }

}
