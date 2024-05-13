&lArr;[Installing Idempiere](../../InstallIdempiere/README.md)

<a name="readme-top"></a>

## Install Idempiere in CentOS Oracle

Install Procedures for CentOS using Oracle database.


| Steps | Link                               | Where             | Comments                                      |
| ----- | ---------------------------------- | ----------------- | --------------------------------------------- |
| 1     | [Install Oracle xe-21c](#step1)    | DataBase machine  | Install Oracle Database version xe-21c         |
| 2     | [Config Oracle xe-21c ](#step2)    | DataBase machine  | Configure Oracle database xe-21c      |
| 3     | [Install JAVA OpenJDK17 ](#step3)  | Idempiere machine | Download and install Java 17                  |
| 4     | [Download Installers](#step4)      | Idempiere machine | Download installers from iDempiere repository |
| 5     | [Install from Installers ](#step5) | Idempiere machine | Unzip and copy Installers in /opt directory   |
| 6     | [Running idempiere11 ](#step6)     | Idempiere machine | Run and Configure auto execute idempiere      |


### <a name="step1"></a>⭐️ 1-Install PostgreSQL 15

#### DATABASE MACHINE

Install in the machine that it is executing DDBB.

```$
$ ssh root@maquina-oracle.com -p 22
```

#### Install the repository RPM


#### Install Oracle


#### Optionally initialize the database and enable automatic start.




<p align="left">(<a href="#readme-top">back to top</a>)</p>

### <a name="step2"></a>⭐️2-Configure Oracle XE-21c

#### DATABASE MACHINE

Install on the machine that runs the database manager.

```text
text
```


#### Re-Start Oracle

restart the Oracle service to enable these changes

```
sudo systemctl ....
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
