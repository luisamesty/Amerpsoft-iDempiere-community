package org.amerp.reports.xlsx.generator;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public interface IReportGenerator {
    
    /**
     * Genera el archivo XLSX con la estructura y lógica específica del reporte.
     * @param ctx El contexto.
     * @param windowNo El número de ventana del formulario.
     * @param parameters Mapa de parámetros de entrada (fechas, cuentas, etc.).
     * @return El archivo File generado.
     * @throws IOException Si falla la escritura del archivo.
     */
    File generate(Properties ctx, int windowNo, Map<String, Object> parameters) throws IOException;
    
    // Opcional: Para obtener el nombre base del archivo
    String getReportName(); 
}