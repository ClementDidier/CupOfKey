package devops.cupofkey.server;
import java.net.*;

import devops.cupofkey.core.ErrorType;
import devops.cupofkey.core.Request;
import devops.cupofkey.core.Response;

import java.io.*;

/**
 * 
 */
public class Connexion extends Thread {

	/**
	 * 
	 */
	private final Socket	socket;

	/**
	 * @param socket
	 */
	public Connexion(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		
		handle();
		
		try {
			this.socket.close();
		} 
		catch (IOException e) {
			// void
		}
	}

	@SuppressWarnings("unused")
	private void handle() {
		try {
			
			BufferedReader input		= new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			PrintStream output			= new PrintStream(this.socket.getOutputStream());		
			String requestString		= input.readLine();
			
			// TODO: Use requestPackages
			Request requestPackages		= Request.deserialize(requestString, Request.class);
			
			// TODO: Use responsePackage
			Response responsePackage	= new Response(ErrorType.UNHANDLED_ERROR);
			
			output.println("j'ai recu quelque chose");
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}