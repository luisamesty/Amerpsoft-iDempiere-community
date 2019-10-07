/**********************************************************************
* This file is part of Adempiere ERP Bazaar                           *
* http://www.adempiere.org                                            *
*                                                                     *
* Copyright (C) Carlos Ruiz - globalqss                               *
* Copyright (C) Contributors                                          *
*                                                                     *
* This program is free software; you can redistribute it and/or       *
* modify it under the terms of the GNU General Public License         *
* as published by the Free Software Foundation; either version 2      *
* of the License, or (at your option) any later version.              *
*                                                                     *
* This program is distributed in the hope that it will be useful,     *
* but WITHOUT ANY WARRANTY; without even the implied warranty of      *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
* GNU General Public License for more details.                        *
*                                                                     *
* You should have received a copy of the GNU General Public License   *
* along with this program; if not, write to the Free Software         *
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
* MA 02110-1301, USA.                                                 *
*                                                                     *
* Contributors:                                                       *
* - Carlos Ruiz - globalqss                                           *
*                                                                     *
* Sponsors:                                                           *
* - Company (http://www.globalqss.com)                                *
**********************************************************************/

package org.amerp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amfmodel.MAMF_AllocationHdr;
import org.compiere.acct.Doc;
import org.compiere.acct.Doc_Invoice;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MBankStatement;
import org.compiere.model.MClientInfo;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * 	Client Accounting Processor
 *
 *  @author Luis Amesty From de ClientAcctProcessor
 */
public class AMFClientAcctProcessor extends SvrProcess
{
	/**	Client Parameter		*/
	private static int		p_AD_Client_ID = 0;
	/* The Table */
	private static int  p_AD_Table_ID;
	/* The Period */
	private static int p_C_Period_ID;
	/* The Document TYpe */
	private static int 	p_C_DocType_ID = 0;
	/*  Account Schema */
	private static int p_C_AcctSchema_ID =0;
	/*  Posted Status */
	private static String p_Posted ="";
	/*  Other vars */
	private static String Msg_Value ="";
	private static 		String DateBetween ="";
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("AD_Table_ID"))
				p_AD_Table_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Period_ID")) 
				p_C_Period_ID =  para[i].getParameterAsInt();
			else if (name.equals("C_DocType_ID")) 
				p_C_DocType_ID =  para[i].getParameterAsInt();
			else if (name.equals("Posted")) 
				p_Posted =  para[i].getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{

		// TODO Auto-generated method stub
		// Process Monitor
		IProcessUI processMonitor = Env.getProcessUI(getCtx());
		String Message="";
		String postReturn = "";
		// Period
		MPeriod mperiod = new MPeriod(Env.getCtx(),p_C_Period_ID, null);
		Timestamp DateIni = mperiod.getStartDate();
		Timestamp DateEnd = mperiod.getEndDate();
		String sql = "";
		// Account Schema
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_AD_Client_ID, null); 
		//MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		MAcctSchema asdef = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		if (p_C_AcctSchema_ID == 0 ) {
			// Get Client Acct Schema Default
			p_C_AcctSchema_ID=asdef.getC_AcctSchema_ID();
		} 
		// Set Target Account Schema
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), p_C_AcctSchema_ID , null);
		//
		int Document_Count = 1000 ;
		int DocumentNo = 0;
		int Percent = 0;
		DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ " AND DateAcct <= "+ DB.TO_DATE(DateEnd);

		// DocType
		MDocType mdoctype = new MDocType(Env.getCtx(),p_C_DocType_ID,null);
		String DocTypeName = mdoctype.getPrintName(Env.getAD_Language(Env.getCtx())).trim();
		// Date Between
		// ******************
		// *** INVOICES
		// ******************
		if (p_AD_Table_ID == MInvoice.Table_ID) {
			DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ " AND DateAcct <= "+ DB.TO_DATE(DateEnd);
			// Invoice Count
			//Document_count= Document_count.getMProductionCount(p_AD_Client_ID, DateIni, DateEnd);
			Document_Count = getInvoiceCount();
			//log.warning("Document_Count:"+Document_Count+ "  C_DocType_ID:"+p_C_DocType_ID+
				//"  DateIni:"+DateIni+"  DateEnd:"+DateEnd);
			// Show 
			addLog( DocTypeName+"  No:"+Document_Count);
			// SQL
			if (p_Posted.isEmpty()) {
				sql = "SELECT C_Invoice_ID "
					+ " FROM C_Invoice " 
					+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
					+ DateBetween 
					+ " AND C_DocType_ID = "+p_C_DocType_ID ;
			} else {
				sql = "SELECT C_Invoice_ID "
						+ " FROM C_Invoice " 
						+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
						+ DateBetween 
						+ " AND C_DocType_ID = "+p_C_DocType_ID 
						+ " AND Posted = '"+p_Posted+"'";
			}
			//log.warning("....sql="+sql);
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{			
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					DocumentNo=DocumentNo+1;
					// Percentage Monitor
					if (Document_Count > 0)
						Percent =  (DocumentNo * 100 /(Document_Count));
					else
						Percent = 0;
					int C_Invoice_ID= rs.getInt(1);
					MInvoice minvoice = new MInvoice(Env.getCtx(), C_Invoice_ID, null);
					Msg_Value =Msg.getElement(Env.getCtx(), "C_Invoice_ID")+":"+minvoice.getDocumentNo().trim()+
							" ("+ DocumentNo+"/"+Document_Count+") " +
							" "+Msg.translate(Env.getCtx(), "Date")+":"+String.format("%-20s", minvoice.getDateAcct() );
					String mpDocStatus = minvoice.getDocStatus();
					if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
							mpDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ) {
						Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
					} else {
						// DOCS
						if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
								mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
								mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed)) {		
							Doc doc =  (Doc) Doc_Invoice.get(as, p_AD_Table_ID, C_Invoice_ID, this.get_TrxName());
							postReturn = doc.post(true, true);
							Msg_Value=Msg_Value+ " ** "+postReturn+" **";
						} else {
							Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
						}
					}					
					// Document Header
					addLog(Msg_Value);
					// 
					// Process Monitor
					if (processMonitor != null)
					{
						Message = "( "+String.format("%-5s",Percent)+ "% ) "+DocTypeName+":"+ DocumentNo+"/"+Document_Count +"  " +
								Msg.translate(Env.getCtx(), "Date")+":"+
								String.format("%-20s", minvoice.getDateAcct()).replace(' ', '_') +" - " +
								"No:"+
								String.format("%-40s", minvoice.getDocumentNo().trim()).replace(' ', '_') ;
						processMonitor.statusUpdate(Message);
					}
				}
			}
			catch (Exception ee)
			{
				log.log(Level.SEVERE, sql, ee);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
		// *********************
		// *** PAYMENTS
		// *********************

		} else if  (p_AD_Table_ID == MPayment.Table_ID) {
			DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ " AND DateAcct <= "+ DB.TO_DATE(DateEnd);
			// Payment Count
			//Document_count= Document_count.getMProductionCount(p_AD_Client_ID, DateIni, DateEnd);
			Document_Count = getPaymentCount();
			//log.warning("Document_Count:"+Document_Count+ "  C_DocType_ID:"+p_C_DocType_ID+
				//"  DateIni:"+DateIni+"  DateEnd:"+DateEnd);
			// Show 
			addLog( DocTypeName+"  No:"+Document_Count);
			// SQL
			if (p_Posted.isEmpty()) {
				sql = "SELECT C_Payment_ID "
						+ " FROM C_Payment " 
						+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
						+ DateBetween 
						+ " AND C_DocType_ID = "+p_C_DocType_ID ;
			} else {
				sql = "SELECT C_Payment_ID "
						+ " FROM C_Payment " 
						+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
						+ DateBetween 
						+ " AND C_DocType_ID = "+p_C_DocType_ID 
						+ " AND Posted = '"+p_Posted+"'";
			}
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{			
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					DocumentNo=DocumentNo+1;
					// Percentage Monitor
					if (Document_Count > 0)
						Percent =  (DocumentNo * 100 /(Document_Count));
					else
						Percent = 0;

					int C_Payment_ID= rs.getInt(1);
					MPayment mpayment = new MPayment(Env.getCtx(), C_Payment_ID, null);
					Msg_Value =Msg.getElement(Env.getCtx(), "C_Payment_ID")+":"+mpayment.getDocumentNo().trim()+
							" ("+ DocumentNo+"/"+Document_Count+") " +
							" "+Msg.translate(Env.getCtx(), "Date")+":"+String.format("%-20s", mpayment.getDateAcct() );
					String mpDocStatus = mpayment.getDocStatus();
					if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
							mpDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ) {
						Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
					} else {
						// DOCS
						if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
								mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
								mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed) ) {
							Doc doc =  (Doc) Doc_Invoice.get(as, p_AD_Table_ID, C_Payment_ID, this.get_TrxName());
							postReturn = doc.post(true, true);
							Msg_Value=Msg_Value+ " ** "+postReturn+" **";

						} else {
							Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
						}
					}					
					// Document Header
					addLog(Msg_Value);
					// 
					// Process Monitor
					if (processMonitor != null)
					{
						Message = "( "+String.format("%-5s",Percent)+ "% ) "+DocTypeName+":"+ DocumentNo+"/"+Document_Count +"  " +
								Msg.translate(Env.getCtx(), "Date")+":"+
								String.format("%-20s", mpayment.getDateAcct()).replace(' ', '_') +" - " +
								"No:"+
								String.format("%-40s", mpayment.getDocumentNo().trim()).replace(' ', '_') ;
						processMonitor.statusUpdate(Message);
					}
				}
			}
			catch (Exception ee)
			{
				log.log(Level.SEVERE, sql, ee);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}	
			
		// *********************
		// *** BANK STATEMENT
		// *********************
		} else if (p_AD_Table_ID == MBankStatement.Table_ID) {
			DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ " AND DateAcct <= "+ DB.TO_DATE(DateEnd);
			// Invoice Count
			//Document_count= Document_count.getMProductionCount(p_AD_Client_ID, DateIni, DateEnd);
			Document_Count = getBankStatementCount();
			//log.warning("Document_Count:"+Document_Count+ "  C_DocType_ID:"+p_C_DocType_ID+
				//"  DateIni:"+DateIni+"  DateEnd:"+DateEnd);
			// Show 
			addLog( DocTypeName+"  No:"+Document_Count);
			// SQL
			if (p_Posted.isEmpty()) {
				sql = "SELECT C_BankStatementt_ID "
						+ " FROM C_BankStatement " 
						+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
						+ DateBetween ;			
			} else {
				sql = "SELECT C_BankStatementt_ID "
						+ " FROM C_BankStatement " 
						+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
						+ DateBetween 		
						+ " AND Posted = '"+p_Posted+"'";
			}
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{			
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					DocumentNo=DocumentNo+1;
					// Percentage Monitor
					if (Document_Count > 0)
						Percent =  (DocumentNo * 100 /(Document_Count));
					else
						Percent = 0;
					int C_BankStatement_ID= rs.getInt(1);
					MBankStatement mbs = new MBankStatement(Env.getCtx(), C_BankStatement_ID, null);
					Msg_Value =Msg.getElement(Env.getCtx(), "C_Payment_ID")+":"+mbs.getDocumentNo().trim()+
							" ("+ DocumentNo+"/"+Document_Count+") " +
							" "+Msg.translate(Env.getCtx(), "Date")+":"+String.format("%-20s", mbs.getDateAcct() );
					String mpDocStatus = mbs.getDocStatus();
					if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
							mpDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ) {
						Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
					} else {
						// DOCS
						if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
								mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
								mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed)) {		
							Doc doc =  (Doc) Doc_Invoice.get(as, p_AD_Table_ID, C_BankStatement_ID, this.get_TrxName());
							postReturn = doc.post(true, true);
							Msg_Value=Msg_Value+ " ** "+postReturn+" **";
						} else {
							Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
						}
					}					
					// Document Header
					addLog(Msg_Value);
					// 
					// Process Monitor
					if (processMonitor != null)
					{
						Message = "( "+String.format("%-5s",Percent)+ "% ) "+DocTypeName+":"+ DocumentNo+"/"+Document_Count +"  " +
								Msg.translate(Env.getCtx(), "Date")+":"+
								String.format("%-20s", mbs.getDateAcct()).replace(' ', '_') +" - " +
								"No:"+
								String.format("%-40s", mbs.getDocumentNo().trim()).replace(' ', '_') ;
						processMonitor.statusUpdate(Message);
					}
				}
			}
			catch (Exception ee)
			{
				log.log(Level.SEVERE, sql, ee);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}	
		// ******************************
		// *** ALLOCATIONS
		// ** Different Account Process
		// ** Due to multiple AcctSchemas
		// *******************************
		// 	AMF_MAllocationHdr  MAllocationHdr
		} else if (p_AD_Table_ID == MAllocationHdr.Table_ID) {
			DateBetween=" AND DateAcct >= "+ DB.TO_DATE(DateIni)+ " AND DateAcct <= "+ DB.TO_DATE(DateEnd);
			// Invoice Count
			//Document_count= Document_count.getMProductionCount(p_AD_Client_ID, DateIni, DateEnd);
			Document_Count = getAllocationHdrCount();
			//log.warning("Document_Count:"+Document_Count+ "  C_DocType_ID:"+p_C_DocType_ID+
				//"  DateIni:"+DateIni+"  DateEnd:"+DateEnd);
			// Show 
			addLog( DocTypeName+"  No:"+Document_Count);
			// SQL
			if (p_Posted.isEmpty()) {
				sql = "SELECT C_AllocationHdr_ID "
						+ " FROM C_AllocationHdr " 
						+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
						+ DateBetween  ;
			} else {
				sql = "SELECT C_AllocationHdr_ID "
						+ " FROM C_AllocationHdr " 
						+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
						+ DateBetween  
						+ " AND Posted = '"+p_Posted+"'";
			}
			//log.warning("....sql="+sql);
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{			
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					DocumentNo=DocumentNo+1;
					// Percentage Monitor
					if (Document_Count > 0)
						Percent =  (DocumentNo * 100 /(Document_Count));
					else
						Percent = 0;
					int C_AllocationHdr_ID= rs.getInt(1);
					MAMF_AllocationHdr mmallhdr = new MAMF_AllocationHdr();
					MAllocationHdr mallochdr = new MAllocationHdr(Env.getCtx(), C_AllocationHdr_ID, null);
					Msg_Value =Msg.getElement(Env.getCtx(), "C_AllocationHdr_ID")+":"+mallochdr.getDocumentNo().trim()+
							" ("+ DocumentNo+"/"+Document_Count+") " +
							" "+Msg.translate(Env.getCtx(), "Date")+":"+String.format("%-20s", mallochdr.getDateAcct() );
					String mpDocStatus = mallochdr.getDocStatus();
					if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
							mpDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ) {
						Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
					} else {
						// DOCS
						if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
								mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
								mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed)) {		
							Doc doc =  (Doc) Doc_Invoice.get(as, p_AD_Table_ID, C_AllocationHdr_ID, this.get_TrxName());
							//postReturn = doc.post(true, true);
							// RePostMAMFAllocationHeader
							postReturn = mmallhdr.RePostMAMFAllocationHeader(mallochdr.getC_AllocationHdr_ID(), as, get_TrxName());
							//postReturn = doc.post(true, true);
							Msg_Value=Msg_Value+ " ** "+postReturn+" **";
						} else {
							Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
						}
					}					
					// Document Header
					addLog(Msg_Value);
					// 
					// Process Monitor
					if (processMonitor != null)
					{
						Message = "( "+String.format("%-5s",Percent)+ "% ) "+DocTypeName+":"+ DocumentNo+"/"+Document_Count +"  " +
								Msg.translate(Env.getCtx(), "Date")+":"+
								String.format("%-20s", mallochdr.getDateAcct()).replace(' ', '_') +" - " +
								"No:"+
								String.format("%-40s", mallochdr.getDocumentNo().trim()).replace(' ', '_') ;
						processMonitor.statusUpdate(Message);
					}
				}
			}
			catch (Exception ee)
			{
				log.log(Level.SEVERE, sql, ee);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
		}  			// TO BE CONTINUED

		return null;
	}	//	doIt

	/**
	 * getInvoiceCount
	 * @param p_AD_Client_ID
	 * 
	 */
	public static Integer getInvoiceCount( ) {
				
		Integer retValue = 0;
		String sql = "";
		// SQL
		if (p_Posted.isEmpty()) {
			sql = "SELECT "
					+ " CAST(count(*) AS numeric ) " 
					+ " FROM C_Invoice " 
					+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
					+ DateBetween 
					+ " AND C_DocType_ID = "+p_C_DocType_ID ;	
		} else {
			sql = "SELECT "
					+ " CAST(count(*) AS numeric ) " 
					+ " FROM C_Invoice " 
					+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
					+ DateBetween 
					+ " AND C_DocType_ID = "+p_C_DocType_ID 	
					+ " AND Posted = '"+p_Posted+"'";
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
 			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue= rs.getInt(1);
				//log.warning("sql:"+sql+"  Product_Count:"+Product_Count);
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//log.warning(" FINAL Invoice_Count:"+retValue);
		return retValue;
	} //  getInvoiceCount
	
	/**
	 * getPaymentCount
	 * @param p_AD_Client_ID
	 * 
	 */
	public static Integer getPaymentCount( ) {
				
		Integer retValue = 0;
		String sql = "";
		// SQL
		if (p_Posted.isEmpty()) {
			sql = "SELECT "
				+ " CAST(count(*) AS numeric ) " 
				+ " FROM C_Payment " 
				+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
				+ DateBetween 
				+ " AND C_DocType_ID = "+p_C_DocType_ID ;	
		} else {
			sql = "SELECT "
					+ " CAST(count(*) AS numeric ) " 
					+ " FROM C_Payment " 
					+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
					+ DateBetween 
					+ " AND C_DocType_ID = "+p_C_DocType_ID 	
					+ " AND Posted = '"+p_Posted+"'";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
  			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue= rs.getInt(1);
				//log.warning("sql:"+sql+"  Product_Count:"+Product_Count);
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//log.warning(" FINAL Payment_Count:"+retValue);
		return retValue;
	}
	
	/**
	 * getBankStatementCount
	 * @param p_AD_Client_ID
	 * 
	 */
	public static Integer getBankStatementCount( ) {
				
		Integer retValue = 0;
		// SQL
		String sql = "";
		if (p_Posted.isEmpty()) {
			sql = "SELECT "
					+ " CAST(count(*) AS numeric ) " 
					+ " FROM C_BankStatement " 
					+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
					+ DateBetween ;
		} else {
			sql = "SELECT "
					+ " CAST(count(*) AS numeric ) " 
					+ " FROM C_BankStatement " 
					+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
					+ DateBetween 
					+ " AND Posted = '"+p_Posted+"'";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
 			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue= rs.getInt(1);
				//log.warning("sql:"+sql+"  Product_Count:"+Product_Count);
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//log.warning(" FINAL BS_Count:"+retValue);
		return retValue;
	}
	
	/**
	 * getAllocationHdrCount
	 * @param p_AD_Client_ID
	 * 
	 */
	public static Integer getAllocationHdrCount( ) {
				
		Integer retValue = 0;
		// SQL
		String sql = "";
		if (p_Posted.isEmpty()) {
			sql = "SELECT "
					+ " CAST(count(*) AS numeric ) " 
					+ " FROM C_AllocationHdr " 
					+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
					+ DateBetween  ;	
		} else {
			sql = "SELECT "
					+ " CAST(count(*) AS numeric ) " 
					+ " FROM C_AllocationHdr " 
					+ " WHERE AD_Client_ID= "+ p_AD_Client_ID 
					+ DateBetween  	
					+ " AND Posted = '"+p_Posted+"'";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
 			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue= rs.getInt(1);
				//log.warning("sql:"+sql+"  Product_Count:"+Product_Count);
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//log.warning(" FINAL Invoice_Count:"+retValue);
		return retValue;
	} //  getAllocationHdrCount

}	//	ClientAcctProcessor
