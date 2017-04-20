package devops.cupofkey.client;

public enum RequestResult 
{
	/**
	 * Stockage réalisé avec succés sur le dépôt distant
	 */
	STORED_SUCCESSFULY,
	/**
	 * Stockage non réalisé, le serveur n'a plus de capacité disponible
	 */
	ERROR_OUT_OF_MEMORY, 
	/**
	 * Réponse reçue invalide
	 */
	INVALID_RESPONSE, 
	/**
	 * Elément supprimé avec succés sur le dépôt distant
	 */
	REMOVED_SUCCESSFULY
}
