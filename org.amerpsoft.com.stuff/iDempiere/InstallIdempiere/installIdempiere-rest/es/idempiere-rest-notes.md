&lArr;[Instalación de Idempiere](../../README_ES.md)
<!-- LOGOTIPO DEL PROYECTO -->
<br />
<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/AMERPSOFT_logo_600.png" alt="Logo" width="150" height="90">
</a>
</div>

<a name="readme-top"></a>

🇬🇧 [English Version](../idempiere-rest-notes.md) | 🇪🇸 Español

# IDEMPIERE-REST

La API REST de iDempiere es un plugin que permite a los desarrolladores comunicarse con un servidor iDempiere mediante una API RESTful. Esta información se entrega mediante HTTP usando JSON.

La API REST de iDempiere busca seguir el estándar OData y utiliza la siguiente estructura y puntos finales.

La API REST de iDempiere es mantenida por BX Service GmbH.

Puede encontrar la documentación sobre cómo usarla aquí: https://wiki.idempiere.org/en/REST_Web_Services.

Siga los pasos para comenzar a usar este plugin:

| Pasos | Título | Comentarios |
| ----: | ---------------------------------------------- | ---------------------------------------------------------------------------------- |
| 1 | [Clonar repositorio](#step1) | Clonar repositorio |
| 2 | [Compilar con Maven](#step2) | Compilar plugin usando el entorno de iDempiere |
| 3 | [Instalar plugin](#step3) | Instalar plugin usando la consola OSGI |
| 4 | [Verificar plugin](#step4) | Verificar que el plugin esté en ejecución |
| 5 | [Instalar Postman](#step5) | Instalar la aplicación Postman |
| 6 | [Importar colección de Postman](#step6) | Importar colecciones de Postman |
| 7 | [Verificar la API con Postman](#step7) | Verificar las funciones de la API con las colecciones de Postman |

## <a name="step1"></a>⭐️1. Clonar el repositorio.

Es obligatorio tener un entorno de Idempiere adecuado y un repositorio Maven local creado durante la instalación local de Idempiere.
Para más información, consulte [Instalar Idempiere](https://wiki.idempiere.org/en/Installing_iDempiere).

Clonar el repositorio desde el origen en el mismo directorio donde se encuentra el entorno de idempiere:

```bash
cd ..\sources\
git clone https://github.com/bxservice/idempiere-rest.git
```

Diseño de carpetas:

```text
* sources\idempiere
* sources\idempiere-rest
* com.trekglobal.idempiere.extensions.parent
* com.trekglobal.idempiere.rest.api
* com.trekglobal.idempiere.extensions.p2
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

## <a name="step2"></a>⭐️2. Compilación con Maven

Utilice la propiedad maven.repo.local de Maven:

```bash
mvn -Dmaven.repo.local=$HOME/.my/other/repository clean install
```

No es necesario modificar el archivo settings.xml.

```bash
mvn -Dmaven.repo.local=$HOME/.m2/repository_12_OK instalación limpia
```
Ejemplo de configuración de usuario:

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/maven_user_settings.png" alt="Logo" width="800">
</a>
</div>

Ejemplo de configuración de Eclipse Run:

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/maven_clean_install.png" alt="Logo" width="800">
</a>
</div>

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

## <a name="step3"></a>⭐️3. INSTALACIÓN DEL PLUGIN. Instalar el plugin usando la consola web OSGI Apache Felix en el nivel 4

Instalando el plugin:

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/osgi_console_api_0.png" alt="Logo" width="800">
</a>
</div>

Seleccionar archivo y nivel 4:

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/osgi_console_api_1.png" alt="Logo" width="800">
</a>
</div>

Verificar la lista de plugins:

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/osgi_console_api_2.png" alt="Logo" width="800">
</a>
</div>

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

## <a name="step4"></a>⭐️4. VERIFICAR EL PLUGIN.

Usando Apache Felix, verifique que el plugin esté ejecutándose. También se recomienda reiniciar el servidor idempiere y verificar que el plugin «idempiere-rest» se inicie automáticamente.

Verificar que el plugin se esté ejecutando en el nivel 4:
<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/osgi_console_api_3.png" alt="Logo" width="800">
</a>
</div>

## <a name="step5"></a>⭐️5. INSTALACIÓN DE POSTMAN.

Descarga la aplicación Postman desde:

https://www.postman.com/downloads/

Para usar la aplicación por primera vez, debes crear una cuenta.

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

## <a name="step6"></a>⭐️6. IMPORTACIÓN DE COLECCIONES DE POSTMAN.

Importar entornos y colecciones desde el directorio de Postman:

* com.trekglobal.idempiere.rest.api\postman

| Archivo | Descripción |
| ------------ | ----------------------------------------------- |
| http---localhost-8080.postman_environment.json | Entorno para el servidor local |
| https---test.idempiere.org.postman_environment.json | Entorno para el servidor test.idempiere |
| https---test-postgresql.idempiere.org.postman_environment.json | Entorno para el servidor test-postgresql.idempiere |
| trekglobal-idempiere-rest.postman_collection.json | Colecciones |

Entorno de Postman para el servidor local.

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/postman_localhost_env.png" alt="Logo" width="800">
</a>
</div>

Entorno de Postman para el servidor test.idempiere.

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/postman_test_env.png" alt="Logo" width="800">
</a>
</div>

Entorno de Postman para el servidor test-postgresql.idempiere.

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/postman_testpg_env.png" alt="Logo" width="800">
</a>
</div>

Postman trekglobal-idempiere-rest.postman_collection.json.

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/postman_collections.png" alt="Logo" width="800">
</a>
</div>

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

## <a name="step7"></a>⭐️7. VERIFICAR LAS LLAMADAS A LA API CON POSTMAN.

Ahora verificaremos las funciones de la API con las colecciones de Postman.

### Iniciar sesión para obtener un token de autorización de portador
IMPORTANTE: La API REST solo permite iniciar sesión en roles con tipo de rol = WebService o sin tipo de rol. Esto significa que, por defecto, no es posible iniciar sesión con el rol "Sistema". Debe borrar el tipo de rol en "Sistema" o, mejor aún, crear un rol de WebService para el inquilino del sistema.

Hay dos maneras de iniciar sesión para obtener tu token de autorización:

#### Inicio de sesión en un solo paso
Una vez que conozcas de antemano toda la información de inicio de sesión necesaria para acceder al sistema, debes realizar una solicitud POST al siguiente punto final:
.../api/v1/auth/tokens

Con un cuerpo como este:

```text
{
"username": "{{userName}}",
"password": "{{password}}",
"parameters": {
"clientId": {{clientId}},
"roleId": {{roleId}},
"organizationId": {{organizationId}},
"warehouseId": {{warehouseId}},
"language": "{{language}}"
}
}
```

Cambia todos los valores de Template:Propertyname por los que quieras usar en tu instancia.

Captura de pantalla:

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/postman_onestep_login.png" alt="Logo" width="800">
</a>
</div>

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

#### Inicio de sesión normal
Si desea iniciar sesión como lo haría un usuario normal en iDempiere (eligiendo un rol, almacén, cliente, etc.), debe realizar las siguientes solicitudes:

POST .../api/v1/auth/tokens
Body:

```text
{
"username": "{{username}}",
"password": "{{password}}"
}
Carga de respuesta

{
"clients": [
{
"id": 11,
"name": "GardenWorld"
}
],
"token": "eyJraWQiOiJpZGVtcGllcmUiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHYXJkZW5BZG1pbiIsImlzcyI6ImlkZW1waWVyZS5vcmciLCJDbGllbnRzIjoiMTEiLCJleHAiOjE2NjY5NjQ5Mjh9.3t0MuK6ReF7xNmb36ITM36VSKI5QnK3n0ZF_LIgPQSrso4oRhDsL8Mudc0NqH4qjvvKDlYsPquYKtrHnB5UiZg"
}
```

Captura de pantalla:

<div align="center">
<a href="AMERPSOFT_logo">
<img src="./images/postman_auth_token.png" alt="Logo" width="800">
</a>
</div>

Con ese token, puede solicitar la información de inicio de sesión del usuario que se está autenticando. Debe agregarlo al encabezado de la solicitud con el valor de la clave:
Autorización: Portador {authToken}

La información que puede solicitar es la siguiente y debe hacerse en este orden, ya que cada solicitud necesita información de la llamada anterior:

```text
GET .../api/v1/auth/roles?client={clientId}
```
Devuelve una matriz con los roles a los que el usuario tiene acceso.

```text
GET .../api/v1/auth/organizations?client={clientId}&role={roleId}
```

Devuelve una matriz con las organizaciones a las que el usuario tiene acceso.

/api/v1/auth/warehouses?client={clientId}&role={roleId}&organization={organizationId}

Devuelve una matriz con los almacenes a los que el usuario tiene acceso.

```text
GET .../api/v1/auth/language?client={clientId}
```

Devuelve una matriz con los idiomas en los que el usuario puede iniciar sesión.

Cuando tenga todos los datos, debe realizar una solicitud PUT final como esta:

```text
PUT .../api/v1/auth/tokens
```

```text
Body:

{
"clientId": {clientId},
"roleId": {roleId},
"organizationId": {organizationId},
"warehouseId": {warehouseId},
"language": "{language}"
}
* Las propiedades language, organizationId y warehouseId son opcionales; se pueden omitir.
```

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>