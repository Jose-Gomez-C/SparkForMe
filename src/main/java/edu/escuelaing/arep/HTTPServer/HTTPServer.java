package edu.escuelaing.arep.HTTPServer;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import javax.imageio.ImageIO;

import com.mongodb.Function;

import edu.escuelaing.arep.Spark.SparkConection;

public class HTTPServer extends Thread{
	private  ServerSocket serverSocket = null;
	private  Socket clientSoket = null;
	private Map<String, Function<Respuestas, String>> funciones = new HashMap<String, Function<Respuestas,String>>();
	public HTTPServer () {	

	}
	public void run() {
		try {
			serverSocket = new ServerSocket(getPort());
			while(true) {
				System.out.println("listo para recibir en el puerto 36000");
				clientSoket= serverSocket.accept();
				System.out.println("Hola");
				resolvingRequest(clientSoket.getInputStream());
				clientSoket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void resolvingRequest(InputStream inputStream) throws IOException {
		boolean fin = true;
		String[] get = null ;
		String inputLine;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		if(in != null) {
			System.out.println(in.ready());
			while ((inputLine=in.readLine())!= null && in.ready() && fin) {
				if(inputLine.contains("GET")) {
					System.out.println(inputLine);
					get = inputLine.split(" ");
					Respuestas respuesta = new Respuestas(clientSoket.getOutputStream(), get[1]);
					//createResponse(respuesta, inputStream);
					fin= false;
					if(funciones.get(get[1]) != null) {
						System.out.println(funciones.keySet());
						String page = funciones.get(get[1]).apply(respuesta);
						PrintWriter response = new PrintWriter(respuesta.getResponse(), true);
						String encabezado = "HTTP/1.1 200 OK\r\n"
			                    + "Content-Type: text/plain\r\n\r\n";
			            response.println(encabezado + page);
			            System.out.println(page);
			            //response.flush();
			            response.close();
			            
			            in.close();
						//createResponse(respuesta, inputStream);
					}else {
						createResponse(respuesta, inputStream);
					}
				}else if(inputLine.contains("POST")) {
					System.out.println(inputLine);
					StringBuilder body = new StringBuilder();
					get = inputLine.split(" ");
					while(in.ready()){
						body.append((char) in.read());
					}
					Respuestas respuesta = new Respuestas(clientSoket.getOutputStream(),body.toString(), get[1]);
					SparkConection sparkConection = new SparkConection();
					sparkConection.InsertarDato(respuesta);
					fin = false;
				}
			}
		}
		in.close();

	}

	public void ingresarSolicitud(String path, Function<Respuestas, String> dato) {

		funciones.put(path, dato);
	}
	private static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}
		return 36000;
	}
	private void createResponse (Respuestas respuesta, InputStream outputStream) throws IOException {

		PrintWriter response = new PrintWriter(respuesta.getResponse(), true);

		if (respuesta.getEsImagen()) {
			leerImagen(respuesta.getPath(), response, clientSoket.getOutputStream());;
		}else {
			String page;
			if(respuesta.getIsPost()) {
				page = respuesta.getPagina();
			}else {
				page = respuesta.generatePage();
			}
			
			String encabezado = "HTTP/1.1 200 OK\r\n"
					+ "Content-Type: text/" + respuesta.getContentType() + "\r\n\r\n";
			response.println(encabezado+page);
			response.close();
		}

	}
	private void leerImagen(String recurso, PrintWriter response, OutputStream outputStream) {
		try {
			response.println("HTTP/1.1 200 OK");
			response.println("Content-Type: image/png\r\n");
			BufferedImage image= ImageIO.read(new File(System.getProperty("user.dir"),"src/main/resources/public"+ recurso));
			ImageIO.write(image, "JPG", outputStream);
			response.close();
		} catch (Exception e) {
			response.println("404 File not found");
			response.close();
		}


	}
}

