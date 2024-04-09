&lArr;[AMERPSOFT Editor](../README.md) | [Home](../README.md)
# <b>Amerpsoft editors Version 7.1 and Up: </b>

## <b>Installation </b>

    1. Pack IN AMERPSOFT amxeditor Packs-IN files:
    2. Set es_VE as System language (*)
    3. Create Language extension for es__VE or xx__XX (*)
    4. Verify country code 3 digits is updated on C_Country table
    5. Pack IN Venezuela ExtendedDemography
    6. Install Plugin using Apache felix Web Console
    7. Restart Server
    8. Modify C__BPartner_Location Table
    9. Modify Country Record

(*)Steps 2 and 3 are for Venezuela, skip if not using additional language localization.

AMERPSOFT Editor Home: [AMERPSOFT Editor - Location Extended](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/org.amerpsoft.com.idempiere.editors-com/README.md)

Github Project Home: [Amerpsoft-iDempiere-community](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/README.md)

### <b>1- Pack IN AMERP amxeditor.zip</b>
Verify Pack-In 
- 2Pack_1.0.0.zip
- 2Pack_2.0.0.zip
- 2Pack_3.0.0.zip
- 2Pack_4.0.0.zip
- 2Pack_5.0.0.zip
- 2Pack_6.0.0.zip
- 2Pack_7.0.0.zip
- 2Pack_8.0.0.zip
- 2Pack_8.1.0.zip

Pack IN Manually as System User: 

- AMERPSOFT Editor - CountryCode3.zip
- AMERPSOFT Editor - Cummunity Fill.zip

Remember to do:
    Role Access Update
    Syncronize Terminology
    Sequence Check
	
### <b>2- Set es_VE as System language</b>
- Login idempiere as System in English
- Go to Menu: /System Admin/General Rules/System Rules/Language
- On Window Language: Locate es_VE or your preferred Language xx_XX
- On Window Language: Check System Language
- On Window Language: Execute 'Language Mantenance' Process
    ( If you are using other Locale adjust to your)

### <b>3- Create Language extension for es__VE or xx__XX</b>
- Download Script from Repository (See This plugin documentation directory)
- Example:  Create-language-from-es-CO-to-es-VE.sql" Script
- Execute Query from PostgreSQL environment
- You may edit this Query for your favourite Language xx_XX
- Remember to do Syncronize Terminology

### <b>4- Verify country code 3 digits is updated on C_Country table</b>
- CountryCode3 Fields on C_Country Table are already set on Packin Step 2
- For verifying it: 
- Open the new created window:

- <b>System Admin -> General Rules --> System Rules -->Country Community Region Municipality Parish City</b>
    
### <b>5- Pack IN Venezuela Extended Demography</b>
- Using AD PackIn
- Download:  AMERPSOFT Demografia Venezolana.zip, 
- Packin IT.
- You may look at 'GeografiaVenezolanaCompleta.sql'
- Make your Country Extended Demography Packin
    ( If you are using other local you may build your tables)
- Remember to execute procedures on System Admin --> General Rules: 
    - Role Access Update
    - Sequence Check 
    - Syncronize Terminology
    - Cache Reset

### <b>6-Install Plugin using Apache felix Web Console</b>
- Download plugin jar file from Repository
- Install using Osgi Apache Felix Web Console
- or Any Manual procedure
- Verify plugin is running and updated

### <b>7- Restart Server</b>
- Restart idempiere Server 
- Verify plugin is Installed and Running

### <b>8- C_BPartner_Location Table</b>
- Using Application dictionary Modify Table
- Field: C_Location_ID
- Change Reference 
- with 
- LocationExtended

### <b>9- Modify Country Record</b>
- Change Capture Sequence
-    with
- @CO@ @A1!@ @A2@ @A3@ @A4@  @R!@ @MU@ @PA@ @C@, @P@ 
