# Reportes XLS/XLSX Idempiere
Here is the text translated into English, detailing how to create a process in iDempiere that generates an Excel file and displays it in the viewer:

-----

## **How to Create a Process to Generate and View Excel in iDempiere**

To create a process in iDempiere that generates an Excel file and displays it in the application's viewer, you need to develop a **Java class** and register it within the system.

### 1\. Java Process Development 💻

You must create a Java class that **extends `SvrProcess`** and override the `doIt()` method. The Excel export logic will be contained within this method.

-----

### 2\. Excel Generation Logic (Apache POI)

The standard and most recommended way to generate the Excel file (`.xlsx` or `.xls` format) is by using the **Apache POI** library.

**Steps inside `doIt()`:**

1.  **Retrieve Data:** Execute your SQL query or use iDempiere's ORM to fetch the data you want to export.
2.  **Create Workbook:** Initialize an `XSSFWorkbook` (for `.xlsx`) or `HSSFWorkbook` (for `.xls`) from Apache POI.
3.  **Create Sheet:** Create one or more sheets within the workbook.
4.  **Populate Data:** Iterate over the retrieved data, creating rows (`Row`) and cells (`Cell`) on the sheet, and inserting the values.
5.  **Write the Temporary File:**
      * Create a temporary file on the server to save the Excel content.
      * Use a `FileOutputStream` to write the `Workbook` to that temporary file.
      * **Crucial:** Make sure to close the `Workbook` and the stream after writing.

-----

### 3\. Display in the iDempiere Viewer (The Deployment) 📄

To make iDempiere display the generated Excel in its report viewer, you must configure the process result:

1.  **Create the Report File Object:** Use the temporary file generated in the previous step to create a `java.io.File` object and then a **`ReportResult`** object.

    ```java
    // Assuming 'file' is the java.io.File of the generated Excel
    ReportResult rr = new ReportResult(file);

    // Configure the filename given to the user
    rr.setName(getProcessInfo().getTitle() + ".xlsx"); 

    // Configure the MIME type for Excel
    rr.setMimeType(ReportResult.MIME_TYPE_EXCEL); 

    // This is key: assign the result to the ProcessInfo
    getProcessInfo().setReportResult(rr); 

    // Optional: If you want to force direct download:
    // getProcessInfo().setPrintPreview(false); 

    return "Process completed successfully and Excel generated.";
    ```

2.  **Return Result:** The `doIt()` method must successfully return a message. The `ReportResult` object attached to the `ProcessInfo` will take care of presenting the file to the user in a new browser tab or the document viewer for viewing or download.

-----

### 4\. Registering the Process in iDempiere ⚙️

Once you have the compiled Java code included in a custom plugin's bundle (or jar):

1.  **"Report and Process" Window (AD\_Process):**
      * Create a new record.
      * Define a **Name** and a **Search Key**.
      * In the **Java Class** field, enter the full name of your class (e.g., `com.yourcompany.processes.MyExcelExport`).
      * Check **Active**.
2.  **Access:** Ensure the user role has access to the process through the **Access to Report and Process** tab or the corresponding **Role** window.

When the process is executed from the menu or a window, iDempiere will run your Java code, generate the Excel, and, thanks to the `ReportResult` configuration, present it to the user for viewing or download.