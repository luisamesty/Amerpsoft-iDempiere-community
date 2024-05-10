&lArr;[AMERPSOFT Editor](../README.md) | [Home](../README.md)
# <b>Amerpsoft editors Version 11 </b>

## <b>Installation Steps</b>

Note:
In case you have a previous version installed, verify if tables C_country3 or c_country2 exists in Database. I any of these tables exists, then remove them. C_country3 table will be created.
 
Follow steps:

```text
    1. Install Plugin using Apache felix Web Console
    2. Pack IN CommunityFill Packs
    3. Verify country code 3 digits is updated on C_Country table
    4. Pack IN ExtendedDemography
    5. Restart Server
    6. Modify C__BPartner_Location Table
    7. Modify Country Record
    8. Pack IN World Demography
    A. Additional Note.
```

### <b>1-Install Plugin using Apache felix Web Console</b>

```text
- Download plugin jar file from Repository. 
  Named as: org.amerpsoft.com.idempiere.editors-com_11.0.0.202404091015.jar
- Install using Osgi Apache Felix Web Console
- or Any Manual procedure
- Verify plugin is running and updated
- Restart idempiere Server, ignore error the first time.
- Verify plugin is Installed and Running
```

Verify Pack-In automatically installed:

```text
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
- 2Pack_8.5.0.zip
- 2Pack_8.5.1.zip
```

** **-Ignore error starting server-**  **.
- <b>Restart idempiere Server</b>

These Packs IN, creates New structure for Extended Location.
								 
Next, do the following actions:

```text
- Role Access Update
- Syncronize Terminology
- Sequence Check
- <b>Restart idempiere Server</b>
```

### <b>2- Pack CommunityFill Packs</b>

 Using AD PackIn
 Download Community File an Pack IN:  
 
```text
- AMERPSOFT Editor - Community Fill.zip
```

<b>Restart idempiere Server</b>


Important to select database version folder for packs (oracle - postgresql).

These Packs-in creates C_Community default records. 

One Community for each country in order to be consistent with the model:

```text
- COUNTRY --> COMMUNITY --> REGION --> CITY  
- COUNTRY --> COMMUNITY --> REGION --> MUNICIPALITY --> PARISH
```

### <b>3- Verify country code 3 digits is updated </b>

On C_Country table verify:

- CountryCode3 Fields on C_Country Table are already set on Pack-IN Step 2
- For verifying it: 
- Open the new created window:

- <b>System Admin -> General Rules --> System Rules -->Country Community Region Municipality Parish City</b>
    
### <b>4- Pack IN Extended Demography</b>

Before doing this, a Manual Application Dictionary Changes must be done on:
- Table: <b>AD_Package_Exp_Detail</b>
- Column: <b>SQLStatement</b>

Changes:

- Increase <b>Length to 65.000</b>
- Synchronize Column.
- Restart Server

This is very important, because Demography queries are very long. Next, proceed with Demography Install.

- Using AD PackIn
- Download Country File:  AMERPSOFT Demografia Venezolana.zip or AMERPSOFT Demografia Paraguay.zip 
- Packin IT.

You may take a look at 'GeografiaVenezolanaCompleta.sql' only for information or in case you prefer to do it using DatabaBase Client.
Also 'GeografiaParaguayBasica.sql/GeografiaParaguayExtendida.sql' are available as examples.

Make your Country Extended Demography Pack-IN
( If you are using other local you may build your tables)

Remember to execute procedures on System Admin --> General Rules: 

    - Role Access Update
    - Sequence Check 
    - Syncronize Terminology
    - Cache Reset

(*) Repeat this procedure for any other Demography Pack-Out provided.

Delete Queries are available if something goes wrong with Pack-IN. You may delete records for a selected country.
See 'delete_demografia_venezuela.sql' or 'delete_demografia_paraguay.sql'. Delete commands must be executed in the indicated order.
 
### <b>5- Restart Server</b>
- Restart idempiere Server 
- Verify plugin is Installed and Running

### <b>6- C_BPartner_Location Table</b>
- Using Application dictionary Modify Table <b>C_BPartner_Location</b>
- Column: <b>C_Location_ID</b>
- Change Reference 
- from: <b>Location</b> 
- to  : <b>LocationExtended</b>

### <b>7- Modify Country Record</b>

Using the new window <b>'Country Community Region Municipality Parish'</b> provided.

- Select Country
- Change Capture Sequence field
-    with
- @CO@ @A1!@ @A2@ @A3@ @A4@  @R!@ @MU@ @PA@ @C@, @P@ 

### <b>8- Pack IN World Demography</b>

World Demography includes 44690 Cities in the world.
This Cities must be included applying Pakc-In and import sql Query provided.
- Using 'AMERPSOFT World Demography.zip'. Pack-IN City Changes.
- Import Cities using 'WorldDemography.sql' Query.

Additional columns were added to c_city Table, such as Name2, Latitude, Longitude, Poppulation.
Use 'Country Region and city' Windown and see them on Tab 'City (Without Regions)


### <b>A- Additonal Note</b>

A Pack-Out is provided to execute all changes manually if something goes wrong or you are installing a non fresh database.
Two files are provided in addition:
- AMERPSOFT Editor AD.csv
- AMERPSOFT Editor AD.zip
