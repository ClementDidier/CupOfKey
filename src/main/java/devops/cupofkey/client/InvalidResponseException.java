package devops.cupofkey.client;

public class InvalidResponseException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Réponse invalide";
	}
}
