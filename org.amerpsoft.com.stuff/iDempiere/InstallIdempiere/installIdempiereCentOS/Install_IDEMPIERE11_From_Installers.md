&lArr; [Download idempiere11 Installers ](./Download_idempiere11_Installers.md) | [Installing CentOS](./README_installCentOS.md)  | [Start idempiere11 ](./Running_IDEMPIERE11.md) &rArr;

#### IDEMPIERE MACHINE
````
Install on Idempiere Machine and requires Java.
ssh root@maquina-idempiere.com -p 22
````
##### INSTALL IDEMPIERE 11 FROM Installers
##### CREATE USER idempiere
````
adduser idempiere
````
##### ADD USER PASWORD
````
passwd idempiere
````

##### INSTALL unzip
````
yum install unzip
````

##### UNZIP INSTALLERS
##### Unzip idempiereServer11Daily.gtk.linux.x86_64.zip
````
unzip idempiereServer11Daily.gtk.linux.x86_64.zip
````

##### MOVE INSTALLERS to /opt
````
mv idempiere.gtk.linux.x86_64/idempiere-server /opt
rmdir idempiere.gtk.linux.x86_64
chown -R idempiere:idempiere /opt/idempiere-server 
````

##### CONFIGURE IDEMPIERE
````
su - idempiere  ##### not necessary if you're already as user idempiere
cd /opt/idempiere-server
````
##### Execute Shell
````
sh console-setup-alt.sh
````

##### Answer Questions:
````
Java Home [/usr/lib/jvm/java-17-openjdk-17.0.6.0.10-3.el9.x86_64]:  Enter OK
Java Options [-Xms64M -Xmx512M]: Enter OK
iDempiere Home [/opt/idempiere-server]: Enter OK
Key Store Password [myPassword]: Enter OK

10:34:23.196 KeyStoreMgt.<init>: /opt/idempiere-server/jettyhome/etc/keystore [1]
KeyStore Settings.
(ON) Common Name [idempiere]: Enter OK

(OU) Organization Unit [iDempiereUser]:

(O) Organization [idempiere]:

(L) Locale/Town [MyTown]:

(S) State []:

(C) Country (2 Char) [US]

10:34:38.997 ConfigurationData.testAdempiere: OK: AdempiereHome = /opt/idempiere-server [1]
10:34:38.998 KeyStoreMgt.<init>: /opt/idempiere-server/jettyhome/etc/keystore [1]
10:34:38.999 KeyStoreMgt.createCertificate:  [1]
10:34:40.289 ConfigurationData.testAdempiere: OK: KeyStore = /opt/idempiere-server/jettyhome/etc/keystore [1]
Application Server Host Name [0.0.0.0]: Enter OK

Application Server Web Port [8080]: (Change ONLY if you Want) Enter OK
Application Server SSL Port[8443]:

DB Already Exists?(Y/N) [N]: Y Enter
1. Oracle
2. PostgreSQL
Database Type [2] Enter  (PostgreSQL)
Database Server Host Name [localhost]: Enter IP of PostgreSQL server (10.XXX.XXX.XXX) or (maquina-postgresql.com)
Database Server Port [5432]: Enter 
Database Name[idempiere]: idempiereSeed11. (Name of database)
Database user [adempiere]: Enter
Database Password [adempiere]: Enter
Database System User Password []. (Enter Password for PostgreSQL postgres user: #PosgreSQLPASS# )
Mail Server Host Name [localhost]: Enter (Will be configured later)
Mail User Login []: Enter (Will be configured later)
Mail User Password []
Administrator EMail []:
10:43:04.150 ConfigurationData.testMail: OK: EMail not configured [1]
Save changes (Y/N) [Y]: Y Enter

Changes will be updated in idempiere.properties and idempiereEnv.properties.
````

##### UPDATE DATABASE idempiereSeed11
In order to keep the database synchronized with the code it is required to run the following script:
````
su - idempiere  # not necessary if you're already as user idempiere
cd /opt/idempiere-server/utils
sh RUN_SyncDB.sh
````

##### UPDATE DATABASE idempiereSeed11
##### Register version code in database
````
In order to sign the database with the version code running on the server it is advised (or required depending on configuration)
to run the following script:
su - idempiere  # not necessary if you're already as user idempiere
cd /opt/idempiere-server
sh sign-database-build-alt.sh
````



