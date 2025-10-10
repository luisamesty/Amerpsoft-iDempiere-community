
# Reports with JRDatasource

Pack to be included: **AMERPSOFT Financial Test Reports.zip**

## How is it done in practice?

During development and testing in Jaspersoft Studio:
✅ The **Query is embedded** in the JRXML.
✅ This allows you to preview with your test data and design the entire layout comfortably.

When your design is ready and validated:
✅ You remove or empty the **Query** in the JRXML.
✅ You only leave the defined **\<field\>** elements (which are the ones your JRDataSource will supply).
✅ Now Jasper doesn't know what query to run, and it expects your Java process to pass the data.

In production, your Java class:

1.  Generates the data list (or temporary table).
2.  Calls `fillReport()` passing the **JRDataSource**.
3.  Jasper populates the fields with what your `getFieldValue()` returns.

This is the most normal and recommended approach.
No problem doing it this way:
✅ Development with embedded query
✅ Production with JRDataSource

💡 **A convenient trick**
If you want to keep the embedded query for compatibility but have Jasper ignore it in production, you can:

-----

## Option 1 — Leave the Query and Don't Pass the Connection

If you call:

```java
fillReport(jasperReport, parameters, dataSource);
```

and **you don't pass a Connection**, Jasper ignores the query and uses your **JRDataSource**.
Thus:

  * When you open it in Jaspersoft Studio, the query runs.
  * When you run your Java process, the JRDataSource is used.

This is a super practical option.

-----

## Option 2 — Keep the Query in a Comment

Another possibility is to leave the query commented out, for example:

```xml
```

But beware, in this case, you won't have an embedded query in Studio either.

-----

## Option 3 — Keep the Query but Ignore It

If your JRXML has the query:

```xml
<queryString>
    <![CDATA[
        SELECT * FROM c_elementvalue
    ]]>
</queryString>
```

But in your Java code, you do:

```java
fillReport(jasperReport, parameters, dataSource);
```

Jasper will **never** execute that query because it only executes it if you pass a **Connection**.
That's why many companies leave the embedded query "just in case," but their process always feeds the report with a JRDataSource.

-----

## 📂 How does Jasper define which fields to use then?

The **\<field\>** elements are still defined in your JRXML:

```xml
<field name="ElementName" class="java.lang.String"/>
<field name="Account_ID" class="java.lang.Integer"/>
```

and in your JRDataSource, **you are responsible** for returning the correct value for each field.

-----

## ✅ Practical Recommendation

### 🔹 During Development and Testing:

  * Embedded Query.
  * Connection in Jaspersoft Studio.
  * Layout and data testing.

### 🔹 When moving to Production:

  * **If you want simplicity:**
      * Leave the embedded query.
      * Do **not** pass the Connection.
      * Jasper will use the **JRDataSource**.
  * **Or:**
      * Delete the query.
      * Only leave the fields.
      * Jasper will always use the **JRDataSource**.

This way, you get the best of both worlds: convenience in Studio and control in production.

Let me know if you'd like examples on how to leave the query and control it, or JRDataSource templates. 🚀

-----

# Tools

## POJO Report Example

### POJO Class.

```java
package org.amerp.reports.AccountElements_Tree;

import java.math.BigDecimal; // Import BigDecimal if you don't have it already

public class AccountElement {

    private Integer level;
    private BigDecimal node_id;
    private BigDecimal parent_id;
    private String clivalue;
    private String cliname;
    private String clidescription;
    private byte[] cli_logo; // For byte[] type fields like images
    private BigDecimal c_element_id;
    private BigDecimal c_elementvalue_id;
    private BigDecimal ad_client_id;
    private BigDecimal ad_org_id;
    private String isactive;
    private String value;
    private String name;
    private String description;
    private Integer length;
    private String accounttype;
    private String accountsign;
    private String isdoccontrolled;
    private BigDecimal COLUMN_20; // Ensure this field is correctly mapped if it's not a duplicate of c_element_id
    private String issummary;
    private String value_parent;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
    private String value6;
    private String value7;
    private String value8;
    private String value9;

    // Constructor (optional, but useful for initializing objects)
    public AccountElement(Integer level, BigDecimal node_id, BigDecimal parent_id, String clivalue, String cliname,
                          String clidescription, byte[] cli_logo, BigDecimal c_element_id, BigDecimal c_elementvalue_id,
                          BigDecimal ad_client_id, BigDecimal ad_org_id, String isactive, String value, String name,
                          String description, Integer length, String accounttype, String accountsign,
                          String isdoccontrolled, BigDecimal COLUMN_20, String issummary, String value_parent,
                          String value1, String value2, String value3, String value4, String value5,
                          String value6, String value7, String value8, String value9) {
        this.level = level;
        this.node_id = node_id;
        this.parent_id = parent_id;
        this.clivalue = clivalue;
        this.cliname = cliname;
        this.clidescription = clidescription;
        this.cli_logo = cli_logo;
        this.c_element_id = c_element_id;
        this.c_elementvalue_id = c_elementvalue_id;
        this.ad_client_id = ad_client_id;
        this.ad_org_id = ad_org_id;
        this.isactive = isactive;
        this.value = value;
        this.name = name;
        this.description = description;
        this.length = length;
        this.accounttype = accounttype;
        this.accountsign = accountsign;
        this.isdoccontrolled = isdoccontrolled;
        this.COLUMN_20 = COLUMN_20;
        this.issummary = issummary;
        this.value_parent = value_parent;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.value9 = value9;
    }

    // Empty constructor (needed for some serialization/deserialization libraries)
    public AccountElement() {
    }

    // --- Getters and Setters for each field ---

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public BigDecimal getNode_id() { return node_id; }
    public void setNode_id(BigDecimal node_id) { this.node_id = node_id; }

    public BigDecimal getParent_id() { return parent_id; }
    public void setParent_id(BigDecimal parent_id) { this.parent_id = parent_id; }

    public String getClivalue() { return clivalue; }
    public void setClivalue(String clivalue) { this.clivalue = clivalue; }

    public String getCliname() { return cliname; }
    public void setCliname(String cliname) { this.cliname = cliname; }

    public String getClidescription() { return clidescription; }
    public void setClidescription(String clidescription) { this.clidescription = clidescription; }

    public byte[] getCli_logo() { return cli_logo; }
    public void setCli_logo(byte[] cli_logo) { this.cli_logo = cli_logo; }

    public BigDecimal getC_element_id() { return c_element_id; }
    public void setC_element_id(BigDecimal c_element_id) { this.c_element_id = c_element_id; }

    public BigDecimal getC_elementvalue_id() { return c_elementvalue_id; }
    public void setC_elementvalue_id(BigDecimal c_elementvalue_id) { this.c_elementvalue_id = c_elementvalue_id; }

    public BigDecimal getAd_client_id() { return ad_client_id; }
    public void setAd_client_id(BigDecimal ad_client_id) { this.ad_client_id = ad_client_id; }

    public BigDecimal getAd_org_id() { return ad_org_id; }
    public void setAd_org_id(BigDecimal ad_org_id) { this.ad_org_id = ad_org_id; }

    public String getIsactive() { return isactive; }
    public void setIsactive(String isactive) { this.isactive = isactive; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }

    public String getAccounttype() { return accounttype; }
    public void setAccounttype(String accounttype) { this.accounttype = accounttype; }

    public String getAccountsign() { return accountsign; }
    public void setAccountsign(String accountsign) { this.accountsign = accountsign; }

    public String getIsdoccontrolled() { return isdoccontrolled; }
    public void setIsdoccontrolled(String isdoccontrolled) { this.isdoccontrolled = isdoccontrolled; }

    public BigDecimal getCOLUMN_20() { return COLUMN_20; }
    public void setCOLUMN_20(BigDecimal COLUMN_20) { this.COLUMN_20 = COLUMN_20; }

    public String getIssummary() { return issummary; }
    public void setIssummary(String issummary) { this.issummary = issummary; }

    public String getValue_parent() { return value_parent; }
    public void setValue_parent(String value_parent) { this.value_parent = value_parent; }

    public String getValue1() { return value1; }
    public void setValue1(String value1) { this.value1 = value1; }

    public String getValue2() { return value2; }
    public void setValue2(String value2) { this.value2 = value2; }

    public String getValue3() { return value3; }
    public void setValue3(String value3) { this.value3 = value3; }

    public String getValue4() { return value4; }
    public void setValue4(String value4) { this.value4 = value4; }

    public String getValue5() { return value5; }
    public void setValue5(String value5) { this.value5 = value5; }

    public String getValue6() { return value6; }
    public void setValue6(String value6) { this.value6 = value6; }

    public String getValue7() { return value7; }
    public void setValue7(String value7) { this.value7 = value7; }

    public String getValue8() { return value8; }
    public void setValue8(String value8) { this.value8 = value8; }

    public String getValue9() { return value9; }
    public void setValue9(String value9) { this.value9 = value9; }
}
```

### Data Populator

Example of a Data Populator, to fill the POJO with a Query similar to the one originally contained in the jrxml.

```java
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
     * Gets a list of AccountElement objects populated with data from the 
     * iDempiere database using PreparedStatement.
     * @param ad_client_id The current client ID.
     * @param ad_org_id The current organization ID.
     * @param p_AMNDateAssist Example parameter for the query (Timestamp).
     * @return A list of AccountElement.
     */
    public static List<AccountElement> getAccountElementsFromiDempiereDB(int ad_client_id, int c_acctschema_id) {
        List<AccountElement> accountElements = new ArrayList<>();

        // Complete SQL. We have replaced $P{param} with ?.
        // Ensure that the column aliases match the names of your fields in the jrxml and your POJO.
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
            "    ELV.\"value\"," + // Note: "value" is enclosed in double quotes for PostgreSQL as it's a reserved word
            "    COALESCE(ELV.name,'') as name," +
            "    LPAD('', char_length(ELV.value),' ') || COALESCE(ELV.description,ELV.name) as description," +
            "    char_length(ELV.value) as length," +
            "    ELV.accounttype," +
            "    ELV.accountsign," +
            "    ELV.isdoccontrolled," +
            "    ELV.c_element_id AS COLUMN_20," + // Explicit alias for COLUMN_20
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
            // Prepare the SQL statement.
            pstmt = DB.prepareStatement(sqlQuery, null);

            // Set parameters in the order the '?' appear in the query
            // There are 5 placeholders in total for 2 parameters:
            // 1. tn.AD_Client_ID = ?
            // 2. accsh.C_AcctSchema_ID = ? (inside the tree subquery)
            // 3. adcli.AD_client_ID = ? (inside the tree subquery)
            // 4. accsh.C_AcctSchema_ID = ? (inside the ELE subquery)
            //
            // Since parameters are repeated, they are assigned in order of appearance.
            // Values are passed as integers (int).

            int paramIndex = 1;
            pstmt.setInt(paramIndex++, ad_client_id);
            pstmt.setInt(paramIndex++, c_acctschema_id);
            pstmt.setInt(paramIndex++, ad_client_id);
            pstmt.setInt(paramIndex++, c_acctschema_id); // The last parameter for the 'ELE' subquery

            // Execute the query
            rs = pstmt.executeQuery();

            // Iterate through the results and populate the POJO
            while (rs.next()) {
                AccountElement element = new AccountElement();
                // Mapping from ResultSet to POJO. Column names must match those in your SQL.
                // It's preferable to use specific ResultSet methods (getString, getBigDecimal, etc.)
                // for clarity and efficiency, handling nulls with if/else if necessary,
                // or using the wrapper getters if you expect nulls and don't want NPE on primitives.

                element.setLevel(rs.getObject("level", Integer.class)); // Better for nulls
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
            log.log(Level.SEVERE, "Error retrieving AccountElements with PreparedStatement from iDempiere DB", e);
        } finally {
            // It is CRITICAL to close PreparedStatement and ResultSet in the finally block
            DB.close(rs, pstmt); // DB.close() handles nulls automatically
        }
        return accountElements;
    }
}
```

### Process

Process to be executed with the Application Dictionary.

```java
package org.adempiere.report.yourpackage;

import java.io.InputStream; // Import to load the .jasper
import java.math.BigDecimal; // Possibly needed if the POJO has BigDecimals
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.report.treedata.AccountElement; // Ensure this path is correct
import org.adempiere.report.treedata.DataPopulator;   // Ensure this path is correct
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.report.ReportStarter; // To start JasperReports in iDempiere
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import net.sf.jasperreports.engine.JRException; // Import if not present
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader; // To load the .jasper

public class MyAccountTreeReportProcess extends SvrProcess {

    private static final CLogger log = CLogger.getCLogger(MyAccountTreeReportProcess.class);

    private int p_AD_Client_ID = 0;
    private int p_C_AcctSchema_ID = 0;
    // Define the path to your .jasper file
    // Ideally, it should be within the resources of your OSGi plugin
    // For example: if your .jasper is in `src/main/resources/reports/AccountElements_Tree.jasper`
    private final String JASPER_REPORT_PATH = "/reports/AccountElements_Tree.jasper";

    @Override
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (ProcessInfoParameter pp : para) {
            String name = pp.getParameterName();
            if (name.equals("AD_Client_ID")) {
                p_AD_Client_ID = pp.getParameterAsInt();
            } else if (name.equals("C_AcctSchema_ID")) {
                p_C_AcctSchema_ID = pp.getParameterAsInt();
            }
        }
        // If not passed as a parameter, get it from the current iDempiere context
        if (p_AD_Client_ID == 0) {
            p_AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
        }
        // For C_AcctSchema_ID, if not passed as a parameter, it might require logic to get the default
        // or be a mandatory parameter in the process window. Here we leave it as 0 if not passed.
    }

    @Override
    protected String doIt() throws Exception {
        log.info("Starting Account Tree Report generation for Client: " + p_AD_Client_ID + ", Accounting Schema: " + p_C_AcctSchema_ID);

        // --- STEP 1: Get the data into the POJO ---
        List<AccountElement> reportData = DataPopulator.getAccountElementsFromiDempiereDB(p_AD_Client_ID, p_C_AcctSchema_ID);

        if (reportData.isEmpty()) {
            String msg = "No data found for the report with Client: " + p_AD_Client_ID + " and Accounting Schema: " + p_C_AcctSchema_ID;
            log.log(Level.INFO, msg);
            return msg; // Message to be shown to the user in iDempiere
        }
        log.info("Data retrieved: " + reportData.size() + " elements.");

        // --- STEP 2: Load the .jasper file (the compiled report design) ---
        JasperReport jasperReport = null;
        try (InputStream reportStream = getClass().getResourceAsStream(JASPER_REPORT_PATH)) {
            if (reportStream == null) {
                String errorMsg = "Could not find the .jasper file: " + JASPER_REPORT_PATH + ". Make sure it is in your plugin's classpath.";
                log.log(Level.SEVERE, errorMsg);
                throw new JRException(errorMsg);
            }
            jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
            log.info(".jasper file loaded successfully.");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading the .jasper file from: " + JASPER_REPORT_PATH, e);
            throw e; // Rethrow the exception for iDempiere to handle
        }

        // --- STEP 3: Create the JRBeanCollectionDataSource from your list of POJOs ---
        // THIS IS THE CRITICAL POINT TO ASSIGN YOUR POJO TO THE REPORT
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        log.info("JRBeanCollectionDataSource created with " + dataSource.getRecordCount() + " records.");


        // --- STEP 4: Prepare the report parameters (if any) ---
        // These parameters are those defined in the .jrxml and are not part of the detail fields.
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "Account Element Tree Report");
        parameters.put("AD_CLIENT_ID", p_AD_Client_ID);
        parameters.put("C_ACCTSCHEMA_ID", p_C_AcctSchema_ID);
        // You can add more parameters, such as the company logo (if it doesn't come from the POJO)
        // or user information, etc.
        log.info("Report parameters prepared.");

        // --- STEP 5: Fill the report ---
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            log.info("JasperReports report filled successfully.");
        } catch (JRException e) {
            log.log(Level.SEVERE, "Error filling the JasperReports report.", e);
            throw e; // Rethrow the exception
        }

        // --- STEP 6: Start the report viewing/exporting in iDempiere ---
        // The third parameter is the suggested filename when saving.
        try {
            ReportStarter.startViewer(Env.getCtx(), jasperPrint, "AccountElements_Tree.pdf");
            log.info("Report viewer started.");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error starting the report viewer in iDempiere.", e);
            throw e; // Rethrow the exception
        }

        return "Report generated successfully.";
    }
}
```