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