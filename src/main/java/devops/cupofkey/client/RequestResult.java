package devops.cupofkey.client;

public enum RequestResult 
{
	/**
	 * Stockage réalisé avec succés sur le dépôt distant
	 */
	SUCCESS,
	/*/**
	 * Stockage non réalisé, le serveur n'a plus de capacité disponible
	 */
	/*ERROR_OUT_OF_MEMORY,*/ 
	/**
	 * Réponse reçue invalide
	 */
	INVALID_RESPONSE, 
	/**
	 * Elément inexistant sur le dépôt distant, clé introuvable
	 */
	KEY_NOT_FOUND
}
