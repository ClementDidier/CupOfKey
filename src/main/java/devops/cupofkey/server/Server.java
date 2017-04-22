package devops.cupofkey.server;
/**
 * Classe principale du service Server l'initialisant et lan�ant le dispatcher.
 */
public class Server {
	
	
	/**
	 * Lance un serveur de donnees
	 * @param args UNUSED
	 */
	public static void main(String[] args) {
		Database 	db 			= new Database();
		Dispatcher 	dispatcher 	= new Dispatcher(db);
		dispatcher.start();
	}
}
