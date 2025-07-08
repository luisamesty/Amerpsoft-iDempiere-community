package org.amerp.reports.AccountElements_Tree;

import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.CLogger;

public class DataPopulator {

    private static final CLogger log = CLogger.getCLogger(DataPopulator.class);

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
}