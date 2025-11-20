&lArr;[COMMUNITY PLUGINS](../README.md) | [Home](../../README.md)

# <b>Amerpsoft-Financial Basic version 11</b>

## <b>Installation Content</b>

```text
1. Install Plugin using Apache felix Web Console
2. Pack IN AMERPSFOT Financial.zip
3. Restart Server
```

### <b>1-Install Plugin using Apache felix Web Console</b>

```text
	- Download plugin jar file from Repository. (Named as: org.amerpsoft.com.idempiere.financial_11.0.0.202404091015.jar )
	- Install using Osgi Apache Felix Web Console
	- or Any Manual procedure
	- Verify plugin is running and updated
```

### <b>2-Pack-IN AMERPSFOT Financial.zip</b>

```text
	 1. Download ‘AMERPSOFT Financial.zip’
	 2. Pack-IN using Application Dictionary --> Pack In menu 
	 3. Run Synchronize Terminology, Sequence Check and Role Access Update
	 4. Restart Server
 ```
 
### <b>3-Verify reports and processes menus created. </b>

Using Application Dictionary For Menus and Processes, make translations for your Language. In case of Spanish, you may copy es_CO (Colombia) Translations.
AMERPSOFT Financial Processes.
Company Logo is shown on most reports. To update go to 'System Admin / Tenant Rules / Tenant', on Tab 'Tenant Info', update 'Logo' and 'Logo Report' fields with your company image.

 ```text
	- Amfin Process Reset Accounting
	- Amfin Process Repost Accounting
	- Amfin Process GLJournal Annual Closing
```

AMERPSOFT Financial Reports

```text
	- Amfin Account Elements Jasper
	- Amfin Trial Balance Jasper One Period
	- Amfin Trial Balance Jasper by Two Dates
	- Amfin State Financial Balance Jasper
	- Amfin State Financial Integral Results Jasper
```

### <b>4-Import AMERPSOFT Financial Taxes. </b>

Using Application Dictionary pack-IN AMERPSOFT Financial Taxes as a System User.
This Packs modify table C_TaxCategory as explained in excel 'tablesFinancial.xlsx'.
It also modify Window for Tax Category, therefore we will be able to import Taxes.

Entering as a Tenant user and selecting the client import Taxes in this order:

- Import C_TaxCategory CSV file related with your Localization.
- Import C_Tax CSV file related with your Localization.

If it is required you must create an appropiated CSV file, or enter manually taxes categories and taxes.
** It is important this Step to follow with Withholding plugin.

 
 
### <b>5-Import AMERPSOFT Financial Reports. </b>
 
Update Packs - IN.

See : https://docs.amerpsoft.com/

    • AMERPSOFT Financial Test Reports. 
    Financial Test report for learning how to make reports using different techniques.
    See Blog: https://docs.amerpsoft.com/blog/reports-idempiere.
    
    • AMERPSOFT Financial Reports Xlsx.
    Special Form for Financial reports using Apache POIs classes to export into Xlsx 
    compatible formats.
    
    • AMERPSOFT Financial Rpt OrgParent. 
    Financial reports with Organization Tree cases. For clients with many organizations.
    Remove Attach from process ‘Amfin Account Elements Jasper’, before apply it.

