package devops.cupofkey.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsule le stockage d'objet de Type String ou d'objet serialize dans la Base de donnee.
 * ne permet pas d'effectuer des operations mathematique.
 * Permet de stocker plusieurs element sur une liste et d'y acceder en fonction de leur indice.
 */
public class StringDBEntry implements DBEntry {
	
	/**
	 * liste de String permettant de stocker des String a un emplacement de la base de donnee
	 */
	private final List<String> masterList;

	/**
	 * Initialise une liste de String permettant de stocker des String a un emplacement de la base de donnee
	 */
	public StringDBEntry() {
		this.masterList	= new ArrayList<String>();
	}

	@Override
	synchronized public List<String> getEntry(int index) {
		List<String> res = new ArrayList<String>();
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

}
