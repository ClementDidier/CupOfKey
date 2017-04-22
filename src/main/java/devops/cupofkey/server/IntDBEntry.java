package devops.cupofkey.server;

import java.util.ArrayList;
import java.util.List;

import devops.cupofkey.core.SerialClass;

/**
 * Encapsule le stockage d'objet de Type String ou d'objet serialize dans la Base de donnee.
 * permet d'effectuer des operations mathematiques simples.
 * Permet de stocker plusieurs element sur une liste et d'y acceder en fonction de leur indice.
 */
public class IntDBEntry implements DBEntry{
	
	/**
	 * liste de Integer permettant de stocker des String a un emplacement de la base de donnee
	 */
	private final List<Integer> masterList;

	/**
	 * Initialise une liste de Integer permettant de stocker des String a un emplacement de la base de donnee
	 */
	public IntDBEntry() {
		this.masterList = new ArrayList<Integer>();
	}

	@Override
	synchronized public List<String> getEntry(int index) {
		List<String> res = new ArrayList<String>();
		if(index >= 0 && index < this.masterList.size() ){
			res.add(String.valueOf(this.masterList.get(index)));
		}
		return res;
	}

	@Override
	synchronized public List<String> getEntry() {
		return SerialClass.getStringList(this.masterList);
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
		return this.masterList.addAll(SerialClass.getIntegerList(entries));
	}

	@SuppressWarnings("boxing")
	@Override
	synchronized public boolean increment(int value) {
		for(Integer x : this.masterList){
			x += value;
		}
		return true;
	}

	@SuppressWarnings("boxing")
	@Override
	synchronized public boolean multiply(int value) {
		for(Integer x : this.masterList){
			x *= value;
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return this.masterList.isEmpty();
	}

}
