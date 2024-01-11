# <b>AMERPSOFT LCO Withholding</b>

## <b>AMERPSOFT LCO - Withholding</b>
This tutorial is brought to you by Luis Amesty from:

[Amerpsoft Consulting](http://amerpsoft.com/index.php/en/)

For any question or improvement see me at:

[Idempiere WIKI User: Luis Amesty](https://wiki.idempiere.org/en/User:Luisamesty)

[LinkedIn: Luis Amesty](https://www.linkedin.com/in/luisamesty/)

[email](luisamesty @ gmail.com) to me. <br/>


Github Project Home: [Amerpsoft-iDempiere-community](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/README.md)
## <b>Description</b>
AMERPSOFT LCO Withholding is a Fork for LCO Withholding from GolbalQss (Carlos Ruiz).

This Plugin is intended to solve Venezuela´s requirement for Withholding Documents Registration and Printing. These Modifications include Multiple Invoices registering.
Carlos Ruiz – GlobalQSS, developed original Plugin. This version is actually tested on Idempiere Version 8.2.

## <b>Documentation</b>
For complete information.
See AMERPSOFT LCO Withholding.docx 
  or 
AMERPSOFT LCO Withholding.pdf

PDF: [AMERPSOFT LCO Withholding - AMERPSOFT LCO Withholding.pdf ](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/org.amerpsoft.com.idempiere.lco.withholding/documentation/AMERPSOFT_LCO_Withholding.pdf)


## <b>Source code</b>

Source code may be downloaded from:

https://github.com/luisamesty/Amerpsoft-iDempiere-community/tree/master/org.amerpsoft.com.idempiere.lco.withholding

## <b>Installation Procedure</b>

### <b>1- Install Jar Plugin</b>
- Using Apache felix Web Console install:
  org.amerpsoft.com.idempiere.lco.withholding_10.0.0.201912231154.jar
  (Versión may differ)
- Restart Server
-	Remember to do:
    * Role Access Update
    * Syncronize Terminology
    * Sequence Check
### <b>2- Verify Pack-In</b>
Verify that 2Pack files were updated and loaded without errors.

You must login as a System user.

There are two Pack files:
- AMERPSOFT LCO Withholding (2Pack_7.1.1.zip)
- AMERPSOFT LCO Withholding Windows (2Pack_7.1.2.zip)

### <b>3- Add Withholding Data (Venezuela)</b>
This apply for Venezuela Withholding Seniat Rules.
Other countries has to bee created.
- Login idempiere as GardenAdmin.
- Pack in 'AMERPSOFT LCO Withholding Data.zip'
   * File is located on install directory.

### <b>4- Create and Update Document Types</b>

Create Document Types as indicated on Spread Sheet file:

  <b>AMERPSOFT_Document_Types.xls</b>

Also, Some Document Types must be updated as indicated.
Invoice, Credit Memo and Debit Memo.

### <b>5- Create Sequence for Document Type included</b>

Verify Sequence for Withholding Documents.
AP Invoice - Withholding Number Multiple Invoice.


When is completed, you should have:
- Tax rates
- Tax categories.
- Withholding Categories
- Withholding Types with Rules and Calc
- Invoice (Vendor) Window  ** UPDATED **
- Invoice Withholding (VAT) Window
- Invoice Withholding (MUNICIPAL) Window
- Invoice (Customer) Window  ** UPDATED **
- Invoice (Customer Withholding) Window 
- Invoice (Customer Return Check)  Window

For more information Contact to [me](luisamesty@gmail.com).
### Updated for Idempiere release 8.1 - November 2020
### Updated for Idempiere release 11 - January 2024
  Under Test - See release-11 branch.