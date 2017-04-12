package devops.cupofkey.core;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
}
