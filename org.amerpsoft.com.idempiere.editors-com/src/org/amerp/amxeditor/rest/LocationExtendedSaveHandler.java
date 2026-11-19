package org.amerp.amxeditor.rest;

import java.util.List;
import java.util.logging.Level;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MLocation;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

/**
 * Second half of LocationExtendedSerializerFactory: before a record of a table with LocationExtended columns is saved,
 * the id the REST received (PO attribute AMX_REST_&lt;column&gt;) is put in the column. Records saved from the
 * screen or by code never carry the attribute and are not touched.
 */
public class LocationExtendedSaveHandler extends AbstractEventHandler {

   private static final CLogger log = CLogger.getCLogger(LocationExtendedSaveHandler.class);

   @Override
   protected void initialize() {
      String sql = "SELECT DISTINCT t.TableName FROM AD_Column c"
            + " JOIN AD_Table t ON t.AD_Table_ID = c.AD_Table_ID"
            + " JOIN AD_Reference r ON r.AD_Reference_ID = c.AD_Reference_ID"
            + " WHERE r.Name = 'LocationExtended' AND c.IsActive = 'Y' AND c.ColumnSQL IS NULL";
      try {
         List<List<Object>> rows = DB.getSQLArrayObjectsEx(null, sql);
         if (rows != null) {
            for (List<Object> row : rows) {
               String table = String.valueOf(row.get(0));
               registerTableEvent(IEventTopics.PO_BEFORE_NEW, table);
               registerTableEvent(IEventTopics.PO_BEFORE_CHANGE, table);
               log.info("REST LocationExtended: " + table);
            }
         }
      } catch (Exception e) {
         log.log(Level.SEVERE, "cannot read the LocationExtended columns", e);
      }
   }

   @Override
   protected void doHandleEvent(Event event) {
      PO po = getPO(event);
      for (String column : LocationExtendedSerializerFactory.locationExtendedColumns(po.get_TableName())) {
         String attribute = LocationExtendedSerializerFactory.ATTRIBUTE_PREFIX + column;
         Object v = po.get_Attribute(attribute);
         if (!(v instanceof Integer))
            continue;
         po.set_Attribute(attribute, null);
         int id = (Integer) v;
         if (id > 0) {
            MLocation loc = new MLocation(po.getCtx(), id, po.get_TrxName());
            if (loc.get_ID() != id || loc.getAD_Client_ID() != po.getAD_Client_ID())
               throw new AdempiereException("C_Location_ID " + id + " not found in this tenant");
         }
         po.set_ValueNoCheck(column, id > 0 ? Integer.valueOf(id) : null);
      }
   }
}
