package org.amerp.reports.xlsx;


//Interfaz para notificar progreso de generación de Excel
public interface IProgressListener {
 /**
  * Se llama cada vez que hay un avance.
  * @param message Mensaje de progreso
  */
 void onProgress(String message);
}