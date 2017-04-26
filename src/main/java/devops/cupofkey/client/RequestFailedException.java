package devops.cupofkey.client;

/**
 * Exception lorsque la requete n'a pas pu etre realisee
 */
public class RequestFailedException extends Exception{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "La requéte a echoué";
	}
}
