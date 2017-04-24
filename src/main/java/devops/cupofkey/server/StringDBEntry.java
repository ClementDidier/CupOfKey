package devops.cupofkey.server;

import java.util.ArrayList;
import java.util.List;
import devops.cupofkey.core.DataType;

/**
 * Encapsule le stockage d'objet de Type String ou d'objet serialize dans la Base de donnee.
 * ne permet pas d'effectuer des operations mathematique.
 * Permet de stocker plusieurs element sur une liste et d'y acceder en fonction de leur indice.
 */
public class StringDBEntry implements DBEntry {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3885020599405490913L;
	
	/**
	 * liste de String permettant de stocker des String a un emplacement de la base de donnee
	 */
	private final List<String> masterList;
	
	/**
	 * le nom de la cle de l'entree
	 */
	private final String key;
	
	/**
	 * Initialise une liste de String permettant de stocker des String a un emplacement de la base de donnee
	 * @param key le nom de la cle de l'entree 
	 */
	public StringDBEntry(String key) {
		this.masterList	= new ArrayList<>();
		this.key = key;
	}

	@Override
	synchronized public List<String> getEntry(int index) {
		List<String> res = new ArrayList<>();
		if(index >= 0 && index < this.masterList.size() ){
			res.add(this.masterList.get(index));
		}
		return res;
	}

	@Override
	synchronized public List<String> getEntry() {
		return this.masterList;
	}

	@Override
	synchronized public boolean removeEntry(int index) {
		if(index >= 0 && index < this.masterList.size() ){
			this.masterList.remove(index);
			return true;
		}
		return false;
	}

	@Override
	synchronized public boolean addEntry(List<String> entries) {
		return this.masterList.addAll(entries);
	}

	@Override
	synchronized public boolean increment(int value) {
		return false;
	}

	@Override
	synchronized public boolean multiply(int value) {
		return false;
	}
	
	@Override
	synchronized public boolean isEmpty() {
		return this.masterList.isEmpty();
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public DataType getType() {
		return DataType.STRING;
	}

}
