package org.amerp.reports.xlsx.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.amerp.reports.xlsx.util.ExcelUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

public abstract class AbstractXlsxGenerator implements IReportGenerator {

    // Variables protegidas para acceso desde las subclases
	protected SXSSFWorkbook workbook; // <--- ¡DEBE SER SXSSFWorkbook!
	protected SXSSFSheet sheet;
	protected XSSFCellStyle styleHeader; 
    protected Properties ctx;
    protected int windowNo;
    protected Map<String, Object> parameters;
    protected Map<String, CellStyle> styleMap;
    
    @Override
    public final ReportMetadata generate(Properties ctx, int windowNo, Map<String, Object> parameters) throws IOException {
        this.ctx = ctx;
        this.windowNo = windowNo;
        this.parameters = parameters;
        // Inicializar el libro de Excel como SXSSFWorkbook, que es la única que soporta SXSSFSheet
        this.workbook = new SXSSFWorkbook(100); // 100 filas en memoria antes de escribir a disco
        this.workbook.setCompressTempFiles(true); // BUENA PRÁCTICA DE SXSSF
        
        // Configurar estilos y hoja inicial
        setupStyles();
        
        // Ejecutar el método abstracto (la lógica específica de cada reporte)
        generateReportContent();
        
        // Escribir el archivo
        File tempFile = writeWorkbookToFile();
        // Crear y devolver el objeto ReportMetadata
        return new ReportMetadata(
            tempFile, 
            getColumnHeaders(), 
            getColumnWidths(), 
            getHeaderRowCount()
        );

    }

    /**
     * Define la lógica de generación del contenido: creación de la hoja, 
     * obtención de datos y escritura de filas. (Implementación obligatoria)
     */
    protected abstract void generateReportContent();
    /**
     * Crea un método para configurar los estilos comunes del reporte.
     */
    protected void setupStyles() {
        // Crear estilos de cabecera, fecha, números, etc.
        styleHeader = (XSSFCellStyle) workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        styleHeader.setFont(font);
        this.styleMap = new java.util.HashMap<>();
        // --- Lógica de Estilos de Niveles L1-L9 ---
        styleMap.put("L1", ExcelUtils.createStyle(this.workbook, 14, false));
        styleMap.put("L2", ExcelUtils.createStyle(this.workbook, 14, false));
        styleMap.put("L3", ExcelUtils.createStyle(this.workbook, 12, false));
        styleMap.put("L4", ExcelUtils.createStyle(this.workbook, 12, false));
        styleMap.put("L5", ExcelUtils.createStyle(this.workbook, 10, false));
        styleMap.put("L6", ExcelUtils.createStyle(this.workbook, 10, false));
        styleMap.put("L7", ExcelUtils.createStyle(this.workbook, 10, false));
        styleMap.put("L8", ExcelUtils.createStyle(this.workbook, 10, false));
        styleMap.put("L9", ExcelUtils.createStyle(this.workbook, 10, false));

        // Versiones en negrita (isSummary = 'Y')
        styleMap.put("L1B", ExcelUtils.createStyle(this.workbook, 14, true));
        styleMap.put("L2B", ExcelUtils.createStyle(this.workbook, 14, true));
        styleMap.put("L3B", ExcelUtils.createStyle(this.workbook, 12, true));
        styleMap.put("L4B", ExcelUtils.createStyle(this.workbook, 12, true));
        styleMap.put("L5B", ExcelUtils.createStyle(this.workbook, 10, true));
        styleMap.put("L6B", ExcelUtils.createStyle(this.workbook, 10, true));
        styleMap.put("L7B", ExcelUtils.createStyle(this.workbook, 10, true));
        styleMap.put("L8B", ExcelUtils.createStyle(this.workbook, 10, true));
        styleMap.put("L9B", ExcelUtils.createStyle(this.workbook, 10, true));
        
        // 1. Crear el DataFormat (necesario para formatos numéricos)
        org.apache.poi.ss.usermodel.DataFormat format = workbook.createDataFormat();
        // Formato: Separador de miles, 2 decimales, y negativos en rojo
        String numericFormat = "#,##0.00;[RED]-#,##0.00"; 
        // 2. Crear el estilo Base Numérico Estándar
        CellStyle numberStyle = workbook.createCellStyle();
        // 3. Aplicar el formato de datos y alineación
        numberStyle.setDataFormat(format.getFormat(numericFormat)); 
        numberStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
        // 4. Almacenar el estilo estándar
        styleMap.put("N_STANDARD", numberStyle); 
        // Opcional: Crear el estilo Numérico Estándar Negrita
        CellStyle numberStyleBold = workbook.createCellStyle();
        numberStyleBold.setDataFormat(format.getFormat(numericFormat));
        numberStyleBold.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        numberStyleBold.setFont(boldFont);
        styleMap.put("BN", numberStyleBold); // Usado para R/60 (bold = true, tipo N)
        // 
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
        styleMap.put("N", textStyle);
        styleMap.put("L1",  textStyle);
    }
    

    /**
     * Método de Plantilla (FINAL) que coordina la escritura del encabezado.
     */
    protected final void writeClientHeader(int AD_Client_ID) {
        
        // 1. Obtiene la hoja y el contexto (Lógica de infraestructura)
        this.sheet = (SXSSFSheet) this.workbook.createSheet(getReportName());
        
        // 2. Ejecuta la lógica Específica (Delegado a las subclases)
        writeReportSpecificHeader(AD_Client_ID);
        
        // 3. Escribe las cabeceras de columna (Esto es común o se puede delegar)
        writeColumnHeader();
    }

    /**
     * Método Abstracto: Contiene la lógica única del logo, nombre, fechas, etc.
     * CADA SUBCLASE DEBE IMPLEMENTAR ESTE MÉTODO.
     */
    protected abstract void writeReportSpecificHeader(int AD_Client_ID);

    /**
     * Método Abstracto: Contiene la lógica de las cabeceras de columna (Value, Name, etc.)
     * CADA SUBCLASE DEBE IMPLEMENTAR ESTE MÉTODO.
     */
    protected abstract void writeColumnHeader();

 
    /**
     * Escribe el workbook a un archivo temporal y lo cierra.
     */
    private File writeWorkbookToFile() throws IOException {
    	// 1. Obtener el nombre base (AccountElementsReport)
        String fileNameBase = getReportName(); 

        // 2. Generar la ruta y el nombre final con timestamp.
        String tempDir = System.getProperty("java.io.tmpdir");
        
        // Usamos el nombre base + timestamp + extensión.
        // Esto es ahora responsabilidad de la Abstract Class.
        String finalFileName = fileNameBase + "_" + System.currentTimeMillis() + ".xlsx";
        File tempFile = new File(tempDir, finalFileName);
        
        // Si necesitas la ruta completa globalmente, deberías pasarla/guardarla aquí.
        // fullPath = tempFile.getAbsolutePath(); 
        
        // 3. Escribir a disco UNA SOLA VEZ
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            workbook.write(fos);
        } finally {
        	// 🚨 Cierre del libro (libera recursos escritos)
            workbook.close();
            // 💡 CRÍTICO: Elimina los archivos temporales creados por el SXSSFWorkbook
            if (workbook != null) {
                workbook.dispose(); 
            }
        }
        return tempFile;
    }
    
    // Métodos abstractos para obtener los metadatos de las subclases
	protected abstract String[] getColumnHeaders();

	protected abstract int[] getColumnWidths();

	protected abstract int getHeaderRowCount();
}