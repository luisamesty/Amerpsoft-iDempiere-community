&lArr;[Installing Idempiere](../../InstallIdempiere/README.md)

<a name="readme-top"></a>

## Install Idempiere in CentOS

Install Procedures for CentOS using PostgreSQL database.


| Steps | Link                               | Where             | Comments                                      |
| ----- | ---------------------------------- | ----------------- | --------------------------------------------- |
| 1     | [Install PostgreSQL 15](#step1)    | DataBase machine  | Install PostgeSQL Database version 15         |
| 2     | [Config PostgreSQL 15 ](#step2)    | DataBase machine  | Edit pg_hba.conf / postgresql.conf files      |
| 3     | [Install JAVA OpenJDK17 ](#step3)  | Idempiere machine | Download and install Java 17                  |
| 4     | [Download Installers](#step4)      | Idempiere machine | Download installers from iDempiere repository |
| 5     | [Install from Installers ](#step5) | Idempiere machine | Unzip and copy Installers in /opt directory   |
| 6     | [Running idempiere11 ](#step6)     | Idempiere machine | Run and Configure auto execute idempiere      |


### <a name="step1"></a>⭐️ 1-Install PostgreSQL 15

#### DATABASE MACHINE

Install in the machine that it is executing DDBB.

```$
$ ssh root@maquina-postgresql.com -p 22
```

#### Install the repository RPM

```
sudo yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
```

#### Install PostgreSQL

```
sudo yum install -y postgresql15-server
```

#### Optionally initialize the database and enable automatic start.

```
sudo /usr/pgsql-15/bin/postgresql-15-setup initdb
sudo systemctl enable postgresql-15
sudo systemctl start postgresql-15
dnf install postgresql15-contrib
```
#### Modify postgres password.

In order to start using PostgreSQL, you will need to connect to its prompt. Start by switching to the postgres user.

```
 sudo su postgres
```
Change User Password

```
postgres=# ALTER USER postgres PASSWORD 'NewPassw0rd';
```

#### Re-Start Postgresl

Lastly, restart the PostgreSQL service to enable these changes
```
sudo systemctl restart postgresql-15
```

<p align="left">(<a href="#readme-top">back to top</a>)</p>

### <a name="step2"></a>⭐️2-Config PostgreSQL 15

#### DATABASE MACHINE

#### DATABASE MACHINE

Install on the machine that runs the database manager.

````
ssh root@maquina-postgresql.com -p 22
````

##### Execute Postgres

Execute postgres on the machine that runs the database manager.

<div style="padding-left: 20px;">

````
OS Command (Port No may be different 5432,5433)
$ sudo psql -p 5432 postgres -U postgres
password user postgres: PostgreSQLPassword (NewPassw0rd) 	# Enter postgres password
````

</div>

##### CREATE ROLE

Create idempiere role and any other if necessary.

<div style="padding-left: 20px;">

````
Postgres Commands:
# CREATE ROLE adempiere SUPERUSER LOGIN PASSWORD 'adempiere';
# CREATE ROLE luisamesty SUPERUSER LOGIN PASSWORD 'NNNNNNN';
````

</div>

##### CREATE Database: idempiereSeed11

Create Database entity (idempiereSeed11). It is created empty.

<div style="padding-left: 20px;">
DROP IF NECESSARY ONLY

````
Postgres Commands:
These two firsts commands disconnect all processes to database:
# SELECT pg_terminate_backend(pg_stat_activity.pid) FROM   pg_stat_activity
WHERE pg_stat_activity.datname = 'idempiereSeed11'  AND pid <> pg_backend_pid();

# SELECT pg_terminate_backend(pid) FROM  pg_stat_get_activity(NULL::integer)
WHERE datid = (SELECT oid FROM pg_database WHERE datname = 'idempiereSeed11');

DROP Database
# DROP DATABASE "idempiereSeed11";

CREATE Database:
# CREATE DATABASE "idempiereSeed11"
    WITH 
    OWNER = adempiere
    ENCODING = 'UTF8'
   TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

# ALTER DATABASE "idempiereSeed11"
    SET search_path TO adempiere;   
````

</div>

##### RENAME Postgres Database

<div style="padding-left: 20px;">
RENAME IF NECESSARY ONLY FOR KEEP a BACKUP

````
Postgres Commands:
# ALTER DATABASE "idempiereSeed11" RENAME TO "idempiereSeed11_Backup"
````

</div>

##### EXIT Postgres

<div style="padding-left: 20px;">

````
Postgres Commands:
# \q
````

</div>

##### IMPORT DATABASE idempiereSeed11 FROM Adempiere_pg.jar

* Locate in: sources/iDempiere11/idempiere/org.adempiere.server-feature/data/seed
* Unzip Adempiere_pg.jar from /home/Adempiere_pg.jar to Adempiere_pg.dmp

<div style="padding-left: 20px;">

````
$ cd /home
$ ls -l
total 53488
-rw-r--r--. 1 root root 44945504 Mar 21 10:05 Adempiere_pg.dmp
-rw-r--r--. 1 root root  9818290 Mar 21 09:08 Adempiere_pg.jar
-- EXECUTE POSTGRES COMMAND
$ psql -d idempiereSeed11 -U adempiere -f Adempiere_pg.dmp
password user adempiere: adempiere 
````

</div>

##### PostgreSQL Files and Folders

<div style="padding-left: 20px;">

Data and configuration files:

````
/var/lib/pgsql/15/data/pg_hba.conf
/var/lib/pgsql/15/data/postgresql.conf
````

Edit postgresql.conf

````
#------------------------------------------------------------------------------
# CONNECTIONS AND AUTHENTICATION
#------------------------------------------------------------------------------
# - Connection Settings -
#listen_addresses = '*'         # what IP address(es) to listen on;
                                        # comma-separated list of addresses;
                                        # defaults to 'localhost'; use '*' for all
                                        # (change requires restart)
port = 5432                             # (change requires restart)
max_connections = 100                   # (change requires restart)
#superuser_reserved_connections = 3     # (change requires restart)
````

Edit pg_hba.conf
Add remote connections

````
# IPv4 local connections:
host    all             all             127.0.0.1/32            scram-sha-256
host    all             all             79.150.136.208/32           scram-sha-256
````

</div>

DATABASE IS CREATED

VERIFY Database is created with pgadmin4
Schema: adempiere should be created with 899 tables, 160 Views, 70 Functions.

</div>

<p align="left">(<a href="#readme-top">back to top</a>)</p>


### <a name="step3"></a>⭐️3-Install JAVA OpenJDK17


#### MAQUINA IDEMPIERE

````
Instalar en la maquina que ejecuta Idempiere y requiere de Java.
ssh root@maquina-idempiere.com -p 22
````

#### Install JAVA OpenJDK17

````
ssh root@maquina-idempiere.com -p 22
yum install java-17-openjdk
````

#### Verify Java

````
java -version
openjdk version "17.0.6" 2023-01-17 LTS
OpenJDK Runtime Environment (Red_Hat-17.0.6.0.10-3.el9) (build 17.0.6+10-LTS)
OpenJDK 64-Bit Server VM (Red_Hat-17.0.6.0.10-3.el9) (build 17.0.6+10-LTS, mixed mode, sharing)
````

#### Install to avoid java.lang.reflect.InvocationTargetException

````
yum install fontconfig
DigitalOcean Droplet Agent                                                                                                                            35 kB/s | 3.3 kB     00:00
Package fontconfig-2.14.0-2.el9.x86_64 is already installed.
Dependencies resolved.
Nothing to do.
Complete!
````

<p align="left">(<a href="#readme-top">back to top</a>)</p>


### <a name="step4"></a>⭐️4-Download Installers


#### IDEMPIERE MACHINE

````
Install on Idempiere Machine and requires Java.
ssh root@maquina-idempiere.com -p 22
````

##### GET IDEMPIERE 11 Installers

##### Install wget

````
$ sudo yum install wget
````

##### Check wget version by running:

````
$ sudo wget --version
````

##### DOWNLOAD INSTALLERS

````
$ cd /home/
$ wget https://sourceforge.net/projects/idempiere/files/v11/daily-server/idempiereServer11Daily.gtk.linux.x86_64.zip
...
$ wget https://sourceforge.net/projects/idempiere/files/v11/daily-server/idempiereServer11Daily.gtk.linux.x86_64.zip.MD5
````

...

##### VERIFY

````
$ md5sum -c idempiereServer11Daily.gtk.linux.x86_64.zip.MD5
idempiereServer11Daily.gtk.linux.x86_64.zip: OK
````

<a href="#top">Back to top</a>


### <a name="step5"></a>⭐️5-Install from Installers

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


<a href="#top">Back to top</a>



### <a name="step6"></a>⭐️6-Running idempiere11



##### IDEMPIERE MACHINE
````
Install on Idempiere Machine and requires Java.
ssh root@maquina-idempiere.com -p 22
````
##### RUNNING IDEMPIERE 11 FROM Installers

##### MAnual Running
Once installed and configured the iDempiere server you can start it with:

````
$ su - idempiere  # not necessary if you're already as user idempiere
$ cd /opt/idempiere-server
$ sh idempiere-server.sh
or
$ idempiere
or
$ nohup sh idempiere-server.sh >> idempiere-server.log 2>&1 &
````

##### Installing as service
iDempiere can be registered as a service in linux, in order to do that you can copy the provided scripts to /etc/rc.d/init.d folder like this:

````
$ sudo su -    # this must be executed as root
# cp /opt/idempiere-server/utils/unix/idempiere_Debian.sh /etc/rc.d/init.d/idempiere
# systemctl daemon-reload
````
##### Running Idempiere as a service
After iDempiere is registered as a service, it will be started automatically on server reboots, also it can be started / stopped / restarted / checked as usual with:

````
# systemctl status idempiere     # to check the status of the app
# systemctl restart idempiere    # to restart the iDempiere app
# systemctl stop idempiere       # to stop the iDempiere app
# systemctl start idempiere      # to start the iDempiere app when stopped
````

<a href="#top">Back to top</a>
