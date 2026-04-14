&lArr;[Instalación de Idempiere](../../README_ES.md)
<a name="readme-top"></a>

<div style="text-align: right;">

🇬🇧 [English Version](../README_installUbuntu2404.md) | 🇪🇸 Español

</div>

## Instalar Idempiere en Ubuntu 24.04

Procedimientos de instalación para Ubuntu 24.04 con base de datos PosgreSQL.

Este procedimiento se aplica a una instalación en dos equipos diferentes.

- Equipo de la base de datos
- Equipo host de Idempiere

Este es el método recomendado, pero si tiene un solo equipo, todos los procedimientos deben realizarse en el mismo.

Este procedimiento incluye información extraída de [Instalación de iDempiere](https://wiki.idempiere.org/en/Installing_iDempiere). Página wiki cortesía de [Carlos Ruiz](https://wiki.idempiere.org/en/User:CarlosRuiz) de [BX Service](https://www.bx-service.com/).

| Pasos | Enlace | Dónde | Comentarios |
| ----: | ---------------------------------------------- | ----------------- | --------------------------------------------------------------- |
| 1 | [Instalar PostgreSQL 17](#step1) | Máquina de base de datos | Instalar PostgreSQL desde la distribución de Ubuntu. Actualizar el repositorio. |
| 2 | [Configurar PostgreSQL 17](#step2) | Máquina de base de datos | Editar los archivos pg_hba.conf / postgresql.conf |
| 3 | [Instalar JAVA OpenJDK17](#step3) | Máquina de Idempiere | Descargar e instalar Java 17 |
| 4 | [Descargar instaladores](#step4) | Máquina de Idempiere | Descargar instaladores del repositorio de iDempiere |
| 5 | [Instalar idempiere12 desde instaladores](#step5) | Máquina de Idempiere | Descomprimir y copiar los instaladores en el directorio /opt |
| 6 | [Ejecutar idempiere12](#step6) | Máquina de Idempiere | Ejecutar | Configurar ejecución automática de idempiere |

### <a name="step1"></a>1-⭐️Instalar PostgreSQL 17

#### **MÁQUINA DE BASE DE DATOS**

Instalar en la máquina donde se ejecuta DDBB. 

```bash
ssh root@maquina-postgresql.com -p 22
```

#### Instalar el repositorio:

Para usar el repositorio apt, siga estos pasos:

```bash
# Crear la configuración del repositorio de archivos:
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'
# Importar la clave de firma del repositorio:
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
# Actualizar las listas de paquetes:
sudo apt-get update
```

#### Instalar PostgreSQL

```bash
# Instalar la versión 17 de PostgreSQL.
sudo apt-get -y install postgresql-17
# o
# Para la versión 16
sudo apt-get -y install postgresql
```

#### Opcionalmente, inicialice la base de datos y habilite el inicio automático:

```bash
sudo systemctl enable postgresql-17
sudo systemctl start postgresql-17
````

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

### <a name="step2"></a>2-⭐️Configurar PostgreSQL 17

#### Modificar el archivo ***ph_hba.conf***

Después de instalar PostgreSQL, debe verificar la configuración correcta de:

*/etc/postgresql/17/main/pg_hba.conf*

La siguiente línea requiere cambiar el método de autenticación:

```bash
local all all Par
```

CAMBIAR A:

```bash
local all all scram-sha-256
```

Añadir conexión remota a tu IP

```bash
# Conexiones locales IPv4:
host all all 127.0.0.1/32 scram-sha-256
host all all 83.49.112.218/32 scram-sha-256
```

#### Modificar el archivo postgresql.conf

Para desarrollo, abrir la dirección de escucha '*'; para producción, localhost.

Puerto 5432/5433.

Editar: '/etc/postgresql/17/main/postgresql.conf'

```text
#------------------------------------------------------------------------------
# CONEXIONES Y AUTENTICACIÓN
#------------------------------------------------------------------------------
# - Configuración de la conexión -
listen_addresses = '*' # en qué dirección(es) IP se escuchará; # Lista de direcciones separadas por comas; # El valor predeterminado es 'localhost'; usar '*' para todos
# (el cambio requiere reiniciar)
puerto = 5432 # (el cambio requiere reiniciar)
máx_conexiones = 100 # (el cambio requiere reiniciar)
#superuser_reserved_connections = 3 # (el cambio requiere reiniciar)
```

#### Cambiar contraseña de administrador

```bash
# Máquina:
usuario@linux:$ sudo su postgres
postgres@linux:/root/$ psql -U postgres
# Comandos de PostgreSQL:
Postgres-# ALTER USER postgres PASSWORD 'your_chosen_password';
```

#### Actualización del firewall (opcional)

Si hay un firewall instalado, actualice el puerto.

```bash
sudo ufw allow 5432
sudo ufw allow 5433
```

#### Crear usuarios

Usuario: adempiere y cualquier otro requerido

Ejecutar Postgres en Linux, puerto 5432 - 5433 o 5434

```bash
sudo -u postgres psql -p 5432 template1
```

En las líneas de comando de PostgreSQL:

```sql
CREAR ROL adempiere CONTRASEÑA DE INICIO DE SESIÓN DE SUPERUSUARIO 'adempiere';
CREAR ROL luisamesty CONTRASEÑA DE INICIO DE SESIÓN DE SUPERUSUARIO '5167830';
\q
```

Y luego recarga la configuración en Linux:

```bash
sudo service postgresql reload
```

Crear la base de datos idempiereSeed12 y producción

Ejecutar Postgres en Linux, puerto 5432 - 5433 o 5434

```bash
sudo -u postgres psql -p 5432 template1
```

Consultas para eliminar conexiones, si es necesario al ejecutar clientes PostgreSQL.

```sql
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'database_name' AND pid <> pg_backend_pid(); SELECT pg_terminate_backend(pid)
FROM pg_stat_get_activity(NULL::integer)
WHERE datid = ( SELECT oid FROM pg_database WHERE datname = 'database_name');
```

En líneas de comandos de PostgreSQL:

```sql
-- Eliminar solo si es necesario
DROP DATABASE "idempiereSeed11";
CREATE DATABASE "idempiereSeed11"
  WITH OWNER = adempiere  ENCODING = 'UTF8' TABLESPACE = pg_default  CONNECTION LIMIT = -1;
ALTER DATABASE "idempiereSeed11"     SET search_path TO adempiere;
```

Restaurar desde la copia de seguridad de Seed o Producción, ejecutar en Linux:

```bash
# Adempiere_pg.dmp para Seed; para producción, usar la copia de seguridad dmp
# Adempiere_pg.jar en el directorio idempiere/org.adempiere.server-feature/data/seed/
# Descomprimir en dmp
jar -xvf Adempiere_pg.jar
# PostgreSQL en Linux Ubuntu, ubicado en /usr/lib/postgresql/17/bin
/usr/lib/postgresql/17/bin/psql -p 5432 -d idempiereSeed12 -f Adempiere_pg.dmp
```

#### SALIR de Postgres

En las líneas de comandos de PostgreSQL:

```sql
\q
```

<p align="left">(<a href="#readme-top">volver a arriba</a>)</p>


### <a name="step3"></a>3-⭐️Instalar JAVA OpenJDK17

#### Instalar Java OpenJDK17 en la máquina Idempiere.

OpenJDK tiene paquetes separados: JDK (Java Development Kit) para desarrollar aplicaciones Java y JRE (Java Runtime Environment) para ejecutar aplicaciones Java.

Primero, actualice el índice del repositorio en Linux.

```bash
sudo apt update
```

Luego, instale el paquete OpenJDK o JRE según sea necesario.
OpenJDK 17 JDK

```bash
sudo apt install -y openjdk-17-jdk
```

OpenJDK 17 JRE

```bash
sudo apt install -y openjdk-17-jre
```

#### Instalar Oracle JDK 17 en Ubuntu 24.04

Esto no es necesario para idempiere, solo es informativo.

Primero, instale las dependencias para la instalación de Oracle JDK 17 en Linux.

```bash
sudo apt update
sudo apt install -y libc6-x32 libc6-i386
```

Luego, descargue Oracle Java JDK 17 con el comando wget en la terminal.

```bash
wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.deb
```

Finalmente, instale Oracle Java JDK 17 con el comando dpkg.

```bash
sudo dpkg -i jdk-17_linux-x64_bin.deb
```

En algunos casos, es posible que necesite instalar Oracle JDK 17 en la ruta PATH.

```bash
sudo update-alternatives --config java
# o
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-17/bin/java 1
```

#### Verificar la instalación del JDK de Java

Después de instalar el JDK, use el siguiente comando para verificar la versión.

```bash
java -version
```

Resultado:

```text
java version "17.0.3" 2022-04-19 LTS
Java™ SE Runtime Environment (compilación 17.0.3+8-LTS-111)
Java HotSpot™ 64-Bit Server VM (compilación 17.0.3+8-LTS-111, modo mixto, compartido)
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

### <a name="step4"></a>4-⭐️Descargar instaladores

La versión 12 de iDempiere se mantiene y es estable. Se recomienda instalar esta versión para fines de implementación y producción.

Puede encontrar instaladores diarios para esta versión en:

https://sourceforge.net/projects/idempiere/files/v12/daily-server/

Descargue la versión más reciente con estos comandos en Linux:

```bash
wget https://sourceforge.net/projects/idempiere/files/v12/daily-server/idempiereServer12Daily.gtk.linux.x86_64.zip
wget https://sourceforge.net/projects/idempiere/files/v12/daily-server/idempiereServer12Daily.gtk.linux.x86_64.zip.MD5
md5sum -c idempiereServer12Daily.gtk.linux.x86_64.zip.MD5
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

### <a name="step5"></a>5-⭐️Instalar idempiere12 desde Instaladores


#### Crear usuario

Se recomienda ejecutar el servidor iDempiere con un usuario creado para tal fin, generalmente idempiere, en lugar de ejecutarlo como root.

EN LINUX:

```bash
adduser idempiere
```

Instalar servidor

Descomprima el instalador del servidor que descargó o creó, por ejemplo:

```bash
jar xvf idempiereServer12Daily.gtk.linux.x86_64.zip
```

Mueva la carpeta a /opt

```bash
mv idempiere.gtk.linux.x86_64/idempiere-server /opt
rmdir idempiere.gtk.linux.x86_64
chown -R idempiere:idempiere /opt/idempiere-server
```

De ahora en adelante, es preferible ejecutar todo como usuario idempiere:

```bash
su - idempiere # no es necesario si ya es usuario idempiere
cd /opt/idempiere-server
```

Gráfico. Se puede ejecutar en Linux:

```bash
sh setup.sh
```

![1714558912245](images/README_installUbuntu2404/1714558912245.png)

O Comando: ejecutar en Linux

```bash
sh console-setup-alt.sh
```

NOTA: Al ejecutar el comando, debe ingresar cada valor línea por línea. Opcionalmente, puede agregar el parámetro NIVEL DE REGISTRO (los valores aceptados son: DESACTIVADO, GRAVE, ADVERTENCIA, INFORMACIÓN, CONFIG, FINO, MÁS FINO, MÁXIMO, TODO). Por ejemplo, `sh setup-alt.sh`

Puede completar los parámetros como se muestra en la captura de pantalla o con sus propios valores. En particular, debe tener en cuenta lo siguiente:

```text
Inicio de Java: /usr/lib/jvm/jdk-17-oracle-x64
Opciones de Java: -Xms64M -Xmx512M
Inicio de iDempiere: Esta es la carpeta del repositorio (/opt/idempiere-server)
Nombre de host del servidor de aplicaciones [0.0.0.0]:
Puerto web del servidor de aplicaciones [8080]:
Puerto SSL del servidor de aplicaciones [8443]:
Puerto web/SSL: Tenga cuidado de no usar un puerto que ya esté usando otra aplicación. En Linux, los puertos inferiores a 1000 no pueden ser utilizados por usuarios que no sean root. Por ejemplo, el puerto 8080 lo usa Oracle-XE.
La base de datos ya existe: si la base de datos se creó durante la instalación de Postgres, marque esta casilla; de lo contrario, se creará más adelante.
Base de datos Nombre: Aquí se completa el nombre de la base de datos que creó (idempiereSeed12) o que desea crear posteriormente.
Puerto del servidor de la base de datos: Generalmente 5432.
Contraseña del administrador de la base de datos: Debe completarse con la contraseña de PostgreSQL que configuró durante la instalación de PostgreSQL.
Usuario de la base de datos: Este usuario se creará; se recomienda mantener el predeterminado "adempiere".
Contraseña de la base de datos: Complete aquí con la contraseña, generalmente "adempiere".
Correo electrónico: Puede configurarse posteriormente.
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

### <a name="step6"></a>6-⭐️Ejecución de idempiere12

#### Ejecución manual

Una vez instalado y configurado el servidor iDempiere, puede iniciarlo con:

```bash
su - idempiere # No es necesario si ya es usuario idempiere.
cd /opt/idempiere-server
sh idempiere-server.sh
```

o

```bash
idempiere
```

o

```bash
nohup sh idempiere-server.sh >> idempiere-server.log 2>&1 &
```

#### Instalación como servicio

iDempiere se puede registrar como servicio en Linux. Para ello, copie los scripts proporcionados en la carpeta /etc/init.d de la siguiente manera:

```bash
$ sudo su - # debe ejecutarse como root
cp /opt/idempiere-server/utils/unix/idempiere_Debian.sh /etc/init.d/idempiere
systemctl daemon-reload
update-rc.d idempiere defaults
```

Una vez registrado iDempiere como servicio, se iniciará automáticamente en el servidor. Reinicia. También se puede iniciar, detener, reiniciar o comprobar como siempre con:

```bash
# debe ejecutarse como root
systemctl status idempiere # para comprobar el estado de la aplicación
systemctl restart idempiere # para reiniciar la aplicación iDempiere
systemctl stop idempiere # para detener la aplicación iDempiere
systemctl start idempiere # para iniciar la aplicación iDempiere al detenerse
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

