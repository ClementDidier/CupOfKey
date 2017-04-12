package devops.cupofkey.core;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * Fournit des m�thodes de serialization
 */
public class SerialClass implements Serializable {
	
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1686690481650862280L;

	/** 
     * Read the object from Base64 string. 
     * @param s la chaine de la classe serializee
     * @return une nouvelle instance de la classe Response
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
	@SuppressWarnings("resource")
	public static Response deserialize(String s) throws IOException , ClassNotFoundException {
		byte [] data			= Base64.getDecoder().decode(s);
		ObjectInputStream ois	= new ObjectInputStream(new ByteArrayInputStream(data));
		Response resp			= (Response)ois.readObject();
		ois.close();
		return resp;
   }

    /** 
     * Write the object to a Base64 string. 
     * @return une chaine de caract�re correspondant � cette instance de la classe Response
     * @throws IOException 
     */
    @SuppressWarnings("resource")
	public String serialize() throws IOException {
        ByteArrayOutputStream baos	= new ByteArrayOutputStream();
        ObjectOutputStream oos		= new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }
}
