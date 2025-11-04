# Ejecutar un Form, desde un Process.

**puedes ejecutar un Form desde un Process**, pero hay matices importantes dependiendo de *cómo* lo quieras hacer:

* si solo quieres **abrir el Form** en la interfaz de iDempiere (modo visual), o
* si quieres **ejecutar la lógica del Form** (modo backend, sin UI).

Veamos ambos escenarios:

---

## 🧭 1️⃣ Caso A — Ejecutar el *Form* visualmente desde un *Process* (abrir ventana)

Esto **sí es posible**, pero **solo tiene sentido desde la UI ZK**, no desde REST o background.

### 🧩 Cómo hacerlo

1. Asegúrate de tener el *Form* registrado:

   ```sql
   SELECT AD_Form_ID, Name, ClassName FROM AD_Form WHERE ClassName LIKE '%AccountElements_Tree_Form%';
   ```

   Supongamos que el resultado es `AD_Form_ID = 50001`.

2. Crea un nuevo proceso en **Menu → Application Dictionary → Report & Process → New**

   | Campo             | Valor                                        |
   | ----------------- | -------------------------------------------- |
   | **Name**          | Account Elements Tree                        |
   | **ClassName**     | org.amerpsoft.form.LaunchAccountElementsForm |
   | **Access Level**  | Organization                                 |
   | **IsReport**      | No                                           |
   | **IsDirectPrint** | No                                           |

3. Crea la clase `LaunchAccountElementsForm`:

   ```java
   package org.amerpsoft.form;

   import org.compiere.process.SvrProcess;
   import org.compiere.apps.AEnv;
   import org.compiere.apps.form.FormFrame;

   public class LaunchAccountElementsForm extends SvrProcess {

       @Override
       protected void prepare() {
       }

       @Override
       protected String doIt() throws Exception {
           int AD_Form_ID = 50001; // ID real del formulario
           FormFrame ff = new FormFrame(getCtx(), AD_Form_ID, getWindowNo());
           AEnv.showWindow(ff);
           return "Form abierto: " + ff.getTitle();
       }
   }
   ```

4. Crea un **menú** para ese proceso en **Application Dictionary → Menu**
   y selecciona:

   * Action = “Process”
   * Process = “Account Elements Tree”

✅ Cuando hagas clic en ese menú → iDempiere abrirá tu Form ZK directamente.
(Internamente ejecuta tu proceso que llama a `AEnv.showWindow()`).

---

## ⚙️ 2️⃣ Caso B — Ejecutar la lógica del Form desde un *Process* (modo batch)

Esto **no mostrará la UI**, sino que usará el código del Form (tu lógica de negocio) para generar un archivo o realizar acciones.

Por ejemplo:

```java
public class RunAccountElementsFormLogic extends SvrProcess {
    @Override
    protected void prepare() {}

    @Override
    protected String doIt() throws Exception {
        AccountElements_Tree_Form form = new AccountElements_Tree_Form();
        form.runBatchLogic(); // método que implementas tú, sin UI
        return "Batch logic executed successfully.";
    }
}
```

Esto te serviría para ejecutar el mismo cálculo que el Form realiza, pero sin mostrar nada visualmente.
Es ideal si luego quieres conectarlo con REST o planificarlo con el Scheduler.

---

## 🧰 En resumen

| Objetivo                  | Método recomendado                    | Clase                         | Ejecutable desde        |
| ------------------------- | ------------------------------------- | ----------------------------- | ----------------------- |
| Abrir el formulario ZK    | `AEnv.showWindow(new FormFrame(...))` | `LaunchAccountElementsForm`   | Menú (UI)               |
| Ejecutar la lógica sin UI | Llamar a un método interno del Form   | `RunAccountElementsFormLogic` | Menú, REST, o Scheduler |

---

👉 **Siguiente paso sugerido:**
Vamos a crear juntos el proceso `LaunchAccountElementsForm` para que puedas probarlo desde el menú.
¿Quieres que te genere el código completo adaptado a tu paquete (`org.amerpsoft.form`) y con el ID real de tu Form (si me lo das)?
