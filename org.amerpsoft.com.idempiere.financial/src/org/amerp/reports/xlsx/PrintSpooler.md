¡Exactamente! Eso sería un **“Print Spooler de reportes”** dentro de iDempiere. La idea sería separar la **ejecución del reporte** de la **interfaz del usuario**, de manera que puedas enviar varios reportes y luego hacer seguimiento de todos ellos.

Aquí te propongo un diseño general:

---

### 1️⃣ Tabla para gestionar reportes en cola

Tabla `AM_ReportQueue` (ejemplo):

| Campo        | Tipo        | Descripción                          |
| ------------ | ----------- | ------------------------------------ |
| AD_Client_ID | int         | Cliente                              |
| AD_User_ID   | int         | Usuario que lanza el reporte         |
| ReportName   | String      | Nombre del reporte                   |
| Status       | String      | Pending / Running / Finished / Error |
| Progress     | int         | 0 a 100                              |
| StartTime    | Timestamp   | Inicio del proceso                   |
| EndTime      | Timestamp   | Fin del proceso                      |
| Parameters   | JSON/String | Parámetros del reporte               |
| FilePath     | String      | Fichero generado (Excel, PDF…)       |
| Message      | String      | Mensajes o errores                   |

---

### 2️⃣ Workflow

1. Usuario selecciona el reporte y parámetros en tu Form.
2. Se inserta un registro en `AM_ReportQueue` con `Status = Pending`.
3. **Un worker** (puede ser un proceso `SvrProcess` en background o un hilo gestionado) toma los registros `Pending` y los ejecuta:

   * Marca `Status = Running`, `StartTime = now()`
   * Genera el Excel/XLSX por lotes
   * Actualiza `Progress` mientras escribe
   * Al finalizar, guarda `FilePath` y marca `Status = Finished` y `EndTime = now()`
   * Si hay error, guarda `Status = Error` y `Message`

---

### 3️⃣ Interfaz del Form

Puedes tener un **tab “Report Queue”** donde:

* Se listan todos los reportes de la tabla `AM_ReportQueue` para el usuario actual
* Columnas:

  * Nombre del reporte
  * Estado
  * % Progreso
  * Fecha inicio / fin
  * Botones:

    * **Ver / Descargar** (abre el fichero)
    * **Reintentar** si hay error
* Actualización periódica del tab para mostrar progreso en tiempo real (por ejemplo, cada 3-5 segundos usando `Clients.response` en ZK)

---

### 4️⃣ Ventajas

* Puedes **enviar múltiples reportes** sin bloquear el Form
* Mantienes **histórico y trazabilidad**
* Permite **descargar reportes pasados**
* Facilita **migrar a ejecución real en background** sin cambios en la UI

---

Si quieres, puedo hacer un **esquema de código base** de:

* Clase `ReportQueueManager` que inserta y gestiona la cola
* Modificación de tu Form para:

  * “Enviar a Print Spooler” en vez de ejecutar directo
  * Mostrar progreso y botón de descarga

Perfecto. Vamos a plantear un **“Print Spooler” de reportes en iDempiere** basado en tu Form actual y tu proceso XLSX. Te haré un ejemplo **funcional**, listo para adaptarlo a tu código.

---

## 1️⃣ Tabla `AM_ReportQueue`

Puedes crearla como tabla adicional en iDempiere:

| Campo             | Tipo          | Descripción                          |
| ----------------- | ------------- | ------------------------------------ |
| AD_ReportQueue_ID | int (PK)      | Identificador único                  |
| AD_Client_ID      | int           | Cliente                              |
| AD_User_ID        | int           | Usuario que lanza el reporte         |
| ReportName        | String        | Nombre del reporte                   |
| Status            | String        | Pending / Running / Finished / Error |
| Progress          | int           | 0 a 100                              |
| StartTime         | Timestamp     | Inicio del proceso                   |
| EndTime           | Timestamp     | Fin del proceso                      |
| Parameters        | String (JSON) | Parámetros del reporte               |
| FilePath          | String        | Ruta del fichero generado            |
| Message           | String        | Mensajes de error o log              |

> Puedes crearla desde **Application Dictionary > Tables** en iDempiere.

---

## 2️⃣ Clase `ReportQueueManager`

Esta clase gestiona la cola de reportes:

```java
package org.amerp.reports.queue;

import java.sql.Timestamp;
import java.util.List;

import org.compiere.model.MClient;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class ReportQueueManager {

    private static final CLogger log = CLogger.getCLogger(ReportQueueManager.class);

    // Inserta reporte en cola
    public static int enqueueReport(String reportName, int AD_Client_ID, int AD_User_ID, String parametersJson) {
        String sql = "INSERT INTO AM_ReportQueue (AD_Client_ID, AD_User_ID, ReportName, Status, Progress, Parameters) " +
                     "VALUES (?, ?, ?, 'Pending', 0, ?)";
        int id = DB.executeUpdate(sql, new Object[]{AD_Client_ID, AD_User_ID, reportName, parametersJson}, true);
        return id;
    }

    // Actualiza progreso
    public static void updateProgress(int AD_ReportQueue_ID, int progress) {
        String sql = "UPDATE AM_ReportQueue SET Progress=?, Status='Running' WHERE AD_ReportQueue_ID=?";
        DB.executeUpdate(sql, new Object[]{progress, AD_ReportQueue_ID});
    }

    // Marca como terminado
    public static void finishReport(int AD_ReportQueue_ID, String filePath) {
        String sql = "UPDATE AM_ReportQueue SET Progress=100, Status='Finished', EndTime=CURRENT_TIMESTAMP, FilePath=? WHERE AD_ReportQueue_ID=?";
        DB.executeUpdate(sql, new Object[]{filePath, AD_ReportQueue_ID});
    }

    // Marca como error
    public static void errorReport(int AD_ReportQueue_ID, String message) {
        String sql = "UPDATE AM_ReportQueue SET Status='Error', Message=? WHERE AD_ReportQueue_ID=?";
        DB.executeUpdate(sql, new Object[]{message, AD_ReportQueue_ID});
    }

    // Lista reportes del usuario
    public static List<?> getReports(int AD_User_ID) {
        String sql = "SELECT * FROM AM_ReportQueue WHERE AD_User_ID=? ORDER BY Created DESC";
        return DB.getSQLArray(sql, new Object[]{AD_User_ID});
    }
}
```

---

## 3️⃣ Modificación del Form `AccountElements_Tree_Form`

En vez de ejecutar el proceso directamente:

```java
private void runServerProcess() {
    // Serializa parámetros a JSON
    String paramsJson = "{ \"AD_Client_ID\":" + fClient.getValue() +
                        ", \"C_AcctSchema_ID\":" + fAcctSchema.getValue() + " }";

    // Inserta en la cola
    int queueId = ReportQueueManager.enqueueReport(
            "AccountElements_Tree_xlsx",
            Env.getAD_Client_ID(Env.getCtx()),
            Env.getAD_User_ID(Env.getCtx()),
            paramsJson
    );

    Dialog.info(form.getWindowNo(), "Reporte enviado a la cola. ID: " + queueId);
}
```

> Ya no bloquea la UI; el usuario puede enviar varios reportes.

---

## 4️⃣ Proceso Worker (`SvrProcess`)

Crea un proceso en segundo plano que revise `AM_ReportQueue` cada X minutos y ejecute los reportes pendientes:

```java
package org.amerp.reports.queue;

import java.io.File;
import java.util.List;

import org.amerp.reports.xlsx.AccountElements_Tree_xlsx;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class ReportQueueWorker extends SvrProcess {

    @Override
    protected void prepare() {
        // sin parámetros
    }

    @Override
    protected String doIt() throws Exception {
        List<?> reports = ReportQueueManager.getReports(Env.getAD_User_ID(getCtx()));
        for (Object r : reports) {
            int queueId = /* obtener ID de objeto r */;
            // Actualiza status a Running
            ReportQueueManager.updateProgress(queueId, 0);

            try {
                // Ejecuta tu proceso XLSX
                AccountElements_Tree_xlsx process = new AccountElements_Tree_xlsx();
                // parsea params JSON y setea p_AD_Client_ID, p_C_AcctSchema_ID
                File f = process.crearXlsx(/* ProcessInfo */);

                // Marca terminado
                ReportQueueManager.finishReport(queueId, f.getAbsolutePath());
            } catch (Exception e) {
                ReportQueueManager.errorReport(queueId, e.getMessage());
            }
        }
        return "Worker finalizado";
    }
}
```

---

## 5️⃣ Tab en el Form para ver estado

En tu `AccountElements_Tree_Form` puedes agregar un **Tab “Report Queue”** que:

* Muestra lista de reportes enviados por el usuario
* Columnas: `ReportName`, `Status`, `Progress`, `FilePath`, `Message`
* Botón **Descargar** si `FilePath` existe
* Refrescar cada 3-5 segundos con:

```java
Clients.evalJavaScript("window.setTimeout(function(){ /* refrescar grid */ }, 3000);");
```

---

✅ **Ventajas:**

* Permite enviar múltiples reportes sin bloquear la UI
* Muestra progreso de cada reporte
* Permite descargar reportes terminados
* Se integra con tu `AccountElements_Tree_xlsx` actual
* Escalable a otros reportes

---







