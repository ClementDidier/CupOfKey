package devops.cupofkey.client;

/**
 * Exception lordsque la Reponse serveur est invalid/erreur serveur
 */
public class InvalidResponseException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Réponse invalide";
	}
}
