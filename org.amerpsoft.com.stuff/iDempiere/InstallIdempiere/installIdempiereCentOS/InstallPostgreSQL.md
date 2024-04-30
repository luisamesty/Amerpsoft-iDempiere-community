
&lArr;[Installing CentOS](./README_installCentOS.md) | [Installing CentOS](./README_installCentOS.md) | [Config PostgreSQL 15 ](./ConfigPostgreSQL.md) &rArr;

#### DATABASE MACHINE
````
Install in the machine that it is executing DDBB.
$ ssh root@maquina-postgresql.com -p 22
````

#### Install the repository RPM:
````
sudo yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
````

#### Install PostgreSQL:
````
sudo yum install -y postgresql15-server
````

#### Optionally initialize the database and enable automatic start:
````
sudo /usr/pgsql-15/bin/postgresql-15-setup initdb
sudo systemctl enable postgresql-15
sudo systemctl start postgresql-15
dnf install postgresql15-contrib
````
