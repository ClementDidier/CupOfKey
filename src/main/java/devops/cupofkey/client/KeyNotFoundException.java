package devops.cupofkey.client;

/**
 * Exception lorsque la clé n'a pas été trouvée
 */
public class KeyNotFoundException extends Exception{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "La clé demandé n'existe pas";
	}
}
