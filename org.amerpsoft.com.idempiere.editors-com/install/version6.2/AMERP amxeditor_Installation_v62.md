
AMERP Amerpsoft-editors Version 6.2 and Up: 
==========================================

    Installation
    ------------
    1. Pack IN AMERPSOFT amxeditor Packs-IN files:
    2. Set es_VE as System language
    3. Create Language extension for es__VE or xx__XX
    4. Verify country code 3 digits is updated on C_Country table
    5. Pack IN Venezuela ExtendedDemography
    6. Install Plugin using Apache felix Web Console
    7. Restart Server
    8. Modify C__BPartner__location Table
    9. Modify Country Record

1- Pack IN AMERP amxeditor.zip
- Using AD PackIn
- Download:
    AMERPSOFT Editor 6.2 - Step 1.zip
    AMERPSOFT Editor 6.2 - Step 2.zip
    AMERPSOFT Editor 6.2 - Step 3.zip
- Packin all thre in order.
Remember to do:
    Role Access Update
    Syncronize Terminology
    Sequence Check
	
2- Set es_VE as System language
- Login idempiere as System in English
- Go to Menu: /System Admin/General Rules/System Rules/Language
- On Window Language: Locate es_VE or your preferred Language xx_XX
- On Window Language: Check System Language
- On Window Language: Execute 'Language Mantenance' Process
    ( If you are using other Locale adjust to your)

3- Create Language extension for es__VE or xx__XX
- Download Script from Repository (See This plugin documentation directory)
- Example:  Create-language-from-es-CO-to-es-VE.sql" Script
- Execute Query from PostgreSQL environment
- You may edit this Query for your favourite Language xx_XX
- Remember to do Syncronize Terminology

4- Verify country code 3 digits is updated on C_Country table
- CountryCode3 Fields on C_Country Table are already set on Step 2

5- Pack IN Venezuela ExtendedDemography
- Using AD PackIn
- Download  Venezuela ExtendedDemography.zip, 
- Packin IT.
- You may look at 'GeografiaVenezolanaCompleta.saql'
- Make your Country Extended Demography Packin
    ( If you are using other local you may build your tables)
- Remember to execute procedures on System Admin --> General Rules: 
    Role Access Update, Sequence Check and Syncronize Terminology

6-Install Plugin using Apache felix Web Console
- Download plugin jar file from Repository
- Install using Osgi Apache Felix Web Console
- or Any Manual procedure
- Verify plugin is running and updated

7- Restart Server
- Restart idempiere Server 
- Verify plugin is Installed and Running

8- C_BPartner_location Table
- Using Application dictionary Modify Table
- Field: C_Location_ID
- Change Reference 
- with 
- LocationExtended

9- Modify Country Record
- Change Capture Sequence
-    with
- @CO@ @A1!@ @A2@ @A3@ @A4@  @R!@ @MU@ @PA@ @C@, @P@ 
