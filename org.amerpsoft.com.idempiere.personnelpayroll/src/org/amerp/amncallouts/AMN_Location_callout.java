package org.amerp.amncallouts;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Properties;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.util.DB;
import org.compiere.util.Env;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Location;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.model.X_AD_Org;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class AMN_Location_callout implements IColumnCallout {

	int AD_Client_ID =0;
	int AD_Org_ID=0;
	int AD_OrgTo_ID=0;
	int AMN_Location_ID=0;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// 
		String columnName = mField.getColumnName();
		AD_Client_ID = (int) mTab.getValue(MAMN_Location.COLUMNNAME_AD_Client_ID);
		AD_OrgTo_ID = (int) mTab.getValue(MAMN_Location.COLUMNNAME_AD_OrgTo_ID);
		AMN_Location_ID=0;
		// AD_OrgTo_ID
		if (columnName.equalsIgnoreCase(MAMN_Location.COLUMNNAME_AD_OrgTo_ID)) {
			if (mTab.getValue(MAMN_Location.COLUMNNAME_AD_OrgTo_ID)  != null ) {
				// 
				AD_OrgTo_ID = (int) mTab.getValue(MAMN_Location.COLUMNNAME_AD_OrgTo_ID);
				MOrgInfo orgInfo = 	MOrgInfo.get(Env.getCtx(), AD_OrgTo_ID,null);
				int locationID = orgInfo.getC_Location_ID();
				int location_FA_ID = 0;
				//
				MTree mTree = getDefaultOrgTree(ctx, AD_Client_ID);

				//MTree mTree = new MTree(ctx, 1000002, false, true, null);
				MTreeNode rootNode = mTree.getRoot();
				// Buscar la organización específica
				MTreeNode orgNode = findNode(rootNode, AD_OrgTo_ID);
				if (orgNode != null) {
				    MTreeNode parent = (MTreeNode) orgNode.getParent();
				    if (parent != null) {
				    	// este es el AD_Org_ID padre
				    	MOrgInfo orgInfoParent = MOrgInfo.get(Env.getCtx(), parent.getNode_ID(), null);
				    	location_FA_ID = orgInfoParent.getC_Location_ID();  
				        System.out.println("Padre de " + AD_OrgTo_ID + " es " + location_FA_ID);
				    } else {
				    	location_FA_ID = 0;
				        System.out.println("La organización es raíz (no tiene padre).");
				    }
				}
				//
				if (locationID > 0)
					mTab.setValue(MAMN_Location.COLUMNNAME_C_Location_ID, locationID);
				if (location_FA_ID > 0)
					mTab.setValue(MAMN_Location.COLUMNNAME_C_Location_FA_ID, location_FA_ID);
			}

		}
		return null;
	}
	
    /**
     * Devuelve el árbol de organizaciones default de un cliente usando query compatible Oracle/PLSQL.
     * @param ctx Contexto (Properties)
     * @param AD_Client_ID ID del cliente
     * @return MTree cargado, o null si no existe árbol
     */
    public static MTree getDefaultOrgTree(Properties ctx, int AD_Client_ID) {
        if (ctx == null || AD_Client_ID <= 0)
            return null;

        int AD_Tree_ID = 0;
        String sql = "SELECT ad_tree_id " +
                     "FROM ad_tree " +
                     "WHERE ad_client_id=? " +
                     "  AND treetype='OO' " +
                     "  AND isactive='Y' " +
                     "ORDER BY isdefault DESC, ad_tree_id ASC " +
                     "FETCH FIRST 1 ROW ONLY";

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setInt(1, AD_Client_ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    AD_Tree_ID = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (AD_Tree_ID <= 0) {
            System.out.println("No se encontró árbol de organizaciones default para el cliente " + AD_Client_ID);
            return null;
        }

        MTree tree = new MTree(ctx, AD_Tree_ID, false, true, null);
        MTreeNode root = tree.getRoot();
        // Verificamos hijos
        if (root != null && root.getChildCount() > 0) {
            System.out.println("Hijos de la raíz:");
            for (int i = 0; i < root.getChildCount(); i++) {
                MTreeNode child = (MTreeNode) root.getChildAt(i);
                System.out.println(child.getName());
            }
        }


        return tree;
    }

    /**
     * Busca un nodo en el árbol recursivamente por su ID.
     * @param root Nodo raíz
     * @param nodeId ID del nodo
     * @return MTreeNode encontrado o null
     */
    public static MTreeNode findNode(MTreeNode root, int nodeId) {
        if (root == null)
            return null;

        if (root.getNode_ID() == nodeId)
            return root;

        for (int i = 0; i < root.getChildCount(); i++) {
            MTreeNode child = (MTreeNode) root.getChildAt(i);
            MTreeNode result = findNode(child, nodeId);
            if (result != null)
                return result;
        }

        return null;
    }
}
