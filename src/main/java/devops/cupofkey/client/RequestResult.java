package devops.cupofkey.client;

/**
 * Resultat de la Requete client
 */
public enum RequestResult 
{
	/**
	 * Requête réalisée avec succés sur le dépôt distant
	 */
	SUCCESS,
	/*/**
	 * Requête non réalisée, le serveur n'a plus de capacité disponible
	 */
	/*ERROR_OUT_OF_MEMORY,*/ 
	/**
	 * Réponse reçue invalide
	 */
	INVALID_RESPONSE, 
	/**
	 * Elément inexistant sur le dépôt distant, clé introuvable
	 */
	KEY_NOT_FOUND, 
	/**
	 * La requête a échouée
	 */
	FAILED
}
