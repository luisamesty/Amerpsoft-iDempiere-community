&lArr;[Install PostgreSQL 15](./InstallPostgreSQL.md) | | [Installing CentOS](./README_installCentOS.md) | [Install JAVA OpenJDK17 ](./Install_JAVA_OpenJDK17.md) &rArr;

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
password user postgres: PostgreSQLPassword 	# Enter postgres password
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
