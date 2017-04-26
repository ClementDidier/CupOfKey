package devops.cupofkey.core;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Fournit des methodes de serialization
 */
public abstract class SerialClass implements Serializable {
	
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1686690481650862280L;
	
    /** 
     * Write the object to a Base64 string. 
     * @return une chaine de caractere correspondant a cette instance de la classe Response
     * @throws IOException jetee lorqu'une erreur d'ecriture survient
     */
	public String serialize() throws IOException {
        ByteArrayOutputStream baos	= new ByteArrayOutputStream();
        ObjectOutputStream oos		= new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }
	
	/** 
     * Deserialise une représentation textuelle en Base64 de l'objet
     * @param s La repésentation textuelle de l'objet
	 * @param clazz Classe de l'objet vers laquelle déserializer la chaine
     * @return L'instance de l'objet et null dans le cas où le type de l'instance est invalide par rapport au retour attendu
     * @throws IOException Jetée lorsque une erreur de lecture des données survient
	 * @throws ClassNotFoundException Jetée lorsqu'une erreur de deserialisation survient
     */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String s, Class<? extends SerialClass> clazz) throws IOException, ClassNotFoundException {
		byte [] data = Base64.getDecoder().decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object obj = ois.readObject();
		ois.close();
		
		if(clazz.isInstance(obj))
			return (T) clazz.cast(obj);
		
		throw new ClassNotFoundException("Type de retour invalide");
   }
	
	/**
	 * Cast une liste d'Integer vers une liste de String
	 * @param l une liste d'Integer quelconque
	 * @return une liste de String correspondant à la liste l passee en parametre
	 */
	public static List<String> getStringList(List<Integer> l){
		ArrayList<String> stringList = new ArrayList<>(l.size());
		for (int i : l) {
			stringList.add(String.valueOf(i)); 
		}
		return stringList;
	}
	
	/**
	 * Cast une liste de String vers une liste d'Integer
	 * @param l une liste de String quelconque
	 * @return une liste de Integer correspondant à la liste l passee en parametre
	 */
	public static List<Integer> getIntegerList(List<String> l){
		ArrayList<Integer> stringList = new ArrayList<>(l.size());
		for (String i : l) {
			stringList.add(Integer.valueOf(i)); 
		}
		return stringList;
	}
}
