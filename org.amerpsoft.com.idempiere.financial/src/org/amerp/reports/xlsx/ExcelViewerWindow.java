package org.amerp.reports.xlsx;

import java.io.File;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Tabpanel;
import org.adempiere.webui.desktop.IDesktop;
import org.adempiere.webui.panel.ITabOnCloseHandler;
import org.adempiere.webui.session.SessionManager;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.South;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import java.util.logging.Logger;
import org.zkoss.zul.*;

/**
 * Ventana que muestra un Excel en un Tab dentro del Desktop de iDempiere.
 */
public class ExcelViewerWindow extends org.adempiere.webui.component.Window implements ITabOnCloseHandler {


    private static final long serialVersionUID = 1134202607761200859L;

	private static final Logger log = Logger.getLogger(ExcelViewerWindow.class.getName());

	private int m_WindowNo = -1;
    private Borderlayout mainLayout;
    private Center center;
    private South south;

    private File xlsxFile;
    private String[] headers;
    private int[] widths;
    private int headerRowCount;
    private int maxVisibleRows;

	public ExcelViewerWindow(File xlsxFile, String[] headers, int[] widths, int headerRowCount, int maxVisibleRows) {

		this.xlsxFile = xlsxFile;
		this.headers = headers;
		this.widths = widths;
		this.headerRowCount = headerRowCount;
		this.maxVisibleRows = maxVisibleRows;

		m_WindowNo = SessionManager.getAppDesktop().registerWindow(this);
		setAttribute(IDesktop.WINDOWNO_ATTRIBUTE, m_WindowNo);
		
		setTitle("Visor Xlsx");
		setWidth("100%");
		setHeight("100%");
//		setBorder("normal");
//		setSizable(true);
		setClosable(true);

		// === Layout principal ===
		mainLayout = new Borderlayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		appendChild(mainLayout);

		// === Center ===
		center = new Center();
//		center.setVflex("1");
//		center.setHflex("1");
		mainLayout.appendChild(center);

		try {
			if (!xlsxFile.exists()) {
				log.warning("Archivo Excel no encontrado: " + xlsxFile.getAbsolutePath());
			} else {
				Div viewer = ExcelViewerFactory.createViewer(xlsxFile, headers, widths, headerRowCount, maxVisibleRows);
				center.appendChild(viewer);
			}
		} catch (Exception e) {
			log.severe("Error cargando Excel: " + e.toString());
		}

		// === South ===
		south = new South();
		south.setHeight("60px");
		mainLayout.appendChild(south);

		Hbox hbox = new Hbox();
		hbox.setSpacing("5px");

		Button btnClose = new Button("Cerrar");
		btnClose.addEventListener(Events.ON_CLICK, event -> dispose());

		Button btnDownload = new Button("Descargar");
		btnDownload.addEventListener(Events.ON_CLICK, event -> {
			try {
				if (xlsxFile.exists()) {
					Filedownload.save(xlsxFile, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				}
			} catch (Exception e) {
				log.severe("Error al descargar Excel: " + e.getMessage());
			}
		});

		hbox.appendChild(btnClose);
		hbox.appendChild(btnDownload);
		south.appendChild(hbox);
	}


    public void dispose() {
        try {
            SessionManager.getAppDesktop().closeActiveWindow();
        } catch (Exception e) {
            log.warning("Error cerrando ventana ExcelViewerWindow: " + e.getMessage());
        }
    }

	@Override
	public void onClose(Tabpanel tabPanel) {
		dispose();
		
	}
}
