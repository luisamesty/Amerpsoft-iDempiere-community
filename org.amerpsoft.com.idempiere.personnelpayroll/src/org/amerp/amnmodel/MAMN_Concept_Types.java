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
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class MAMN_Concept_Types extends X_AMN_Concept_Types {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5844685516366162787L;

	public MAMN_Concept_Types(Properties ctx, int AMN_Concept_Types_ID, String trxName) {
		super(ctx, AMN_Concept_Types_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Concept_Types(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Salary Base (Default Reference Concept)
	 * sqlGetAMNConceptTypesSB ()
	 * @param nt p_AD_Client_ID	Client ID
	 * 
	 */
	public static int sqlGetAMNConceptTypesSB (int p_AD_Client_ID)
	{
		String sql;
		int AMN_Concept_Type_ID = 0;
		// AMN_Location
    	sql = "select amn_concept_types_id from adempiere.amn_concept_types "+
    			"WHERE value='SB' AND ad_client_id=? " ;
    	AMN_Concept_Type_ID = DB.getSQLValue(null, sql, p_AD_Client_ID);	
		return AMN_Concept_Type_ID;	
	}

	/**
	 * sqlGetAMNConceptTypesPRESTAMODB ()
	 * @param nt p_AD_Client_ID	Client ID
	 */
	public static int sqlGetAMNConceptTypesPRESTAMODB (int p_AD_Client_ID)
	{
		String sql;
		int AMN_Concept_Type_ID = 0;
		// AMN_Location
    	sql = "select amn_concept_types_id from adempiere.amn_concept_types "+
    			"WHERE value='PRESTAMODB' AND ad_client_id=? " ;
    	AMN_Concept_Type_ID = DB.getSQLValue(null, sql, p_AD_Client_ID);	
		return AMN_Concept_Type_ID;	
	}
	
	/**
	 * sqlGetAMNConceptTypesPRESTAMOCR ()
	 * @param nt p_AD_Client_ID	Client ID
	 */
	public static int sqlGetAMNConceptTypesPRESTAMOCR (int p_AD_Client_ID)
	{
		String sql;
		int AMN_Concept_Type_ID = 0;
		// AMN_Location
    	sql = "select amn_concept_types_id from adempiere.amn_concept_types "+
    			"WHERE value='PRESTAMOCR' AND ad_client_id=? " ;
    	AMN_Concept_Type_ID = DB.getSQLValue(null, sql, p_AD_Client_ID);	
		return AMN_Concept_Type_ID;	
	}
	
	/**
	 * sqlGetAMNConceptTypesABONOPS ()
	 * @param nt p_AD_Client_ID	Client ID
	 */
	public static int sqlGetAMNConceptTypesABONOPS (int p_AD_Client_ID)
	{
		String sql;
		int AMN_Concept_Type_ID = 0;
		// AMN_Location
    	sql = "select amn_concept_types_id from adempiere.amn_concept_types "+
    			"WHERE value='ABONOPS' AND ad_client_id=? " ;
    	AMN_Concept_Type_ID = DB.getSQLValue(null, sql, p_AD_Client_ID);	
		return AMN_Concept_Type_ID;	
	}
	
	/**
	 * sqlGetAMNConceptTypesABONOPS ()
	 * @param nt p_AD_Client_ID	Client ID
	 */
	public static int sqlGetAMNConceptTypesByValue (int p_AD_Client_ID, String p_Value)
	{
		String sql;
		int AMN_Concept_Type_ID = 0;
		// AMN_Location
    	sql = "select amn_concept_types_id from adempiere.amn_concept_types "+
    			"WHERE value='"+p_Value.trim()+"' AND ad_client_id=? " ;
    	AMN_Concept_Type_ID = DB.getSQLValue(null, sql, p_AD_Client_ID);	
		return AMN_Concept_Type_ID;	
	}
	
	/**
	 * Copy copyAccountsFrom settings from another ConceptTypes
	 * overwrites existing data
	 * @param source 
	 */
	public void copyAccountsFrom (MAMN_Concept_Types source)
	{

		if (log.isLoggable(Level.FINE))log.log(Level.FINE, "Copying from:" + source + ", to: " + this);
		// CRE
		setAMN_Cre_Acct(source.getAMN_Cre_Acct());
		setAMN_Cre_DW_Acct(source.getAMN_Cre_DW_Acct());
		setAMN_Cre_IW_Acct(source.getAMN_Cre_IW_Acct());
		setAMN_Cre_SW_Acct(source.getAMN_Cre_SW_Acct());
		setAMN_Cre_MW_Acct(source.getAMN_Cre_MW_Acct());
		// DEB
		setAMN_Deb_Acct(source.getAMN_Deb_Acct());
		setAMN_Deb_DW_Acct(source.getAMN_Deb_DW_Acct());
		setAMN_Deb_IW_Acct(source.getAMN_Deb_IW_Acct());
		setAMN_Deb_SW_Acct(source.getAMN_Deb_SW_Acct());
		setAMN_Deb_MW_Acct(source.getAMN_Deb_MW_Acct());
		saveEx();
		
	}
	
	/**************************************************************************
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//log.warning("newRecord="+newRecord+"  success="+success);
		if (!success)
			return success;
		// NEW RECORDS
		if (newRecord)
		{

		}
		// UPDATES RECORDS
		if (!newRecord ){
			//log.warning("AMN_Concept_Types_ID="+this.getAMN_Concept_Types_ID());
			// UPDATE ALL AMNConcept_Types_pro
	    	this.updateALL_AMNConcept_Types_Proc(this.getAMN_Concept_Types_ID());
	    	// UPDATE ALL AMN_Concept_Types_contract
	    	this.updateALL_AMNConcept_Types_Contract(this.getAMN_Concept_Types_ID());
		}
		return success;
	}	//	afterSave
	
	 /**
     * updateALL_AMNConcept_Types_Proc
     * @param p_AMNConcept_Types_ID
     * @return
     */
    public int updateALL_AMNConcept_Types_Proc(int p_AMNConcept_Types_ID) {
		
		MAMN_Concept_Types act = new MAMN_Concept_Types(Env.getCtx(), p_AMNConcept_Types_ID, null);
		
    	String sql;
		int AMN_Concept_Type_Proc_ID = 0;
		int AMN_Process_ID = 0;
		int no=0;
		// AMN_Location
    	sql = "select amn_concept_types_proc_id, amn_process_id "+
    			"from adempiere.amn_concept_types_proc "+
    			"WHERE amn_concept_types_id = ? ";
    	//log.warning("... amn_concept_types_proc_id sql="+sql);
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, p_AMNConcept_Types_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Concept_Type_Proc_ID = rs.getInt(1);
				AMN_Process_ID = rs.getInt(2);
				//log.warning("AMN_Concept_Type_Proc_ID="+AMN_Concept_Type_Proc_ID+" AMN_Process_ID="+AMN_Process_ID);
				MAMN_Concept_Types_Proc actp = new MAMN_Concept_Types_Proc(Env.getCtx(), AMN_Concept_Type_Proc_ID, null);
				MAMN_Process amp = new MAMN_Process(Env.getCtx(), AMN_Process_ID, null);
				// Update AMNConcept_Types_proc
				actp.updateAMNConcept_Types_Proc(actp.getAMN_Concept_Types_Proc_ID(),amp.getAMN_Process_Value(), act.getValue(),act.getName(),act.getCalcOrder());
				//actp.updateAMNConcept_Types_Proc(actp.getAMN_Concept_Types_Proc_ID(),amp.getAMN_Process_Value(), Value_Concept,Name_Concept,CalcOrder);
				no=+1;
			}
		}
	    catch (SQLException e)
	    {
	    	AMN_Concept_Type_Proc_ID = 0;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
    	return no;
    }
    
    /**
     * updateALL_AMNConcept_Types_Contract
     * @param p_AMNConcept_Types_ID
     * @return
     */
    public int updateALL_AMNConcept_Types_Contract(int p_AMNConcept_Types_ID) {
		
		MAMN_Concept_Types act = new MAMN_Concept_Types(Env.getCtx(), p_AMNConcept_Types_ID, null);
		
    	String sql;
		int AMN_Concept_Type_Contract_ID = 0;
		int AMN_Contract_ID = 0;
		// amn_concept_types_contract_id
    	sql = "select amn_concept_types_contract_id, amn_contract_id "+
    			"from adempiere.amn_concept_types_contract "+
    			"WHERE amn_concept_types_id = ? ";
    	//log.warning("sql="+sql);
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, p_AMNConcept_Types_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Concept_Type_Contract_ID = rs.getInt(1);
				AMN_Contract_ID = rs.getInt(2);
				//log.warning("AMN_Concept_Type_Contract_ID="+AMN_Concept_Type_Contract_ID+" AMN_Contract_ID="+AMN_Contract_ID);
				MAMN_Concept_Types_Contract actc = new MAMN_Concept_Types_Contract(Env.getCtx(), AMN_Concept_Type_Contract_ID, null);
				MAMN_Contract amc = new MAMN_Contract(Env.getCtx(), AMN_Contract_ID, null);
				// Update AMNConcept_Types_proc
				actc.updateAMNConcept_Types_Contract(actc.getAMN_Concept_Types_Contract_ID(), amc.getValue(), act.getValue(),act.getName(),act.getCalcOrder());
				//actc.updateAMNConcept_Types_Contract(actc.getAMN_Concept_Types_Contract_ID(), amc.getValue(),Value_Concept,Name_Concept,CalcOrder);	
			}
		}
	    catch (SQLException e)
	    {
	    	AMN_Concept_Type_Contract_ID = 0;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
    	return 0;
    }
    
	/**
	 * sqlGetAMNConceptTypesID : Return AMN_Concept_Type_ID from Concept_Reference
	 * @param p_AD_Client_ID
	 * @param p_Concept_Reference
	 * @return
	 */
	public static int sqlGetAMNConceptTypesID (int p_AD_Client_ID, String p_Concept_Reference)
	{
		String sql;
		int AMN_Concept_Type_ID = 0;
		// AMN_Location
    	sql = "select distinct amn_concept_types_id from adempiere.amn_concept_types "+
    			" WHERE value='"+p_Concept_Reference+"'"+
    			" AND ad_client_id=? " ;
    	AMN_Concept_Type_ID = DB.getSQLValue(null, sql, p_AD_Client_ID);	
		return AMN_Concept_Type_ID;	
	}
}
