package devops.cupofkey.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import devops.cupofkey.core.CommandType;
import devops.cupofkey.core.ResponseType;
import devops.cupofkey.core.Request;
import devops.cupofkey.core.Response;
import devops.cupofkey.core.SerialClass;

/**
 * 
 */
public class RequestHandler implements Runnable {

	/**
	 * Socket d'envoi/reception de la reponse/requete
	 */
	private final Socket	socket;
	
	/**
	 * service de Base de Donnee
	 */
	private final Database	db;

	/**
	 * @param socket Socket d'envoi/reception de la reponse/requete
	 * @param db service de Base de Donnee
	 */
	public RequestHandler(Socket socket, Database db) {
		this.socket = socket;
		this.db		= db;
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
	
	

	/**
	 * Deserialize la requete client puis tente de la traiter.
	 * Recupere une Reponse de service Database, la serialize puis l'envoie au client.
	 */
	private void handle() {
		try {
			
			BufferedReader input		= new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			PrintStream output			= new PrintStream(this.socket.getOutputStream());		
			String requestString		= input.readLine();

			if(requestString != null)
			{
				Request requestPackage		= SerialClass.deserialize(requestString, Request.class);
	
				Response responsePackage	= generateResponse(requestPackage);
				
				output.println(responsePackage.serialize());
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Interroge la Base de donnee en fonction du type de la Requete
	 * @param requestPackage une Request client
	 * @return une reponse en fonction de la requete
	 * @see CommandType
	 */
	private Response generateResponse(Request requestPackage) {
		switch (requestPackage.getCommandType()) {
			case GET:
				return this.db.get(requestPackage.getKey(), requestPackage.getIndice());
			case PUSH:
				return this.db.push(requestPackage.getKey(), requestPackage.getData(), requestPackage.getDataType());
			case GET_LIST:
				return this.db.get(requestPackage.getKey());
			case CLEAR:
				return this.db.clear(requestPackage.getKey());
			case DELETE:
				return this.db.delete(requestPackage.getKey(), requestPackage.getIndice());
			case EMPTY:
				return this.db.isEmpty(requestPackage.getKey());
			case INCREMENT:
				return this.db.increment(requestPackage.getKey(), requestPackage.getIndice());
			case MULT:
				return this.db.multiply(requestPackage.getKey(), requestPackage.getIndice());
			case SET:
				return this.db.set(requestPackage.getKey(), requestPackage.getData(), requestPackage.getDataType());
			default:
				return new Response(ResponseType.UNHANDLED_ERROR);
		}
	}
}