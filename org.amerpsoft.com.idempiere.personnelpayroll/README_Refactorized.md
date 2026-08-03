# **LPY-49**

Refactorizar Clases de Cálculo de Nóminas.  
Rama de desarrollo release-11\_LPY-49\_Refactorizar  
La clase **AmerpPayrollCalc.java,** contiene varios métodos y tiene mucha codigo responsable del cálculo. Se pretende separar en varias clases para distribuir la carga de codigo sobre una sola clase.

## Clases Involucradas Cálculo de Nóminas.

### Paquete src/org/amerp/amnutilities:

*   **AmerpPayrollCalc.java**. Clase original a refactorizar.
*   **AmerpPayrollCalcArray.java**. Nueva
*   **AmerpPayrollCalcUtilDVFormulas.java.** Clase a refactorizar.
*   **PayrollScriptEngine.java**. Nueva
*   **PayrollVariables.java**. Nueva
*   **PayrollVariablesLoad.java**. Nueva
*   **PayrollVariablesMemoryVars**.java. Nueva
*   **PayrollVariablesTest.java**. Nueva
*   **ScriptResult**.java. Nueva
    

### Paquete Callouts src/org/amerp/amncallouts

*   AMN\_Payroll\_Detail\_callout.java
*   AMN\_Payroll\_Detail\_Qty\_callout.java

### Paquete Modelos src/org/amerp/amnmodel

*   MAMN\_Concept\_Types.java
*   MAMN\_Payroll\_Detail.java
*   MAMN\_Payroll.java

### Paquete Procesos src/org/amerp/process

*   AMNPayrollCreateDocs.java
*   AMNPayrollCreateOnePeriod.java
*   AMNPayrollRefresh.java
*   AMNPayrollRefreshOnePeriod.java
*   AMNPayrollRefreshOnePeriodConcept.java
*   AMNPayrollTransferPayrollAssist.java
    


## Caso AMN_Employee

💡 Estrategia para Eliminar Campos Innecesarios en iDempiere

Eliminar campos de la tabla `AMN_Employee` que fueron agregados durante una migración requiere una estrategia cuidadosa para asegurar que no se rompa la funcionalidad, la interfaz de usuario, o los reportes en tu modelo de iDempiere.

La estrategia clave es la **Revisión Inversa (Reverse Lookup)** de las referencias a estos campos.

### 1\. 🔍 Revisión en el Application Dictionary (Diccionario de Aplicación)

El primer paso es verificar si iDempiere está utilizando estos campos a nivel de metadatos (configuración).

*   **Ventana Columna y Campo (**`**AD_Column**`**):**
    
    *   Busca la tabla `AMN_Employee`.
    *   Identifica cada campo (columna) que deseas eliminar.
    *   **Acción:** Una vez identificados, puedes cambiar el estado de la columna a **&quot;Inactivo&quot; (**`**IsActive = 'N'**`**)**.
    *   **Beneficio:** Si la desactivación no causa errores en las pruebas subsiguientes, puedes estar seguro de que al menos el código base de iDempiere y los formularios estándar no los requieren. _No los elimines todavía de la base de datos_.
*   **Campos de Referencia (**`**AD_Reference**`**):**
    *   Verifica si alguno de los campos de tipo **ID** (como `C_Bpartner_TO_ID`, `C_JOBSHIFT_ID`, etc.) está siendo utilizado como **Referencia de Búsqueda (Search Reference)**, en **Validación de Reglas (Validation Rule)** o en **Display Logic** de otros campos o ventanas.
        
*   **Lógica en Campos/Pestañas (**`**Display Logic**`**,** `**Read Only Logic**`**):**
    *   Revisa la configuración de otros campos o pestañas en la ventana `Empleado` (`AMN_Employee`) para asegurar que no dependan del valor de estos campos para su visibilidad o editabilidad.
        

### 2\. 💻 Revisión de Código y Callouts (Procesos)

Esta es la parte más crítica, ya que el código Java no se registrará en el Application Dictionary.

*   **Callouts y Modelos (Java):**
    *   Los campos que se llenan o se utilizan para calcular otros valores generalmente están en los **Modelos de Entidad (ej.** `**ModelValidator**` **o** `**PO**` **listeners)** o en los **Callouts**.
    *   **Estrategia:** Realiza una **búsqueda de texto completa (Full-Text Search)** en el código fuente de tu proyecto iDempiere (en el IDE como Eclipse o VS Code) por el **nombre exacto de la columna** (ej. `MOLI_IRPPERCENT`).
    *   **Archivos a buscar:**
        *   Archivos de la lógica de tu módulo (`AMN_`).
        *   Clases `ModelValidator` que afecten a `AMN_Employee`.
        *   Clases `Callout` asociadas a la ventana Empleado.
        *   Cualquier proceso o formulario personalizado.
            
*   **Identificación:** Si la búsqueda devuelve resultados, el código está interactuando con el campo y **no puedes eliminarlo** sin modificar la lógica de negocio.
    

### 3\. 📊 Revisión de Reportes y Formularios Personalizados (Jasper/BIRT)

Los reportes suelen acceder directamente a la base de datos sin pasar por la capa de lógica de iDempiere.

*   **Reportes Jasper/BIRT:**
    *   Busca en los archivos `.jrxml` o `.rptdesign` (reportes) la cadena del **nombre de la columna**.
    *   **Estrategia:** Si un reporte utiliza el campo, debes **modificar el reporte** para eliminar la referencia o el cálculo basado en ese campo.
        
*   **Vistas y Consultas (SQL):**
    *   Verifica si alguna **Vista (**`**AD_View**`**)** o **Consulta de Búsqueda (**`**AD_Process**` **\-&gt; SQL)** utiliza estos campos como parte de su definición. Si los utilizan, eliminarlos romperá la Vista/Consulta.
        

### 4\. ✅ Etapas de Pruebas y Eliminación

Una vez que has marcado los campos como inactivos y has revisado el código, procede con las pruebas en un **ambiente de desarrollo/QA**.

<figure class="table op-uc-figure_align-center op-uc-figure"><table class="op-uc-table"><thead class="op-uc-table--head"><tr class="op-uc-table--row"><th class="op-uc-table--cell op-uc-table--cell_head"><p class="op-uc-p">Etapa</p></th><th class="op-uc-table--cell op-uc-table--cell_head"><p class="op-uc-p">Acción</p></th><th class="op-uc-table--cell op-uc-table--cell_head"><p class="op-uc-p">Resultado Esperado</p></th></tr></thead><tbody><tr class="op-uc-table--row"><td class="op-uc-table--cell"><p class="op-uc-p"><strong>Inactivación</strong></p></td><td class="op-uc-table--cell"><p class="op-uc-p">Desactiva las columnas en <code class="op-uc-code">AD_Column</code>.</p></td><td class="op-uc-table--cell"><p class="op-uc-p">La ventana <code class="op-uc-code">Empleado</code> debe funcionar sin errores.</p></td></tr><tr class="op-uc-table--row"><td class="op-uc-table--cell"><p class="op-uc-p"><strong>Prueba de Uso</strong></p></td><td class="op-uc-table--cell"><p class="op-uc-p">Ingresa, edita y guarda un nuevo empleado. Ejecuta procesos clave de nómina.</p></td><td class="op-uc-table--cell"><p class="op-uc-p">Ningún error de ejecución o <i>null pointer</i> asociado a los campos desactivados.</p></td></tr><tr class="op-uc-table--row"><td class="op-uc-table--cell"><p class="op-uc-p"><strong>Prueba de Reportes</strong></p></td><td class="op-uc-table--cell"><p class="op-uc-p">Ejecuta todos los reportes de nómina y los formularios clave.</p></td><td class="op-uc-table--cell"><p class="op-uc-p">Los reportes deben correr sin errores de columna no encontrada.</p></td></tr><tr class="op-uc-table--row"><td class="op-uc-table--cell"><p class="op-uc-p"><strong>Eliminación Lógica</strong></p></td><td class="op-uc-table--cell"><p class="op-uc-p">Si las pruebas pasan, <strong>elimina los registros</strong> de las columnas de la tabla <code class="op-uc-code">AD_Column</code>.</p></td><td class="op-uc-table--cell"><p class="op-uc-p">El Application Dictionary ya no tiene referencia a estos campos.</p></td></tr><tr class="op-uc-table--row"><td class="op-uc-table--cell"><p class="op-uc-p"><strong>Eliminación Física (Final)</strong></p></td><td class="op-uc-table--cell"><p class="op-uc-p">Ejecuta un comando <code class="op-uc-code">ALTER TABLE AMN_Employee DROP COLUMN ...</code> directamente en la base de datos.</p></td><td class="op-uc-table--cell"><p class="op-uc-p">La tabla queda limpia, con la menor probabilidad de conflicto.</p></td></tr></tbody></table></figure>

> **Recomendación Final:** Comienza por los campos que son puramente informativos (como `URL` o `SocialSecurityNO`) y deja los campos de referencia (`*_ID`) para el final, ya que tienen más posibilidades de ser utilizados en _joins_ o _validations_.


**Ventanas de Trabajadores Limpiar**

Eliminar los campos segun la Query.

Son todos los campos de `'Field Group'='Employee MOLI Information'`
Borrar a amano si queda alguno.

```SQL
DELETE FROM AD_Field
WHERE AD_Column_ID IN (
    SELECT AD_Column_ID
    FROM AD_Column
    WHERE AD_Table_ID = (
            SELECT AD_Table_ID FROM AD_Table WHERE TableName = 'AMN_Employee'
        )
        AND ColumnName IN (
	   'URL',
	   'C_Bpartner_TO_ID',
	   'C_CONSOLIDATIONREFERENCE_ID',
	   'C_GMR_KEZO_BAR',
	   'C_JOBSHIFT_ID',
	   'COMMISIONGOAL',
	   'COMMISSIONSGROUP',
	   'FIRSTHIREDATE',
	   'HIREDATE',
	   'IDCOUNTRY',
	   'ISALLOWBLANKETPO',
	   'ISALLOWBLANKETSO',
	   'ISALLOWCUSTOMERCONSIGNED',
	   'ISBPASSIGNED',
	   'ISALLOWVENDORCONSIGNED',
	   'ISFIRSTHIREDATE',
	   'ISEDI',
	   'ISGMRKENZO',
	   'ISMINIMUMWAGE',
	   'ISPERMITREQUIRED',
	   'ISPROMOTER',
	   'isSocialSecurity',
	   'ISSNDHIREDATE',
	   'ISSSPROPORTIONAL',
	   'ISSSPRIVATE',
	   'MOLI_ISIRP',
	   'JRCURRENCY_ID',
	   'JobRemuneration',
	   'MOLI_BPARTNERTYPE',
	   'MOLI_EMPLOYERID',
	   'MOLI_IRPPERCENT',
	   'MOLI_PHOTOID',
	   'MOLI_SSID',
	   'MOLI_TYPEDOCUMENT',
	   'SNDHIREDATE',
	   'SocialSecurityNO',
	   'SSAMOUNT',
	   'SSCURRENCY_ID',
	   'SSENTITY',
	   'SSPRIVATEAMOUNT',
	   'SSPRIVATECURRENCY_ID',
	   'SSPRIVATEENTITY',
	   'SSPRIVATENO',
	   'SSPROPORTIONALAMT'
        )
);
```

**Tabla de Trabajadores , Limpiar.**


```SQL
UPDATE AD_Column
SET IsActive = 'N'
WHERE AD_Table_ID = (
       SELECT AD_Table_ID FROM AD_Table WHERE TableName = 'AMN_Employee'
   )
AND ColumnName IN (
   'URL',
   'C_Bpartner_TO_ID',
   'C_CONSOLIDATIONREFERENCE_ID',
   'C_GMR_KEZO_BAR',
   'C_JOBSHIFT_ID',
   'COMMISIONGOAL',
   'COMMISSIONSGROUP',
   'FIRSTHIREDATE',
   'HIREDATE',
   'IDCOUNTRY',
   'ISALLOWBLANKETPO',
   'ISALLOWBLANKETSO',
   'ISALLOWCUSTOMERCONSIGNED',
   'ISBPASSIGNED',
   'ISALLOWVENDORCONSIGNED',
   'ISFIRSTHIREDATE',
   'ISEDI',
   'ISGMRKENZO',
   'ISMINIMUMWAGE',
   'ISPERMITREQUIRED',
   'ISPROMOTER',
   'isSocialSecurity',
   'ISSNDHIREDATE',
   'ISSSPROPORTIONAL',
   'ISSSPRIVATE',
   'MOLI_ISIRP',
   'JRCURRENCY_ID',
   'JobRemuneration',
   'MOLI_BPARTNERTYPE',
   'MOLI_EMPLOYERID',
   'MOLI_IRPPERCENT',
   'MOLI_PHOTOID',
   'MOLI_SSID',
   'MOLI_TYPEDOCUMENT',
   'SNDHIREDATE',
   'SocialSecurityNO',
   'SSAMOUNT',
   'SSCURRENCY_ID',
   'SSENTITY',
   'SSPRIVATEAMOUNT',
   'SSPRIVATECURRENCY_ID',
   'SSPRIVATEENTITY',
   'SSPRIVATENO',
   'SSPROPORTIONALAMT'
);
```

