# 🛠️ Guía de Desarrollo y Compilación - Amerpsoft iDempiere

Este documento explica cómo compilar los módulos de Amerpsoft en iDempiere 12 usando un script bash personalizado. También se incluye el script completo al final.

## 📁 Estructura del Proyecto

El repositorio contiene los siguientes módulos:

* `org.amerpsoft.com.idempiere.editors-com`
* `org.amerpsoft.com.idempiere.feature`
* `org.amerpsoft.com.idempiere.financial`
* `org.amerpsoft.com.idempiere.lco.withholding`
* `org.amerpsoft.com.idempiere.personnelpayroll`
* `org.amerpsoft.com.idempiere.tools`
* `org.amerpsoft.com.idempiere.webform`
* `org.amerpsoft.com.idempiere.workflow`
* `org.amerpsoft.com.idempiere.p2.site`

## ▶️ Opciones del Script de Compilación

El script interactivo te permite:

1. **Compilar todos los módulos uno por uno y luego el P2**.
2. **Compilar un solo módulo**.
3. **Limpiar las carpetas `target`**.
4. **Compilar todo el proyecto en un único reactor** (para evitar errores de `.qualifier`).
5. **Salir del menú**.

## ✅ Requisitos

* Tener instalado Maven.
* Java 17.
* Repositorio correctamente clonado y módulos listos para compilar.

## 🔧 Script `build.sh`

Guarda este contenido como `build.sh` y dale permisos de ejecución:

```bash
#!/bin/bash

REPO_LOCAL="$HOME/.m2/repository_12_OK"
LOG_DIR="logs"

MODULES=(
  org.amerpsoft.com.idempiere.editors-com
  org.amerpsoft.com.idempiere.feature
  org.amerpsoft.com.idempiere.financial
  org.amerpsoft.com.idempiere.lco.withholding
  org.amerpsoft.com.idempiere.personnelpayroll
  org.amerpsoft.com.idempiere.tools
  org.amerpsoft.com.idempiere.webform
  org.amerpsoft.com.idempiere.workflow
)

P2_MODULE="org.amerpsoft.com.idempiere.p2.site"
declare -A BUILD_RESULTS

clean_targets() {
  echo "🧹 Borrando carpetas target..."
  for module in "${MODULES[@]}" "$P2_MODULE"; do
    if [ -d "$module/target" ]; then
      echo "🗑️  $module/target"
      rm -rf "$module/target"
    fi
  done
}

compile_module() {
  local module="$1"
  mkdir -p "$LOG_DIR"
  local LOG_FILE="$LOG_DIR/$module.log"
  echo "🔨 Compilando $module..."
  if mvn clean install -pl "$module" -Dmaven.repo.local="$REPO_LOCAL" > "$LOG_FILE" 2>&1; then
    BUILD_RESULTS["$module"]="✅ OK"
  else
    BUILD_RESULTS["$module"]="❌ ERROR"
  fi
}

compile_all() {
  clean_targets
  for module in "${MODULES[@]}"; do
    compile_module "$module"
  done
  compile_module "$P2_MODULE"
}

compile_full_reactor() {
  echo "🔁 Compilando TODO el proyecto completo en un solo reactor..."
  mkdir -p "$LOG_DIR"
  local LOG_FILE="$LOG_DIR/full-reactor.log"
  if mvn clean install -Dmaven.repo.local="$REPO_LOCAL" > "$LOG_FILE" 2>&1; then
    echo "✅ Compilación completa exitosa."
  else
    echo "❌ Error al compilar todo el proyecto. Revisa el log: $LOG_FILE"
  fi
}

resumen() {
  echo
  echo "📋 RESUMEN FINAL DE COMPILACIÓN:"
  echo "---------------------------------------------------------------"
  for module in "${MODULES[@]}" "$P2_MODULE"; do
    status=${BUILD_RESULTS["$module"]}
    printf "%-50s %s\n" "$module" "$status"
  done
  echo "---------------------------------------------------------------"
  echo "📝 Puedes revisar los logs en la carpeta: $LOG_DIR"
}

while true; do
  echo
  echo "🚀 MENU DE COMPILACIÓN AMERPSOFT:"
  echo "1. Compilar TODOS los módulos + P2"
  echo "2. Compilar UN solo módulo"
  echo "3. Limpiar carpetas target"
  echo "4. Compilar TODO el proyecto (en un solo reactor)"
  echo "5. Salir"
  read -p "Selecciona una opción [1-5]: " opcion

  case "$opcion" in
    1)
      compile_all
      resumen
      ;;
    2)
      echo "📦 Módulos disponibles:"
      for i in "${!MODULES[@]}"; do
        echo "$((i+1)). ${MODULES[$i]}"
      done
      echo "$(( ${#MODULES[@]} + 1 )). $P2_MODULE"
      read -p "Elige el número del módulo: " num
      if (( num > 0 && num <= ${#MODULES[@]} + 1 )); then
        if (( num == ${#MODULES[@]} + 1 )); then
          compile_module "$P2_MODULE"
        else
          compile_module "${MODULES[$((num-1))]}"
        fi
        resumen
      else
        echo "❌ Selección inválida."
      fi
      ;;
    3)
      clean_targets
      ;;
    4)
      compile_full_reactor
      ;;
    5)
      echo "👋 Saliendo."
      exit 0
      ;;
    *)
      echo "❌ Opción inválida."
      ;;
  esac
done
```

## 📎 Notas Finales

* El `.qualifier` es obligatorio en los proyectos Tycho si estás haciendo builds SNAPSHOT.
* Para evitar errores con Tycho y dependencias no resueltas, se recomienda compilar todo en un solo reactor (opción 4).
* Los logs de compilación quedan en la carpeta `logs/`.

---

¿Necesitas que este script se convierta también en `.desktop` para lanzarlo desde el entorno gráfico en Linux?
