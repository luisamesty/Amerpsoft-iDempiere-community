package org.amerp.amnforms;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.component.WListbox;
import org.compiere.apps.IStatusBar;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.model.MColumn;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class AMNPayrollAssist {

	private static final long serialVersionUID = 8686158223508939747L;
	/**	Window No			*/
	public int         	m_WindowNo = 0;
	/** MWindow                 */
	public GridWindow         m_mWindow = null;
	/** MTab pointer            */
	public GridTab            m_mTab = null;

	public MQuery          m_staticQuery = null;
	/**	Logger			*/
	public static CLogger log = CLogger.getCLogger(AMNPayrollAssist.class);
	
	/** Client ID               */
	public int             m_AD_Client_ID = 0;
	/** Account Schema ID               */
	public int             	m_C_AcctSchema_ID = 0;
	public Integer         		m_AMN_Contract_ID = 0;
	public Integer         		m_AMN_Location_ID = 0;
	public Integer         		m_AMN_Sector_ID = 0;
	public Integer         		m_AMN_Employee_ID = 0;
	public int				m_Status_Item=0;
	public String			m_Status="A";
	public Timestamp		m_DateFrom;
	public Timestamp		m_DateTo;
	public String			m_isActive="Y";
	
	/**
	 *  Dynamic Layout (Grid).
	 * 	Based on AD_Window: Material Transactions
	 */
	public void dynInit() throws Exception
	{
		m_staticQuery = new MQuery();

	}   //  dynInit
	
	/**************************************************************************
	 *  Refresh - Create Query and refresh grid
	 */
	public void refresh(Object organization, Object locator, Object product, Object movementType, 
			Timestamp movementDateFrom, Timestamp movementDateTo, IStatusBar statusBar)
	{
		/**
		 *  Create Where Clause
		 */
		MQuery query = m_staticQuery.deepCopy();
		//  Organization
		if (organization != null && organization.toString().length() > 0)
			query.addRestriction("AD_Org_ID", MQuery.EQUAL, organization);

		/**
		 *  Refresh/Requery
		 */
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "StartSearch"), false);
		//
//		m_mTab.setQuery(query);
//		m_mTab.query(false);
		//
//		int no = m_mTab.getRowCount();
		statusBar.setStatusLine(" ", false);
//		statusBar.setStatusDB(Integer.toString(no));
	}   //  refresh
	
	public int AD_Window_ID;
	public MQuery query;
	
	/**
	 *  Zoom
	 */
	public void zoom()
	{
		log.info("");
		//
		AD_Window_ID = 0;
		String ColumnName = null;
		String SQL = null;

	}   //  zoom

	public Vector<String> getAMNEmployeeColumnNames()
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Value"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Name"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Status"));
		columnNames.add(Msg.getElement(Env.getCtx(), "AMN_Contract_ID"));
		columnNames.add(Msg.getElement(Env.getCtx(), "AMN_Location_ID"));
		columnNames.add(Msg.getElement(Env.getCtx(), "AMN_Sector_ID"));
		columnNames.add(Msg.getElement(Env.getCtx(), "IsActive"));
		return columnNames;
	}
	
	public void setEmployeeColumnClass(WListbox employee)
	{
		int i = 0;
		employee.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		employee.setColumnClass(i++, String.class, true);        	//  1-Value
		employee.setColumnClass(i++, String.class, true);           //  2-Name
		employee.setColumnClass(i++, String.class, true);      		//  3-Status
		employee.setColumnClass(i++, String.class, true);   		//  4-Contract
		employee.setColumnClass(i++, String.class, true);       	//  5-Location
		employee.setColumnClass(i++, String.class, true);      	 	//  6-Sector
		employee.setColumnClass(i++, Boolean.class, false);			//  7-IsActive
		employee.autoSize();
	}
	
	public Vector<Vector<Object>> getEmployeeData()
	{		
		/********************************
		 *  Load unallocated Payments
		 ********************************/
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuilder sql = new StringBuilder(""
				+ "SELECT \n"
				+ "e.value, e.name AS employee_name, c.name AS contract_name, "
				+ "l.name AS location_name, s.name AS sector_name, "
				+ "e.status AS employee_status, e.isactive "
				+ "FROM AMN_Employee e "
				+ "LEFT OUTER JOIN AMN_Contract c ON (c.AMN_contract_ID = e.AMN_Contract_ID) "
				+ "LEFT OUTER JOIN AMN_Location l ON(l.AMN_Location_ID = e.AMN_Location_ID) "
				+ "LEFT OUTER JOIN AMN_Sector s ON(s.AMN_Sector_ID = e.AMN_Sector_ID) "  
				+ " WHERE e.isActive= ? " // primer param
				);
		if (m_AMN_Contract_ID > 0) {
		    sql.append(" AND e.AMN_Contract_ID = "+m_AMN_Contract_ID);
		}
		if (m_AMN_Location_ID > 0) {
		    sql.append(" AND e.AMN_Location_ID = "+m_AMN_Location_ID);
		}
		if (m_AMN_Sector_ID > 0) {
		    sql.append(" AND e.AMN_Sector_ID = "+m_AMN_Sector_ID);
		}
		sql.append(" AND e.Status = ? "); // segundo param
		sql.append(" ORDER BY e.value");                 		//      #5
		// role security
		sql = new StringBuilder( MRole.getDefault(Env.getCtx(), false).addAccessSQL( sql.toString(), "e", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO ) );
		
		if (log.isLoggable(Level.FINE)) log.fine("PaySQL=" + sql.toString());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setString(1, m_isActive);
			pstmt.setString(2, m_Status);

			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getString(1));      	//  1-Value
				line.add(rs.getString(2));			//  2-Name
				line.add(rs.getString(3));			//  3-Status
				line.add(rs.getString(4));      	//  4-Contract
				line.add(rs.getString(5));			//  5-Location
				line.add(rs.getString(6));			//  6-Sector
				line.add(rs.getBoolean(7));       	//  7-IsActive
				//
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
		}
		
		return data;
	}
	
	/** 
	 * getAMNPayrollAssistProcColumnNames()
	 * @return columnNames
	 */
	public Vector<String> getAMNPayrollAssistProcColumnNames()
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Event_Date"));
		columnNames.add(Msg.getElement(Env.getCtx(), "dayofweek"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Protected"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Shift_In1"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Shift_Out1"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Shift_In2"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Shift_Out2"));
		return columnNames;
	}

	// Método para obtener el primer item de AMN_Contract_ID
    public int getFirstActiveContractID(int roleID) {
        List<KeyNamePair> validContracts = getValidContracts(roleID);
        return !validContracts.isEmpty() ? validContracts.get(0).getKey() : 0;
    }

    protected List<KeyNamePair> getValidContracts(int AD_Role_ID) {
        List<KeyNamePair> list = new ArrayList<>();
        String sql = """
            SELECT c.AMN_Contract_ID, c.Name
            FROM AMN_Contract c
            WHERE c.IsActive='Y'
              AND c.AD_Client_ID=?
              AND c.AMN_Contract_ID IN (
                SELECT DISTINCT ra.AMN_Contract_ID
                FROM AMN_Role_Access ra
                WHERE ra.AD_Role_ID=?
                  AND ra.AMN_Process_ID IN (
                    SELECT p.AMN_Process_ID
                    FROM AMN_Process p
                    WHERE p.AMN_Process_Value='NN'
                  )
              )
            ORDER BY c.Value
            """;

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
            pstmt.setInt(2, AD_Role_ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error cargando contratos", e);
        }

        return list;
    }
    
	// Método para obtener el primer item de AMN_Location_ID
    public int getFirstActiveLocationID() {
        List<KeyNamePair> validLocations = getValidLocations();
        return !validLocations.isEmpty() ? validLocations.get(0).getKey() : 0;
    }

    protected List<KeyNamePair> getValidLocations() {
        List<KeyNamePair> list = new ArrayList<>();
        String sql = """
            SELECT l.AMN_Location_ID, l.Name
            FROM AMN_Location l
            WHERE l.IsActive='Y'
              AND l.AD_Client_ID=?
            ORDER BY l.value
            """;

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error cargando localidades", e);
        }

        return list;
    }
    
    // Método para obtener el primer item de AMN_Sector_ID
    public int getFirstActiveSectorID(int m_AMN_Location_ID) {
        List<KeyNamePair> validSectors = getValidSectors(m_AMN_Location_ID);
        return !validSectors.isEmpty() ? validSectors.get(0).getKey() : 0;
    }

    protected List<KeyNamePair> getValidSectors(int m_AMN_Location_ID) {
        List<KeyNamePair> list = new ArrayList<>();
        String sql = """
             SELECT s.AMN_Sector_ID, s.Name
            FROM AMN_Sector s
            LEFT JOIN AMN_Location l ON l.AMN_Location_ID = s.AMN_Location_ID
            WHERE s.IsActive='Y'
              AND s.AD_Client_ID=?
              AND (COALESCE(?, 0) = 0 OR s.AMN_Location_ID = ?)
            ORDER BY l.value, s.value
            """;

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
            pstmt.setInt(2, m_AMN_Location_ID);
            pstmt.setInt(3, m_AMN_Location_ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error cargando sectores", e);
        }

        return list;
    }
    
    // Método para obtener el primer item de Status
    public int getFirstActiveStatus(int m_AMN_Location_ID) {
        List<KeyNamePair> validStatus = getValidListReferenceTranslated("AMN_Employee", "Status");
        return !validStatus.isEmpty() ? validStatus.get(0).getKey() : 0;
    }
    

    List<KeyNamePair> getValidListReferenceTranslated(String tableName, String columnName) {
  
	    List<KeyNamePair> list = new ArrayList<>();

	    try {
	        int columnID = MColumn.getColumn_ID(tableName, columnName);
	        MColumn col = new MColumn(Env.getCtx(), columnID, null);

	        // Solo columnas tipo List
	        int referenceID = col.getAD_Reference_Value_ID();
	        if (referenceID <= 0) {
	            log.warning("La columna " + columnName + " no tiene referencia de lista válida.");
	            return list;
	        }

	        String sql = """
				SELECT l.Value,
				       COALESCE(t.Name, l.Name) AS TranslatedName
				FROM AD_Ref_List l
				LEFT JOIN AD_Ref_List_Trl t
				       ON t.AD_Ref_List_ID = l.AD_Ref_List_ID
				      AND t.AD_Language = ?
				WHERE l.AD_Reference_ID = ?
				  AND l.IsActive='Y'
				ORDER BY COALESCE(t.Name, l.Name)
	        """;

	        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
	            // Idioma actual del usuario
	            pstmt.setString(1, Env.getAD_Language(Env.getCtx()));
	            pstmt.setInt(2, referenceID);

	            try (ResultSet rs = pstmt.executeQuery()) {
	                int key = 1;
	                while (rs.next()) {
	                    String value = rs.getString(1);
	                    String name  = rs.getString(2);
	                    String combined = value + "-" + name;
	                    list.add(new KeyNamePair(key++, combined));
	                }
	            }
	        }

	    } catch (Exception e) {
	        log.log(Level.SEVERE, "Error cargando lista de referencia traducida para " + columnName, e);
	    }

	    return list;
	}

}
