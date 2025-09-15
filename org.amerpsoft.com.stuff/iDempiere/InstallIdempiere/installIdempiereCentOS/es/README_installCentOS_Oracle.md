&lArr;[Instalación de Idempiere](../../InstallIdempiere/README_ES.md)

<a name="readme-top"></a>

<div style="text-align: right;">

🇬🇧 [English Version](../README_installCentOS_Oracle.md) | 🇪🇸 Español

</div>

## Instalar Idempiere en CentOS Oracle

Procedimientos de instalación para CentOS con base de datos Oracle.

| Pasos | Enlace | Dónde | Comentarios |
| ----- | ---------------------------------- | ----------------- | --------------------------------------------- |
| 1 | [Instalar Oracle xe-21c](#step1) | Máquina de base de datos | Instalar la versión xe-21c de la base de datos Oracle |
| 2 | [Configurar Oracle xe-21c](#step2) | Máquina de base de datos | Configurar la base de datos Oracle xe-21c |
| 3 | [Instalar JAVA OpenJDK17](#step3) | Máquina de Idempiere | Descargar e instalar Java 17 |
| 4 | [Descargar instaladores](#step4) | Máquina Idempiere | Descargar instaladores del repositorio de iDempiere |
| 5 | [Instalar desde instaladores](#step5) | Máquina Idempiere | Descomprimir y copiar los instaladores en el directorio /opt |
| 6 | [Ejecutar idempiere12](#step6) | Máquina Idempiere | Ejecutar y configurar la ejecución automática de idempiere |

### <a name="step1"></a>⭐️ 1- Instalar Oracle XE-21c

#### MÁQUINA DE BASE DE DATOS

Instale en la máquina donde se ejecuta DDBB.

```
$ ssh root@maquina-oracle.com -p 22
```

Este procedimiento ha tomado información de la página oficial de Oracle:

https://docs.oracle.com/en/database/oracle/oracle-database/21/xeinl/installing-oracle-database-free.html#GUID-46EA860A-AAC4-453F-8EEE-42CC55A4FAD5

Puede seguir este enlace.

#### Repositorio RPM

Para descargar archivos RPM, debe abrir una cuenta Oracle.
Descargar repositorio:
https://www.oracle.com/database/technologies/xe-downloads.html

Usamos OL7 para CentOS 7 Linux.

#### Instalar Oracle

<b>Preinstalación</b>

Superusuario.
```texto
$ sudo -s
```

Comandos de preinstalación:

```texto
# curl -o oracle-database-preinstall-21c-1.0-1.el7.x86_64.rpm https://yum.oracle.com/repo/OracleLinux/OL7/latest/x86_64/getPackage/oracle-database-preinstall-21c-1.0-1.el7.x86_64.rpm
# yum -y localinstall oracle-database-preinstall-21c-1.0-1.el7.x86_64.rpm
```

Instalación.
Descargar RPM. URL1: https://www.oracle.com/database/technologies/xe-downloads.htm

Comando de instalación:

```text
# yum -y localinstall oracle-database-xe-21c-1.0-1.ol7.x86_64.rpm
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

### <a name="step2"></a>⭐️2-Configurar Oracle XE-21c

#### MÁQUINA DE BASE DE DATOS

Instale en la máquina donde se ejecuta el gestor de bases de datos.

Ejecute como usuario root usando sudo.

```texto
$ sudo -s
```

Ejecute el script de configuración del servicio:

```texto
# /etc/init.d/oracle-xe-21c configure
```

Contraseñas de respuesta:
```texto
root@localhost Descargas]# /etc/init.d/oracle-xe-21c configure
Especifique una contraseña para las cuentas de base de datos. Oracle recomienda que la contraseña introducida tenga al menos 8 caracteres, contenga al menos una mayúscula, una minúscula y un dígito [0-9]. Tenga en cuenta que se utilizará la misma contraseña para las cuentas SYS, SYSTEM y PDBADMIN:
La contraseña introducida contiene caracteres no válidos. Introduzca la contraseña:
Confirme la contraseña:

Las contraseñas no coinciden. Introduzca la contraseña:
Confirme la contraseña:
Configurando Oracle Listener.
La configuración del Listener se realizó correctamente.
Configurando Oracle Database XE. Introduce la contraseña de usuario SYS:
*********
Introduzca la contraseña de usuario SISTEMA:
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

##### GET IDEMPIERE 12 Installers

##### Install wget

````
$ sudo yum install wget
````

##### Check wget version by running:

````
$ sudo wget --version
````

##### DESCARGAR INSTALADORES

````
$ cd /home/
$ wget https://sourceforge.net/projects/idempiere/files/v12/daily-server/idempiereServer12Daily.gtk.linux.x86_64.zip
...
$ wget https://sourceforge.net/projects/idempiere/files/v12/daily-server/idempiereServer12Daily.gtk.linux.x86_64.zip.MD5
````

...

##### VERIFICAR

````
$ md5sum -c idempiereServer12Daily.gtk.linux.x86_64.zip.MD5
idempiereServer12Daily.gtk.linux.x86_64.zip: OK
````

<a href="#top">Volver arriba</a>

### <a name="step5"></a>⭐️5-Instalar desde los instaladores

#### MÁQUINA IDEMPIERE
````
Instalar en la máquina Idempiere y requiere Java. ssh root@maquina-idempiere.com -p 22
````
##### INSTALAR IDEMPIERE 12 DESDE Instaladores
##### CREAR USUARIO idempiere
````
adduser idempiere
````
##### AÑADIR CONTRASEÑA DE USUARIO
````
passwd idempiere
````

##### INSTALAR descomprimir
````
yum install descomprimir
````

##### DESCOMPRIMIR INSTALADORES
##### Descomprimir idempiereServer12Daily.gtk.linux.x86_64.zip
```

unzip idempiereServer12Daily.gtk.linux.x86_64.zip
```

##### MOVER INSTALADORES a /opt
````
mv idempiere.gtk.linux.x86_64/idempiere-server /opt
rmdir idempiere.gtk.linux.x86_64
chown -R idempiere:idempiere /opt/idempiere-server
````

##### CONFIGURAR IDEMPIERE

Todos los comandos deben ejecutarse como usuario idempiere.
Además, el usuario idempiere debe tener una ruta a Oracle 21c.
```text
#.bashrc
export ORACLE_SID=XE
export ORAENV_ASK=NO
. /opt/oracle/product/21c/dbhomeXE/bin/oraenv
#ORACLE_HOME = [] ? /opt/oracle/product/21c/dbhomeXE
```
Comience a configurar idempiere.

```copy
su - idempiere ##### No es necesario si ya eres el usuario idempiere.
cd /opt/idempiere-server
````
##### Ejecutar shell
````
sh console-setup-alt.sh
````

##### Valores de configuración:
````
JavaHome=/usr/lib/jvm/jdk-17.0.1 [1]
Opciones de Java [-Xms64M -Xmx512M]:
iDempiere Home [/opt/idempiere-server]:
Contraseña del almacén de claves [myPassword]:
Nombre de host del servidor de aplicaciones [0.0.0.0]:
Puerto web del servidor de aplicaciones [8080]:
Puerto SSL del servidor de aplicaciones [8443]:
¿Ya existe la base de datos? (S/N) [N]:
1. Oracle
2. PostgreSQL
Tipo de base de datos [1]
Nombre del host del servidor de la base de datos [dbhost.localhost.localdomain]:
Puerto del servidor de la base de datos [1521]:
Nombre de la base de datos [xepdb1]:
Usuario de la base de datos [adempiere]:
Contraseña de la base de datos [adempiere]:
Contraseña del usuario del sistema de la base de datos [YourOraclePassword]
CONEXIÓN
-----------
Éxito

Nombre del host del servidor de correo [localhost]:
Inicio de sesión del usuario de correo []:
Contraseña del usuario de correo []
Correo electrónico del administrador []:
11:24:30.476 ConfigurationData.testMail: OK: Correo electrónico no configurado [1]
Guardar cambios (S/N) [S]:
11:24:30.658 Ini.loadProperties: /opt/idempiere-server/idempiere.properties #27 [1]
11:24:30.709 ConfigurationData.save: /opt/idempiere-server/idempiereEnv.properties [1]
Los cambios se guardaron correctamente. ````

#### Configurar permisos para las carpetas del servidor

Primero, debe obtener el GID (ID de grupo) del administrador de base de datos (DBA) dentro del contenedor:
Verificar ‘/etc/group’
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

#### Verificar los atributos del grupo Adempiere.dmp
```texto
sudo chgrp 54322 /opt/idempiere-server/data
sudo chgrp 54322 /opt/idempiere-server/data/seed
sudo chgrp 54322 /opt/idempiere-server/data/seed/Adempiere.dmp
```

#### Importar la semilla de la base de datos

```texto
cd /opt/idempiere-server/utils
bash RUN_ImportIdempiere.sh
```

NOTA: Si su sistema tiene un grupo llamado dba, es posible que este comando falle porque el script oracle/ImportIdempiere.sh lo reestablece a dba. En ese caso, puede comentar esas líneas en el script para evitar reestablecer el grupo.

#### Actualizar la semilla de la base de datos

No es necesario en Seed, pero por si acaso.
```text
cd /opt/idempiere-server/utils
bash RUN_SyncDB.sh
```

#### Ejecutar iDempiere Server

```text
cd /opt/idempiere-server
bash idempiere-server.sh
```

#### Disfrutar de iDempiere Web
Abrir URL.
```copy
http://localhost:8080/
```

<a href="#top">Volver arriba</a>

### <a name="step6"></a>⭐️6-Ejecutar idempiere12

##### MÁQUINA IDEMPIERE
````
Instalar en la máquina Idempiere y requiere Java. ssh root@maquina-idempiere.com -p 22
````
##### EJECUTAR IDEMPIERE 12 DESDE Instaladores

##### Ejecución manual
Una vez instalado y configurado el servidor iDempiere, puede iniciarlo con:

````
$ su - idempiere # no es necesario si ya es usuario idempiere
$ cd /opt/idempiere-server
$ sh idempiere-server.sh
o
$ idempiere
o
$ nohup sh idempiere-server.sh >> idempiere-server.log 2>&1 &
````

##### Instalación como servicio
iDempiere se puede registrar como servicio en Linux. Para ello, copie los scripts proporcionados en la carpeta /etc/rc.d/init.d de la siguiente manera:

````
$ sudo su - # Debe ejecutarse como root
# cp /opt/idempiere-server/utils/unix/idempiere_RedHat.sh /etc/rc.d/init.d/idempiere
# systemctl daemon-reload
````

##### Ejecución de iDempiere como servicio
Una vez registrado iDempiere como servicio, se iniciará automáticamente al reiniciar el servidor. También puede iniciarse, detenerse, reiniciarse o comprobarse como de costumbre con:

````
# systemctl status idempiere # para comprobar el estado de la aplicación
# systemctl restart idempiere # para reiniciar la aplicación iDempiere
# systemctl stop idempiere # para detener La aplicación iDempiere
# systemctl start idempiere # para iniciar la aplicación iDempiere al detenerse
````

<a href="#top">Volver arriba</a>