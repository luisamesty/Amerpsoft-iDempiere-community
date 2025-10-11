
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

See: AccountElements_Tree_Pojo.java

### Data Populator

Example of a Data Populator, to fill the POJO with a Query similar to the one originally contained in the jrxml.

See: DataPopulator.java


### Process

Process to be executed with the Application Dictionary.

```java
// REPORTS JASPER
	// AccountElements_Tree 
    if (p_className.equals("org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree_Std"))
        return new org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree_Std();
    if (p_className.equals("org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree_Pojo"))
        return new org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree_Pojo();
```
