package devops.cupofkey.server;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe recevant des connexions clients, cr�ant un Thread par client pour traiter les requ�tes.
 */
public class Dispatcher extends Thread {
	
	/**
	 * Port �cout� par le seveur
	 */
	private final static int PORT = 8888;

	@Override
	public void run(){
		try {
			ServerSocket socketServeur = new ServerSocket(PORT);
			System.out.println("Lancement du serveur");
			while (!isInterrupted()) {
				Socket socketClient = socketServeur.accept();
				Connexion t = new Connexion(socketClient);
				t.start();
				
			}
			socketServeur.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
