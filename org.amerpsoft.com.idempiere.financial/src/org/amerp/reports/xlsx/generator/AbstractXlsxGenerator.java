package org.amerp.reports.xlsx.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.amerp.reports.xlsx.util.ExcelUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public abstract class AbstractXlsxGenerator implements IReportGenerator {

    // Variables protegidas para acceso desde las subclases
	protected XSSFWorkbook workbook;
	protected XSSFSheet sheet;
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
        // 🏆 Inicializar el libro de Excel AQUI
        this.workbook = new XSSFWorkbook(); 
       
        // Configurar estilos (que usan el workbook)
        setupStyles();
        
        // 1. Inicializa la hoja (Nuevo método)
        initializeSheet();
        
        // 2. 🚨 EJECUTA LÓGICA ESPECÍFICA (Ahora llamada directamente)
        int AD_Client_ID = Env.getAD_Client_ID(ctx);
        writeReportSpecificHeader(AD_Client_ID, this.parameters);
        
        // 3. Escribe las cabeceras de columna (Llamada directa)
        writeColumnHeader(this.parameters);
        
        // 4. Ejecutar el método abstracto (la lógica específica de cada reporte)
        generateReportContent();
        // Escribir el archivo
        File tempFile = writeWorkbookToFile();
        // Crear y devolver el objeto ReportMetadata
        return new ReportMetadata(
            tempFile, 
            getColumnHeaders(parameters), 
            getColumnWidths(parameters), 
            getHeaderRowCount(parameters)
        );

    }

    /**
     * Inicializa la hoja de cálculo para el reporte.
     */
    protected final void initializeSheet() {
    	// Obtener el título usando el método abstracto (que debería leer el parámetro)
        String sheetName = getReportTitle(this.parameters);
        //  Manejar Null/Empty. Si el método abstracto no devuelve nada (porque el parámetro falta)
        if (sheetName == null || sheetName.trim().isEmpty()) {
            // Fallback robusto: Usar un nombre de hoja por defecto, traducido si es posible.
            // "Reporte" o "Sheet1" es un nombre seguro.
            sheetName = Msg.getMsg(Env.getCtx(), "ReporteGenerado", true); 
            
            // Si la traducción falla o es vacía, usar el valor literal.
            if (sheetName == null || sheetName.trim().isEmpty()) {
                sheetName = "Reporte"; 
            }
        }
        // Esto podría dejar la cadena vacía si el título original eran solo caracteres inválidos.
        sheetName = sheetName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        // Segundo Fallback y límite de 31 caracteres
        if (sheetName.length() == 0) {
            sheetName = "Reporte";
        } else if (sheetName.length() > 31) {
            sheetName = sheetName.substring(0, 31);
        }
        // Crea la hoja
        this.sheet = this.workbook.createSheet(sheetName); 
    }
    
    /**
     * Define la lógica de generación del contenido: creación de la hoja, 
     * obtención de datos y escritura de filas. (Implementación obligatoria)
     */
    protected abstract void generateReportContent();
    
    /**
     * Método Abstracto: Contiene la lógica única del logo, nombre, fechas, etc.
     * CADA SUBCLASE DEBE IMPLEMENTAR ESTE MÉTODO.
     */
    protected abstract void writeColumnHeader(Map<String, Object> parameters);
    
    
    /**
     * Devuelve el título principal del reporte.
     */
    protected abstract String getReportTitle(Map<String, Object> parameters);

    /**
     * Devuelve el subtítulo o descripción del reporte.
     */
    protected abstract String getReportSubTitle(Map<String, Object> parameters);
    
    /**
     *  MÉTODO DE ENCABEZADO ESPECÍFICO (Control total para la subclase)
     * @param AD_Client_ID
     * @param parameters
     */
    protected abstract void writeReportSpecificHeader(int AD_Client_ID, Map<String, Object> parameters);
    
    // Métodos abstractos para obtener los metadatos de las subclases
 	protected abstract String[] getColumnHeaders(Map<String, Object> parameters);

 	protected abstract int[] getColumnWidths(Map<String, Object> parameters);

 	protected abstract int getHeaderRowCount(Map<String, Object> parameters);
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
        // Función auxiliar para aplicar LEFT
        java.util.function.Consumer<String> setLeft = (key) -> {
            CellStyle s = styleMap.get(key);
            if (s != null) {
                s.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
            }
        };
        // --- Lógica de Estilos de Niveles L1-L9 ---
        styleMap.put("L1", ExcelUtils.createStyle(this.workbook, 14, false));
        setLeft.accept("L1");
        styleMap.put("L2", ExcelUtils.createStyle(this.workbook, 14, false));
        setLeft.accept("L2");
        styleMap.put("L2", ExcelUtils.createStyle(this.workbook, 12, false));
        setLeft.accept("L3");
        styleMap.put("L4", ExcelUtils.createStyle(this.workbook, 12, false));
        setLeft.accept("L4");
        styleMap.put("L5", ExcelUtils.createStyle(this.workbook, 10, false));
        setLeft.accept("L5");
        styleMap.put("L6", ExcelUtils.createStyle(this.workbook, 10, false));
        setLeft.accept("L6");
        styleMap.put("L7", ExcelUtils.createStyle(this.workbook, 10, false));
        setLeft.accept("L7");
        styleMap.put("L8", ExcelUtils.createStyle(this.workbook, 10, false));
        setLeft.accept("L8");
        styleMap.put("L9", ExcelUtils.createStyle(this.workbook, 10, false));
        setLeft.accept("L9");
        
        // Versiones en negrita (isSummary = 'Y')
        styleMap.put("L1B", ExcelUtils.createStyle(this.workbook, 14, true));
        setLeft.accept("L1B");
        styleMap.put("L2B", ExcelUtils.createStyle(this.workbook, 14, true));
        setLeft.accept("L2B");
        styleMap.put("L3B", ExcelUtils.createStyle(this.workbook, 12, true));
        setLeft.accept("L3B");
        styleMap.put("L4B", ExcelUtils.createStyle(this.workbook, 12, true));
        setLeft.accept("L4B");
        styleMap.put("L5B", ExcelUtils.createStyle(this.workbook, 10, true));
        setLeft.accept("L5B");
        styleMap.put("L6B", ExcelUtils.createStyle(this.workbook, 10, true));
        setLeft.accept("L6B");
        styleMap.put("L7B", ExcelUtils.createStyle(this.workbook, 10, true));
        setLeft.accept("L7B");
        styleMap.put("L8B", ExcelUtils.createStyle(this.workbook, 10, true));
        setLeft.accept("L8B");
        styleMap.put("L9B", ExcelUtils.createStyle(this.workbook, 10, true));
        setLeft.accept("L9B");
        
        // 💯 Formatos numéricos
        // DataFormat (necesario para formatos numéricos)
        org.apache.poi.ss.usermodel.DataFormat format = workbook.createDataFormat();
        // Formato: Separador de miles, 2 decimales, y negativos en rojo
        String numericFormat = "#,##0.00;[RED]-#,##0.00"; 
        // ESTILO DE NUMERICO Normal (NUM_N)
        // Crear el estilo Base Numérico Estándar
        CellStyle numberStyle = workbook.createCellStyle();
        // Aplicar el formato de datos y alineación
        numberStyle.setDataFormat(format.getFormat(numericFormat)); 
        numberStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
        // Almacenar el estilo estándar
        styleMap.put("NUM_N", numberStyle); 
        // ESTILO DE NUMERICO BOLD (NUM_B)
        CellStyle numberStyleBold = workbook.createCellStyle();
        numberStyleBold.setDataFormat(format.getFormat(numericFormat));
        numberStyleBold.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        numberStyleBold.setFont(boldFont);
        styleMap.put("NUM_B", numberStyleBold); // Usado para R/60 (bold = true, tipo N)
        // ESTILO DE TEXTO NORMAL (TEXT_N)
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
        styleMap.put("TEXT_N", textStyle);
        // ESTILO DE TEXTO NEGRITA (TEXT_B) 
        CellStyle boldTextStyle = workbook.createCellStyle();
        boldTextStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
        boldTextStyle.setFont(boldFont);
        styleMap.put("TEXT_B", boldTextStyle);
        // ESTILO DE TEXTO NORMAL CON WRAP (TEXT_N_WRAP)
        CellStyle textStyleWrap = workbook.createCellStyle();
        textStyleWrap.cloneStyleFrom(styleMap.get("TEXT_N")); 
        textStyleWrap.setWrapText(true);
        styleMap.put("TEXT_N_WRAP", textStyleWrap);
        // ESTILO DE TEXTO NEGRITA CON WRAP (TEXT_B_WRAP)
        CellStyle boldTextStyleWrap = workbook.createCellStyle();
        boldTextStyleWrap.cloneStyleFrom(styleMap.get("TEXT_B")); 
        boldTextStyleWrap.setWrapText(true);
        styleMap.put("TEXT_B_WRAP", boldTextStyleWrap);
        // Crear estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle = workbook.createCellStyle();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.LEFT);
        styleMap.put("HEADER", headerStyle);
    }
    

    /**
     * Escribe el workbook a un archivo temporal y lo cierra.
     */
    private File writeWorkbookToFile() throws IOException {
    	// Obtener el nombre base (AccountElementsReport)
        String fileNameBase = getReportName(); 

        // Generar la ruta y el nombre final con timestamp.
        String tempDir = System.getProperty("java.io.tmpdir");
        
        // Usamos el nombre base + timestamp + extensión.
        // Esto es ahora responsabilidad de la Abstract Class.
        String finalFileName = fileNameBase + "_" + System.currentTimeMillis() + ".xlsx";
        File tempFile = new File(tempDir, finalFileName);
        
        // Escribir a disco UNA SOLA VEZ
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            workbook.write(fos);
            fos.flush();
        } finally {
        	// Cierre del libro (libera recursos escritos)
            workbook.close();
        }
        return tempFile;
    }
    
    /**
     * Devuelve el nombre base del reporte para el archivo temporal.
     */
    public String getReportName() {
        // Usa getReportTitle() para el nombre base, asegurando que se obtenga con los parámetros
        return getReportTitle(this.parameters).replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); 
    }

}