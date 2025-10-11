package org.amerp.reports.xlsx;

import java.io.InputStream;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.idempiere.keikai.view.ExcelMediaView;
import io.keikai.ui.Spreadsheet;
import io.keikai.api.Importer;
import io.keikai.api.Importers;
import io.keikai.api.model.Book;
import org.zkoss.zk.ui.Component;

public class MyExcelMediaView implements org.idempiere.ui.zk.media.IMediaView {

    @Override
    public Component renderMediaView(Component container, AMedia media, boolean readOnly) {
        try {
            Spreadsheet spreadsheet = new Spreadsheet();
            container.appendChild(spreadsheet); // container debe ser Desktop o Panel Web existente
            Importer importer = Importers.getImporter();
            Book book = importer.imports(media.getStreamData(), media.getName());
            spreadsheet.setBook(book);
            return spreadsheet;
        } catch (Exception e) {
            throw new RuntimeException("Error al mostrar XLSX: " + e.getMessage(), e);
        }
    }
}
