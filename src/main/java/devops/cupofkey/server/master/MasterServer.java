package devops.cupofkey.server.master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serveur principale ne contenant pas la base de donnee mais une liste de serveur a contacter en fonction du hash de la cle
 */
public class MasterServer {
	
	
	public static MasterDispatcher getDefaultMasterServer() throws IOException{
		List<DistantServer> servers = getListFromFile("data/servers.txt");
		return new MasterDispatcher(servers,0);
	}
	
	private static List<DistantServer> getListFromFile(String filename) throws IOException{
		List<DistantServer> servers = new ArrayList<>();
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
		   String[] split = line.split(":");
		   servers.add(new DistantServer(split[0],Integer.valueOf(split[1])));
		}
		br.close();
		return servers;
	}
	
	/**
	 * @param args la liste des ports des serveurs secondaires (contenant la base de donnee)
	 * @return 
	 */
	public static void main(String[] args) {
		try {
			
			List<DistantServer> servers = getListFromFile(args[0]);
			
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
