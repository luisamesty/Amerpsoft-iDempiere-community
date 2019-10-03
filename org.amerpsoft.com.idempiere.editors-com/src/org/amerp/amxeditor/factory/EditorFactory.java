/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 ******************************************************************************/
package org.amerp.amxeditor.factory;

import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WLocationEditor;
import org.adempiere.webui.factory.IEditorFactory;
import org.amerp.amxeditor.editor.WLocationExtEditor;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

/**
 * @author luisamesty
 *
 */
public class EditorFactory implements IEditorFactory {

	/* (non-Javadoc)
	 * @see org.adempiere.webui.factory.IEditorFactory#getEditor(org.compiere.model.GridTab, org.compiere.model.GridField, boolean)
	 */
	CLogger log = CLogger.getCLogger(EditorFactory.class);
	
    @Override
    public WEditor getEditor(GridTab p_gridTab, GridField p_gridField, 
    		boolean p_tableEditor) {
	    // TODO Auto-generated method stub
    	if (p_gridField == null)
    	{
    		return null;
    	}
    	WEditor editor = null;
    	int displayType = p_gridField.getDisplayType();
    	
    	if (displayType == DisplayTypeFactory.LocationExtended) {
//log.warning("..............EditorFactory........" );
//log.warning("DisplayTypeFactory: LocationExtended ");
    		editor = new WLocationExtEditor(p_gridField, p_gridTab);
    		//editor = new WLocationEditor(p_gridField);
    	}
    	if(editor != null)
    		editor.setTableEditor(p_tableEditor);
	    return editor;
    }

}
