&lArr;[AMERPSOFT-LVE PLUGINS](../README_ES.md) | [Inicio](../README_E.md)

  <div>
	🇬🇧 <a href="README.md">English</a> | 🇪🇸 Español</a>
  </div>

# <b>LCO - Retención</b>

<a name="readme-top"></a>

## <b>Descripción</b>
AMERPSOFT LCO Withholding es una versión derivada de LCO Withholding de GlobalQss (Carlos Ruiz).

Este plugin está diseñado para resolver el requisito de Venezuela para el registro e impresión de documentos de retención. Estas modificaciones incluyen el registro de múltiples facturas.
Carlos Ruiz – GlobalQSS, desarrolló el plugin original. Esta versión se ha probado en las versiones 8.2 y 12 de Idempiere.

## <b>Documentación</b>
Para obtener información completa.
Consulte:
- [AMERPSOFT LCO Withholding - AMERPSOFT LCO Withholding.docx](./documentation/AMERPSOFT_LCO_Withholding.doc)

- [AMERPSOFT LCO Withholding - AMERPSOFT LCO Withholding.pdf](./documentation/AMERPSOFT_LCO_Withholding.pdf)

## <b>Código fuente</b>

El código fuente se puede descargar desde:

https://github.com/luisamesty/Amerpsoft-iDempiere-lve/tree/release-12/org.amerpsoft.lve.idempiere.lco.withholding

## <b>Procedimiento de instalación</b>

#### <b>1- Instalar el complemento JAR</b>
- Instalar mediante la consola web de Apache Felix:

org.amerpsoft.lve.idempiere.lco.withholding_12.0.0.XXXXXXXXXXX.jar

(La versión puede variar)
- Reiniciar el servidor
- Recuerde:
* Actualizar el acceso a roles
* Sincronizar la terminología
* Verificar la secuencia
#### <b>2- Verificar el paquete</b>
Verifique que los archivos de 2Pack se hayan actualizado y cargado correctamente.

Debe iniciar sesión como usuario del sistema.

Hay dos archivos de paquete:

- AMERPSOFT LCO Withholding (2Pack_7.1.1.zip)
- AMERPSOFT LCO Withholding Windows (2Pack_7.1.2.zip)

#### <b>3- Agregar datos de retención (Venezuela)</b>

Esto aplica a las normas de retención del SENIAT en Venezuela.

Se deben crear otros países.
- Inicie sesión en idempiere como GardenAdmin. - Empaquetar en 'AMERPSOFT LCO Withholding Data.zip'
* El archivo se encuentra en el directorio de instalación.

#### <b>4- Crear y actualizar tipos de documentos</b>

Cree los tipos de documentos como se indica en el archivo de hoja de cálculo:

<b>AMERPSOFT_Document_Types.xls</b>

Además, algunos tipos de documentos deben actualizarse como se indica.

Factura, nota de crédito y nota de débito.

#### <b>5- Crear secuencia para el tipo de documento incluido</b>

Verificar la secuencia de los documentos de retención.

Factura AP - Factura múltiple con número de retención.

3. Importar LCO_ISIC.csv

1. LCO ISIC

EXPORTAR CSV e importar para el nuevo cliente

2. Tipo de contribuyente

Crear manualmente

3. Categorías de impuestos

Exportar CSV e importar para el nuevo cliente

4. Tasas de impuestos

Exportar CSV e importar para el nuevo cliente

Ajustar la contabilidad por grupo, manualmente y mediante el comando ACTUALIZAR BD

5. Categorías de retención

EXPORTAR CSV e importar para el nuevo cliente

6. Tipos de retención

EXPORTAR CSV e importar para el nuevo cliente

7. Reglas de retención

Configuración de la regla: Ajustar manualmente

Primero crear el cálculo de retención y luego las reglas de retención

Cálculo de retención: Exportar uno por uno e importar

Reglas de retención: Exportar uno por uno e importar

Al finalizar, debería tener:

- Tasas de impuestos
- Categorías de impuestos
- Categorías de Retención
- Tipos de Retención con Reglas y Cálculo
- Ventana Factura (Proveedor) **ACTUALIZADO**
- Ventana Retención de Factura (IVA)
- Ventana Retención de Factura (MUNICIPAL)
- Ventana Factura (Cliente) **ACTUALIZADO**
- Ventana Factura (Retención de Cliente)
- Ventana Factura (Cheque de Devolución de Cliente)

<!-- CONTACTO -->
## Contacto

Estos plugins y tutoriales son cortesía de Luis Amesty de [Amerpsoft Consulting](http://amerpsoft.com/).

Para cualquier pregunta o mejora, contácteme en: [Idempiere WIKI](https://wiki.idempiere.org/en/User:Luisamesty)

## Notas de la versión:

- Actualizado para la versión 8.1 de Idempiere - noviembre de 2020
- Actualizado para la versión 11 de Idempiere - enero de 2024
- Actualizado para la versión 12 de Idempiere - marzo de 2025

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>