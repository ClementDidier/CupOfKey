package devops.cupofkey.server;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Classe recevant des connexions clients, creant un Thread par client pour traiter les requetes.
 */
public class Dispatcher extends Thread {
	
	/**
	 * Port ecoute par le seveur
	 */
	private final static int PORT = 8888;
	
	/**
	 * Executor du pool de Thread gérant les connexion
	 */
	private final ThreadPoolExecutor executor;
	
	/**
	 * Service de la Base de Donnée
	 */
	private final Database db;
	
	/**
	 * @param db service de stockage de la Base de données
	 */
	public Dispatcher(Database db) {
		this.executor 	= (ThreadPoolExecutor) Executors.newCachedThreadPool();
		this.db 		= db;
	}

	@Override
	public void run(){
		try {
			ServerSocket socketServeur = new ServerSocket(PORT);
			System.out.println("Lancement du serveur");
			while (!isInterrupted()) {
				@SuppressWarnings("resource")
				Socket socketClient = socketServeur.accept();
				RequestHandler t = new RequestHandler(socketClient,this.db);
				this.executor.execute(t);
			}
			socketServeur.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
