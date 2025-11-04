package org.amerp.reports.xlsx;

import org.adempiere.webui.Extensions; // <-- ¡Necesitas esta clase!
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.idempiere.ui.zk.media.IMediaView; // Interfaz que implementa ExcelMediaView
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.ClientInfo; // Para saber si estamos en móvil
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Div;
import org.compiere.util.Env;

import java.io.File;
import java.nio.file.Files;

import org.zkoss.zk.ui.Component;

// Importaciones de tu proyecto
// import org.amerp.reports.xlsx.ExcelViewerPanel;
//// 1. Visor de Excel (usando tu clase actual)
//if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
//  
//  // Si el archivo es Excel, usamos tu ExcelViewerPanel
//  return new ExcelViewerPanel(file.getAbsolutePath(), headers, widths, headerRowCount, maxVisibleRows);
//
//} 

public class ExcelViewerFactory {

	private static final CLogger log = CLogger.getCLogger(ExcelViewerFactory.class);
	
    public static Div createViewer(File file, String[] headers, int[] widths, int headerRowCount, int maxVisibleRows) 
            throws Exception {
        
        String fileName = file.getName();
        String mimeType = getMimeType(fileName); // Debes crear este método auxiliar
        String extension = getExtension(fileName); // Debes crear este método auxiliar
        
        // --- Lógica de Integración de Keikai ---
        if (extension.equals("xlsx") || extension.equals("xls")) {
        	
        	// Obtener el límite de tamaño de previsualización de SysConfig
            int maxPreviewSize = MSysConfig.getIntValue(
                MSysConfig.ZK_MAX_ATTACHMENT_PREVIEW_SIZE, 
                1048576, // 1 MB
                Env.getAD_Client_ID(Env.getCtx())
            );

            // Comprobación y Excepción
            if (file.length() > maxPreviewSize) {
                throw new AdempiereException("El archivo es demasiado grande para previsualizarlo. Límite: " 
                    + (maxPreviewSize / 1024 / 1024) + " MB.");
            }
        	
        	// Cargar el archivo completo en memoria (byte[])
            byte[] fileData = Files.readAllBytes(file.toPath());

            // 1. Obtener la implementación del visor
            IMediaView view = Extensions.getMediaView(mimeType, extension, ClientInfo.isMobile());

            if (view != null) {
                
                // 2. Crear AMedia usando el byte[] (ya no depende de InputStream)
                AMedia media = new AMedia(fileName, null, mimeType, fileData); // 👈 ¡Cambio clave!
                
                // 3. Crear el Div contenedor (para evitar ClassLoader issues)
                org.zkoss.zul.Div container = new org.zkoss.zul.Div();
                
                // 4. Renderizar (cargar) el objeto Media.
                Component keikaiComponent = ((IMediaView) view).renderMediaView(container, media, true); 
                
                // 5. Configurar y retornar
                if (keikaiComponent != null) {
                    container.appendChild(keikaiComponent);
                }
                
                container.setVflex("1");
                container.setHflex("1");
                
                return container;
            }
 
        }  else {
                // ...
                return new Div();
        }
		return null;
    }
    // Métodos Auxiliares
    private static String getExtension(String name) {
        // Implementación simple basada en WAttachment
        int index = name.lastIndexOf(".");
        return (index > 0) ? name.substring(index + 1) : "";
    }
    
    private static String getMimeType(String fileName) {
        String ext = getExtension(fileName);
        // Implementación simple de MIME Type (debe ser más robusta)
        if (ext.equals("xlsx")) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if (ext.equals("xls")) return "application/vnd.ms-excel";
        // Añadir otros tipos MIME si es necesario
        return "application/octet-stream";
    }
}