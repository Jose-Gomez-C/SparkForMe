package edu.escuelaing.arep.HTTPServer;

public class Solicitud {
	private String body;
	private String path; 
	private String type;
	public Solicitud(String body, String path, String type) {
		this.body = body;
		this.path = path;
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public String getPath() {
		return path;
	}
	public String getType() {
		return type;
	}
}
