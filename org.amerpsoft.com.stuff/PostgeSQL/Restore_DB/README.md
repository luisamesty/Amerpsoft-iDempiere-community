# Idempiere_Restore

## <b>Description</b>

<pre>

<b>Idempiere_Restore</b> contains a File Script on <b>bin-bash</b> that provides a way to automatic restore an Idempiere Database from a Backup done on another PostgreSQL instance. 
Very useful for replicating an idempiere server each time that user may program on a cron scheduled process.
This procedure is for Ubuntu Linux, specifically tested on UBUNTU 18.04 version.
</pre>

[Return to Amerpsoft Stuff Home](https://github.com/luisamesty/Amerpsoft-stuff/blob/master/README.md)
## <b>Basic concepts</b>
<pre>
<pre>
<h3><b>Backup File types:</b></h3>
Backup files can be plain .sql or .pgsql
Also .gz (gunzip) or .bz2 
</pre>
<pre>
<h3><b>Database Name definitions:</b></h3>
<b>Name convention as</b>
idempiere<i>[Company]</i>7.1pro_<i>[DateTime]</i>.<i>[extension]</i>
Examples:
idempiereTAM7.1pro_2020_07_21_1215.pgsql.gz
idempiereMCC7.1pro_2020_07_20_0400.pgsql.bz2
idempiereSeed7.1.pgsql
</pre>
<pre>
<h3><b>Directories:</b></h3>
As a recommendation restore procedure should be done by root user, in such a way scripft file must be placed on <b>'/root'</b> directory.
Also <b>.pgpass</b> file should be placed on same directory, becuase it has to be available for user that perform postgreSQL task. Also 'rw-------' (chmod 600) is mandatory.
</pre>
</pre>


## <b>Install procedures</b>
<pre>
<b>1 - Create restore directory
2 - Copy Idempiere_Restore.sh file
3 - Configure Idempiere_Restore.sh file
4 - Configure .pgpass file
5 - Contab configuration</b>

<b>1 - Create restore directory</b>
<pre>
Create a directory like:
/home/luisamesty/Idempiere_Restore
It must be writable to main user and root user.
This directory will contain last backup file copied from another server.
</pre>

<b>2 - Copy Idempiere_Restore.sh file</b>
<pre>
Copy predefined Idempiere_restore.sh file 
to the root home directory:
/root/Idempiere_Restore
It must be executable to main user and root user.
</pre>

<b>3 - Configure Idempiere_Restore.sh file</b>
<pre>
<b>Verify Global Variables</b>
# Host name (or IP address) of PostgreSQL server e.g localhost
<b>DBHOST</b>=localhost
# Postgresql Port usually 5432 or 5433
<b>PORT</b>=5433
# DBNAME to be restored
<b>DBNAME</b>="idempiereMCC7.1pro"
# DBNAMEBAK Temporary Database name to be assigned before delete it
<b>DBNAMEBAK</b>="${DBNAME}_bak"
# Restore directory location e.g /backups/
# Ends with '/'
<b>RESTOREDIR</b>="/home/luisamesty/Idempiere_Restore/"
</pre>

<b> 4 - Configure .pgpass file</b>
<pre>
Create a file $HOME/.pgpass containing a line like this:
   hostname:*:*:dbuser:dbpass
Replace hostname with the value of DBHOST and postgres with the value of USERNAME
PGPASSFILE=/root/.pgpass
Sample:
   *:*:*:postgres:XXXX@NNNN
   *:*:*:adempiere:adempiere
</pre>
<b> 5 - Contab configuration </b>
<pre>
<b>Create a crontab with command:</b> 

\# crontab –e

Edit File and add:

0 4 * * * /lib64/home/BackupPG/backup.sh

This will perform a Backup all days at 4:00 AM
</pre>
</pre>