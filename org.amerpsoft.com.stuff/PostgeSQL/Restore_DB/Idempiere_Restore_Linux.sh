#!/bin/bash
#
# Idempiere_Restore_Linux.sh
#=====================================================================
# Set the following variables to your system needs
# (Detailed instructions below variables)
#=====================================================================
# Username to access the PostgreSQL server e.g. dbuser
USERNAME=postgres
USERNAMEID=adempiere
# Password
# create a file $HOME/.pgpass containing a line like this
#   hostname:*:*:dbuser:dbpass
# replace hostname with the value of DBHOST and postgres with 
# the value of USERNAME
PGPASSFILE=/root/.pgpass
# PostgreSQL Home Binaries
# FOR MAC OS
#PGHOME=/Library/PostgreSQL/10/bin/
# FOR LINUX PostgreSQL 10
PGHOME=/usr/lib/postgresql/10/bin/
# PATH
PATH=$PGHOME:$PATH
#PGDROP ="/Library/PostgreSQL/10/bin/dropdb"
#PGSQL  ="/Library/PostgreSQL/10/bin/psql"
#PGDUMP ="/Library/PostgreSQL/10/bin/pg_dump"
# Host name (or IP address) of PostgreSQL server e.g localhost
DBHOST=localhost
# Postgresql Port usually 5432 or 5433
PORT=5432
# DBNAME to be restored
DBNAME="idempiereTAM7.1pro"
# DBNAMEBAK Temporary Database name to be assigned before delete it
DBNAMEBAK="${DBNAME}_bak"
# Restore directory location e.g /backups/
# Ends with '/'
RESTOREDIR="/private-backup/BackupPG/TAM71/"
# RESTOREFILE Restore File on pgsql extension 
RESTOREFILE="idempiereTAM7.1pro.pgsql"
# HOST Variable
if [ "$DBHOST" = "localhost" ]; then
	HOST="-h $DBHOST -p $PORT"
else
	HOST="-h $DBHOST -p $PORT"
fi
# START 
echo "============================================"
echo "SCRIPT:Idempiere_Restore.sh  "
echo "idempiere Database Import	"
echo "DATABASE: $DBNAME"
echo "============================================"
echo "Stopping idempiere service ....."
sudo service idempiere stop
echo "Service for idempiere is Stopped! "
echo "1 - VERIFY IF DATABASE $DBNAME EXISTS "
# 
#if /Library/PostgreSQL/10/bin/psql -U postgres -d postgres -p 5433 -lqt | cut -d \| -f 1 | grep -qw "$DBNAME";then
if psql -U $USERNAME $HOST -d postgres -p 5432 -lqt | cut -d \| -f 1 | grep -qw "$DBNAME";then
    echo "****** WARNING ******************************"
    echo "  $DBNAME WILL BE RENAMED TO $DBNAMEBAK ........"
    echo "****** WARNING ******************************"
    DBEXIST=1
else 
    DBEXIST=0
fi
# RENAME  
# COMMAND ALTER DATABASE db RENAME TO newdb;
# FOR RENAME connect as user postgres
echo "=============================================================="
echo "2 - RENAMING OLD DATABASE $DBNAME  IF EXISTS"
echo "El Valor de DBEXIST es = $DBEXIST"
#if ["$DBEXIST" == 1 ]; then
if psql -U $USERNAME $HOST -d postgres -p 5432 -lqt | cut -d \| -f 1 | grep -qw "$DBNAME";then
	# RENAME DATA BASE
	echo "DATABASE $DBNAME Renamed to $DBNAMEBAK"
	ADEMPIERE_DISCONNECT="SELECT pg_terminate_backend(pid ) FROM pg_stat_activity WHERE pid <> pg_backend_pid( ) AND datname = '$DBNAME' "
	echo "Disconnect Command:"
	echo $ADEMPIERE_DISCONNECT
	#/Library/PostgreSQL/10/bin/psql -U $USERNAME $HOST -d postgres -c "$ADEMPIERE_DISCONNECT"
	psql -U $USERNAME $HOST -d postgres -c "$ADEMPIERE_DISCONNECT"
	#ADEMPIERE_ALTER_DB_SQL="ALTER DATABASE ${DBNAME} RENAME TO ${DBNAMEBAK} "
	ADEMPIERE_ALTER_DB_SQL="ALTER DATABASE \"$DBNAME\" RENAME TO \"$DBNAMEBAK\" "
	echo "Alter Command:"
	echo $ADEMPIERE_ALTER_DB_SQL
	#/Library/PostgreSQL/10/bin/psql -U $USERNAME $HOST -d postgres -c "$ADEMPIERE_ALTER_DB_SQL"
	psql -U $USERNAME $HOST -d postgres -c "$ADEMPIERE_ALTER_DB_SQL"
else
    echo "Database  $DBNAME  NOT EXISTS ........"
    echo "****** WARNING ******************************"
fi

#
echo "=============================================================="
echo "3 - CREATE NEW DATABASE"
echo " Creating a New Database $DBNAME ......"
#/Library/PostgreSQL/10/bin/createdb -U $USERNAMEID $HOST -T template1 -E UNICODE $DBNAME
createdb -U $USERNAMEID $HOST -T template1 -E UNICODE $DBNAME
echo " Role assign ......"
ADEMPIERE_ALTER_DB_SQL="ALTER DATABASE \"$DBNAME\" SET search_path TO adempiere"
echo $ADEMPIERE_ALTER_DB_SQL
#/Library/PostgreSQL/10/bin/psql -U $USERNAME $HOST -d $DBNAME -c "$ADEMPIERE_ALTER_DB_SQL"
psql -U $USERNAME $HOST -d $DBNAME -c "$ADEMPIERE_ALTER_DB_SQL"
echo " A New Database $DBNAME has been created ......"
echo "=============================================================="
echo "4 - RESTORE DATABASE"
echo " Restoring Database :  $DBNAME"
RESTOREFILE="${RESTOREDIR}${RESTOREFILE}"
echo "          From File :  $RESTOREFILE"
echo " .......... WAIT ........."
#/Library/PostgreSQL/10/bin/psql -U $USERNAME $HOST -d $DBNAME -f $RESTOREFILE
psql -U $USERNAME $HOST -d $DBNAME -f $RESTOREFILE
echo " Database $DBNAME has been restored ......"

# Final Delete
echo "=============================================================="
echo "5 - DELETE OLD DATABASE"
echo " Deleting OLD Database Backup $DBNAMEBAK IF EXISTS......"
echo "$DBEXIST"
#if [ "$DBEXIST" == 1 ]; then
if psql -U $USERNAME $HOST -d postgres -p 5432 -lqt | cut -d \| -f 1 | grep -qw "$DBNAMEBAK";then

	echo "DATABASE $DBNAMEBAK to be disconnected"
        ADEMPIERE_DISCONNECT="SELECT pg_terminate_backend(pid ) FROM pg_stat_activity WHERE pid <> pg_backend_pid( ) AND datname = '$DBNAMEBAK' "
        echo "Disconnect Command:"
        echo $ADEMPIERE_DISCONNECT
        #/Library/PostgreSQL/10/bin/psql -U $USERNAME $HOST -d postgres -c "$ADEMPIERE_DISCONNECT"
        psql -U $USERNAME $HOST -d postgres -c "$ADEMPIERE_DISCONNECT"

	# DROP DATA BASE
	#/Library/PostgreSQL/10/bin/dropdb -U $USERNAME $HOST $DBNAMEBAK
	dropdb -U $USERNAME $HOST $DBNAMEBAK
	echo "OLD Database $DBNAMEBAK has been deleted ....."
else
	echo "OLD Database $DBNAMEBAK has been verify that does not exist ....."
fi


echo "Starting idempiere service ....."
sudo service idempiere restart
echo "Service for idempiere is Started! "
