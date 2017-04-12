package devops.cupofkey.server;
/**
 * Classe principale du service Server l'initialisant et lan�ant le dispatcher.
 */
public class Server {
	
	
	/**
	 * Lance un serveur de donn�es
	 * @param args UNUSED
	 */
	public static void main(String[] args) {
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.start();
	}
}
