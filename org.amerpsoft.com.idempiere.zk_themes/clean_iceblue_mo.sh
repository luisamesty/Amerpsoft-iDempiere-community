#!/bin/bash

# --- CONFIGURACIÓN DE RUTAS ---
# La base de tu proyecto de plugin Amerpsoft en el workspace de Eclipse.
HOME_PLUGIN_DIR="${HOME}/sources/iDempiere12/Amerpsoft-iDempiere-community/org.amerpsoft.com.idempiere.zk_themes"

# Ruta al tema 'iceblue_c' dentro de TU PLUGIN (tu clon local de iDempiere 12)
# ¡ESTA ES LA RUTA DE REFERENCIA PARA LA COMPARACIÓN!
DIR1="${HOME_PLUGIN_DIR}/src/web/theme/iceblue_c"

# Ruta a tu tema 'iceblue_mo' dentro de TU PLUGIN (el que quieres limpiar).
DIR2="${HOME_PLUGIN_DIR}/src/web/theme/iceblue_mo"

# Mensaje de "diff" para identificar archivos idénticos.
# Si tu sistema está en español, usa "son idénticos".
# Si tu sistema está en inglés, usa "are identical".
DIFF_IDENTICAL_MSG="son idénticos"
# --- FIN CONFIGURACIÓN ---

echo "----------------------------------------------------------------"
echo "  Herramienta de Limpieza y Gestión de Temas iDempiere"
echo "----------------------------------------------------------------"
echo "Directorio Base de Tu Plugin: ${HOME_PLUGIN_DIR}"
echo "Tema de Origen (Referencia - Tu Clon de iceblue_c): ${DIR1}"
echo "Tema a Limpiar (Tu Copia iceblue_mo):              ${DIR2}"
echo "----------------------------------------------------------------"
echo ""

# Verificar que los directorios existen
if [ ! -d "$DIR1" ]; then
    echo "ERROR: El directorio del tema de origen (DIR1) no existe: ${DIR1}"
    echo "Asegúrate de que tu clon de 'iceblue_c' esté en la ruta esperada dentro de tu plugin."
    exit 1
fi
if [ ! -d "$DIR2" ]; then
    echo "ERROR: El directorio del tema a limpiar (DIR2) no existe: ${DIR2}"
    echo "Asegúrate de que la subruta a 'iceblue_mo' es correcta."
    exit 1
fi

echo "Selecciona una opción:"
echo "  1) Ver solo los ficheros **DIFERENTES o ÚNICOS** (en '${DIR2}' respecto a '${DIR1}')"
echo "  2) **Borrar** los ficheros **IDÉNTICOS** (en '${DIR2}' respecto a '${DIR1}')"
echo "  q) Salir"
echo ""

read -p "Ingresa tu elección (1, 2 o q): " choice

case "$choice" in
    1)
        echo "----------------------------------------------------------------"
        echo "  Opción 1: Listando solo los ficheros DIFERENTES o ÚNICOS en '${DIR2}'"
        echo "  (Rutas relativas a tu plugin: org.amerpsoft.com.idempiere.zk_themes/)"
        echo "----------------------------------------------------------------"
        echo ""
        # The key is to strip the HOME_PLUGIN_DIR prefix from all paths reported by diff
        diff -qr "$DIR1" "$DIR2" | grep -v "$DIFF_IDENTICAL_MSG" | sed "s#${HOME_PLUGIN_DIR}/##g"
        echo ""
        echo "Fin de la lista de ficheros diferentes/únicos."
        ;;
    2)
        echo "----------------------------------------------------------------"
        echo "  Opción 2: Procediendo a borrar ficheros IDÉNTICOS en '${DIR2}'"
        echo "----------------------------------------------------------------"
        echo ""

        # ---- PASO CRÍTICO: HAZ UNA COPIA DE SEGURIDAD ANTES DE CONTINUAR ----
        echo "ADVERTENCIA: Se recomienda ENCARECIDAMENTE hacer una copia de seguridad de '${DIR2}' antes de continuar."
        echo "Puedes hacerlo ejecutando en otra terminal: cp -r \"${DIR2}\" \"${DIR2}_backup_$(date +%Y%m%d%H%M%S)\""
        read -p "¿Has hecho una copia de seguridad y deseas continuar? (s/n): " confirm
        if [[ ! "$confirm" =~ ^[Ss]$ ]]; then
            echo "Operación cancelada."
            exit 1
        fi
        echo ""
        # --------------------------------------------------------------------

        # Encuentra y borra archivos idénticos
        diff -qsr "$DIR1" "$DIR2" | grep "$DIFF_IDENTICAL_MSG" | while read -r line; do
            # Extract the relative path of the identical file within DIR2
            relative_path=$(echo "$line" | sed -E "s/^Files (.+) and (.+) ${DIFF_IDENTICAL_MSG}/\2/" | sed "s#$DIR2/##")

            if [ -n "$relative_path" ]; then
                file_to_delete="${DIR2}/${relative_path}"
                if [ -f "$file_to_delete" ]; then
                    echo "Borrando archivo idéntico: ${file_to_delete}"
                    rm "${file_to_delete}"
                else
                    echo "Advertencia: Se esperaba archivo '${file_to_delete}' pero no se encontró o no es un archivo regular."
                fi
            else
                echo "Advertencia: No se pudo extraer la ruta relativa de la línea: ${line}"
            fi
        done

        # Limpia directorios vacíos en DIR2 después de que los archivos han sido eliminados
        echo ""
        echo "Limpiando directorios vacíos en '${DIR2}'..."
        find "${DIR2}" -depth -type d -empty -delete
        echo "Proceso de limpieza completado para '${DIR2}'."
        ;;
    q)
        echo "Saliendo del script."
        ;;
    *)
        echo "Opción no válida. Por favor, elige 1, 2 o q."
        ;;
esac

echo "----------------------------------------------------------------"
echo " Script finalizado."
echo "----------------------------------------------------------------"