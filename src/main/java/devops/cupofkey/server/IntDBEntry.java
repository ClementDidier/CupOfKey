package devops.cupofkey.server;

import java.util.ArrayList;
import java.util.List;
import devops.cupofkey.core.DataType;
import devops.cupofkey.core.SerialClass;

/**
 * Encapsule le stockage d'objet de Type String ou d'objet serialize dans la Base de donnee.
 * permet d'effectuer des operations mathematiques simples.
 * Permet de stocker plusieurs element sur une liste et d'y acceder en fonction de leur indice.
 */
public class IntDBEntry implements DBEntry{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 769448837557712455L;
	
	/**
	 * liste de Integer permettant de stocker des String a un emplacement de la base de donnee
	 */
	private final List<Integer> masterList;
	
	/**
	 * le nom de la cle de l'entree
	 */
	private final String key;

	/**
	 * Initialise une liste de Integer permettant de stocker des String a un emplacement de la base de donnee
	 * @param key le nom de la cle de l'entree
	 */
	public IntDBEntry(String key) {
		this.masterList = new ArrayList<Integer>();
		this.key = key;
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
		for (int i = 0; i < this.masterList.size(); i++) {
		    this.masterList.set(i, this.masterList.get(i) + value);
		}
		return true;
	}

	@SuppressWarnings("boxing")
	@Override
	synchronized public boolean multiply(int value) {
		for (int i = 0; i < this.masterList.size(); i++) {
		    this.masterList.set(i, this.masterList.get(i) * value);
		}
		return true;
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
		return DataType.INTEGER;
	}

}
