# Selection Keys

(`Y_Venezuela_Y` o `N_Vietnam_N`) **sí se debe al Application Dictionary** y es el comportamiento nativo de iDempiere cuando el Lookup junta las **columnas de Identificador (`IsIdentifier = 'Y'`)** de la tabla original (`C_Country`).

La `Y` o `N` al inicio (y al final) corresponde a un campo booleano de la tabla `C_Country` que tiene marcada la casilla **Identificador** en el Diccionario de Datos.

---

### Causa en el Application Dictionary (AD)

En la tabla `C_Country`:

1. La columna **`IsDefault`** (o **`IsSummary`** / **`IsActive`**) o (**hasCommunity** **hasRegion** ) tienen marcada la casilla **Identificador (`IsIdentifier = 'Y'`)** con una secuencia baja (ej. Secuencia `1` o `2`).
2. La columna **`Name`** tiene Secuencia `2` o `3`.

Cuando iDempiere construye el nombre para el combo, concatena todos los campos marcados como identificadores separados por un guion bajo:
`[IsDefault] _ [Name] _ [IsSummary]` $\rightarrow$ `Y _ Venezuela _ Y`

---

### Cómo Solucionarlo desde el Application Dictionary (Sin Código)

1. Inicia sesión como **System Administrator** (`System`).
2. Abre la ventana **Tabla y Columna** (`Table & Column`).
3. Busca la tabla **`C_Country`** (País).
4. Ve a la pestaña **Columna**.
5. Revisa qué columnas tienen la casilla **Identificador** marcada (`IsIdentifier = 'Y'`):
* La columna **`Name`** DEBE tener **Identificador = Sí** (Secuencia = `1`).
* Desmarca la casilla **Identificador** en cualquier otro campo de tipo Sí/No (como `IsDefault`, `IsActive`, `IsSummary`, etc.).


6. Guarda los cambios.
7. Ve a la ventana **Cache de Sistema / Restablecer Cache** (`Reset Cache`) para aplicar el cambio inmediatamente.

---

### Solución desde Java (si estás construyendo el MLookup manualmente)

Si estás creando el `MLookupInfo` mediante código Java para ese combo específico y no quieres modificar la base de datos global, puedes indicar expresamente que solo devuelva la columna `Name`:

```java
// Al construir el MLookup, asegúrate de pedir únicamente la columna de visualización 'Name'
MLookupInfo lookupInfo = MLookupFactory.getLookupInfo(
    Env.getCtx(), 
    m_WindowNo, 
    0, 
    DisplayType.Table, 
    "C_Country_ID", 
    0, 
    false, 
    "C_Country.Name" // <--- Forzar que muestre solo el nombre
);

```