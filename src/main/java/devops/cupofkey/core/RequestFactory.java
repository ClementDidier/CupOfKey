package devops.cupofkey.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe proposant plusieurs moyen de creer une Requete en fonction du type de la requete et des donnee client.
 */
public class RequestFactory {
	
	/**
	 * @param cmdtype type de la requete
	 * @param dType type des donnee de la requete
	 * @param key cle definie par le client ou sont/seront stocker les donnees
	 * @param indice indice souhaite (optionnel) de la liste ou sont stockee les donnee a l'indice indice
	 * @param data une liste de String representant des donnees eventuelles
	 * @return une Requete pouvant etre envoyee vers le serveur
	 */
	public static Request createRequest(CommandType cmdtype, DataType dType, String key, int indice, List<String> data){
		return new Request(cmdtype, dType, key, indice, data);
	}
	
	
	/**
	 * @param cmdtype type de la requete
	 * @param dType type des donnee de la requete
	 * @param key cle definie par le client ou sont/seront stocker les donnees
	 * @param data une liste de String representant des donnees eventuelles
	 * @return une Requete pouvant etre envoyee vers le serveur
	 */
	public static Request createRequest(CommandType cmdtype, DataType dType, String key, List<String> data){
		return new Request(cmdtype, dType, key, 0, data);
	}
	
	
	/**
	 * @param cmdtype type de la requete
	 * @param dType type des donnee de la requete
	 * @param key cle definie par le client ou sont/seront stocker les donnees
	 * @param dataString Une String representant des donnees eventuelles
	 * @return une Requete pouvant etre envoyee vers le serveur
	 */
	public static Request createRequest(CommandType cmdtype, DataType dType, String key, String dataString){
		ArrayList<String> data = new ArrayList<String>();
		data.add(dataString);
		return new Request(cmdtype, dType, key, 0, data);
	}
	
	
	/**
	 * @param cmdtype type de la requete
	 * @param dType type des donnee de la requete
	 * @param key cle definie par le client ou sont/seront stocker les donnees
	 * @param indice indice souhaite (optionnel) de la liste ou sont stockee les donnee a l'indice indice
	 * @param dataString Une String representant des donnees eventuelles
	 * @return une Requete pouvant etre envoyee vers le serveur
	 */
	public static Request createRequest(CommandType cmdtype, DataType dType, String key, int indice, String dataString){
		ArrayList<String> data = new ArrayList<String>();
		data.add(dataString);
		return new Request(cmdtype, dType, key, indice, data);
	}
	
	
	/**
	 * @param cmdtype type de la requete
	 * @param dType type des donnee de la requete
	 * @param key cle definie par le client ou sont/seront stocker les donnees
	 * @param dataInt un Integer a etre caster en String pour etre ajoutee aux donnees
	 * @return une Requete pouvant etre envoyee vers le serveur
	 */
	public static Request createRequest(CommandType cmdtype, DataType dType, String key, int dataInt){
		ArrayList<String> data = new ArrayList<String>();
		data.add(String.valueOf(dataInt));
		return new Request(cmdtype, dType, key, 0, data);
	}
	
	
	/**
	 * @param cmdtype type de la requete
	 * @param dType type des donnee de la requete
	 * @param key cle definie par le client ou sont/seront stocker les donnees
	 * @return une Requete pouvant etre envoyee vers le serveur
	 */
	public static Request createRequest(CommandType cmdtype, DataType dType, String key){
		ArrayList<String> data = new ArrayList<String>();
		return new Request(cmdtype, dType, key, 0, data);
	}
	/**
	 * @param key cle definie par le client ou sont stocker les donnees a supprimer
	 * @return une Requete pouvant etre envoyee vers le serveur
	 */
	public static Request createSuppressionRequest(String key){
		return new Request(CommandType.CLEAR,key);
	}
	/**
	 * @param key cle definie par le client sont stocker la liste des donnees a supprimer
	 * @param indice indice souhaite de la liste ou sont stockee les donnee a l'indice indice
	 * @return une Requete pouvant etre envoyee vers le serveur
	 */
	public static Request createRemoveRequest(String key,int indice){
		return new Request(CommandType.DELETE,null,key,indice,null);
	}
	
	
}
