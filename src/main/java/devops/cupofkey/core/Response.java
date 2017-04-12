package devops.cupofkey.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * Repr�sente une r�ponse du serveur � un client suite � une requ�te.
 */
public class Response extends SerialClass {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1037588044429127369L;

	/**
	 * Type des erreurs pouvant �tre retourn� par le serveur
	 */
	@SuppressWarnings("javadoc")
	private enum errorType{
		UNHANDLED_ERROR,
	}
	
	/**
	 * type d'erreur, peut etre null
	 */
	private final errorType	error;
	
	/**
	 * donnees serializees de la reponse
	 */
	private final String	data;
	
	/**
	 * @param error nom de l'erreur eventuelle
	 */
	public Response(errorType error){
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
	public errorType getError(){
		return this.error;
	}
	
	/** 
     * Read the object from Base64 string. 
     * @param s la chaine de la classe serializee
     * @return une nouvelle instance de la classe Response
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
	public static Response deserialize(String s) throws IOException , ClassNotFoundException {
		byte [] data			= Base64.getDecoder().decode(s);
		ObjectInputStream ois	= new ObjectInputStream(new ByteArrayInputStream(data));
		Response resp			= (Response)ois.readObject();
		ois.close();
		return resp;
   }
}
