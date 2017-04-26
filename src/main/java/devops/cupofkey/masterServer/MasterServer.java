package devops.cupofkey.masterServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Serveur principale ne contenant pas la base de donnee mais une liste de serveur a contacter en fonction du hash de la cle
 */
public class MasterServer {
	
	/**
	 * @param args la liste des ports des serveurs secondaires (contenant la base de donnee)
	 */
	public static void main(String[] args) {
		try {
			List<DistantServer> servers = new ArrayList<>();
			File file = new File(args[0]);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
			   String[] split = line.split(":");
			   servers.add(new DistantServer(split[0],Integer.valueOf(split[1])));
			}
			br.close();
			
			if(servers.size() > 0){
				MasterDispatcher dispatcher = new MasterDispatcher(servers,0);
				dispatcher.start();
			}
			else{
				throw new Exception("Liste de serveurs secondaires vide");
			}
		} catch (Exception e) {
			System.out.println("Veuillez fournir en argument le nom d'un fichier de configuration associe a une liste de serveurs secondaires distant");
			System.out.println("Le fichier contient un serveur par ligne represente de la forme:");
			System.out.println("hostname:port");
			System.out.println("un fichier exemple data/servers.txt est fourni");
		}

	}
}
