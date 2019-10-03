package org.amerpsoft.themes.panel;

import java.io.IOException;

import org.adempiere.webui.panel.HeaderPanel;
import org.adempiere.webui.theme.ThemeManager;
import org.compiere.util.CLogger;
import org.zkoss.zul.Image;

public class AMHeaderPanel extends HeaderPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6442750584675935215L;
	static CLogger log = CLogger.getCLogger(AMHeaderPanel.class);
	
	@Override 
    protected void onCreate() { 
		//log.warning("...onCreate...AMHeaderPanel..");
        super.onCreate(); 
        Image image = (Image) getFellow("logo"); 
        if (image != null) { 
        	try {
				org.zkoss.image.Image logo = ThemeManager.getClientWebLogo();
				if (logo != null) { 
                    image.setContent(logo); 
                } else { 
                	image.setSrc(ThemeManager.getSmallLogo());
                } 
				image.setVisible(true); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    } 

}
