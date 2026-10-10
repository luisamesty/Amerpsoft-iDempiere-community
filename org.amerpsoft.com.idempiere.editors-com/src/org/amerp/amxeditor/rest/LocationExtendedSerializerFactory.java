package org.amerp.amxeditor.rest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.model.MLocation;
import org.compiere.model.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.trekglobal.idempiere.rest.api.json.DefaultPOSerializer;
import com.trekglobal.idempiere.rest.api.json.IPOSerializer;
import com.trekglobal.idempiere.rest.api.json.IPOSerializerFactory;

/**
 * REST serializer for tables that have columns with Amerpsoft's reference «LocationExtended» (Monalisa:
 * C_BPartner_Location.C_Location_ID). The iDempiere REST has no type converter for that reference, so it leaves the
 * column out when reading and drops it when writing. This factory (service.ranking 100, above the default 0) answers
 * only for those tables: a proxy delegates every call to the REST's own DefaultPOSerializer and then
 * <ul>
 * <li>fromJson: copies the column from the JSON to the PO (before the REST saves it);</li>
 * <li>toJson: adds the column to the JSON as {"id", "identifier", "model-name": "c_location"}.</li>
 * </ul>
 * A proxy and not a subclass, so it works with any REST version (the overloads of IPOSerializer change between them).
 * For every other table it answers null and the REST uses its default serializer.
 */
public class LocationExtendedSerializerFactory implements IPOSerializerFactory {

   private static final CLogger log = CLogger.getCLogger(LocationExtendedSerializerFactory.class);
   /** PO attribute that carries the id sent by the REST until the save: prefix + column name */
   public static final String ATTRIBUTE_PREFIX = "AMX_REST_";
   /** table name (lower case) → names of its LocationExtended columns (empty array: none) */
   private static final CCache<String, String[]> columnsCache = new CCache<>("AMX_RestLocationExtColumns", 50, 120);

   @Override
   public IPOSerializer getPOSerializer(String tableName, Class<?> modelClass) {
      String[] columns = locationExtendedColumns(tableName);
      if (columns.length == 0)
         return null;
      IPOSerializer delegate = new DefaultPOSerializer();
      return (IPOSerializer) Proxy.newProxyInstance(IPOSerializer.class.getClassLoader(),
            new Class<?>[] { IPOSerializer.class }, new Handler(delegate, columns));
   }

   public static String[] locationExtendedColumns(String tableName) {
      if (tableName == null)
         return new String[0];
      String key = tableName.toLowerCase();
      String[] cached = columnsCache.get(key);
      if (cached != null)
         return cached;
      List<String> names = new ArrayList<>();
      String sql = "SELECT c.ColumnName FROM AD_Column c"
            + " JOIN AD_Table t ON t.AD_Table_ID = c.AD_Table_ID"
            + " JOIN AD_Reference r ON r.AD_Reference_ID = c.AD_Reference_ID"
            + " WHERE LOWER(t.TableName) = ? AND r.Name = 'LocationExtended' AND c.IsActive = 'Y'"
            + " AND c.ColumnSQL IS NULL";
      try {
         List<List<Object>> rows = DB.getSQLArrayObjectsEx(null, sql, key);
         if (rows != null)
            for (List<Object> row : rows)
               names.add(String.valueOf(row.get(0)));
      } catch (Exception e) {
         log.log(Level.WARNING, "cannot read the LocationExtended columns of " + tableName, e);
      }
      String[] result = names.toArray(new String[0]);
      columnsCache.put(key, result);
      return result;
   }

   static final class Handler implements InvocationHandler {
      private final IPOSerializer delegate;
      private final String[] columns;

      Handler(IPOSerializer delegate, String[] columns) {
         this.delegate = delegate;
         this.columns = columns;
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         Object result;
         try {
            result = method.invoke(delegate, args);
         } catch (InvocationTargetException e) {
            throw e.getCause();
         }
         if (args == null || args.length == 0)
            return result;
         if ("fromJson".equals(method.getName()) && result instanceof PO && args[0] instanceof JsonObject)
            applyFromJson((JsonObject) args[0], (PO) result);
         else if ("toJson".equals(method.getName()) && result instanceof JsonObject && args[0] instanceof PO)
            addToJson((PO) args[0], (JsonObject) result, method.getParameterTypes(), args);
         return result;
      }

      /**
       * JSON → PO. The value is NOT set here: between fromJson and saveEx the REST runs po.validForeignKeysEx(), which
       * cannot resolve the table of the LocationExtended reference and fails («"ft" is null»). So the column keeps its
       * previous value (the default serializer has just blanked it, which on a PUT would also fail as mandatory) and the
       * requested id travels in a PO attribute; LocationExtendedSaveHandler puts it in before the save (PO_BEFORE_*).
       */
      private void applyFromJson(JsonObject json, PO po) {
         for (String column : columns) {
            String key = findKey(json, column);
            if (key == null)
               continue;
            JsonElement e = json.get(key);
            if (e != null && e.isJsonObject() && e.getAsJsonObject().has("id"))
               e = e.getAsJsonObject().get("id");
            int id = 0;
            if (e != null && e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber())
               id = e.getAsInt();
            else if (e != null && e.isJsonPrimitive() && e.getAsJsonPrimitive().isString() && !e.getAsString().isBlank())
               id = Integer.parseInt(e.getAsString().trim());
            int idx = po.get_ColumnIndex(column);
            if (idx < 0)
               continue;
            Object old = po.is_new() ? null : po.get_ValueOld(idx);
            po.set_ValueNoCheck(column, old);
            po.set_Attribute(ATTRIBUTE_PREFIX + column, Integer.valueOf(id));
            // on a PUT that only changes this column the PO would look unchanged and save() would skip the events:
            // touching Updated (the save sets it again anyway) makes the PO dirty so PO_BEFORE_CHANGE runs
            int oldId = old instanceof Number ? ((Number) old).intValue() : 0;
            if (!po.is_new() && oldId != id && po.get_ColumnIndex("Updated") >= 0)
               po.set_ValueNoCheck("Updated", new java.sql.Timestamp(System.currentTimeMillis()));
         }
      }

      /** PO → JSON: each LocationExtended column the REST left out, unless excluded by $select / exclude. */
      private void addToJson(PO po, JsonObject json, Class<?>[] types, Object[] args) {
         // toJson(PO, [MRestView,] String[] includes, String[] excludes [, String]): the 1st String[] parameter is
         // includes ($select), the 2nd excludes; overloads without them include everything
         String[] includes = null;
         String[] excludes = null;
         int n = 0;
         for (int i = 0; i < types.length; i++) {
            if (types[i] == String[].class) {
               if (n++ == 0)
                  includes = (String[]) args[i];
               else
                  excludes = (String[]) args[i];
            }
         }
         for (String column : columns) {
            if (findKey(json, column) != null)
               continue;
            if (includes != null && includes.length > 0 && Arrays.stream(includes).noneMatch(column::equalsIgnoreCase))
               continue;
            if (excludes != null && Arrays.stream(excludes).anyMatch(column::equalsIgnoreCase))
               continue;
            int idx = po.get_ColumnIndex(column);
            if (idx < 0)
               continue;
            Object v = po.get_Value(idx);
            int id = v instanceof Number ? ((Number) v).intValue() : 0;
            if (id <= 0)
               continue;
            JsonObject ref = new JsonObject();
            ref.addProperty("propertyLabel", column);
            ref.addProperty("id", id);
            try {
               MLocation loc = MLocation.get(po.getCtx(), id, po.get_TrxName());
               if (loc != null && loc.get_ID() == id)
                  ref.addProperty("identifier", loc.toString());
            } catch (Exception e) {
               log.log(Level.FINE, "identifier of C_Location " + id, e);
            }
            ref.addProperty("model-name", "c_location");
            json.add(column, ref);
         }
      }

      private static String findKey(JsonObject json, String column) {
    	    for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
    	        String k = entry.getKey();

    	        if (k.equalsIgnoreCase(column)) {
    	            return k;
    	        }
    	    }
    	    return null;
    	}
   }
}
