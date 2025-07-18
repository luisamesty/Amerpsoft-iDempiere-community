#!/bin/bash

DIR1="/home/luisamesty/sources/iDempiere12/Amerpsoft-iDempiere-community/org.amerpsoft.com.idempiere.zk_themes/src/web/theme/iceblue_c"
DIR2="/home/luisamesty/sources/iDempiere12/Amerpsoft-iDempiere-community/org.amerpsoft.com.idempiere.zk_themes/src/web/theme/my_iceblue_c"

echo "Preparando para limpiar $DIR2, dejando solo los archivos modificados o nuevos..."
echo "----------------------------------------------------------------"

# Find files that are identical in both directories
# -q: Quiet, just report if files differ
# -s: Report when files are identical
# -r: Recursive
# | while read -r line: Process each line of diff output
# grep "son idénticos": Filter for lines indicating identical files (in Spanish locale)
# awk '{print $NF}': Get the last field (the filename)
# sed 's/^.\///': Remove the leading './' from the filename (relative path)
# xargs -I {} rm "$DIR2/{}" : For each identical file, remove it from DIR2

diff -qsr "$DIR1" "$DIR2" | grep "son idénticos" | while read -r line; do
    # Extract the relative path of the identical file
    relative_path=$(echo "$line" | sed -E 's/Files \S+\/(.*) and \S+\/(.*) are identical/\2/') # More robust extraction

    if [ -n "$relative_path" ]; then # Ensure path is not empty
        file_to_delete="$DIR2/$relative_path"
        if [ -f "$file_to_delete" ]; then
            echo "Borrando archivo idéntico: $file_to_delete"
            rm "$file_to_delete"
        elif [ -d "$file_to_delete" ]; then
            # If it's a directory, we need to handle it carefully.
            # For now, this script primarily targets files.
            # If you want to remove identical empty directories, you'd need rmdir after files are gone.
            # For this scenario, it's safer to only remove files.
            echo "Skipping directory (or file within it) $file_to_delete - manual check might be needed."
        else
            echo "Advertencia: No se encontró el archivo $file_to_delete aunque diff lo reportó como idéntico."
        fi
    fi
done

# Clean up empty directories in DIR2 after files have been removed
echo ""
echo "Limpiando directorios vacíos en $DIR2..."
find "$DIR2" -depth -type d -empty -delete
echo "Proceso completado."
