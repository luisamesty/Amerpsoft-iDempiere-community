&lArr; [Install JAVA OpenJDK17 ](./Install_JAVA_OpenJDK17.md) | [Installing CentOS](./README_installCentOS.md)  | [Install idempiere11 from Installers ](./Install_IDEMPIERE11_From_Installers.md) &rArr;

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

#####  DOWNLOAD INSTALLERS

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
