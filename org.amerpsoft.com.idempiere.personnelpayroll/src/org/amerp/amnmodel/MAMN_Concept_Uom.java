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

public class MAMN_Concept_Uom extends X_AMN_Concept_Uom {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7092439919702026649L;

	public MAMN_Concept_Uom(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Concept_Uom(Properties ctx, int AMN_Concept_Uom_ID,
			String trxName) {
		super(ctx, AMN_Concept_Uom_ID, trxName);
		// TODO Auto-generated constructor stub
	}

}