package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.model.MInvoice;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class MAMN_Payroll_Docs extends X_AMN_Payroll_Docs{
	

	private static final long serialVersionUID = -5376694001103700714L;
	/**	Cache							*/
	private static CCache<Integer,MAMN_Payroll_Docs> s_cache = new CCache<Integer,MAMN_Payroll_Docs>(Table_Name, 10);
	/** Logger	 */
	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Docs.class);
	
	public MAMN_Payroll_Docs(Properties ctx, int AMN_Payroll_Docs_ID, String trxName) {
		super(ctx, AMN_Payroll_Docs_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_Payroll_Docs(Properties ctx, int AMN_Payroll_Docs_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Payroll_Docs_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_Payroll_Docs(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_Payroll_Docs(Properties ctx, String AMN_Payroll_Docs_UU, String trxName) {
		super(ctx, AMN_Payroll_Docs_UU, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_Payroll_Docs(Properties ctx, String AMN_Payroll_Docs_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Payroll_Docs_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}
	
	 /**
     * Obtiene los registros de AMN_Payroll_Docs asociados a un AMN_Payroll_ID.
     *
     * @param ctx       Contexto de la aplicación.
     * @param payrollID ID de la nómina (AMN_Payroll_ID).
     * @param trxName   Nombre de la transacción.
     * @return Lista de registros de AMN_Payroll_Docs asociados al AMN_Payroll_ID.
     */
	public static List<MAMN_Payroll_Docs> getByPayrollID(Properties ctx, int payrollID, String trxName) {
	    // Construye la consulta para obtener los registros asociados al AMN_Payroll_ID
	    String whereClause = MAMN_Payroll_Docs.COLUMNNAME_AMN_Payroll_ID + "=?";
	    List<MAMN_Payroll_Docs> docsList = new ArrayList<>();
	    
	    List<PO> result = new Query(ctx, Table_Name, whereClause, trxName)
	            .setParameters(payrollID)
	            .list();

	    for (PO po : result) {
	        if (po instanceof MAMN_Payroll_Docs) {
	            docsList.add((MAMN_Payroll_Docs) po);
	        } else {
	            docsList.add(new MAMN_Payroll_Docs(ctx, po.get_ID(), trxName)); // Cargar correctamente el objeto
	        }
	    }

	    return docsList;
	}


    /**
     * Obtiene los registros de AMN_Payroll_Docs asociados a un AMN_Payroll_ID y C_Invoice_ID.
     *
     * @param ctx        Contexto de la aplicación.
     * @param payrollID  ID de la nómina (AMN_Payroll_ID).
     * @param invoiceID  ID de la factura (C_Invoice_ID).
     * @param trxName    Nombre de la transacción.
     * @return Lista de registros de AMN_Payroll_Docs asociados al AMN_Payroll_ID y C_Invoice_ID.
     */
    public static List<MAMN_Payroll_Docs> getByPayrollAndInvoiceID(Properties ctx, int payrollID, int invoiceID, String trxName) {
        String whereClause = MAMN_Payroll_Docs.COLUMNNAME_AMN_Payroll_ID + "=? AND " +
                             MAMN_Payroll_Docs.COLUMNNAME_C_Invoice_ID + "=?";
        
        List<MAMN_Payroll_Docs> docsList = new ArrayList<>();
        
        List<PO> result = new Query(ctx, Table_Name, whereClause, trxName)
                .setParameters(payrollID, invoiceID)
                .list();

        for (PO po : result) {
            if (po instanceof MAMN_Payroll_Docs) {
                docsList.add((MAMN_Payroll_Docs) po);
            } else {
                docsList.add(new MAMN_Payroll_Docs(ctx, po.get_ID(), trxName)); // Cargar correctamente el objeto
            }
        }

        return docsList;
    }



  
    /**
     * Obtiene el primer registro de AMN_Payroll_Docs asociado a un AMN_Payroll_ID y C_Invoice_ID.
     *
     * @param ctx        Contexto de la aplicación.
     * @param payrollID  ID de la nómina (AMN_Payroll_ID).
     * @param invoiceID  ID de la factura (C_Invoice_ID).
     * @param trxName    Nombre de la transacción.
     * @return Primer registro de AMN_Payroll_Docs encontrado o null si no existe.
     */
    public static MAMN_Payroll_Docs getFirstByPayrollAndInvoiceID(Properties ctx, int payrollID, int invoiceID, String trxName) {
        // Construye la consulta con los filtros
        String whereClause = MAMN_Payroll_Docs.COLUMNNAME_AMN_Payroll_ID + "=? AND " +
                             MAMN_Payroll_Docs.COLUMNNAME_C_Invoice_ID + "=?";
        
        Integer docID = new Query(ctx, Table_Name, whereClause, trxName)
                .setParameters(payrollID, invoiceID)
                .firstId();

        return (docID == null) ? null : new MAMN_Payroll_Docs(ctx, docID, trxName);
    }

    
    /**
     * Obtiene el primer registro de AMN_Payroll_Docs asociado a un AMN_Payroll_ID y C_DocType_ID.
     *
     * @param ctx        Contexto de la aplicación.
     * @param payrollID  ID de la nómina (AMN_Payroll_ID).
     * @param docTypeID  ID del tipo de documento (C_DocType_ID).
     * @param trxName    Nombre de la transacción.
     * @return Primer registro de AMN_Payroll_Docs encontrado o null si no existe.
     */
    public static MAMN_Payroll_Docs getFirstByPayrollAndDocTypeID(Properties ctx, int payrollID, int docTypeID, String trxName) {
        String whereClause = MAMN_Payroll_Docs.COLUMNNAME_AMN_Payroll_ID + "=? AND " +
                             MAMN_Payroll_Docs.COLUMNNAME_C_DocType_ID + "=?";
        
        Integer docID = new Query(ctx, Table_Name, whereClause, trxName)
                .setParameters(payrollID, docTypeID)
                .firstId();

        return (docID == null) ? null : new MAMN_Payroll_Docs(ctx, docID, trxName);
    }

    
    /**
     * Obtiene el primer registro de AMN_Payroll_Docs asociado a un AMN_Payroll_ID y AMN_Concept_Types_ID.
     *
     * @param ctx            Contexto de la aplicación.
     * @param payrollID      ID de la nómina (AMN_Payroll_ID).
     * @param conceptTypesID ID del tipo de concepto (AMN_Concept_Types_ID).
     * @param trxName        Nombre de la transacción.
     * @return Primer registro de AMN_Payroll_Docs encontrado o null si no existe.
     */
    public static MAMN_Payroll_Docs getFirstByPayrollAndConceptTypesID(Properties ctx, int payrollID, int conceptTypesID, String trxName) {
        // Construye la consulta con los filtros
        String whereClause = MAMN_Payroll_Docs.COLUMNNAME_AMN_Payroll_ID + "=? AND " +
                             MAMN_Payroll_Docs.COLUMNNAME_AMN_Concept_Types_ID + "=?";
        
        Integer docID = new Query(ctx, Table_Name, whereClause, trxName)
                .setParameters(payrollID, conceptTypesID)
                .firstId();

        return (docID == null) ? null : new MAMN_Payroll_Docs(ctx, docID, trxName);
    }


    /**
     * Crea o actualiza un registro en la tabla AMN_Payroll_Docs.
     *
     * @param ctx               Contexto de la aplicación.
     * @param payrollID         ID de la nómina (AMN_Payroll_ID).
     * @param conceptTypesID    ID del tipo de concepto (AMN_Concept_Types_ID).
     * @param minvoice          Documento tipo C_Invoice
     * @param trxName           Nombre de la transacción.
     * @return El registro creado o actualizado.
     */
    public static MAMN_Payroll_Docs createOrUpdate(Properties ctx, int payrollID, int conceptTypesID, MInvoice minvoice, String trxName) {
        // Buscar si ya existe un registro con el mismo AMN_Payroll_ID y AMN_Concept_Types_ID
        String whereClause = COLUMNNAME_AMN_Payroll_ID + "=? AND " + COLUMNNAME_AMN_Concept_Types_ID + "=?";
        Integer docID = new Query(ctx, Table_Name, whereClause, trxName)
                .setParameters(payrollID, conceptTypesID)
                .firstId();

        MAMN_Payroll_Docs doc = (docID == null) ? null : new MAMN_Payroll_Docs(ctx, docID, trxName);

        // Actualizar los campos
        doc.setName(minvoice.getDescription());
        doc.setValue(minvoice.getDocumentNo());
        doc.setC_Invoice_ID(minvoice.getC_Invoice_ID());
        doc.setAMN_Concept_Types_ID(conceptTypesID);
        doc.setAMN_Payroll_ID(payrollID);
        doc.setC_DocType_ID(minvoice.getC_DocType_ID());
        // Guardar el registro
        doc.saveEx(); // Guarda el registro en la base de datos

        return doc;
    }
}
