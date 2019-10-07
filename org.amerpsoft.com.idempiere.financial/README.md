## README ##
##### AMERP Financial:
5.1 Actualizado Marzo 2018
Created and Starting Branch release-5.1 on March 13 2019
default on idempiere release 6.2
*********************************************************
##### Directory: #####
* /Volumes/Datos/Adempiere/iDempiere5.1srcMac/AMX_Plugins5.1.0/amerpsoft-financial
* Push:
* New: hg push https://bitbucket.org/amerpsoft/amerpsoft-financial
* Revisada 3 Septiembre 2018

##### AMERP-AMFIN: Plugin Financial
* Name: org.amerp.amfmodel
* Pack: 2Pack.zip
* CSV: Amerp_Financial_Package_Exp_Detail.csv 
* (For Import PACKOUT on a New Developing Data Base)

##### 1.	AMERP Consultation.
* Application Dictionary Windows, for Consulting Accounts, Balances and Detail.

##### 2.	Tables.
Tables involved:
###### C\_ElementValue (AD Account Element):
######  Fields Added:
######  - Name2: String 60 , Non Mandaatory, Updatable
######  - Value_Parent : String 60 , Non Mandaatory, Updatable
######C\_ElementParent_ID:  
###### - Table Direct 10 length, Updatable
###### - Value_Parent: String 20,  Non Mandatory, Updatable
#####AMF\_AcctBalDetail: (New table for Consulting Definition)

##### 3.	Functions.
##### Special Function for calculating Balances and Dates:
* amf_acctdate0prevbalance.sql
* amf_acctdate1deb.sql
* amf_acctdate2cre.sql
* amf_acctdate3balance.sql
* amf_acctdate4currentbalance.sql
* amf_acctper0prevbalance.sql
* amf_acctper1deb.sql
* amf_acctper2cre.sql
* amf_acctper3balance.sql
* amf_acctper4currentbalance.sql
* amf_dow2letter.sql
* amf_fiscalstartdate.sql
* amf_month2letter.sql
* amf_nextperiodenddate.sql
* amf_nextperiodstartdate.sql
* amf_num2letter.sql
* amf_num2letter1000.sql
* amf_periodenddate.sql
* amf_periodstartdate.sql
* amf_prevperiodenddate.sql
* amf_prevperiodstartdate.sql

##### 4.	Amf_Views:
##### Must be created in order, because Views are dependant bethween them.
1. amf_accounts_v.sql
2. amf_accounts_detail_v.sql
3. amf_accounts_balance_v.sql

##### 5.	Report Modules: AMERP Reports. (Jasper Reports Financial Standard Formats)
##### All Menu: Amerp Reports
* Amerp Windows Financial Consultations
* Amerp Financial Reports
* Bank Reports
* VAT and Tax Reports
