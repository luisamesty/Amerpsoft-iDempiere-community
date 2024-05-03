# Backup_auto

## <b>Description</b>
<pre>
Backup_auto contains install procedure to perform Daily Backups with additional Week and Month recording.
</pre>

[Return to PostgreSQL](../PostgreSQL.md)
## <b>Install procedures</b>

<pre>
- Create File autopgbackup.sh
- PREBACKUP and POSTBACKUP
- Create .pgpass file
- Contab configuration
</pre>

### <b> File autopgbackup.sh</b>

<pre>
Create file on a direcctory like: ="/home/BackupPG/autopgbackup.sh”
(SEE Attached file)
MODIFY VARIABLES:
USERNAME=postgres
PGPASSFILE=/root/.pgpass
DBHOST=localhost
DBNAMES="idempiereMCC7.1pro"
BACKUPDIR="/private-backup"
</pre>
### <b> Create .pgpass file </b>

<pre>
Create a file backup.sh as indicated on a sample.
*:*:*:postgres:XXXX@NNNN
*:*:*:adempiere:adempiere
</pre>

### <b> PREBACKUP and POSTBACKUP </b>

<pre>
Configure this varibles if needed
#PREBACKUP="/etc/pgsql-backup-pre"
# Command run after backups (uncomment to use)
#POSTBACKUP="bash /home/backups/scripts/ftp_pgsql"
<b>As a sample:</b>
POSTBACKUP="scp -P 897 /private-backup/last/idempiereMCC7.1pro/idempiereMCC7.1pro.sql luisamesty@luisamesty.sytes.net:/home/luisamesty/Idempiere_Restore/idempiereMCC7.1pro.pgsql"
This Command will copy last backup to specific directory on <b>"luisamesty.sytes.net"</b> server
Remember generate ssh keys.
</pre>


### <b> Crontab config </b>

<pre>
Create a crontab with command: 

\# crontab –e

Edit File and add:

0 4 * * * /lib64/home/BackupPG/backup.sh

This will perform a Backup all days at 4:00 AM
</pre>