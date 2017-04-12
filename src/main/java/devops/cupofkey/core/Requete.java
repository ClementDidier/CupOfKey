package devops.cupofkey.core;

/**
 * Container contenant les informations d'une requ�te
 */
public class Requete extends SerialClass{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4146445245811612066L;
	
	/**
	 * Type de la requete client.
	 */
	private enum commandType{
		
		/**
		 * Retourne l'�l�ment (indice en param�tre) de la liste (key en param�tre).
		 */
		GET,
		
		/**
		 * Ajoute l'�l�ment dans la liste key.
		 */
		PUSH,
		
		/**
		 * Retourne la liste compl�te de l'indice key
		 */
		GET_LIST,
		
		/**
		 * Re d�finit l'�l�ment � la cl� key avec data
		 */
		SET,
		
		/**
		 * Supprime l'�lement i de la liste de cl� key
		 */
		DELETE,
		
		/**
		 * supprime la liste � la cl� key
		 */
		CLEAR,
		
		/**
		 * retourne 1 si emplacement vide � la cl� key, 0 sinon
		 */
		EMPTY,
		
		/**
		 * Ajoute (int)data � tout les �l�ment de liste.
		 */
		INCREMENT,
		
		/**
		 * Multiplie les �l�ment de la liste par (int)data.
		 */
		MULT
	}
	
	/**
	 * type des donn�es �chang�e
	 */
	@SuppressWarnings("javadoc")
	private enum dataType{
		INTEGER,
		STRING,
		INT_LIST,
		STRING_LIST,
		JSON
	}
	
	private commandType cmdtype;
	private dataType dType;
	private int indice;
	private int key;
	private String JSONdata;

	
}
