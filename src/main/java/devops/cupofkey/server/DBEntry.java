package devops.cupofkey.server;

import java.util.List;

/**
 * Represente une entree de la Base de donnees.
 * Les entrees de la Base de donnees sont des Liste d'entier ou de String ayant chacune des operation distinctes
 */
public interface DBEntry {

	/**
	 * Permet de recuperer les donnee presente a cet emplacement de la Base de donnee
	 * @param index index de la Liste sur lequel recuperer la donnee
	 * @return une liste contenant la donnee presente a l'indice passe en parametre
	 */
	public abstract List<String> getEntry(int index);
	
	/**
	 * Permet de recuperer les donnee presente a cet emplacement de la Base de donnee
	 * @return une liste contenant toute la liste presente a cet emplacement
	 */
	public abstract List<String> getEntry();
	
	/**
	 * permet de retirer un element de la liste
	 * @param index index dans la liste ou supprimer
	 * @return vrai si l'on a pu supprimer a l'indice passe en parametre, faux sinon
	 */
	public abstract boolean removeEntry(int index);
	
	/**
	 * permet d'ajouter des element en fin de liste
	 * @param entries une liste contenant 0,1 ou plusieurs element a ajouter a cet emplacement
	 * @return vrai si l'on a pu ajouter, faux sinon
	 */
	public abstract boolean addEntry(List<String> entries);
	
	/**
	 * @param value valeur a ajouter aux entier de la liste
	 * @return vrai si la liste était une liste d'enier, faux sinon
	 */
	public abstract boolean increment(int value);
	
	/**
	 * @param value valeur a multiplier aux entier de la liste
	 * @return vrai si la liste était une liste d'enier, faux sinon
	 */
	public abstract boolean multiply(int value);
	
	/**
	 * @return vrai si la liste est vide, faux sinon
	 */
	public abstract boolean isEmpty();
	
}