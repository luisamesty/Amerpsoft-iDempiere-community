/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.amerp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MBankStatement;
import org.compiere.model.MCash;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInventory;
import org.compiere.model.MInvoice;
import org.compiere.model.MJournal;
import org.compiere.model.MMatchInv;
import org.compiere.model.MMatchPO;
import org.compiere.model.MMovement;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.MPeriodControl;
import org.compiere.model.MProjectIssue;
import org.compiere.model.MRequisition;
import org.compiere.model.MTable;
import org.compiere.model.X_M_Production;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.eevolution.model.X_DD_Order;
import org.eevolution.model.X_HR_Process;
import org.eevolution.model.X_PP_Cost_Collector;
import org.eevolution.model.X_PP_Order;

/**
 *	Accounting Fact Reset
 *	
 *  @author Jorg Janke
 *  @version $Id: FactAcctReset.java,v 1.5 2006/09/21 21:05:02 jjanke Exp $
 *  Luis Amesty: Reset by Dates Allways
 */
public class AMFFactAcctReset extends SvrProcess
{
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/**	Client Parameter		*/
	private int		p_AD_Client_ID = 0;
	/** Table Parameter			*/
	private int		p_AD_Table_ID = 0;
	/* The Period */
	private int 	p_C_Period_ID = 0;
	/* The Document TYpe */
	private int 	p_C_DocType_ID = 0;
	/**	Delete Parameter		*/
	private boolean	p_DeletePosting = false;
	/** DocBaseType **/
	private int p_DocBaseType = 0;
	
	private int		m_countReset = 0;
	private int		m_countDelete = 0;
	private Timestamp p_DateAcct_From = null ;
	private Timestamp p_DateAcct_To = null;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null && para[i].getParameter_To() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("AD_Table_ID"))
				p_AD_Table_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_Period_ID")) 
				p_C_Period_ID =  para[i].getParameterAsInt();
			else if (name.equals("C_DocType_ID")) 
				p_C_DocType_ID =  para[i].getParameterAsInt();
			else if (name.equals("DeletePosting"))
				p_DeletePosting = "Y".equals(para[i].getParameter());
			else if (name.equals("DateAcct"))
			{
				p_DateAcct_From = (Timestamp)para[i].getParameter();
				p_DateAcct_To = (Timestamp)para[i].getParameter_To();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (log.isLoggable(Level.INFO)) log.info("AD_Client_ID=" + p_AD_Client_ID 
			+ ", AD_Table_ID=" + p_AD_Table_ID + ", C_Period_ID=" + p_C_Period_ID 
			+  ", C_DocType_ID=" + p_C_DocType_ID +", DeletePosting=" + p_DeletePosting);
		//	Table with Accounting Consequences
		MTable mtable = new MTable(getCtx(), p_AD_Table_ID, null);
		String TableName = mtable.getTableName().trim();
log.warning("AD_Client_ID=" + p_AD_Client_ID 
				+ ", AD_Table_ID=" + p_AD_Table_ID + ", C_Period_ID=" + p_C_Period_ID 
				+  ", C_DocType_ID=" + p_C_DocType_ID +", DeletePosting=" + p_DeletePosting);
		
		if (p_DeletePosting){
			// DELETE FACTS
			deleteFacts (TableName, p_AD_Table_ID, p_C_Period_ID,p_C_DocType_ID);
		} else {
			// RESET ONLY
			resetDocs (TableName, p_AD_Table_ID, p_C_Period_ID,p_C_DocType_ID);
		}
		return "@Updated@ = " + m_countReset + ", @Deleted@ = " + m_countDelete;
		
	}	//	doIt

	/**
	 * 	Reset Accounting Table and update count
	 * 	@param TableName table name
	 *	@param AD_Table_ID table
	 *	@param C_Period_ID fiscal period
	 *	@param C_DocType_ID Document Type
	 *
	 */
	private void resetDocs (String TableName, int AD_Table_ID, int C_Period_ID, int C_DocType_ID)
	{
		// Period
		MPeriod mperiod = new MPeriod(Env.getCtx(),p_C_Period_ID, null);
		Timestamp DateIni = mperiod.getStartDate();
		Timestamp DateEnd = mperiod.getEndDate();
		String DateBetween ="";
		// Date Between
		if (AD_Table_ID == MInvoice.Table_ID)
			DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ "AND DateAcct <= "+ DB.TO_DATE(DateEnd);
		else if (AD_Table_ID == MPayment.Table_ID)
			DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ "AND DateAcct <= "+ DB.TO_DATE(DateEnd);
		else if (AD_Table_ID == MBankStatement.Table_ID)
			DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ "AND DateAcct <= "+ DB.TO_DATE(DateEnd);
		else if (AD_Table_ID == MAllocationHdr.Table_ID)
			DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ "AND DateAcct <= "+ DB.TO_DATE(DateEnd);

		//
		String sql = "UPDATE " + TableName
			+ " SET Processing='N' "
			+ "WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND C_docType_ID="+C_DocType_ID
			+ DateBetween
			+ " AND (Processing<>'N' OR Processing IS NULL)";
		int unlocked = DB.executeUpdate(sql, get_TrxName());
		//log.warning("1 sql update="+sql);
		//
		sql = "UPDATE " + TableName
			+ " SET Posted='N' "
			+ "WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND C_docType_ID="+C_DocType_ID
			+ DateBetween
			+ " AND (Posted NOT IN ('Y','N') OR Posted IS NULL) AND Processed='Y'";
		//log.warning("1 sql update="+sql);
		// OJO no ejecuta 
		int invalid = DB.executeUpdate(sql, get_TrxName());
		//
		if (unlocked + invalid != 0)
			if (log.isLoggable(Level.FINE)) log.fine(TableName + " - Unlocked=" + unlocked + " - Invalid=" + invalid);
		m_countReset += unlocked + invalid; 
	}	//	reset

	/**
	 * 	Delete Accounting Table where period status is open and update count.
	 * 	@param TableName table name
	 *	@param AD_Table_ID table
	 *	@param C_Period_ID fiscal period
	 *	@param C_DocType_ID Document Type
	 */
	private void deleteFacts (String TableName, int AD_Table_ID, int C_Period_ID, int C_DocType_ID)
	{
		// MDocType
		MDocType mdoctype = new MDocType(Env.getCtx(), C_DocType_ID, null);
		String docBaseType = mdoctype.getDocBaseType();
		// Period
		MPeriod mperiod = new MPeriod(Env.getCtx(),p_C_Period_ID, null);
		Timestamp DateIni = mperiod.getStartDate();
		Timestamp DateEnd = mperiod.getEndDate();
		String DateBetween =" AND DateAcct >=" + DB.TO_DATE(DateIni)
				+" AND DateAcct <= "+ DB.TO_DATE(DateEnd) ;

		// AMERP Not Reset
		// reset(TableName);
		m_countReset = 0;
		
		//	Doc
		String sql1 = "UPDATE " + TableName
			+ " SET Posted='N', Processing='N' "
			+ "WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND (Posted<>'N' OR Posted IS NULL OR Processing<>'N' OR Processing IS NULL)"
			+ " AND C_docType_ID="+C_DocType_ID
			+ DateBetween
			+ " AND (Processing<>'N' OR Processing IS NULL)";
		
		if (log.isLoggable(Level.FINE))log.log(Level.FINE, sql1);
		//log.warning("UPDATE sql update="+sql1);
		int reset = DB.executeUpdate(sql1, get_TrxName()); 
		//	Fact
		String sql2="";
		if (AD_Table_ID == MInvoice.Table_ID) {
			sql2 = "DELETE FROM Fact_Acct "+
					" WHERE AD_Client_ID = "+ p_AD_Client_ID +
					" AND AD_table_ID = "+ p_AD_Table_ID +
					" AND Fact_Acct_ID IN ( "+
					"   SELECT Fact_Acct_ID "+
					"   FROM Fact_Acct faa "+
					"   LEFT JOIN C_Invoice inv ON (inv.C_Invoice_ID = faa.Record_ID) "+
					"    WHERE faa.AD_Client_ID= " + p_AD_Client_ID +
					"	 AND faa.AD_table_ID =  " + p_AD_Table_ID +
					"    AND faa.DateAcct >= " + DB.TO_DATE(DateIni) +
					"    AND faa.DateAcct <= "+ DB.TO_DATE(DateEnd) +
					"    AND inv.C_DocType_ID = "+ p_C_DocType_ID+
					" )" ;
		}
		else if (AD_Table_ID == MPayment.Table_ID) {
			sql2 = "DELETE FROM Fact_Acct "+
					" WHERE AD_Client_ID = "+ p_AD_Client_ID +
					" AND AD_table_ID = "+ p_AD_Table_ID +
					" AND Fact_Acct_ID IN ( "+
					"   SELECT Fact_Acct_ID "+
					"   FROM Fact_Acct faa "+
					"   LEFT JOIN C_Payment pay ON (pay.C_Payment_ID = faa.Record_ID) "+
					"    WHERE faa.AD_Client_ID= " + p_AD_Client_ID +
					"	 AND faa.AD_table_ID =  " + p_AD_Table_ID +
					"    AND faa.DateAcct >= " + DB.TO_DATE(DateIni) +
					"    AND faa.DateAcct <= "+ DB.TO_DATE(DateEnd) +
					"    AND pay.C_DocType_ID = "+ p_C_DocType_ID +
					" )" ;
		}
		else if (AD_Table_ID == MBankStatement.Table_ID) {
			sql2 = "DELETE FROM Fact_Acct "+
					" WHERE AD_Client_ID = "+ p_AD_Client_ID +
					" AND AD_table_ID = "+ p_AD_Table_ID +
					" AND Fact_Acct_ID IN ( "+
					"   SELECT Fact_Acct_ID "+
					"   FROM Fact_Acct faa "+
					"   LEFT JOIN C_BankStatement bas ON (bas.C_BankStatement_ID = faa.Record_ID) "+
					"    WHERE faa.AD_Client_ID= " + p_AD_Client_ID +
					"	 AND faa.AD_table_ID =  " + p_AD_Table_ID +
					"    AND faa.DateAcct >= " + DB.TO_DATE(DateIni) +
					"    AND faa.DateAcct <= "+ DB.TO_DATE(DateEnd) ;
		}
		else if (AD_Table_ID == MAllocationHdr.Table_ID) {
			
		}
		//log.warning("DELETE sql2 ="+sql2);
		if (log.isLoggable(Level.FINE))log.log(Level.FINE, sql2);
		
		int deleted = DB.executeUpdate(sql2, get_TrxName());
		//
		if (log.isLoggable(Level.INFO)) log.info(TableName + "(" + AD_Table_ID + ") - Reset=" + reset + " - Deleted=" + deleted);
		m_summary.append("@TableName@"+":"+TableName+ "\r\n");
		m_summary.append("@DocProcessed@"+ ": \r\n"+mdoctype.getDocBaseType()+"-"+mdoctype.getName().trim());
		
		String s = TableName + " - Reset=" + reset + " - Deleted=" + deleted+ "\r\n"+ m_summary.toString();
		addLog(s);
		//
		m_countReset += reset;
		m_countDelete += deleted;
	}	//	delete

}	//	FactAcctReset
