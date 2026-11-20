package org.amerp.amxeditor.rest;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	private static final String SQL_EXTENDED_TABLES = "SELECT DISTINCT t.TableName FROM AD_Column c"
			+ " JOIN AD_Table t ON t.AD_Table_ID = c.AD_Table_ID"
			+ " JOIN AD_Reference r ON r.AD_Reference_ID = c.AD_Reference_ID"
			+ " WHERE r.Name = 'LocationExtended' AND c.IsActive = 'Y' AND c.ColumnSQL IS NULL";

	private volatile Set<String> cachedTables;

	@Override
	protected void initialize() {
		// No consultar BD aquí: en iDempiere 12 puede ejecutarse antes de inicializar DB.s_cc
		registerTableEvent(IEventTopics.PO_BEFORE_NEW, "C_Location");
		registerTableEvent(IEventTopics.PO_BEFORE_CHANGE, "C_Location");
		log.info("REST LocationExtendedSaveHandler initialized (lazy table lookup)");
	}

	@Override
	protected void doHandleEvent(Event event) {
		PO po = getPO(event);
		if (po == null) {
			return;
		}

		String tableName = po.get_TableName();
		if (!getLocationExtendedTables().contains(tableName)) {
			return;
		}

		for (String column : LocationExtendedSerializerFactory.locationExtendedColumns(tableName)) {
			String attribute = LocationExtendedSerializerFactory.ATTRIBUTE_PREFIX + column;
			Object v = po.get_Attribute(attribute);
			if (!(v instanceof Integer)) {
				continue;
			}
			po.set_Attribute(attribute, null);
			int id = (Integer) v;
			if (id > 0) {
				MLocation loc = new MLocation(po.getCtx(), id, po.get_TrxName());
				if (loc.get_ID() != id || loc.getAD_Client_ID() != po.getAD_Client_ID()) {
					throw new AdempiereException("C_Location_ID " + id + " not found in this tenant");
				}
			}
			po.set_ValueNoCheck(column, id > 0 ? Integer.valueOf(id) : null);
		}
	}

	private Set<String> getLocationExtendedTables() {
		Set<String> current = this.cachedTables;
		if (current != null) {
			return current;
		}
		if (!DB.isConnected() || DB.getDatabase() == null) {
			return Collections.emptySet();
		}
		try {
			List<List<Object>> rows = DB.getSQLArrayObjectsEx(null, SQL_EXTENDED_TABLES);
			Set<String> tables = new HashSet<>();
			tables.add("C_Location");
			if (rows != null) {
				for (List<Object> row : rows) {
					if (row != null && !row.isEmpty() && row.get(0) != null) {
						tables.add(String.valueOf(row.get(0)));
					}
				}
			}
			Set<String> unmodifiable = Collections.unmodifiableSet(tables);
			this.cachedTables = unmodifiable;
			return unmodifiable;
		} catch (Exception e) {
			log.log(Level.SEVERE, "cannot read the LocationExtended columns", e);
			return Collections.singleton("C_Location");
		}
	}
}