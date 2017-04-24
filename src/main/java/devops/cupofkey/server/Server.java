package devops.cupofkey.server;
/**
 * Classe principale du service Server l'initialisant et lancant le dispatcher.
 */
public class Server {
	
	
	/**
	 * Lance un serveur de donnees
	 * @param args port du serveur
	 */
	public static void main(String[] args) {
		try {
			Database 	db 			= new Database();
			Dispatcher 	dispatcher 	= new Dispatcher(Integer.valueOf(args[0]),db);
			dispatcher.start();
		} catch (Exception e) {
			System.out.println("Veuillez preciser en argument le port de ce serveur");
		}
	}
}
