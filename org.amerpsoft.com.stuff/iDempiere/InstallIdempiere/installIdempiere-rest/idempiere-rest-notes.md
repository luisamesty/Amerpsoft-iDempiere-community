&lArr;[COMMUNITY PLUGINS](../README.md) | [Home](../README.md)
<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/AMERPSOFT_logo_600.png" alt="Logo" width="150" height="90">
  </a>
</div>

<a name="readme-top"></a>

# IDEMPIERE-REST

The iDempiere REST API is a plugin that allows developers to communicate with an iDempiere server via a RESTful API. This information is delivered via HTTP using JSON.

The iDempiere REST API seeks to follow the OData standard and uses the following structure and endpoints.

The iDempiere REST API is maintained by BX Service GmbH.

You can find the documentation on how to use it here: https://wiki.idempiere.org/en/REST_Web_Services.


Follow steps to start using this plugin:

| Steps | Title                                          | Comments                                                                           |
| ----: | ---------------------------------------------- | ---------------------------------------------------------------------------------- |
|     1 | [Clone Repository](#step1)                     | Clone repository                                                                   |
|     2 | [Compile with maven](#step2)                   | Compile pluging using iDempiere environment                                        |
|     3 | [Installing plugin](#step3)                    | Install plugin using OSGI console                                                  |
|     4 | [Verify Plugin](#step4)                        | Verify pluging is runniing                                                         |
|     5 | [Installing Postman](#step5)                   | Install Postman application                                                        |
|     6 | [Import Postman Colection](#step6)             | Import Postman collections                                                         |
|     7 | [Verify API with Postman](#step7)              | Verify API functions with Postman collections                                      |


## <a name="step1"></a>⭐️1.	Clone repository.

It is mandatory to have a proper idempiere environtment and local maven repository created during idempiere local install.
For more information you can see [Install Idempiere](https://wiki.idempiere.org/en/Installing_iDempiere).

Clone repository from origin in the same forlder you have idempiere environment:

```copy
cd ..\sources\
git clone https://github.com/bxservice/idempiere-rest.git
```

Folder layout:

* sources\idempiere
* sources\idempiere-rest
  * com.trekglobal.idempiere.extensions.parent
  * com.trekglobal.idempiere.rest.api
  * com.trekglobal.idempiere.extensions.p2
    
```text

```

<p align="left">(<a href="#readme-top">back to top</a>)</p>

## <a name="step2"></a>⭐️2. Compiling with maven 

Use maven property maven.repo.local:

mvn -Dmaven.repo.local=$HOME/.my/other/repository clean install

No modifications to settings.xml are necessary.

```copy
mvn -Dmaven.repo.local=$HOME/.m2/repository_11_OK clean install
```
User settings example:

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/maven_user_settings.png" alt="Logo" width="800">
  </a>
</div>

Eclipse Run configuration Example:

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/maven_clean_install.png" alt="Logo" width="800">
  </a>
</div>



##  <a name="step3"></a>⭐️3.INSTALLING PLUGIN.


Install plugin using OSGI Apache Felix Web Console at Level 4

Installing plugin:

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/osgi_console_api_0.png" alt="Logo" width="800">
  </a>
</div>

Select file and level 4:

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/osgi_console_api_1.png" alt="Logo" width="800">
  </a>
</div>

Verify plugin list:

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/osgi_console_api_2.png" alt="Logo" width="800">
  </a>
</div>



##  <a name="step4"></a>⭐️4.VERIFY PLUGIN.

Using Apache Felix, verify plugin is running. Also it is recommended to restart idempiere server and verify that ¡idempiere-rest' plugin starts automatically.

Verify plugin running at level 4:
<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/osgi_console_api_3.png" alt="Logo" width="800">
  </a>
</div>

##  <a name="step5"></a>⭐️5.INSTALLING POSTMAN.

Download Postman App from:

https://www.postman.com/downloads/

For the first use, you must create an account to use the APP.

##  <a name="step6"></a>⭐️6.IMPORT POSTMAN COLLECTIONS.

Import Environments and collection from postman forlder:

* com.trekglobal.idempiere.rest.api\postman


| File          | Description                                     |
| ------------  | ----------------------------------------------- |
| http---localhost-8080.postman_environment.json    | Environment for localhost server |
| https---test.idempiere.org.postman_environment.json   | Environment for test.idempiere server |
| https---test-postgresql.idempiere.org.postman_environment.json | Environment for test-postgresql.idempiere server |
| trekglobal-idempiere-rest.postman_collection.json | Collections |


Postman Environment for localhost server.

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/postman_localhost_env.png" alt="Logo" width="800">
  </a>
</div>

Postman Environment for test.idempiere server.

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/postman_test_env.png" alt="Logo" width="800">
  </a>
</div>
 
Postman Environment for test-postgresql.idempiere server.

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


##  <a name="step7"></a>⭐️7.VERIFY API CALLS WITH POSTMAN.

Now we are going to verify API functions with Postman collections.

### Login in for a Bearer authorization token
IMPORTANT: REST API just allows to log in to roles with Role Type = WebService, or without Role Type. This means by default is not possible to login with role System, you need to clear the Role Type in System, or even better create a WebService role for System tenant.

There are two ways to log in to get your authorization token:

#### One-step log-in
When you know beforehand all the login information needed to get into the system, you need to do a POST request to the following endpoint
.../api/v1/auth/tokens

With a body like this:
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
Change all the Template:Propertyname values to the ones you want to use in your instance.

Screen shot:

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/postman_onestep_login.png" alt="Logo" width="800">
  </a>
</div>


#### Normal log-in
If you want to log in as a user would normally do in iDempiere (choosing a role, warehouse, client, etc), you need to do the following requests

POST .../api/v1/auth/tokens
Body:
```text
{
    "username": "{{username}}",
    "password": "{{password}}"
}
Response Payload

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

Screen shot:

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/postman_auth_token.png" alt="Logo" width="800">
  </a>
</div>

With that token you can request for login information from the user that is being authenticated. You need to add it to the request Header with Key value as:
Authorization: Bearer {authToken}

The information you can request is the following, and it needs to be done in this order because each request needs information from the previous call:
```text
GET .../api/v1/auth/roles?client={clientId}
```
Returns an array with the roles that the user has access to

GET .../api/v1/auth/organizations?client={clientId}&role={roleId}

Returns an array with the organizations that the user has access to.

/api/v1/auth/warehouses?client={clientId}&role={roleId}&organization={organizationId}

Returns an array with the warehouses that the user has access to.

GET .../api/v1/auth/language?client={clientId}

Returns an array with the languages the user can login with.

When you have all the data you need to do a final PUT request like this:

PUT .../api/v1/auth/tokens

Body:

{
  "clientId": {clientId},
  "roleId": {roleId},
  "organizationId": {organizationId},
  "warehouseId": {warehouseId},
  "language": "{language}"
}
* The language, organizationId and warehouseId properties are optional, it can be omitted and the system will use the default language and 0 respectively.

Response Payload

{
    "userId": 101,
    "language": "en_US",
    "token": "eyJraWQiOiJpZGVtcGllcmUiLCJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJHYXJkZW5BZG1pbiIsIkFEX0NsaWVudF9JRCI6MTEsIkFEX1VzZXJfSUQiOjEwMSwiQURfUm9sZV9JRCI6MjAwMDAxLCJBRF9PcmdfSUQiOjExLCJNX1dhcmVob3VzZV9JRCI6MTAzLCJBRF9MYW5ndWFnZSI6ImVuX1VTIiwiQURfU2Vzc2lvbl9JRCI6MTAwMDE2MiwiaXNzIjoiaWRlbXBpZXJlLm9yZyIsImV4cCI6MTcwMTc3NTUzN30.bAUEhPylAQhZjZquJhvLpO9zMZG3g6zlM_IqO9ifeXJpAJBOoJtDqd8CrYPU1PKURzoPRSUglbKqr1LsXdz38A",
    "refresh_token": "eyJraWQiOiJpZGVtcGllcmUiLCJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIwODk1OTIyNC1iYzBhLTRkNTQtOTlhZS1jNmRmZjNiOGEwMzUiLCJpc3MiOiJpZGVtcGllcmUub3JnIiwiZXhwIjoxNzAxODU4MzM3fQ.uSE5SOtWgPvReC4JtyV4alHd-ccU0L9QhIpP2TwT7C5TJFeCGVYTdyWc291DaIweyiIGCfWFgQlbe0oH1EEXXg"
}

### Refresh Token

As you see above, the login process returns a token and a refresh_token, normally the token expires in 1 hour and the refresh_token expires in 24 hours. These expiration defaults can be changed using SysConfig keys REST_TOKEN_EXPIRE_IN_MINUTES and REST_REFRESH_TOKEN_EXPIRE_IN_MINUTES.

It is a common practice in front-end development to store the refresh_token in a cookie or browser data, while keeping the token itself just in memory. This approach is taken due to the sensitive nature of the information contained within the token, which must not be disclosed.

When the token expires, you can get a new token calling the following request:

POST .../api/v1/auth/refresh
Body:
```text
{
    "refresh_token": "{{refreshToken}}"
}
```
Response Payload
```text
{
    "token": "eyJraWQiOiJpZGVtcGllcmUiLCJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJTdXBlclVzZXIiLCJBRF9DbGllbnRfSUQiOjAsIkFEX1VzZXJfSUQiOjEwMCwiQURfUm9sZV9JRCI6MTAwMDAxNCwiQURfT3JnX0lEIjowLCJBRF9MYW5ndWFnZSI6ImVuX1VTIiwiQURfU2Vzc2lvbl9JRCI6MTAwMDE2MywiaXNzIjoiaWRlbXBpZXJlLm9yZyIsImV4cCI6MTcwMTc3NTc5NX0.pdMdr3ihTz9fSOkoDP-tvrirjQNAw0Z8OHn1xMCq3A6b94JBD74MHd4hvb6y51xp8eezElsg1nVq0sMh7AsEvA",
    "refresh_token": "eyJraWQiOiJpZGVtcGllcmUiLCJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhNWQ0ZjM3NS1hMzAzLTRiNTYtYjFjYi1hZWJkYTRmNjRkOTciLCJpc3MiOiJpZGVtcGllcmUub3JnIiwiZXhwIjoxNzAxODU4NTk1fQ.2ISJgrKBvcKOob1Jruy2apGqv_WcEu_ayGRDkgxadAoHGaG6c1WwcCi7thrrJ5OwOvS4fPh4d63REuRezLJjSw"
}
```

Screen shot:

<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="./images/postman_refresh_token.png" alt="Logo" width="800">
  </a>
</div>

The new tokens are set with new expiration times as configured.

Refresh tokens can be expired (deleted) using the process "Expire Refresh Tokens"

A refresh token can be used just one time.
