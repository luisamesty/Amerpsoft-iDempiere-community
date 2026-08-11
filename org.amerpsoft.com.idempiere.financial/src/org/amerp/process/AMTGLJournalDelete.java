package org.amerp.process;

import java.util.logging.Level;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * Proceso para eliminar una Nota Contable (GL Journal) en estado Borrador.
 */
public class AMTGLJournalDelete extends SvrProcess {

    private int p_GL_Journal_ID = 0;
    private String msgValue = "";

    @Override
    protected void prepare() {
        ProcessInfoParameter[] paras = getParameter();
        for (ProcessInfoParameter para : paras) {
            String paraName = para.getParameterName();
            if (paraName.equals("GL_Journal_ID")) {
                p_GL_Journal_ID = para.getParameterAsInt();
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
            }
        }
    }

    @Override
    protected String doIt() throws Exception {
        if (p_GL_Journal_ID <= 0) {
            throw new IllegalArgumentException("Parámetro GL_Journal_ID no válido.");
        }

        // Instanciar el diario contable
        MJournal gljournal = new MJournal(getCtx(), p_GL_Journal_ID, get_TrxName());

        if (gljournal.get_ID() == 0) {
            return "No se encontró el registro con ID: " + p_GL_Journal_ID;
        }

        // Registrar datos del documento en la bitácora del proceso
        msgValue = Msg.getElement(Env.getCtx(), "GL_Journal_ID") + ": " + gljournal.getDocumentNo();
        addLog(msgValue);
        
        msgValue = Msg.getElement(Env.getCtx(), "DocStatus") + ": " + gljournal.getDocStatus();
        addLog(msgValue);

        // Validar que únicamente se procesen documentos en borrador o inválidos
        String docStatus = gljournal.getDocStatus();
        if (!MJournal.DOCSTATUS_Drafted.equals(docStatus) 
                && !MJournal.DOCSTATUS_Invalid.equals(docStatus)) {
            return "Error: Solo se pueden eliminar notas contables en estado Borrador (Drafted) o Inválido. Estado actual: " + docStatus;
        }

        // 1. Obtener y eliminar todas las líneas del diario (GL_JournalLine) mediante el modelo de objetos
        MJournalLine[] lines = gljournal.getLines(true);
        int deletedLines = 0;

        for (MJournalLine line : lines) {
            // line.deleteEx(true) fuerza la eliminación y lanza una excepción con Rollback automático si falla
            line.deleteEx(true, get_TrxName());
            deletedLines++;
        }

        msgValue = "Líneas eliminadas: " + deletedLines;
        addLog(msgValue);

        // 2. Eliminar la cabecera de la nota contable
        gljournal.deleteEx(true, get_TrxName());

        return "Nota Contable " + gljournal.getDocumentNo() + " y sus " + deletedLines + " líneas fueron eliminadas exitosamente.";
    }
}