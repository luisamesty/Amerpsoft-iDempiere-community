&lArr;[Instalación de Idempiere](../../README_ES.md)
<!-- LOGOTIPO DEL PROYECTO -->
<a name="readme-top"></a>

<div style="text-align: right;">

🇬🇧 [English Version](../README_installCentOS.md) | 🇪🇸 Español

</div>

## Instalar Idempiere en CentOS

Procedimientos de instalación para CentOS con la base de datos PostgreSQL.

| Pasos | Enlace | Dónde | Comentarios |
| ----- | ---------------------------------- | ----------------- | --------------------------------------------- |
| 1 | [Instalar PostgreSQL 15](#step1) | Máquina de la base de datos | Instalar la base de datos PostgreSQL versión 15 |
| 2 | [Configurar PostgreSQL 15](#step2) | Máquina de la base de datos | Editar los archivos pg_hba.conf / postgresql.conf |
| 3 | [Instalar JAVA OpenJDK17](#step3) | Máquina de Idempiere | Descargar e instalar Java 17 |
| 4 | [Descargar instaladores](#step4) | Máquina Idempiere | Descargar instaladores del repositorio de iDempiere |
| 5 | [Instalar desde instaladores](#step5) | Máquina Idempiere | Descomprimir y copiar los instaladores en el directorio /opt |
| 6 | [Ejecutar idempiere12](#step6) | Máquina Idempiere | Ejecutar y configurar la ejecución automática de idempiere |

### <a name="step1"></a>⭐️ 1- Instalar PostgreSQL 15

#### MÁQUINA DE BASE DE DATOS

Instálelo en la máquina donde se ejecuta DDBB.

```bash
ssh root@maquina-postgresql.com -p 22
```

#### Instalar el RPM del repositorio

```bash
sudo yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
```

#### Instalar PostgreSQL

```bash
sudo yum install -y postgresql15-server
```

#### Opcionalmente, inicializar la base de datos y habilitar el inicio automático.

```bash
sudo /usr/pgsql-15/bin/postgresql-15-setup initdb
sudo systemctl enable postgresql-15
sudo systemctl start postgresql-15
dnf install postgresql15-contrib
```
#### Modificar la contraseña de PostgreSQL.

Para empezar a usar PostgreSQL, deberá conectarse a su indicador de comandos. Comience cambiando al usuario de PostgreSQL.

```bash
sudo su postgres
```
Cambiar contraseña de usuario

```bash
postgres=# ALTER USER postgres PASSWORD 'NewPassw0rd';
```

#### Reiniciar PostgreSQL

Por último, reinicie el servicio PostgreSQL para habilitar estos cambios.

```bash
sudo systemctl restart postgresql-15
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

### <a name="step2"></a>⭐️2-Configurar PostgreSQL 15

#### MÁQUINA DE BASE DE DATOS

Instale en la máquina donde se ejecuta el administrador de bases de datos.

```bash
ssh root@maquina-postgresql.com -p 22
```

#### Modificar el archivo ***ph_hba.conf***

Después de instalar Postgres, debe verificar la configuración correcta de:

"/var/lib/pgsql/15/data/pg_hba.conf"

La siguiente línea requiere cambiar el método de autenticación:

```bash
local all all peer
```
CAMBIAR A:

```bash
local all all scram-sha-256
```

Añadir conexión remota para su IP

```bash
# Conexiones locales IPv4:
host all all 127.0.0.1/32 scram-sha-256
host all all 83.49.112.218/32 scram-sha-256
```

#### Modificar el archivo postgresql.conf

Para desarrollo, abra la dirección de escucha '*'; para producción, localhost.

Puerto 5432/5433.

Editar: '/var/lib/pgsql/15/data/postgresql.conf'

```bash
#------------------------------------------------------------------------------
# CONEXIONES Y AUTENTICACIÓN
#------------------------------------------------------------------------------
...
listen_addresses = '*' # en qué dirección(es) IP se escuchará; ....
max_connections = 300 # (el cambio requiere reinicio)
....
shared_buffers = 512 MB # mín. 128 kB
```

#### Reiniciar Postgres

Reinicie el servicio PostgreSQL para habilitar estos cambios.

```bash
sudo systemctl restart postgresql-15
```

##### Ejecutar Postgres

Ejecute Postgres en la máquina que ejecuta el gestor de bases de datos.

<div style="padding-left: 20px;">

```bash
# Comando del SO (el número de puerto puede ser diferente de 5432, 5433)
sudo su postgres
psql -p 5432 postgres -U postgres
password user postgres: PostgreSQLPassword (NewPassw0rd) # Ingresar la contraseña de Postgres
```

</div>

##### CREAR ROL

Crear el rol idempiere y cualquier otro si es necesario.

<div style="padding-left: 20px;">

```sql
-- Comandos de Postgres:
CREATE ROL adempiere CONTRASEÑA DE INICIO DE SESIÓN DE SUPERUSUARIO 'adempiere';
CREATE ROL luisamesty CONTRASEÑA DE INICIO DE SESIÓN DE SUPERUSUARIO 'NNNNNNN'; ````
```

</div>
##### CREAR Base de Datos: idempiereSeed12

Crear la entidad de base de datos (idempiereSeed12). Se crea vacía.

<div style="padding-left: 20px;">
DESCARGAR SOLO SI ES NECESARIO

```sql
-- Comandos de Postgres:
-- Estos dos primeros comandos desconectan todos los procesos de la base de datos:
SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'idempiereSeed12' AND pid <> pg_backend_pid();

SELECT pg_terminate_backend(pid) FROM pg_stat_get_activity(NULL::integer)
WHERE datid = (SELECT oid FROM pg_database WHERE datname = 'idempiereSeed12');

-- DROP Database
DROP DATABASE "idempiereSeed11";

-- CREATE Database:
CREATE DATABASE "idempiereSeed11"
    WITH 
    OWNER = adempiere
    ENCODING = 'UTF8'
   TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

ALTER DATABASE "idempiereSeed11"
    SET search_path TO adempiere;
```

</div>

##### Cambiar nombre base de datos Postgres

<div style="padding-left: 20px;">
Cambiar nombre solo si es necesario para mantener una copia de seguridad

```sql
-- Comandos Postgres:
Modificar base de datos "idempiereSeed12" Cambiar nombre a "idempiereSeed12_Backup"
```

</div>

##### SALIR de Postgres

<div style="padding-left: 20px;">

```sql
-- Comandos de Postgres:
\q
```

</div>

##### IMPORTAR LA BASE DE DATOS idempiereSeed12 DESDE Adempiere_pg.jar

* Ubicar en: sources/iDempiere12/idempiere/org.adempiere.server-feature/data/seed
* Descomprimir Adempiere_pg.jar de /home/Adempiere_pg.jar a Adempiere_pg.dmp

<div style="padding-left: 20px;">

```bash
cd /home
ls -l
total 53488
-rw-r--r--. 1 root root 44945504 21 mar 10:05 Adempiere_pg.dmp
-rw-r--r--. 1 root root 9818290 21 mar 09:08 Adempiere_pg.jar
```
EJECUTAR COMANDO DE POSTGRES
```bash
psql -d idempiereSeed12 -f Adempiere_pg.dmp
contraseña usuario adempiere: adempiere
```

</div>

#### BASE DE DATOS CREADA

VERIFICAR que la base de datos se haya creado con pgadmin4 o cualquier otro cliente PostgreSQL.
Esquema: adempiere debe crearse con 899 tablas, 160 vistas y 70 funciones.

</div>

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

### <a name="step3"></a>⭐️3-Instalar Java OpenJDK17

#### MÁQUINA IDEMPIERE

```bash
Instalar en la máquina que ejecuta Idempiere; requiere Java.
ssh root@maquina-idempiere.com -p 22
```

#### Instalar JAVA OpenJDK17

```bash
ssh root@maquina-idempiere.com -p 22
yum install java-17-openjdk
```

#### Verificar Java

```bash
java -version
Versión de OpenJDK "17.0.6" 2023-01-17 LTS
Entorno de ejecución de OpenJDK (Red_Hat-17.0.6.0.10-3.el9) (compilación 17.0.6+10-LTS)
Máquina virtual de servidor OpenJDK de 64 bits (Red_Hat-17.0.6.0.10-3.el9) (compilación 17.0.6+10-LTS, modo mixto) Compartir)
```

#### Instalar para evitar java.lang.reflect.InvocationTargetException

```bash
yum install fontconfig
Agente Droplet DigitalOcean 35 kB/s | 3.3 kB 00:00
El paquete fontconfig-2.14.0-2.el9.x86_64 ya está instalado.
Dependencias resueltas.
No hay nada que hacer.
¡Listo!
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

### <a name="step4"></a>⭐️4-Descargar instaladores

#### MÁQUINA IDEMPIERE

Instalar en la máquina Idempiere y requiere Java.

```bash
 ssh root@maquina-idempiere.com -p 22
```

##### Instaladores de GET IDEMPIERE 12

##### Instalar wget

```bash
sudo yum install wget
```

##### Verificar la versión de wget ejecutando:

```bash
sudo wget --version
```

##### DESCARGAR INSTALADORES

```bash
$ cd /home/
$ wget https://sourceforge.net/projects/idempiere/files/v12/daily-server/idempiereServer12Daily.gtk.linux.x86_64.zip
...
$ wget https://sourceforge.net/projects/idempiere/files/v12/daily-server/idempiereServer12Daily.gtk.linux.x86_64.zip.MD5
```

...

##### VERIFICAR

```bash
$ md5sum -c idempiereServer12Daily.gtk.linux.x86_64.zip.MD5
idempiereServer12Daily.gtk.linux.x86_64.zip: OK
```

<a href="#top">Volver arriba</a>

### <a name="step5"></a>⭐️5-Instalar desde los instaladores

#### MÁQUINA IDEMPIERE

```bash
Instalar en la máquina Idempiere y requiere Java. ssh root@maquina-idempiere.com -p 22
```
##### INSTALAR IDEMPIERE 12 DESDE Instaladores
##### CREAR USUARIO idempiere

```bash
adduser idempiere
```
##### AÑADIR CONTRASEÑA DE USUARIO

```bash
passwd idempiere
```

##### INSTALAR descomprimir

```bash
yum install descomprimir
```

##### DESCOMPRIMIR INSTALADORES

##### Descomprimir idempiereServer12Daily.gtk.linux.x86_64.zip

```bash
unzip idempiereServer12Daily.gtk.linux.x86_64.zip
```

##### MOVER INSTALADORES a /opt

```bash
mv idempiere.gtk.linux.x86_64/idempiere-server /opt
rmdir idempiere.gtk.linux.x86_64
chown -R idempiere:idempiere /opt/idempiere-server
```

##### CONFIGURAR IDEMPIERE

```bash
su - idempiere ##### No es necesario si ya eres el usuario idempiere.
cd /opt/idempiere-server
```
##### Ejecutar shell

```bash
sh console-setup-alt.sh
```

##### Responder preguntas:

```bash
Inicio de Java [/usr/lib/jvm/java-17-openjdk-17.0.6.0.10-3.el9.x86_64]: Intro Aceptar
Opciones de Java [-Xms64M -Xmx512M]: Intro Aceptar
Inicio de iDempiere [/opt/idempiere-server]: Intro Aceptar
Contraseña del almacén de claves [myPassword]: Intro Aceptar

10:34:23.196 KeyStoreMgt.<init>: /opt/idempiere-server/jettyhome/etc/keystore [1]
Configuración del almacén de claves.
(ON) Nombre común [idempiere]: Enter OK

(OU) Unidad organizativa [iDempiereUser]:

(O) Organización [idempiere]:

(L) Localidad/Ciudad [MyTown]:

(S) Estado []:

(C) País (2 caracteres) [US]

10:34:38.997 ConfigurationData.testAdempiere: OK: AdempiereHome = /opt/idempiere-server [1]
10:34:38.998 KeyStoreMgt.<init>: /opt/idempiere-server/jettyhome/etc/keystore [1]
10:34:38.999 KeyStoreMgt.createCertificate: [1]
10:34:40.289 ConfigurationData.testAdempiere: OK: KeyStore = /opt/idempiere-server/jettyhome/etc/keystore [1]
Nombre del host del servidor de aplicaciones [0.0.0.0]: Ingrese OK

Puerto web del servidor de aplicaciones [8080]: (Cámbielo solo si lo desea) Ingrese OK
Puerto SSL del servidor de aplicaciones [8443]:

¿La base de datos ya existe? (S/N) [N]: S Ingrese
1. Oracle
2. PostgreSQL
Tipo de base de datos [2] Ingrese (PostgreSQL)
Nombre del host del servidor de bases de datos [localhost]: Ingrese la IP del servidor PostgreSQL (10.XXX.XXX.XXX) o (maquina-postgresql.com)
Puerto del servidor de bases de datos [5432]: Ingrese
Nombre de la base de datos [idempiere]: idempiereSeed12. (Nombre de la base de datos)
Usuario de la base de datos [adempiere]: Ingrese
Contraseña de la base de datos [adempiere]: Ingrese
Contraseña del usuario del sistema de bases de datos []. (Ingrese la contraseña del usuario de PostgreSQL: #PosgreSQLPASS#)
Nombre del host del servidor de correo [localhost]: Ingresar (se configurará más adelante)
Inicio de sesión del usuario de correo []: Ingresar (se configurará más adelante)
Contraseña del usuario de correo []
Correo electrónico del administrador []:
10:43:04.150 ConfigurationData.testMail: OK: Correo electrónico no configurado [1]
Guardar cambios (S/N) [S]: Ingresar

Los cambios se actualizarán en idempiere.properties e idempiereEnv.properties.
```

##### ACTUALIZAR BASE DE DATOS idempiereSeed12
Para mantener la base de datos sincronizada con el código, es necesario ejecutar el siguiente script:

```bash
su - idempiere # No es necesario si ya eres el usuario idempiere
cd /opt/idempiere-server/utils
sh RUN_SyncDB.sh
```

##### ACTUALIZAR BASE DE DATOS idempiereSeed12
##### Registrar el código de versión en la base de datos

```bash
Para firmar la base de datos con el código de versión ejecutándose en el servidor, se recomienda (o es obligatorio según la configuración)
ejecutar el siguiente script:
su - idempiere # No es necesario si ya eres el usuario idempiere
cd /opt/idempiere-server
sh sign-database-build-alt.sh
```

<a href="#top">Volver arriba</a>

### <a name="step6"></a>⭐️6- Ejecutando idempiere12

##### MÁQUINA IDEMPIERE

Se instala en la máquina Idempiere y requiere Java. 

```bash
ssh root@maquina-idempiere.com -p 22
```

##### EJECUTAR IDEMPIERE 12 DESDE Instaladores

##### Ejecución manual
Una vez instalado y configurado el servidor iDempiere, puede iniciarlo con:

```bash
su - idempiere # no es necesario si ya es usuario idempiere
cd /opt/idempiere-server
sh idempiere-server.sh
# o
idempiere
# o
nohup sh idempiere-server.sh >> idempiere-server.log 2>&1 &
```

##### Instalar como servicio
iDempiere se puede registrar como servicio en Linux. Para ello, puede copiar los scripts proporcionados a la carpeta /etc/rc.d/init.d de la siguiente manera:

```bash
sudo su - # esto debe Ejecutar como root
cp /opt/idempiere-server/utils/unix/idempiere_RedHat.sh /etc/rc.d/init.d/idempiere
systemctl daemon-reload
```

##### Ejecutar Idempiere como servicio

Una vez registrado iDempiere como servicio, se iniciará automáticamente al reiniciar el servidor. También se puede iniciar, detener, reiniciar y comprobar como de costumbre con:

```bash
# Ejecutar como root
systemctl status idempiere # para comprobar el estado de la aplicación
systemctl restart idempiere # para reiniciar la aplicación iDempiere
systemctl stop idempiere # para detener la aplicación iDempiere
systemctl start idempiere # para iniciar la aplicación iDempiere al detenerse
```

<a href="#top">Volver arriba</a>


