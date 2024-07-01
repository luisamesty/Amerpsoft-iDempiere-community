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
package org.amerp.pp.factories;

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.adempiere.base.IDocFactory;
import org.amerp.amndocument.Doc_AMNPayroll;
import org.amerp.amnmodel.*;
import org.compiere.acct.Doc;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MInOut;
import org.compiere.model.MProduction;
import org.compiere.model.MTable;
import org.compiere.util.*;

/**
 * @author luisamesty
 *
 */
public class AMNDocumentFactory implements IDocFactory{

	private final static CLogger s_log = CLogger.getCLogger(AMNDocumentFactory.class);

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDocFactory#getDocument(org.compiere.model.MAcctSchema, int, int, java.lang.String)
	 */
    @Override
    public Doc getDocument(MAcctSchema p_as, int AD_Table_ID, int Record_ID, String p_trxName) {

    	String tableName = MTable.getTableName(Env.getCtx(), AD_Table_ID);
    	//s_log.warning("==== getDocument Record_ID:"+Record_ID+"  tableName 1:"+tableName);
		//
		Doc doc = null;
		StringBuffer sql = new StringBuffer("SELECT * FROM ")
			.append(tableName)
			.append(" WHERE ").append(tableName).append("_ID=? AND Processed='Y'");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), p_trxName);
			pstmt.setInt (1, Record_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				doc = getDocument(p_as, AD_Table_ID, rs, p_trxName);
			}
			else
				s_log.severe("Not Found: " + tableName + "_ID=" + Record_ID);
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		return doc;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDocFactory#getDocument(org.compiere.model.MAcctSchema, int, java.sql.ResultSet, java.lang.String)
	 */
    @Override
    public Doc getDocument(MAcctSchema p_as, int AD_Table_ID, ResultSet p_rs, String p_trxName) {

    	Doc doc = null;	
		String tableName = MTable.getTableName(Env.getCtx(), AD_Table_ID);

		// Table Name: AMN_Payroll
		if (tableName.equalsIgnoreCase(MAMN_Payroll.Table_Name)) {
			doc = new Doc_AMNPayroll(p_as, p_rs, p_trxName);
//s_log.warning("======= IS PP AMN getDocument Doc:"+doc);
			return doc;
		} 

		return doc;
    }

}

//AMERP-1000021
////s_log.warning("======AMN = getDocument ResultSet AD_Table_ID:"+AD_Table_ID+"  Table Name:"+tableName);
//	// Table Name: M_Production
//	if (tableName.equalsIgnoreCase(MProduction.Table_Name)) {
////		doc = new Doc_AMMProduction(p_as, p_rs, p_trxName);
////s_log.warning("======= IS AMN getDocument Doc:"+doc);
//		return doc;
//	// Table Name_ M_InOut
//	} 
//	// MInOut
//	if (tableName.equalsIgnoreCase(MInOut.Table_Name)) {
////		doc = new Doc_AMMInOut(p_as, p_rs, p_trxName);
////s_log.warning("======= IS AMN getDocument Doc:"+doc);
//		return doc;
//
//	} 

//// Other Tables
////s_log.log(Level.SEVERE, "Unknown AD_Table_ID=" + AD_Table_ID);
//String packageName = "org.compiere.acct";
//String className = null;
//
//int firstUnderscore = tableName.indexOf("_");
//if (firstUnderscore == 1)
//	className = packageName + ".Doc_" + tableName.substring(2).replaceAll("_", "");
//else
//	className = packageName + ".Doc_" + tableName.replaceAll("_", "");
//try
//{
//	Class<?> cClass = Class.forName(className);
//	Constructor<?> cnstr = cClass.getConstructor(new Class[] {MAcctSchema.class, ResultSet.class, String.class});
//	doc = (Doc) cnstr.newInstance(p_as, p_rs, p_trxName);
//}
//catch (Exception e)
//{
//	s_log.log(Level.SEVERE, "Doc Class invalid: " + className + " (" + e.toString() + ")");
//	throw new AdempiereUserError("Doc Class invalid: " + className + " (" + e.toString() + ")");
//}
//if (doc == null)
//	s_log.log(Level.SEVERE, "Unknown AD_Table_ID=" + AD_Table_ID);
////s_log.warning("======= IS NOT AMN getDocument Doc:"+doc);
