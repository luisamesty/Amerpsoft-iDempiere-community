package org.amerp.reports.xlsx.generator;

import java.io.File;

public class ReportMetadata {
    private final File reportFile;
    private final String[] headers;
    private final int[] widths;
    private final int headerRows;
    
    public ReportMetadata(File reportFile, String[] headers, int[] widths, int headerRows) {
        this.reportFile = reportFile;
        this.headers = headers;
        this.widths = widths;
        this.headerRows = headerRows;
    }

	// Getters
	public File getReportFile() {
		return reportFile;
	}

	public String[] getHeaders() {
		return headers;
	}

	public int[] getWidths() {
		return widths;
	}

	public int getHeaderRows() {
		return headerRows;
	}

	// Y un método auxiliar para obtener la ruta
	public String getFullPath() {
		return reportFile.getAbsolutePath();
	}
	
}