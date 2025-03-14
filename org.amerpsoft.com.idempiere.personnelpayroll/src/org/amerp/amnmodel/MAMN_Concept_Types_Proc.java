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

import org.compiere.util.CLogger;
import org.compiere.util.DB;
/**
 * @author luisamesty
 *
 */
public class MAMN_Concept_Types_Proc extends X_AMN_Concept_Types_Proc {

	/**
	 * 
	 */
    private static final long serialVersionUID = 6446719074404224169L;
    
    static CLogger log = CLogger.getCLogger(MAMN_Concept_Types_Proc.class);
	

	/**
	 * @param p_ctx
	 * @param AMN_Concept_Types_Proc_ID
	 * @param p_trxName
	 */
    public MAMN_Concept_Types_Proc(Properties p_ctx, int AMN_Concept_Types_Proc_ID, String p_trxName) {
	    super(p_ctx, AMN_Concept_Types_Proc_ID, p_trxName);
	    // 
    }
    
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MAMN_Concept_Types_Proc(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // 
    }

    
    /**
     * updateAMNConcept_Types_Proc
     * @param p_AMNConcept_Types_Proc_ID
     * @param p_Process_Value
     * @param p_Value
     * @param p_Name
     * @param p_CalcOrder
     * @return
     */
  
    public boolean updateAMNConcept_Types_Proc(int p_AMNConcept_Types_Proc_ID, 
    		String p_Process_Value, String p_Value, String p_Name, int p_CalcOrder) {

        // Validar si el ID es válido
        if (p_AMNConcept_Types_Proc_ID <= 0) {
            return false;
        }

        // Trim de los parámetros una vez
        String processValue = p_Process_Value != null ? p_Process_Value.trim() : "";
        String value = p_Value != null ? p_Value.trim() : "";
        String name = p_Name != null ? p_Name.trim() : "";

        // Cargar el registro existente
        MAMN_Concept_Types_Proc actp = new MAMN_Concept_Types_Proc(p_ctx, p_AMNConcept_Types_Proc_ID, null);
        
        if (actp.get_ID() <= 0) { // Verificar si el registro realmente existe en la BD
            return false;
        }

        // Construcción de valores formateados
        actp.setValue(processValue + p_CalcOrder);
        actp.setName(String.format("%08d-%s", p_CalcOrder, name));
        actp.setDescription(String.format("%s-%s", value, name));

        // Guardar y verificar si se guardó correctamente
        return actp.save();
    }
    
	/**
	 * sqlGetAMNConceptTypesProcSB ()
	 * @param int p_AMNConcept_Types_ID
	 * @param int p_AMNProcess_ID
	 * FirstReference 
	 */
	public int sqlGetAMNConceptTypesProcFirstReference (	int p_AMNProcess_ID)
	{
		String sql;
		int AMN_Concept_Type_Proc_ID = 0;
		// AMN_Location
    	sql = "select distinct on (ctp.amn_process_id) "
    			+ "ctp.amn_concept_types_proc_id "
    			+ " from adempiere.amn_concept_types_proc as ctp "
    			+ " LEFT JOIN adempiere.amn_concept_types as cty ON ( ctp.amn_concept_types_id = cty.amn_concept_types_id) "
    			+ " WHERE cty.optmode = 'R' "
    			+ " AND amn_process_id=? " ;
    	AMN_Concept_Type_Proc_ID = DB.getSQLValue(null, sql, p_AMNProcess_ID);	
		return AMN_Concept_Type_Proc_ID;	
	}

	/**
	 * sqlGetAMNConceptTypesProc_FromConcept ()
	 * @param int p_AMNConcept_Types_ID
	 * @param int p_AMNProcess_ID
	 */
	public static int sqlGetAMNConceptTypesProc_FromConcept (int p_AMNConcept_Types_ID,
			int p_AMNProcess_ID)
	{
		String sql;
		int AMN_Concept_Type_Proc_ID = 0;
		// AMN_Location
    	sql = "select amn_concept_types_proc_id "+
    			"from adempiere.amn_concept_types_proc "+
    			"WHERE amn_concept_types_id = ? "+
    			"AND amn_process_id=? " ;
    	AMN_Concept_Type_Proc_ID = DB.getSQLValue(null, sql, p_AMNConcept_Types_ID,p_AMNProcess_ID);	
		return AMN_Concept_Type_Proc_ID;	
	}
	
	
	/**
	 * sqlGetAMNConceptTypesProcPRESTAMODB ()
	 * @param int p_AMNConcept_Types_ID
	 * @param int p_AMNProcess_ID
	 */
	public static int sqlGetAMNConceptTypesProcPRESTAMODB (int p_AMNConcept_Types_ID,
			int p_AMNProcess_ID)
	{
		String sql;
		int AMN_Concept_Type_Proc_ID = 0;
		// AMN_Location
    	sql = "select amn_concept_types_proc_id "+
    			"from adempiere.amn_concept_types_proc "+
    			"WHERE amn_concept_types_id = ? "+
    			"AND amn_process_id=? " ;
    	AMN_Concept_Type_Proc_ID = DB.getSQLValue(null, sql, p_AMNConcept_Types_ID,p_AMNProcess_ID);	
		return AMN_Concept_Type_Proc_ID;	
	}
	
	/**
	 * sqlGetAMNConceptTypesProcPRESTAMOCR ()
	 * @param int p_AMNConcept_Types_ID
	 * @param int p_AMNProcess_ID
	 */
	public static int sqlGetAMNConceptTypesProcPRESTAMOCR (int p_AMNConcept_Types_ID,
			int p_AMNProcess_ID)
	{
		String sql;
		int AMN_Concept_Type_Proc_ID = 0;
		// AMN_Location
    	sql = "select amn_concept_types_proc_id "+
    			"from adempiere.amn_concept_types_proc "+
    			"WHERE amn_concept_types_id = ? "+
    			"AND amn_process_id=? " ;
//log.warning("sql="+sql);
    	AMN_Concept_Type_Proc_ID = DB.getSQLValue(null, sql, p_AMNConcept_Types_ID,p_AMNProcess_ID);	
//    	PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try
//		{
//			pstmt = DB.prepareStatement(sql, null);
//            pstmt.setInt(1, p_AMNConcept_Types_ID);
//            pstmt.setInt(2, p_AMNProcess_ID);
//			rs = pstmt.executeQuery();
//			while (rs.next())
//			{
//				AMN_Concept_Type_Proc_ID = rs.getInt(1);
//			}
//		}
//	    catch (SQLException e)
//	    {
//	    	AMN_Concept_Type_Proc_ID = 0;
//	    }
//		finally
//		{
//			DB.close(rs, pstmt);
//			rs = null; pstmt = null;
//		}
		return AMN_Concept_Type_Proc_ID;		}
	
	/**
	 * sqlGetAMNConceptTypesProcABONOPS ()
	 * @param int p_AD_Client_ID
	 * @param int p_AD_Org_ID
	 * @param int p_AMN_Concept_Types_ID
	 */
	public static int sqlGetAMNConceptTypesProcABONOPS (int p_AD_Client_ID, int p_AD_Org_ID, 
			int p_AMN_Concept_Types_ID)
	{
		String sql;
		int AMN_Concept_Type_Proc_ID = 0;
		// AMN_Concept_Types_ID for ABONOPS="ABONOPS"
		//int AMN_Concept_Types_ID_ABONOPS = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOPS'",null).first()).getAMN_Concept_Types_ID();
		// AMN_Location
    	sql = "select amn_concept_types_proc_id "+
    			"from adempiere.amn_concept_types_proc "+
    			"WHERE amn_concept_types_id = ? "+
    			"AND ad_client_id=? " +
    			"AND ad_org_id in (0,?) ";
    	//log.warning("p_AMN_Concept_Types_ID:"+p_AMN_Concept_Types_ID);
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, p_AMN_Concept_Types_ID);
            pstmt.setInt(2, p_AD_Client_ID);
            pstmt.setInt(3, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Concept_Type_Proc_ID = rs.getInt(1);
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
		return AMN_Concept_Type_Proc_ID;	
	}

	/**
	 * sqlGetAMNConceptTypesProcABONOPSA ()
	 * @param int p_AD_Client_ID
	 * @param int p_AD_Org_ID
	 * @param int p_AMN_Concept_Types_ID
	 */
	public static int sqlGetAMNConceptTypesProcABONOPSA (int p_AD_Client_ID, int p_AD_Org_ID, 
			int p_AMN_Concept_Types_ID)
	{
		String sql;
		int AMN_Concept_Type_Proc_ID = 0;
		// AMN_Concept_Types_ID for ABONOPS="ABONOPS"
		//int AMN_Concept_Types_ID_ABONOPSA = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOPSA'",null).first()).getAMN_Concept_Types_ID();
		// int AMN_Concept_Types_ID_ABONOPSA =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ABONOPSA");
		// AMN_Location
    	sql = "select amn_concept_types_proc_id "+
    			"from adempiere.amn_concept_types_proc "+
    			"WHERE amn_concept_types_id = ? "+
    			"AND ad_client_id=? " +
    			"AND ad_org_id in (0,?) ";
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, p_AMN_Concept_Types_ID);
            pstmt.setInt(2, p_AD_Client_ID);
            pstmt.setInt(3, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Concept_Type_Proc_ID = rs.getInt(1);
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
		return AMN_Concept_Type_Proc_ID;	
	}

	/**
	 * sqlGetAMNConceptTypesProcABONOMPSA ()
	 * @param int p_AD_Client_ID
	 * @param int p_AD_Org_ID
	 * @param int p_AMN_Concept_Types_ID
	 */
	public static int sqlGetAMNConceptTypesProcABONOMPSA (int p_AD_Client_ID, int p_AD_Org_ID, 
			int p_AMN_Concept_Types_ID)
	{
		String sql;
		int AMN_Concept_Type_Proc_ID = 0;
		// AMN_Concept_Types_ID for ABONOPS="ABONOPS"
		//int AMN_Concept_Types_ID_ABONOPSA = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOMPSA'",null).first()).getAMN_Concept_Types_ID();
		// int AMN_Concept_Types_ID_ABONOPSA =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ABONOMPSA");
		// AMN_Location
    	sql = "select amn_concept_types_proc_id "+
    			"from adempiere.amn_concept_types_proc "+
    			"WHERE amn_concept_types_id = ? "+
    			"AND ad_client_id=? " +
    			"AND ad_org_id in (0,?) ";
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, p_AMN_Concept_Types_ID);
            pstmt.setInt(2, p_AD_Client_ID);
            pstmt.setInt(3, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Concept_Type_Proc_ID = rs.getInt(1);
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
		return AMN_Concept_Type_Proc_ID;	
	}

}
