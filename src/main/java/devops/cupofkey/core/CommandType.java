package devops.cupofkey.core;

public enum CommandType {	
	/**
	 * Retourne l'element (indice en parametre) de la liste (key en parametre).
	 */
	GET,
	
	/**
	 * Ajoute l'element dans la liste key.
	 */
	PUSH,
	
	/**
	 * Retourne la liste complete de l'indice key
	 */
	GET_LIST,
	
	/**
	 * Re definit l'element a la cle key avec data
	 */
	SET,
	
	/**
	 * Supprime l'element i de la liste de cle key
	 */
	DELETE,
	
	/**
	 * supprime la liste a la cle key
	 */
	CLEAR,
	
	/**
	 * retourne 1 si emplacement vide a la cle key, 0 sinon
	 */
	EMPTY,
	
	/**
	 * Ajoute (int)data a tout les element de liste.
	 */
	INCREMENT,
	
	/**
	 * Multiplie les element de la liste par (int)data.
	 */
	MULT
}
