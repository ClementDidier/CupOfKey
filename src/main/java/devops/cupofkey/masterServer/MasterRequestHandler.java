package devops.cupofkey.masterServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import devops.cupofkey.core.ErrorType;
import devops.cupofkey.core.Request;
import devops.cupofkey.core.Response;
import devops.cupofkey.core.SerialClass;

/**
 * 
 */
public class MasterRequestHandler implements Runnable {

	/**
	 * Socket d'envoi/reception de la reponse/requete
	 */
	private final Socket	socket;
	
	/**
	 * Timeout de connexion a un autre serveur
	 */
	private final static int CONNEXION_TIMEOUT = 1000;
	
	/**
	 * liste des ports des serveur
	 */
	private final List<DistantServer> servers;
	
	/**
	 * nombre de serveurs secondaires
	 */
	private final int nbServeur;
	
	/**
	 * @param socket Socket d'envoi/reception de la reponse/requete
	 * @param nbServeur nombre de serveurs secondaires
	 * @param servers liste des ports des serveur
	 */
	public MasterRequestHandler(Socket socket, List<DistantServer> servers, int nbServeur) {
		this.socket		= socket;
		this.servers	= servers;
		this.nbServeur	= nbServeur;
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
			
			BufferedReader input	= new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			PrintStream output		= new PrintStream(this.socket.getOutputStream());		
			String requestString	= input.readLine();

			Request requestPackage	= SerialClass.deserialize(requestString, Request.class);

			String response			= generateResponse(requestPackage.getKey(),requestString);
			
			output.println(response);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param key la cle de la requete
	 * @param serializedRequest la requete serialisee
	 * @return une reponse recue d'un serveur secondaire a renvoyer a un client (serialisee)
	 * @throws IOException lors d'une erreur de communication
	 */
	@SuppressWarnings("resource")
	private String generateResponse(String key, String serializedRequest) throws IOException {
		try {
			int idSecondaireServeur = key.hashCode() % this.nbServeur;
			InetSocketAddress inet = new InetSocketAddress(this.servers.get(idSecondaireServeur).getHostName(),this.servers.get(idSecondaireServeur).getPort());
			Socket socket = new Socket(this.servers.get(idSecondaireServeur).getHostName(), this.servers.get(idSecondaireServeur).getPort());
			socket.connect(inet, CONNEXION_TIMEOUT);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.println(serializedRequest);
			String res = inputFromServer.readLine();
			out.close();
			socket.close();
			return res;
		} catch (Exception e) {
			Response response = new Response(ErrorType.INTERNAL_SERVER_ERROR);
			return response.serialize();
		}

		
		
	}
}