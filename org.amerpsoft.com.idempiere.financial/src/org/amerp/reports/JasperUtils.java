package org.amerp.reports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class JasperUtils {

	public JasperUtils() {
		super();
	}

	public String getTempFolder() throws Exception {
	    // Carpeta temporal
	    String tmpFolder = System.getProperty("java.io.tmpdir") + File.separator + "idempiere_reports" + File.separator;
	
	    // Crear carpeta base
	    File tmpFolderFile = new File(tmpFolder);
	    if (!tmpFolderFile.exists()) {
	        boolean created = tmpFolderFile.mkdirs();
	        if (!created) {
	            throw new Exception("No se pudo crear el directorio temporal: " + tmpFolder);
	        }
	    }
	    // Retunrs folder
		return tmpFolder;
	}    
	
	
	public void copyResourceToTmp(String resourceName, String tmpFolder) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);
        if (resourceUrl == null) {
            throw new IOException("No se encontró el recurso: " + resourceName);
        }
        String localName = resourceName;
        if (localName.startsWith("/")) {
            localName = localName.substring(1);
        }
        // Convertir la carpeta padre en un nombre con _
        String parentPath = localName.substring(0, localName.lastIndexOf("/")).replace("/", "_");
        String fileName = localName.substring(localName.lastIndexOf("/") + 1);

        Path targetFolder = Path.of(tmpFolder, parentPath);
        if (!Files.exists(targetFolder)) {
            Files.createDirectories(targetFolder);
        }
        Path destFile = targetFolder.resolve(fileName);

        try (InputStream in = resourceUrl.openStream()) {
            Files.copy(in, destFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
