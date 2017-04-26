package devops.cupofkey.masterServer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Classe recevant des connexions clients, creant un Thread par client pour traiter les requetes.
 */
public class MasterDispatcher extends Thread {
	
	/**
	 * Port ecoute par le seveur principale
	 */
	private int port = 0;
	
	/**
	 * Executor du pool de Thread gérant les connexion
	 */
	private final ThreadPoolExecutor executor;
	
	/**
	 * Liste des ports des serveurs secondaires
	 */
	private final List<DistantServer> servers;

	private ServerSocket socketServeur;
	
	/**
	 * Initialise un pool de Thread poru gérer les requete clients
	 * @param servers une liste de serveurs secondaires.
	 */
	public MasterDispatcher(List<DistantServer> servers, int port) {
		super("MasterDispatcher");
		this.executor 	= (ThreadPoolExecutor) Executors.newCachedThreadPool();
		this.servers 	= servers;
		this.port 		= port;
	}
	
	/**
	 * Renvoie le port du master dispatcher
	 * @return port
	 */
	public int getPort()
	{
		if(this.socketServeur != null)
			return this.socketServeur.getLocalPort();
		return -1;
	}

	@Override
	public void run(){
		try {
			this.socketServeur = new ServerSocket(port);
			System.out.println("Lancement du serveur maitre sur le port "+ port);
			while (!isInterrupted()) {
				Socket socketClient = socketServeur.accept();
				MasterRequestHandler t = new MasterRequestHandler(socketClient,this.servers,this.servers.size());
				this.executor.execute(t);
			}
			
			// Demande l'arrêt immédiat de chaque thread du Pool
			this.executor.shutdownNow();
			
			// Attend la terminaison de tous les threads avec un timeout de 2s
			this.executor.awaitTermination(2, TimeUnit.SECONDS);
			
			// termine le socket server
			socketServeur.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
