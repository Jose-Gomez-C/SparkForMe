package edu.escuelaing.arep.HTTPServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;

import javax.print.attribute.standard.OutputDeviceAssigned;

/**
 * @author Jose
 *
 */
public class Respuestas {
	private String contentType;
	private OutputStream response;
	private String path;
	private boolean esImagen;
	private boolean isPost;
	private String estado;
	private String pagina;
	private String body; 
	private static final String ok = "HTTP/1.1 200 OK\r\n";
	private static final String bad = "HTTP/1.1 404 Not Found \r\n" + "Content-type: text/html" + "\r\n\r\n";
	public Respuestas() {
		
	}
	public Respuestas(OutputStream response, String path) {
		isPost=false; 
		this.response = response;
		this.path = path;
		if(path.endsWith(".html")) {
			contentType = "html";
			esImagen =false;
		}else if(path.endsWith("css")) {
			contentType = "css";
			esImagen =false;
		}else if(path.endsWith(".jpg")) {
			contentType = "jpg";
			esImagen =true;
		}else if(path.endsWith(".png")) {
			contentType = "png";
			esImagen =true;
		}else if (path.endsWith("js")) {
			contentType = "js";
			esImagen =false;
		}else {
			esImagen =false;
		}
		
	}
	public Respuestas(OutputStream outputStream, String body, String path) {
		this.response = outputStream;
		this.path =path;
		this.body = body; 
		isPost = true;
	}
	public String generatePage() {
		pagina ="";
		try {
			String body = "";
			BufferedReader page = new BufferedReader(new FileReader(new File(System.getProperty("user.dir"),"src/main/resources/public"+path)));
			String linea;
			while ((linea = page.readLine()) != null) {
				body += linea+"\n";
			}
			pagina = body;
			estado=ok;
		} catch (Exception e) {
			estado = bad;
			pagina= "404 File not found";
		}
		return pagina; 
	}
	public String getPagina() {
		return pagina;
	}
	public boolean getIsPost() {
		return isPost;
	}
	public String getOk() {
		return ok;
	}
	
	public String getBad() {
		return bad;
	}
	
	public OutputStream getResponse() {
		return response;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public OutputStream getOutputStream() {
		return response;
	}
	
	public String getPath() {
		return path;
	}
	
	public boolean getEsImagen() {
		return esImagen;
	}
	public String getEstado() {
		return estado;
	}
	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	public String getBody() {
		return body; 
}

}
