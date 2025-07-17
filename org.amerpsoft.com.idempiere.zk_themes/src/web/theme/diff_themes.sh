#!/bin/bash

DIR1="/home/luisamesty/sources/iDempiere12/Amerpsoft-iDempiere-community/org.amerpsoft.com.idempiere.zk_themes/src/web/theme/iceblue_c"
DIR2="/home/luisamesty/sources/iDempiere12/Amerpsoft-iDempiere-community/org.amerpsoft.com.idempiere.zk_themes/src/web/theme/iceblue_mo"

echo "Comparando $DIR1 con $DIR2 ..."
echo ""

diff -qr "$DIR1" "$DIR2" | while read -r line; do
  echo "$line"
done

