&lArr;[Installing Idempiere](../../InstallIdempiere/README.md)

<a name="readme-top"></a>

## Install Idempiere in CentOS Oracle

Install Procedures for CentOS using Oracle database.


| Steps | Link                               | Where             | Comments                                      |
| ----- | ---------------------------------- | ----------------- | --------------------------------------------- |
| 1     | [Install Oracle xe-21c](#step1)    | DataBase machine  | Install Oracle Database version xe-21c        |
| 2     | [Config Oracle xe-21c ](#step2)    | DataBase machine  | Configure Oracle database xe-21c              |
| 3     | [Install JAVA OpenJDK17 ](#step3)  | Idempiere machine | Download and install Java 17                  |
| 4     | [Download Installers](#step4)      | Idempiere machine | Download installers from iDempiere repository |
| 5     | [Install from Installers ](#step5) | Idempiere machine | Unzip and copy Installers in /opt directory   |
| 6     | [Running idempiere11 ](#step6)     | Idempiere machine | Run and Configure auto execute idempiere      |


### <a name="step1"></a>⭐️ 1-Install Oracle XE-21c

#### DATABASE MACHINE

Install in the machine that it is executing DDBB.

```
$ ssh root@maquina-oracle.com -p 22
```

This procedure has taken information from Official Oracle page:

https://docs.oracle.com/en/database/oracle/oracle-database/21/xeinl/installing-oracle-database-free.html#GUID-46EA860A-AAC4-453F-8EEE-42CC55A4FAD5

You may follow this link.

#### Repository RPM

For Dowloading RPM files you must open an oracle account.
Download repository:
https://www.oracle.com/database/technologies/xe-downloads.html

We are using OL7 for CentOS 7 Linux.

#### Install Oracle

<b>Pre-Instalation.</b>

Super user.
```text
$ sudo -s
```

Preinstalation commands:

```text
# curl -o oracle-database-preinstall-21c-1.0-1.el7.x86_64.rpm https://yum.oracle.com/repo/OracleLinux/OL7/latest/x86_64/getPackage/oracle-database-preinstall-21c-1.0-1.el7.x86_64.rpm
# yum -y localinstall oracle-database-preinstall-21c-1.0-1.el7.x86_64.rpm
```

Instalation.
Download RPM.
URL1: https://www.oracle.com/database/technologies/xe-downloads.htm

Command for install:

```text
# yum -y localinstall oracle-database-xe-21c-1.0-1.ol7.x86_64.rpm
```


<p align="left">(<a href="#readme-top">back to top</a>)</p>

### <a name="step2"></a>⭐️2-Configure Oracle XE-21c

#### DATABASE MACHINE

Install on the machine that runs the database manager.

Execute as user root using sudo.

```text
$ sudo -s
```


Run the service configuration script:

```text
# /etc/init.d/oracle-xe-21c configure
```

Answer passwords:
```text
root@localhost Descargas]#  /etc/init.d/oracle-xe-21c configure
Specify a password to be used for database accounts. Oracle recommends that the password entered should be at least 8 characters in length, contain at least 1 uppercase character, 1 lower case character and 1 digit [0-9]. Note that the same password will be used for SYS, SYSTEM and PDBADMIN accounts:
The password you entered contains invalid characters. Enter password:
Confirm the password:

Passwords do not match.  Enter the password:
Confirm the password:
Configuring Oracle Listener.
Listener configuration succeeded.
Configuring Oracle Database XE.
Introduzca la contraseña de usuario SYS: 
*********
Introduzca la contraseña de usuario SYSTEM: 
******** 
Introduzca la contraseña de usuario de PDBADMIN: 
**********
Preparar para funcionamiento de base de datos
7% completado
Copiando archivos de base de datos


At the prompt, specify a password for the SYS, SYSTEM, and PDBADMIN administrative user accounts. Oracle recommends that the password entered should be at least 8 characters in length, contain at least 1 uppercase character, 1 lower case character and 1 digit [0-9].


50% completado
Creando Bases de Datos de Conexión
54% completado
71% completado
Ejecutando acciones posteriores a la configuración
93% completado
Ejecutando archivos de comandos personalizados
100% completado
Creación de la base de datos terminada. Consulte los archivos log de /opt/oracle/cfgtoollogs/dbca/XE
 para obtener más información.
Información de Base de Datos:
Nombre de la Base de Datos Global:XE
Identificador del Sistema (SID):XE
Para obtener información detallada, consulte el archivo log "/opt/oracle/cfgtoollogs/dbca/XE/XE.log".

Connect to Oracle Database using one of the connect strings:
     Pluggable database: localhost.localdomain/XEPDB1
     Multitenant container database: localhost.localdomain
Use https://localhost:5500/em to access Oracle Enterprise Manager for Oracle Database XE
```


#### Configuration, Database Files and Logs Location

|File Name and Location                              | Purpose                                      |
| -------------------------------------------------- | -------------------------------------------- |
| /opt/oracle | Oracle Base. This is the root of the Oracle Database XE directory tree.           |
| /opt/oracle/product/21c/dbhomeXE | Oracle Home. This home is where the Oracle Database XE is installed. It contains the directories of the Oracle Database XE executables and network files. |
| /opt/oracle/oradata/XE | Database files. | 
| /opt/oracle/diag subdirectories | Diagnostic logs. The database alert log is /opt/oracle/diag/rdbms/xe/XE/trace/alert_XE.log |
| /opt/oracle/cfgtoollogs/dbca/XE | Database creation logs. The XE.log file contains the results of the database creation script execution. |
| /etc/sysconfig/oracle-xe-21c.conf | Configuration default parameters. |
| /etc/init.d/oracle-xe—21c | Configuration and services script. |


#### Setting the Oracle Database XE Environment Variables

After you have installed and configured Oracle Database XE, you must set the environment before using Oracle Database XE.

Insert in  ./bashrc for oracle and idempiere users.

```text
$ export ORACLE_SID=XE 
$ export ORAENV_ASK=NO 
$ . /opt/oracle/product/21c/dbhomeXE/bin/oraenv
ORACLE_HOME = [] ? /opt/oracle/product/21c/dbhomeXE
The Oracle base has been set to 
```

#### Connecting to Oracle Database XE

Connecting Locally using OS Authentication
When you install Oracle Database XE, the oracle user is granted SYSDBA privileges. You can use the following commands to connect to the database.
As Oracle user

```text
$ cd <oracle_home>/bin
oracle_home = /opt/oracle/product/21c/dbhomeXE
$ cd /opt/oracle/product/21c/dbhomeXE/bin
$ sqlplus / as sysdba
```

Domain: localhost.localdomain
For example, you can connect to the root container of the database from a client computer with SQL*Plus using the following commands:
```text
cd <oracle_home>/bin
$ sqlplus system@dbhost.localhost.localdomain:1521
$ sqlplus system@dbhost.localhost.localdomain:1521
```
You can connect to the default pluggable database XEPDB1 using the following commands:
```text
$ cd <oracle_home>/bin
$ sqlplus system@dbhost.localhost.localdomain:1521/XEPDB1
```

#### Automating Shutdown and Startup

Execute the following commands as root:
```text
$ sudo -s
# systemctl daemon-reload
# systemctl enable oracle-xe-21c
```

Shutting Down and Starting Up Using the Configuration Services Script
```text
# /etc/init.d/oracle-xe-21c status
```

Status of the Oracle XE 21c service:
```text
LISTENER status: RUNNING
XE Database status:   RUNNING
/etc/init.d/oracle-xe-21c status
Status of the Oracle XE 21c service:
LISTENER status: NOT CONFIGURED
XE Database status: STOPPED
```

Run the following commands as root using sudo.

```text
$ sudo -s
Run the following command to start the listener and database:
# systemctl start oracle-xe-21c
Run the following command to stop the database and the listener:
# systemctl stop oracle-xe-21c
Run the following command to stop and start the listener and database:
# systemctl restart oracle-xe-21c
```


<p align="left">(<a href="#readme-top">back to top</a>)</p>


### <a name="step3"></a>⭐️3-Install JAVA OpenJDK17


#### IDEMPIERE MACHINE

````
Install on machine that executes Idempiere, it requires Java.
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

All commands must be executed as idempiere user.
Also idempiere user must have a path to oracle 21c.
```text
#.bashrc
export ORACLE_SID=XE 
export ORAENV_ASK=NO 
. /opt/oracle/product/21c/dbhomeXE/bin/oraenv
#ORACLE_HOME = [] ? /opt/oracle/product/21c/dbhomeXE
```
Start configuring idempiere.

```copy
su - idempiere  ##### not necessary if you're already as user idempiere
cd /opt/idempiere-server
````
##### Execute Shell
````
sh console-setup-alt.sh
````

##### Config values:
````
JavaHome=/usr/lib/jvm/jdk-17.0.1 [1]
Java Options [-Xms64M -Xmx512M]:
iDempiere Home [/opt/idempiere-server]:
Key Store Password [myPassword]:
Application Server Host Name [0.0.0.0]:
Application Server Web Port [8080]:
Application Server SSL Port[8443]:
DB Already Exists?(Y/N) [N]: 
1. Oracle
2. PostgreSQL
Database Type [1]
Database Server Host Name [dbhost.localhost.localdomain]:
Database Server Port [1521]:
Database Name[xepdb1]:
Database user [adempiere]:
Database Password [adempiere]:
Database System User Password [YourOraclePassword]
CONNECTION
-----------
Success

Mail Server Host Name [localhost]:
Mail User Login []:
Mail User Password []
Administrator EMail []:
11:24:30.476 ConfigurationData.testMail: OK: EMail not configured [1]
Save changes (Y/N) [Y]: 
11:24:30.658 Ini.loadProperties: /opt/idempiere-server/idempiere.properties #27 [1]
11:24:30.709 ConfigurationData.save: /opt/idempiere-server/idempiereEnv.properties [1]
Changes save successfully.
````

#### Configure permissions for the server folders

First you need to obtain the GID (Group ID) of the DBA within the container:
Verify ‘/etc/group’
```text
oinstall:x:54321:oracle
dba:x:54322:oracle
oper:x:54323:oracle
backupdba:x:54324:oracle
dgdba:x:54325:oracle
kmdba:x:54326:oracle
racdba:x:54330:oracle
cockpit-ws:x:979:
postgres:x:26:
mock:x:135:
idempiere:x:54331:
```
<b>dba:x:54322:oracle</b>

#### Verify group attributes to Adempiere.dmp
```text
sudo chgrp 54322 /opt/idempiere-server/data
sudo chgrp 54322 /opt/idempiere-server/data/seed
sudo chgrp 54322 /opt/idempiere-server/data/seed/Adempiere.dmp
```

#### Import the database seed

```text
cd /opt/idempiere-server/utils
bash RUN_ImportIdempiere.sh
```

NOTE: if your system has a group with name dba, it's possible that this command fails because the script oracle/ImportIdempiere.sh changes back the group to dba. If that's the case you can comment those lines in the script to avoid changing back the group.

#### Update the database seed

Not necessary on Seed, buty just in case.
```text
cd /opt/idempiere-server/utils
bash RUN_SyncDB.sh
```

#### Run iDempiere Server

```text
cd /opt/idempiere-server
bash idempiere-server.sh
```

#### Enjoy iDempiere Web
Open URL.
```copy
http://localhost:8080/
```

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
# cp /opt/idempiere-server/utils/unix/idempiere_RedHat.sh /etc/rc.d/init.d/idempiere
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
