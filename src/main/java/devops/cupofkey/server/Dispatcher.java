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
	
	/**
	 * Port d'écoute par défaut du serveur
	 */
	private final static int DEFAULT_PORT = 0;
	
	/**
	 * Executor du pool de Thread gérant les connexion
	 */
	private final ThreadPoolExecutor executor;
	
	/**
	 * Service de la Base de Donnée
	 */
	private final Database db;
	
	/**
	 * Port d'écoute du serveur
	 */
	private int port;
	
	private ServerSocket socketServeur;
	
	/**
	 * @param db service de stockage de la Base de données
	 */
	protected Dispatcher(Database db) {
		this.executor 	= (ThreadPoolExecutor) Executors.newCachedThreadPool();
		this.db 		= db;
		this.port = DEFAULT_PORT;
	}
	
	public Dispatcher() {
		this(new Database());
	}

	public Dispatcher(int port) {
		this();
		this.port = port;
	}
	
	@Override
	public void run(){
		try
		{
			this.socketServeur = new ServerSocket(this.port);
			System.out.println("Lancement du serveur");
			while (!isInterrupted()) 
			{
				Socket socketClient = socketServeur.accept();
				RequestHandler t = new RequestHandler(socketClient, this.db);
				this.executor.execute(t);
			}
			
			// Demande l'arrêt immédiat de chaque thread du Pool
			this.executor.shutdownNow();
			
			// Attend la terminaison de tous les threads avec un timeout de 2s
			this.executor.awaitTermination(2, TimeUnit.SECONDS);
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
