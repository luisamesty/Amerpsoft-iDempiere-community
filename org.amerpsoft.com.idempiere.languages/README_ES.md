&lArr;[AMERPSOFT-COM PLUGINS](../README_ES.md) | [Home](../README_ES.md)
<br />
<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="../images/AMERPSOFT_logo_600.png" alt="Logo" width="150" height="90">
  </a>
</div>

<div>
	🇬🇧 <a href="README.md">English</a> | 🇪🇸 Español</a>
</div>

<a name="readme-top"></a>

# <b>Idiomas</b>

Idempiere puede mostrar interfaces web en diferentes idiomas. Debe configurar los idiomas con los paquetes de instalación adecuados.
Como ejemplo, puede consultar la página wiki [Install_german_language_Pack](https://wiki.idempiere.org/en/Install_german_language_Pack). También puede encontrar diferentes paquetes de idiomas en la [Página de traducción](https://wiki.idempiere.org/en/Translations).

En este caso, nos centramos en el español. Tenemos diferentes ubicaciones según el país. Por ejemplo, Venezuela, Paraguay, España, etc. Las ubicaciones en español tienen muchas similitudes y pequeñas diferencias, por lo que copiamos las traducciones y ajustamos las diferencias.

Siga estas instrucciones para definir un idioma español similar al predeterminado de Idempiere.

## <b>Instalación en español</b>

Siga estos pasos para definir un nuevo idioma del sistema en su sistema Idempiere.
La tabla de idiomas contiene la mayoría de los idiomas y localizaciones. Por ejemplo, para español, existen diferentes localizaciones para cada país.
Tenemos:

```text
- es_ES para España.
- es_VE para Venezuela.
- es_PY para Paraguay.
- es_CL para Chile.
```
En la base de datos predeterminada de Idempiere, solo tenemos <b>en_US</b> y <b>es_CO</b> como idiomas del sistema.
Los idiomas del sistema permiten traducir elementos, campos, tablas, mensajes y todas las entidades en Didempiere.
En las localizaciones al español, tenemos básicamente la misma estructura, pero solo se identificaron pequeñas diferencias.

Por estas razones, he creado un script para copiar (clonar) el es_CO proporcionado por Seed a la localización seleccionada es_VE. De esta manera, próximamente podremos realizar cambios menores en el diccionario de nuestra aplicación.

### <b>1- Establecer el español es_VE como idioma del sistema</b>
```text
- Iniciar sesión como idioma del sistema en inglés
- Ir a Menú: /Administración del sistema/Reglas generales/Reglas del sistema/Idioma
- En la ventana de idioma: Localizar es_ES, es_VE, es_PY. O su idioma preferido xx_XX
- Idioma de la ventana: Comprobar el idioma del sistema
- Idioma de la ventana: Ejecutar el proceso de "Mantenimiento del idioma"
(Si usa otra configuración regional, ajústela)
```

### <b>2- Crear la extensión de idioma para es_VE</b>
```texto
- Descargar scripts del repositorio (Consulte la documentación del plugin)
- Ejemplo: Script "Create-language-from-es-CO-to-es-VE.sql"
- Ejecutar la consulta en una consola SQL. SQLDeveloper para entornos Oracle, DBeaver para PostgreSQL.
(Se recomienda hacerlo en consultas parciales o ejecutando un archivo)
- Puede editar esta consulta para su idioma preferido xx_XX
- Recuerde sincronizar la terminología
```

### <b>3- Cambios manuales en algunas traducciones</b>

Traduzca manualmente las etiquetas asociadas a regiones, municipios y parroquias. También el idioma posible Diferencias existentes.
Por ejemplo:
```texto
- Ir a "Diccionario/Mensajes de la aplicación" y seleccionar la configuración regional para la región, el municipio y la parroquia.
- Cambiar para Paraguay a Departamento/Distrito/Barrio.
- Cambiar para Venezuela a Estado/Municipio/Parroquia.

Es importante cambiar estas etiquetas, ya que se utilizan en la ventana de ubicación prevista al seleccionar un idioma distinto del inglés.
```

### <b>4-Traducir manualmente el registro de idioma para es_VE</b>

Menú (Administrador del sistema/Reglas generales/Reglas del sistema/Idioma)

```texto
Modificar el campo "Imprimir texto" y traducir al idioma seleccionado (por ejemplo, español). Español (España) para es_ES
Español (Paraguay) para es_PY
Español (Venezuela) para es_VE
```

### <b>5- Habilitar/Deshabilitar idioma de inicio de sesión</b>

Menú (Administrador del sistema/Reglas generales/Reglas del sistema/Idioma)

Vaya a Idioma específico, por ejemplo, 'es_VE'.

En el campo <b>Configuración regional de inicio de sesión'</b>:

```texto
Marque Inicio de sesión habilitado.
Desmarque Inicio de sesión deshabilitado.
```

<div align="left">
<a href="ISO_CODE">
<img src="./images/language_select_login.png" alt="Logo" width="800" >
</a>
</div>

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>