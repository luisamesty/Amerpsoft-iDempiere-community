&lArr; [Install idempiere11 from Installers ](./Install_IDEMPIERE11_From_Installers.md)  | [Installing CentOS](./README_installCentOS.md)  

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
