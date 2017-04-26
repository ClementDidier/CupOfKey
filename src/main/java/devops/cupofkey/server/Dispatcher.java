package devops.cupofkey.server;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Classe recevant des connexions clients, creant un Thread par client pour traiter les requetes.
 */
public class Dispatcher extends Thread {
	
	private final static boolean DEBUG = false;
	/**
	 * Port d'écoute du serveur
	 */
	private final int port;
	
	/**
	 * Executor du pool de Thread gérant les connexion
	 */
	private final ThreadPoolExecutor executor;
	
	/**
	 * Service de la Base de Donnée
	 */
	private final Database db;
	
	/**
	 * Socket du server
	 */
	private ServerSocket socketServeur;
	
	/**
	 * @param port port du serveur secondaire
	 * @param db service de stockage de la Base de données
	 */
	protected Dispatcher(int port, Database db) {
		this.executor 	= (ThreadPoolExecutor) Executors.newCachedThreadPool();
		this.db 		= db;
		this.port 		= port;
	}
	
	/**
	 * @param port le port du serveur
	 */
	public Dispatcher(int port) {
		this(port, new Database());
	}

	
	@Override
	public void run(){
		try
		{
			this.socketServeur = new ServerSocket(this.port);
			
			if (DEBUG)
				System.out.println("Lancement du serveur secondaire sur le port : " + this.port);
			
			while (!isInterrupted()) 
			{
				Socket socketClient = this.socketServeur.accept();
				RequestHandler t = new RequestHandler(socketClient, this.db);
				this.executor.execute(t);
			}

			// Demande l'arrêt immédiat de chaque thread du Pool
			this.executor.shutdownNow();
			
			// Attend la terminaison de tous les threads avec un timeout de 5s
			try {
				this.executor.awaitTermination(5, TimeUnit.SECONDS);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			
			// termine le socket server
			this.socketServeur.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtient le port d'écoute courant du serveur, -1 s'il n'est pas encore en écoute sur un port
	 * @return Le port d'écoute actuel, -1 dans le cas où le serveur n'est pas en écoute
	 */
	public int getPort()
	{
		if(this.socketServeur != null)
			return this.socketServeur.getLocalPort();
		return -1;
	}
}
