&lArr; [Config PostgreSQL 15 ](./ConfigPostgreSQL.md)| [Installing CentOS](./README_installCentOS.md)  | [Download idempiere11 Installers ](./Download_idempiere11_Installers.md)&rArr;
# MAQUINA IDEMPIERE
````
Instalar en la maquina que ejecuta Idempiere y requiere de Java.
ssh root@maquina-idempiere.com -p 22
````

# Install JAVA OpenJDK17

````
ssh root@maquina-idempiere.com -p 22
yum install java-17-openjdk
````

# Verify Java 
````
java -version
openjdk version "17.0.6" 2023-01-17 LTS
OpenJDK Runtime Environment (Red_Hat-17.0.6.0.10-3.el9) (build 17.0.6+10-LTS)
OpenJDK 64-Bit Server VM (Red_Hat-17.0.6.0.10-3.el9) (build 17.0.6+10-LTS, mixed mode, sharing)
````
#Install to avoid java.lang.reflect.InvocationTargetException
````
yum install fontconfig
DigitalOcean Droplet Agent                                                                                                                            35 kB/s | 3.3 kB     00:00
Package fontconfig-2.14.0-2.el9.x86_64 is already installed.
Dependencies resolved.
Nothing to do.
Complete!
````


