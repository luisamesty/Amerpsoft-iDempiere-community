&lArr;[AMERPSOFT Editor](../README.md) | [Home](../README.md)
# <b>Amerpsoft editors Version 7.1 and Up: </b>

## <b>Installation </b>

    1. Install Plugin using Apache felix Web Console
    2. Verify country code 3 digits is updated on C_Country table
    3. Pack IN Venezuela ExtendedDemography
    4. Restart Server
    5. Modify C__BPartner_Location Table
    6. Modify Country Record

(*)Steps 2 and 3 are for Venezuela, skip if not using additional language localization.

AMERPSOFT Editor Home: [AMERPSOFT Editor - Location Extended](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/org.amerpsoft.com.idempiere.editors-com/README.md)

Github Project Home: [Amerpsoft-iDempiere-community](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/README.md)

### <b>1-Install Plugin using Apache felix Web Console</b>
- Download plugin jar file from Repository. (Named as: org.amerpsoft.com.idempiere.editors-com_11.0.0.202404091015.jar )
- Install using Osgi Apache Felix Web Console
- or Any Manual procedure
- Verify plugin is running and updated
- Restart idempiere Server, ignore error the first time.
- Verify plugin is Installed and Running

Verify Pack-In automatically installed:
- 2Pack_1.0.0.zip
- 2Pack_2.0.0.zip
- 2Pack_3.0.0.zip
- 2Pack_4.0.0.zip
- 2Pack_5.0.0.zip
- 2Pack_6.0.0.zip
- 2Pack_7.0.0.zip
- 2Pack_8.0.0.zip
- 2Pack_8.1.0.zip
- 2Pack_8.2.0.zip
- 2Pack_8.3.0.zip
- 2Pack_8.4.0.zip

Ignore error starting server.
- <b>Restart idempiere Server</b>

These Packs IN, creates New structure for Extended Location, also C_Country3 Table and C_Community default records. One Community for each country in order to be consistent with the model:

- COUNTRY --> COMMUNITY --> REGION --> CITY  
- COUNTRY --> COMMUNITY --> REGION --> MUNICIPALITY --> PARISH
								 
Next, do the following actions:
- Role Access Update
- Syncronize Terminology
- Sequence Check
- <b>Restart idempiere Server</b>
	
### <b>2- Verify country code 3 digits is updated on C_Country table</b>
- CountryCode3 Fields on C_Country Table are already set on Pack-IN Step 1
- For verifying it: 
- Open the new created window:

- <b>System Admin -> General Rules --> System Rules -->Country Community Region Municipality Parish City</b>
    
### <b>3- Pack IN Venezuela Extended Demography</b>

Before doing this, a Manual Application Dictionary Change must be done on:
- Table <b>AD_Package_Exp_Detail</b>
- Column <b>SQLStatement</b>

Changes:

- Increase Length to 50.000
- Synchronize Column.
- Restart Server

This is very important, because Demography queries are very long. Next, proceed with Demography Install.

- Using AD PackIn
- Download:  AMERPSOFT Demografia Venezolana.zip, 
- Packin IT.
- You may take a look at 'GeografiaVenezolanaCompleta.sql' only for information.
- Make your Country Extended Demography Packin
    ( If you are using other local you may build your tables)
- Remember to execute procedures on System Admin --> General Rules: 
    - Role Access Update
    - Sequence Check 
    - Syncronize Terminology
    - Cache Reset

### <b>4- Restart Server</b>
- Restart idempiere Server 
- Verify plugin is Installed and Running

### <b>5- C_BPartner_Location Table</b>
- Using Application dictionary Modify Table
- Field: C_Location_ID
- Change Reference 
- with 
- LocationExtended

### <b>6- Modify Country Record</b>
- Change Capture Sequence
-    with
- @CO@ @A1!@ @A2@ @A3@ @A4@  @R!@ @MU@ @PA@ @C@, @P@ 
