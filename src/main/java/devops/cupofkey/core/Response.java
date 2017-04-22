package devops.cupofkey.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represente une reponse du serveur a un client suite a une requete.
 */
public class Response extends SerialClass {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1037588044429127369L;

	/**
	 * type d'erreur, peut etre null
	 */
	private final ErrorType error;
	
	/**
	 * donnees serializees de la reponse
	 */
	private final List<String> data;
	
	/**
	 * @param error nom de l'erreur
	 */
	public Response(ErrorType error){
		this.error	= error;
		this.data	= new ArrayList<String>();
	}
	
	/**
	 * @param data list des donnees serializees retournee par le serveur
	 */
	public Response(List<String> data){
		this.error	= ErrorType.NO_ERROR;
		this.data	= data;
	}
	
	/**
	 * @return les donnees de la reponse
	 */
	public List<String> getData(){
		return this.data;
	}
	
	/**
	 * @return l'erreur de la reponse
	 */
	public ErrorType getError(){
		return this.error;
	}
}
