package devops.cupofkey.client;

public class RequestFailedException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "La requéte a echoué";
	}
}
