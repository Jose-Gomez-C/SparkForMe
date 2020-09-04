package edu.escuelaing.arep.Spark;

import edu.escuelaing.arep.HTTPServer.Respuestas;
import edu.escuelaing.arep.HTTPServer.Solicitud;
import edu.escuelaing.arep.Persistence.BaseDeDatos;

public class SparkConection {
	private static BaseDeDatos baseDatos;
	private static SparkConection sparkConection;
	public SparkConection() {
		baseDatos = new BaseDeDatos();
	}
	public static SparkConection  getSparkConection() {
		if (sparkConection == null) {
			sparkConection = new SparkConection();
		}
		return sparkConection;
	}
	public void InsertarDato(Respuestas solicitud) {
		String[] dato= solicitud.getBody().split("\n");
		String datoInsertar = dato[dato.length-1];
		datoInsertar = datoInsertar.replace("[", "").replace("]", "");
		//String[] datos = datoInsertar.split(",");
		baseDatos.insertDataBase(datoInsertar);
	}
	public void CapturaInfo(Solicitud solicitud) {
		baseDatos.consultarDatos();
	}
	public static BaseDeDatos getBaseDeDatos() {
		return baseDatos;
	}

}
