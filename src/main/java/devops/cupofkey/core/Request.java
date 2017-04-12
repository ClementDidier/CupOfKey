package devops.cupofkey.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * Container contenant les informations d'une requete
 */
public class Request extends SerialClass{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4146445245811612066L;
	
	/**
	 * Type de la requete client.
	 */
	public enum commandType{
		
		/**
		 * Retourne l'element (indice en parametre) de la liste (key en parametre).
		 */
		GET,
		
		/**
		 * Ajoute l'element dans la liste key.
		 */
		PUSH,
		
		/**
		 * Retourne la liste complete de l'indice key
		 */
		GET_LIST,
		
		/**
		 * Re definit l'element a la cle key avec data
		 */
		SET,
		
		/**
		 * Supprime l'element i de la liste de cle key
		 */
		DELETE,
		
		/**
		 * supprime la liste a la cle key
		 */
		CLEAR,
		
		/**
		 * retourne 1 si emplacement vide a la cle key, 0 sinon
		 */
		EMPTY,
		
		/**
		 * Ajoute (int)data a tout les element de liste.
		 */
		INCREMENT,
		
		/**
		 * Multiplie les element de la liste par (int)data.
		 */
		MULT
	}
	
	/**
	 * type des donnees echangee
	 */
	private enum dataType{
		/**
		 * type entier
		 */
		INTEGER,
		/**
		 * Type chaine
		 */
		STRING,
		/**
		 * type liste d'entier
		 */
		INT_LIST,
		/**
		 * type de liste de chaine
		 */
		STRING_LIST,
		/**
		 * type inconnu
		 */
		OBJECT
	}
	
	/**
	 * Type de la requete
	 */
	private final commandType	cmdtype;
	/**
	 * Type des donnees de la requete
	 */
	private final dataType		dType;
	/**
	 * indice (optionnel) dans la liste a traiter
	 */
	private final int			indice;
	/**
	 * Cle de stockage definit par le client
	 */
	private final String		key;
	/**
	 * Donnees (optionnelle) de la requete
	 */
	private final String		data;
	
	/**
	 * @param cmdtype type de la commande
	 * @param dType type des données
	 * @param indice indice 
	 * @param key
	 * @param data
	 */
	public Request(commandType cmdtype, dataType dType, String key, int indice, String data){
		this.cmdtype	= cmdtype;
		this.dType		= dType;
		this.indice		= indice;
		this.key		= key;
		this.data		= data;
	}

	/**
	 * @param cmdtype
	 * @param dType
	 * @param key
	 * @param data
	 */
	public Request(commandType cmdtype, dataType dType, String key, String data){
		this.cmdtype	= cmdtype;
		this.dType		= dType;
		this.indice		= 0;
		this.key		= key;
		this.data		= data;
	}
	
	/**
	 * @param cmdtype
	 * @param dType
	 * @param key
	 * @param indice
	 */
	public Request(commandType cmdtype, dataType dType, String key, int indice){
		this.cmdtype	= cmdtype;
		this.dType		= dType;
		this.indice		= indice;
		this.key		= key;
		this.data		= null;
	}
	
	/**
	 * @param cmdtype
	 * @param dType
	 * @param key
	 */
	public Request(commandType cmdtype, dataType dType, String key) {
		this.cmdtype	= cmdtype;
		this.dType		= dType;
		this.indice		= 0;
		this.key		= key;
		this.data		= null;
	}
	
	/**
	 * @return le type de la requete
	 */
	public commandType getCmdtype() {
		return this.cmdtype;
	}

	/**
	 * @return le type des donnees (data)
	 */
	public dataType getdType() {
		return this.dType;
	}

	/**
	 * @return l'indice des donnee a traiter
	 */
	public int getIndice() {
		return this.indice;
	}

	/**
	 * @return la cle demandee par le client
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @return retourne les donnees de la requete
	 */
	public String getData() {
		return this.data;
	}
	
	/** 
     * Read the object from Base64 string. 
     * @param s la chaine de la classe serializee
     * @return une nouvelle instance de la classe Response
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
	public static Request deserialize(String s) throws IOException , ClassNotFoundException {
		byte [] data			= Base64.getDecoder().decode(s);
		ObjectInputStream ois	= new ObjectInputStream(new ByteArrayInputStream(data));
		Request resp			= (Request)ois.readObject();
		ois.close();
		return resp;
   }
}
