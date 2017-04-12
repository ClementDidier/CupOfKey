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
	 * Type de la requete
	 */
	private final CommandType	cmdtype;
	/**
	 * Type des donnees de la requete
	 */
	private final DataType		dataType;
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
	public Request(CommandType cmdtype, DataType dType, String key, int indice, String data){
		this.cmdtype	= cmdtype;
		this.dataType		= dType;
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
	public Request(CommandType cmdtype, DataType dType, String key, String data){
		this.cmdtype	= cmdtype;
		this.dataType		= dType;
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
	public Request(CommandType cmdtype, DataType dType, String key, int indice){
		this.cmdtype	= cmdtype;
		this.dataType		= dType;
		this.indice		= indice;
		this.key		= key;
		this.data		= null;
	}
	
	/**
	 * @param cmdtype
	 * @param dType
	 * @param key
	 */
	public Request(CommandType cmdtype, DataType dType, String key) {
		this.cmdtype	= cmdtype;
		this.dataType		= dType;
		this.indice		= 0;
		this.key		= key;
		this.data		= null;
	}
	
	/**
	 * @return le type de la requete
	 */
	public CommandType getCommandType() {
		return this.cmdtype;
	}

	/**
	 * @return le type des donnees (data)
	 */
	public DataType getDataType() {
		return this.dataType;
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
