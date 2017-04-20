package devops.cupofkey.core;

/**
 * Represente une r�ponse du serveur � un client suite � une requ�te.
 */
public class Response extends SerialClass {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1037588044429127369L;

	/**
	 * type d'erreur, peut etre null
	 */
	private final ErrorType	error;
	
	/**
	 * donnees serializees de la reponse
	 */
	private final String	data;
	
	/**
	 * @param error nom de l'erreur eventuelle
	 */
	public Response(ErrorType error){
		this.error	= error;
		this.data	= null;
	}
	
	/**
	 * @param data donn�es serializ�es
	 */
	public Response(String data){
		this.error	= null;
		this.data	= data;
	}
	
	/**
	 * @return les donn�es de la r�ponse
	 */
	public String getData(){
		return this.data;
	}
	
	/**
	 * @return l'erreur de la reponse
	 */
	public ErrorType getError(){
		return this.error;
	}
}
