package devops.cupofkey.core;

/**
 * Représente l'erreur fourni par le Serveur lors d'une Requete.
 * Permet d'obtenir des informations sur l'etat du serveur et le traitement de la requete
 */
public enum ErrorType 
{
	/**
	 * Pas d'erreur
	 */
	NO_ERROR,
	/**
	 * Erreur non supportée
	 */
	UNHANDLED_ERROR, 
	/**
	 * Erreur de Type
	 * Par exemple une tentative d'utiliser une entrée de type String comme une entrée de type Integer
	 */
	TYPE_ERROR,
	/**
	 * Pas de correspondance avec la clé fournie
	 */
	NO_DATA,
	/**
	 * Vrai
	 */
	TRUE,
	/**
	 * Faux
	 */
	FALSE
}
