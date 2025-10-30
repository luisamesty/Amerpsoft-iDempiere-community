package org.amerp.reports;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.amerp.reports.xlsx.generator.ReportGeneratorQuerys;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MImage;
import org.compiere.util.CLogger;


public class DataPopulator {

    private static final CLogger log = CLogger.getCLogger(DataPopulator.class);

    /**
     * Obtiene la lista jerárquica de cuentas contables desde la función
     * adempiere.amf_element_value_tree_basic
     *
     * @param adClientId ID del Grupo Empresarial (AD_Client_ID)
     * @param cAcctSchemaId ID del Esquema Contable (C_AcctSchema_ID)
     * @return Lista de objetos AccountElementBasic
     * Ejecuta Function SQL amf_element_value_tree_basic
     * Es necesario agregar los campos adicionales para mantener la compatibilidad con el 
     * Modelo de AccountElements_Tree_Pojo.java
     *     	private String cliname;
     *		private String clidescription;
     * 		private byte[] cli_logo;
     * 		private Integer ad_org_id;
     *		private String value_parent;
     */
    public static List<AccountElementBasic> getAccountElementBasicList(int adClientId, int cAcctSchemaId) {
        List<AccountElementBasic> accountElements = new ArrayList<>();

        // --- 1️⃣ Leer constantes globales antes del bucle
        String cliName = "";
        String cliDescription = "";
        byte[] cliLogo = null;
        MClient mclient = new MClient(Env.getCtx(),adClientId,null);
        if (mclient != null ) {
        	cliName = mclient.getName();
        	cliDescription = mclient.getDescription() != null ? mclient.getDescription() : mclient.getName();
        	 // --- 2️⃣ Obtener información del cliente (AD_ClientInfo)
            MClientInfo ci = MClientInfo.get(Env.getCtx(), adClientId);
            if (ci != null && ci.getLogoReport_ID() > 0) {
                // --- 3️⃣ Obtener el logo (AD_Image)
                MImage img = new MImage(Env.getCtx(), ci.getLogoReport_ID(), null);
                if (img != null && img.getBinaryData() != null) {
                    cliLogo = img.getBinaryData();
                }
            }	
        }
        // Query amf_element_value_tree_basic
        String sql = """
            SELECT 
                pathel,
                ancestry,
                level,
                node_id,
                parent_id,
                c_element_id,
                c_elementvalue_id,
                ad_client_id,
                isactive,
                codigo,
                name,
                description,
                length,
                accounttype,
                accountsign,
                isdoccontrolled,
                element_c_element_id,
                issummary,
                acctparent
            FROM adempiere.amf_element_value_tree_basic(?, ?)
        """;

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setInt(1, adClientId);
            pstmt.setInt(2, cAcctSchemaId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AccountElementBasic bean = new AccountElementBasic();

                    bean.setPathel(rs.getString("pathel"));

                    Array ancestryArray = rs.getArray("ancestry");
                    if (ancestryArray != null)
                        bean.setAncestry((BigDecimal[]) ancestryArray.getArray());

                    bean.setLevel(rs.getInt("level"));
                    bean.setNodeId(rs.getBigDecimal("node_id"));
                    bean.setParentId(rs.getBigDecimal("parent_id"));
                    bean.setCElementId(rs.getBigDecimal("c_element_id"));
                    bean.setCElementValueId(rs.getBigDecimal("c_elementvalue_id"));
                    bean.setAdClientId(rs.getBigDecimal("ad_client_id"));
                    bean.setIsActive(rs.getString("isactive"));
                    bean.setCodigo(rs.getString("codigo"));
                    bean.setName(rs.getString("name"));
                    bean.setDescription(rs.getString("description"));
                    bean.setLength(rs.getInt("length"));
                    bean.setAccountType(rs.getString("accounttype"));
                    bean.setAccountSign(rs.getString("accountsign"));
                    bean.setIsDocControlled(rs.getString("isdoccontrolled"));
                    bean.setElementCElementId(rs.getBigDecimal("element_c_element_id"));
                    bean.setIsSummary(rs.getString("issummary"));
                    Array acctParentArray = rs.getArray("acctparent");
                    if (acctParentArray != null) {
                    	String[] acctParent = (String[]) acctParentArray.getArray();
                        bean.setAcctParent((String[]) acctParentArray.getArray());
                        if (acctParent.length > 0) {
                            // El último valor es el del padre inmediato
                            String valueParent = acctParent[acctParent.length - 1];
                            bean.setValue_parent(valueParent);
                        }
                    }

                    // ---  Asignar los valores constantes
                    bean.setCliname(cliName);
                    bean.setClidescription(cliDescription);
                    bean.setCli_logo(cliLogo);

                    // Puedes también establecer ad_org_id o value_parent si son constantes:
                    bean.setAd_org_id(null);
    
                    
                    accountElements.add(bean);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountElements;
    }

    public static List<AccountElementExtended> getAccountElementExtendedList(int adClientId, int cAcctSchemaId) {
        List<AccountElementExtended> accountElements = new ArrayList<>();

        String sql = """
            SELECT 
                level,
                node_id,
                parent_id,
                c_element_id,
                c_elementvalue_id,
                ad_client_id,
                isactive,
                codigo,
                name,
                description,
                length,
                accounttype,
                accountsign,
                isdoccontrolled,
                issummary,
                acctparent,
                pathel,
                ancestry,
                codigo0, name0, description0, issummary0,
                codigo1, name1, description1, issummary1,
                codigo2, name2, description2, issummary2,
                codigo3, name3, description3, issummary3,
                codigo4, name4, description4, issummary4,
                codigo5, name5, description5, issummary5,
                codigo6, name6, description6, issummary6,
                codigo7, name7, description7, issummary7,
                codigo8, name8, description8, issummary8,
                codigo9, name9, description9, issummary9
            FROM adempiere.amf_element_value_tree_extended(?, ?)
        """;

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setInt(1, adClientId);
            pstmt.setInt(2, cAcctSchemaId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AccountElementExtended bean = new AccountElementExtended();

                    bean.setLevel(rs.getInt("level"));
                    bean.setNodeId(rs.getInt("node_id"));
                    bean.setParentId(rs.getInt("parent_id"));
                    bean.setcElementId(rs.getInt("c_element_id"));
                    bean.setcElementValueId(rs.getInt("c_elementvalue_id"));
                    bean.setAdClientId(rs.getInt("ad_client_id"));
                    bean.setIsActive(rs.getString("isactive"));
                    bean.setCodigo(rs.getString("codigo"));
                    bean.setName(rs.getString("name"));
                    bean.setDescription(rs.getString("description"));
                    bean.setLength(rs.getInt("length"));
                    bean.setAccountType(rs.getString("accounttype"));
                    bean.setAccountSign(rs.getString("accountsign"));
                    bean.setIsDocControlled(rs.getString("isdoccontrolled"));
                    bean.setIsSummary(rs.getString("issummary"));

                    Array acctParentArray = rs.getArray("acctparent");
                    if (acctParentArray != null)
                        bean.setAcctParent((String[]) acctParentArray.getArray());

                    bean.setPathEl(rs.getString("pathel"));

                    Array ancestryArray = rs.getArray("ancestry");
                    if (ancestryArray != null)
                        bean.setAncestry((Integer[]) ancestryArray.getArray());

                    // Campos jerárquicos niveles 0-9
                    bean.setCodigo0(rs.getString("codigo0"));
                    bean.setName0(rs.getString("name0"));
                    bean.setDescription0(rs.getString("description0"));
                    bean.setIsSummary0(rs.getString("issummary0"));

                    bean.setCodigo1(rs.getString("codigo1"));
                    bean.setName1(rs.getString("name1"));
                    bean.setDescription1(rs.getString("description1"));
                    bean.setIsSummary1(rs.getString("issummary1"));

                    bean.setCodigo2(rs.getString("codigo2"));
                    bean.setName2(rs.getString("name2"));
                    bean.setDescription2(rs.getString("description2"));
                    bean.setIsSummary2(rs.getString("issummary2"));

                    bean.setCodigo3(rs.getString("codigo3"));
                    bean.setName3(rs.getString("name3"));
                    bean.setDescription3(rs.getString("description3"));
                    bean.setIsSummary3(rs.getString("issummary3"));

                    bean.setCodigo4(rs.getString("codigo4"));
                    bean.setName4(rs.getString("name4"));
                    bean.setDescription4(rs.getString("description4"));
                    bean.setIsSummary4(rs.getString("issummary4"));

                    bean.setCodigo5(rs.getString("codigo5"));
                    bean.setName5(rs.getString("name5"));
                    bean.setDescription5(rs.getString("description5"));
                    bean.setIsSummary5(rs.getString("issummary5"));

                    bean.setCodigo6(rs.getString("codigo6"));
                    bean.setName6(rs.getString("name6"));
                    bean.setDescription6(rs.getString("description6"));
                    bean.setIsSummary6(rs.getString("issummary6"));

                    bean.setCodigo7(rs.getString("codigo7"));
                    bean.setName7(rs.getString("name7"));
                    bean.setDescription7(rs.getString("description7"));
                    bean.setIsSummary7(rs.getString("issummary7"));

                    bean.setCodigo8(rs.getString("codigo8"));
                    bean.setName8(rs.getString("name8"));
                    bean.setDescription8(rs.getString("description8"));
                    bean.setIsSummary8(rs.getString("issummary8"));

                    bean.setCodigo9(rs.getString("codigo9"));
                    bean.setName9(rs.getString("name9"));
                    bean.setDescription9(rs.getString("description9"));
                    bean.setIsSummary9(rs.getString("issummary9"));

                    accountElements.add(bean);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountElements;
    }

    /**
     * Obtiene una lista de objetos AccountElement llenados con datos de la base de datos
     * de iDempiere usando PreparedStatement.
     * @param ad_client_id El ID del cliente actual.
     * @param ad_org_id El ID de la organización actual.
     * @param p_AMNDateAssist Parámetro de ejemplo para la consulta (Timestamp).
     * @return Una lista de AccountElement.
     */
    public static List<AccountElement> getAccountElementsFromiDempiereDB(int ad_client_id, int c_acctschema_id) {
        List<AccountElement> accountElements = new ArrayList<>();

        // SQL completa. Hemos reemplazado $P{param} con ?.
        // Asegúrate de que los alias de las columnas coincidan con los nombres de tus campos en el jrxml y tu POJO.
        String sqlQuery = 
            "WITH ElementValueTree AS (" +
            "    WITH RECURSIVE AccountTreeBase AS (" +
            "        SELECT " +
            "            tn.AD_Tree_ID," +
            "            tn.Node_ID," +
            "            tn.Parent_ID," +
            "            tn.SeqNo," +
            "            0 AS level," +
            "            ev.Value::VARCHAR(2000) AS path," +
            "            ev.Value::VARCHAR(40) AS AccountValue," +
            "            ev.Name::VARCHAR(60) AS AccountName," +
            "            ev.AccountType::VARCHAR(40) AS AccountType," +
            "            ev.IsSummary," +
            "            t.Name::VARCHAR(60) AS TreeName," +
            "            ARRAY [tn.Node_ID::text] AS ancestry," +
            "            ARRAY [ev.value::text] AS acctparent" +
            "        FROM AD_TreeNode tn" +
            "        JOIN C_ElementValue ev ON tn.Node_ID = ev.C_ElementValue_ID" +
            "        JOIN AD_Tree t ON tn.AD_Tree_ID = t.AD_Tree_ID" +
            "        WHERE (tn.Parent_ID IS NULL OR tn.Parent_ID = 0)" +
            "            AND tn.AD_Client_ID = ? " + // $P{AD_Client_ID}
            "            AND tn.IsActive = 'Y'" +
            "            AND ev.IsActive = 'Y'" +
            "            AND tn.AD_tree_ID = (" +
            "                SELECT tree.AD_Tree_ID" +
            "                FROM AD_Client adcli" +
            "                LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID" +
            "                LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID " +
            "                LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID" +
            "                LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID = accel.AD_Tree_ID" +
            "                WHERE accee.ElementType = 'AC' " +
            "                AND accsh.C_AcctSchema_ID = ? " + // $P{C_AcctSchema_ID}
            "                AND adcli.AD_client_ID = ? " + // $P{AD_Client_ID}
            "            )" +
            "        UNION ALL" +
            "        SELECT " +
            "            child.AD_Tree_ID," +
            "            child.Node_ID," +
            "            child.Parent_ID," +
            "            child.SeqNo," +
            "            parent.level + 1," +
            "            (parent.path || '->' || child_ev.Value)::VARCHAR(2000) AS path," +
            "            child_ev.Value::VARCHAR(40) AS AccountValue," +
            "            child_ev.Name::VARCHAR(60) AS AccountName," +
            "            child_ev.AccountType::VARCHAR(40) AS AccountType," +
            "            child_ev.IsSummary," +
            "            parent.TreeName," +
            "            parent.ancestry || ARRAY[child.Node_ID::text] AS ancestry," +
            "            parent.acctparent || ARRAY[child_ev.value::text] AS acctparent" +
            "        FROM AD_TreeNode child" +
            "        JOIN C_ElementValue child_ev ON child.Node_ID = child_ev.C_ElementValue_ID" +
            "        JOIN AccountTreeBase parent ON child.Parent_ID = parent.Node_ID" +
            "        WHERE child.IsActive = 'Y'" +
            "            AND child_ev.IsActive = 'Y'" +
            "    )" +
            "    SELECT * FROM AccountTreeBase" +
            ")" +
            "SELECT DISTINCT ON (ELV.Value)" +
            "    PAR.Level," +
            "    PAR.Node_ID, " +
            "    PAR.Parent_ID ," +
            "    CLI.value as clivalue," +
            "    CLI.name as cliname," +
            "    coalesce(CLI.description,CLI.name,'') as clidescription," +
            "    IMG.binarydata as cli_logo," +
            "    ELE.c_element_id," +
            "    ELV.c_elementvalue_id," +
            "    ELV.ad_client_id," +
            "    ELV.ad_org_id," +
            "    ELV.isactive," +
            "    ELV.\"value\"," + // Nota: "value" está entre comillas dobles para PostgreSQL por ser palabra reservada
            "    COALESCE(ELV.name,'') as name," +
            "    LPAD('', char_length(ELV.value),' ') || COALESCE(ELV.description,ELV.name) as description," +
            "    char_length(ELV.value) as length," +
            "    ELV.accounttype," +
            "    ELV.accountsign," +
            "    ELV.isdoccontrolled," +
            "    ELV.c_element_id AS COLUMN_20," + // Alias explícito para COLUMN_20
            "    ELV.issummary," +
            "    COALESCE(ELVP.value,'') as value_parent," +
            "    COALESCE(PAR.acctparent[2],'') as Value1," +
            "    COALESCE(PAR.acctparent[3],'') as Value2," +
            "    COALESCE(PAR.acctparent[4],'') as Value3," +
            "    COALESCE(PAR.acctparent[5],'') as Value4," +
            "    COALESCE(PAR.acctparent[6],'') as Value5," +
            "    COALESCE(PAR.acctparent[7],'') as Value6," +
            "    COALESCE(PAR.acctparent[8],'') as Value7," +
            "    COALESCE(PAR.acctparent[9],'') as Value8," +
            "    COALESCE(PAR.acctparent[10],'') as Value9 " +
            "FROM ElementValueTree PAR " +
            "INNER JOIN ( " +
            "    SELECT adcli.AD_Client_ID, accsh.C_AcctSchema_ID, accee.C_Element_ID, accel.name as element_name, tree.AD_Tree_ID, tree.name as tree_name " +
            "    FROM AD_Client adcli " +
            "    LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID " +
            "    LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID " +
            "    LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID " +
            "    LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID " +
            "    WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = ? " + // $P{C_AcctSchema_ID}
            ") as ELE ON ELE.AD_Tree_ID = PAR.AD_Tree_ID " +
            "LEFT JOIN ad_client AS CLI ON (CLI.ad_client_id = ELE.ad_client_id) " +
            "LEFT JOIN ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id) " +
            "LEFT JOIN ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id) " +
            "LEFT JOIN C_elementValue ELV ON ELV.C_ElementValue_ID = PAR.NODE_ID " +
            "LEFT JOIN C_elementValue ELVP ON ELVP.C_ElementValue_ID = PAR.Parent_ID " +
            "ORDER BY ELV.Value, PAR.ANCESTRY";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Prepara la sentencia SQL.
            pstmt = DB.prepareStatement(sqlQuery, null);

            // Establece los parámetros en el orden en que aparecen los '?' en la query
            // Hay 5 placeholders en total para 2 parámetros:
            // 1. tn.AD_Client_ID = ?
            // 2. accsh.C_AcctSchema_ID = ? (dentro de la subconsulta del árbol)
            // 3. adcli.AD_client_ID = ? (dentro de la subconsulta del árbol)
            // 4. accsh.C_AcctSchema_ID = ? (dentro de la subconsulta ELE)
            //
            // Dado que los parámetros se repiten, se asignan en el orden de aparición.
            // Los valores se pasan como enteros (int).

            int paramIndex = 1;
            pstmt.setInt(paramIndex++, ad_client_id);
            pstmt.setInt(paramIndex++, c_acctschema_id);
            pstmt.setInt(paramIndex++, ad_client_id);
            pstmt.setInt(paramIndex++, c_acctschema_id); // El último parámetro para la subconsulta 'ELE'

            // Ejecuta la consulta
            rs = pstmt.executeQuery();

            // Itera sobre los resultados y llena el POJO
            while (rs.next()) {
                AccountElement element = new AccountElement();
                // Mapeo de ResultSet a POJO. Los nombres de columna deben coincidir con los de tu SQL.
                // Es preferible usar los métodos específicos de ResultSet (getString, getBigDecimal, etc.)
                // para mayor claridad y eficiencia, manejando los nulos con if/else si es necesario,
                // o usando los getters del wrapper si esperas nulos y no quieres NPE en primitivos.

                element.setLevel(rs.getObject("level", Integer.class)); // Mejor para nulos
                element.setNode_id(rs.getBigDecimal("node_id"));
                element.setParent_id(rs.getBigDecimal("parent_id"));
                element.setClivalue(rs.getString("clivalue"));
                element.setCliname(rs.getString("cliname"));
                element.setClidescription(rs.getString("clidescription"));
                element.setCli_logo(rs.getBytes("cli_logo"));
                element.setC_element_id(rs.getBigDecimal("c_element_id"));
                element.setC_elementvalue_id(rs.getBigDecimal("c_elementvalue_id"));
                element.setAd_client_id(rs.getBigDecimal("ad_client_id"));
                element.setAd_org_id(rs.getBigDecimal("ad_org_id"));
                element.setIsactive(rs.getString("isactive"));
                element.setValue(rs.getString("value"));
                element.setName(rs.getString("name"));
                element.setDescription(rs.getString("description"));
                element.setLength(rs.getObject("length", Integer.class));
                element.setAccounttype(rs.getString("accounttype"));
                element.setAccountsign(rs.getString("accountsign"));
                element.setIsdoccontrolled(rs.getString("isdoccontrolled"));
                element.setCOLUMN_20(rs.getBigDecimal("COLUMN_20"));
                element.setIssummary(rs.getString("issummary"));
                element.setValue_parent(rs.getString("value_parent"));
                element.setValue1(rs.getString("value1"));
                element.setValue2(rs.getString("value2"));
                element.setValue3(rs.getString("value3"));
                element.setValue4(rs.getString("value4"));
                element.setValue5(rs.getString("value5"));
                element.setValue6(rs.getString("value6"));
                element.setValue7(rs.getString("value7"));
                element.setValue8(rs.getString("value8"));
                element.setValue9(rs.getString("value9"));

                accountElements.add(element);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al obtener AccountElements con PreparedStatement desde iDempiere DB", e);
        } finally {
            // Es CRÍTICO cerrar PreparedStatement y ResultSet en el bloque finally
            DB.close(rs, pstmt); // DB.close() maneja nulls automáticamente
        }
        return accountElements;
    }
    

    /**
     * 
     */

    public static List<OrgTree> getOrgTreeList(int adClientId, int adOrgId, int adOrgParentId) {
        List<OrgTree> list = new ArrayList<>();

        String sql = """
            SELECT * 
            FROM adempiere.amf_org_tree(?, ?, ?)
        """;

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setInt(1, adClientId);
            pstmt.setInt(2, adOrgId);
            pstmt.setInt(3, adOrgParentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrgTree bean = new OrgTree();
                    bean.setAdClientId(rs.getBigDecimal("org_ad_client_id"));
                    bean.setAdOrgId(rs.getBigDecimal("org_ad_org_id"));
                    bean.setAdOrgParentId(rs.getBigDecimal("org_ad_orgparent_id"));
                    bean.setIsSummary(rs.getString("issummary"));
                    bean.setOrgValue(rs.getString("org_value"));
                    bean.setOrgDescription(rs.getString("org_description"));
                    bean.setOrgName(rs.getString("org_name"));
                    bean.setAllOrgs(rs.getString("all_orgs"));
                    bean.setOrgTaxId(rs.getString("org_taxid"));
                    bean.setOrgLogo(rs.getBytes("org_logo"));
                    list.add(bean);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     *  getTrialBalanceData
	 * Obtiene los datos del balance de comprobación con estructura jerárquica.
     * @param AD_Client_ID
     * @param C_AcctSchema_ID
     * @param AD_Org_ID
     * @param AD_OrgParent_ID
     * @param C_Period_ID
     * @param PostingType
     * @param C_ElementValue_ID
     * @param DateFrom
     * @param DateTo
     * @param isShowZERO
     * @param trxName
     * @return
     */
	public static List<TrialBalanceLine> getTrialBalanceData(
			int AD_Client_ID, int C_AcctSchema_ID, int AD_Org_ID, int AD_OrgParent_ID, int C_Period_ID, 
			String PostingType, Integer C_ElementValue_ID, Timestamp DateFrom, Timestamp DateTo, 
			String isShowZERO, String trxName) {
	        
        List<TrialBalanceLine> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = DB.prepareStatement(ReportGeneratorQuerys.SQL_TRIAL_BALANCE_DATA, trxName);
            int index = 1;
            
         // ==========================================================
            // ASIGNACIÓN DE PARÁMETROS (24 Parámetros)
            // ==========================================================

            // 1. Accounts CTE (2 Parámetros)
            pstmt.setInt(index++, AD_Client_ID);     // 1
            pstmt.setInt(index++, C_AcctSchema_ID);  // 2
            
            // 2. Balances CTE
            // 2.1 amf_element_value_tree_extended (2 Parámetros)
            pstmt.setInt(index++, AD_Client_ID);     // 3
            pstmt.setInt(index++, C_AcctSchema_ID);  // 4
            
            // 2.2 amf_org_tree (3 Parámetros)
            pstmt.setInt(index++, AD_Client_ID);     // 5
            pstmt.setInt(index++, AD_Org_ID);        // 6
            pstmt.setInt(index++, AD_OrgParent_ID);  // 7
            
            // 2.3 amf_balance_account_org_flex_orgparent (9 Parámetros)
            pstmt.setInt(index++, AD_Client_ID);        // 8
            pstmt.setInt(index++, AD_OrgParent_ID);     // 9
            pstmt.setInt(index++, AD_Org_ID);           // 10
            pstmt.setInt(index++, C_AcctSchema_ID);     // 11
            pstmt.setInt(index++, C_Period_ID);         // 12
            pstmt.setString(index++, PostingType);      // 13
            
            // C_ElementValue_ID (Puede ser NULL)
            if (C_ElementValue_ID == null || C_ElementValue_ID == 0) {
                 pstmt.setObject(index++, null, java.sql.Types.INTEGER); // 14
            } else {
                 pstmt.setInt(index++, C_ElementValue_ID);               // 14
            }
            pstmt.setTimestamp(index++, DateFrom);      // 15
            pstmt.setTimestamp(index++, DateTo);        // 16

            // 2.4 Filtro isShowZERO en Balances CTE (2 Parámetros)
            pstmt.setString(index++, isShowZERO); // 17: isShowZERO = 'Y'
            pstmt.setString(index++, isShowZERO); // 18: isShowZERO = 'N'
            
            // 3. BalancesDetailAll CTE (4 Parámetros de Filtro)
            // Filtro '50' (Detalle por Org)
            pstmt.setString(index++, isShowZERO); // 19: isShowZERO = 'Y'
            pstmt.setString(index++, isShowZERO); // 20: isShowZERO = 'N'
            // Filtro '60' (Total Cuenta)
            pstmt.setString(index++, isShowZERO); // 21: isShowZERO = 'Y'
            pstmt.setString(index++, isShowZERO); // 22: isShowZERO = 'N'

            // 4. SELECT FINAL (2 Parámetros de Filtro para Resumen 'R')
            pstmt.setString(index++, isShowZERO); // 23: isShowZERO = 'Y'
            pstmt.setString(index++, isShowZERO); // 24: isShowZERO = 'N'
            
            // ==========================================================
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                TrialBalanceLine line = new TrialBalanceLine();
                
                // Mapeo de columnas
                line.setCodigo(rs.getString("codigo"));
                line.setNombre(rs.getString("nombre"));
                line.setOrgValue(rs.getString("org_value"));
                
                line.setOpenBalance(rs.getBigDecimal("openbalance"));
                line.setAmtAcctDr(rs.getBigDecimal("debitos"));
                line.setAmtAcctCr(rs.getBigDecimal("creditos"));
                line.setBalancePeriodo(rs.getBigDecimal("balance_periodo"));
                line.setCloseBalance(rs.getBigDecimal("closebalance"));
                
                line.setTipoRegistro(rs.getString("tipo_registro")); // R, 60, 50
                line.setLevel(rs.getInt("level"));
                // pathel_order se usa solo para el ORDER BY
                
                list.add(line);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Fallo al obtener los datos del Balance de Comprobación.", e);
        } finally {
            DB.close(rs, pstmt);
        }

        return list;
    }
}