package devops.cupofkey.core;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

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
     * @return une chaine de caract�re correspondant � cette instance de la classe Response
     * @throws IOException 
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
	 * @param <c>
     * @param s La repésentation textuelle de l'objet
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
}
