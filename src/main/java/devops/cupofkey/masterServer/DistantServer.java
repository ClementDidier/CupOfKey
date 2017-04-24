package devops.cupofkey.masterServer;

/**
 * Represente les informations de connexion vers un serveur distant
 */
public class DistantServer {
	
	/**
	 * un nom d'host
	 */
	private final String hostName;
	/**
	 * un numero de port
	 */
	private final int port;
	
	/**
	 * @param h un nom d'host
	 * @param p un numero de port
	 */
	public DistantServer(final String h, final int p){
		this.hostName = h;
		this.port = p;
	}

	/**
	 * @return le nom du serveur
	 */
	public String getHostName() {
		return this.hostName;
	}

	/**
	 * @return le numero de port du serveur
	 */
	public int getPort() {
		return this.port;
	}
}
