package devops.cupofkey.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import devops.cupofkey.core.CommandType;
import devops.cupofkey.core.DataType;
import devops.cupofkey.core.Request;
import devops.cupofkey.core.RequestFactory;
import devops.cupofkey.core.Response;
import devops.cupofkey.core.SerialClass;

/**
 * Classe offrant des primitives a un system client pour l'utilisation de notre Base de données
 */
public class Client implements Closeable
{
	/**
	 * Le port d'écoute par défaut du serveur
	 */
	private final static int DEFAULT_PORT = 0;
	
	/**
	 * Le socket de communication
	 */
	private ClientSocket socket;
	
	/**
	 * Créer une nouvelle instance de Client et le connecte au serveur spécifié
	 * @param hostname L'adresse du serveur
	 * @param port Le port d'écoute du serveur
	 * @throws IOException Jetée lorsqu'une erreur de connection est survenue
	 */
	public Client(String hostname, int port) throws IOException 
	{	
		this.socket = new ClientSocket(hostname, port);
		this.socket.connect();
	}
	
	/**
	 * Créer une nouvelle instance de Client et le connecte au serveur spécifié avec le port d'écoute par défaut
	 * @param hostname L'adresse du serveur
	 * @throws IOException Jetée lorsqu'une erreur de connection est survenue
	 */
	public Client(String hostname) throws IOException
	{
		this(hostname, DEFAULT_PORT);
	}
	
	/**
	 * Connecte le client au serveur de stockage
	 * @throws IOException Jetée lorsque la tentative de connection au serveur a échouée
	 */
	public void connect() throws IOException
	{
		this.socket.connect();
	}
	
	/**
	 * Obtient le status de la connection
	 * @return Le status actuel de la connection, True si connecté, False dans le cas contraire
	 */
	public boolean isConnected()
	{
		return this.socket.isConnected();
	}
	
	/**
	 * Obtient le status du socket de communication
	 * @return True si le socket de communication est fermé, False dans le cas contraire
	 */
	public boolean isClosed()
	{
		return this.socket.isClosed();
	}
	
	/**
	 * Obtient l'état d'existance de la clé donnée
	 * @param key La clé à tester
	 * @return True si la clé existe, False dans tous les autres cas possibles
	 * @throws IOException Jetée lorsqu'une erreur de communication survient
	 */
	
	public boolean keyExists(String key) throws IOException
	{
		Request request = RequestFactory.createRequest(CommandType.EMPTY, DataType.STRING, key);
		String serial = request.serialize();
		this.socket.send(serial);
		String rcv = this.socket.receive();
		
		try { 
			Response resp = SerialClass.deserialize(rcv,  Response.class);
			switch(resp.getResponseType())
			{
				case FALSE:
					return true;
				case TRUE:
					return false;
				default:
					return false;
			}
		}
		catch(ClassNotFoundException e)
		{
			return false;
		}
	}
	
	
	/**
	 * Réalise une demande de stockage de chaîne de caractères auprès du serveur
	 * @param key La clé de stockage voulue
	 * @param str La chaîne de caractères à stocker
	 * @return Le status de la requete
	 * @throws IOException Jetée lorsque la connection n'est pas active
	 */
	public RequestResult store(String key, String str) throws IOException
	{
		Request request = RequestFactory.createRequest(CommandType.SET, DataType.STRING, key, str);
		this.socket.send(request.serialize());
		
		String recvMsg = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(recvMsg, Response.class);
			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}
	
	/**
	 * @param key une clé sur laquelle set une liste d'entier
	 * @param intList une liste d'entier
	 * @return RequestResult en fonction de la réponse serveur
	 * @throws IOException
	 */
	public RequestResult storeIntList(String key, List<Integer> intList) throws IOException
	{
		Request request = RequestFactory.createSetIntListRequest(key, intList);
		this.socket.send(request.serialize());
		
		String recvMsg = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(recvMsg, Response.class);
			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}
	
	/**
	 * @param key une clé sur laquelle set une liste d'entier
	 * @param strList une liste d'entier
	 * @return RequestResult en fonction de la réponse serveur
	 * @throws IOException
	 */
	public RequestResult store(String key, List<String> strList) throws IOException
	{
		Request request = RequestFactory.createSetStringListRequest(key, strList);
		this.socket.send(request.serialize());
		
		String recvMsg = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(recvMsg, Response.class);
			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}
	
	/**
	 * @param key une cle sur laquelle ajouter un element
	 * @param str une chaine de caractere a ajouter
	 * @return RequestResult en fonction du resultat du serveur
	 * @throws IOException
	 */
	public RequestResult push(String key, String str) throws IOException
	{
		Request request = RequestFactory.createRequest(CommandType.PUSH, DataType.STRING, key, str);
		this.socket.send(request.serialize());
		
		String recvMsg = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(recvMsg, Response.class);
			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}
	
	/**
	 * @param key une cle sur laquelle ajouter un element
	 * @param nbr un entier a ajouter
	 * @return RequestResult en fonction du resultat du serveur
	 * @throws IOException
	 */
	public RequestResult push(String key, int nbr) throws IOException
	{
		Request request = RequestFactory.createRequest(CommandType.PUSH, DataType.INTEGER, key, nbr);
		this.socket.send(request.serialize());
		
		String recvMsg = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(recvMsg, Response.class);
			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}
	
	/**
	 * Réalise une demande de stockage d'entier après du serveur
	 * @param key La clé de stockage voulue
	 * @param val La valeur de l'entier à stocker
	 * <pre>
	 * KEY_NOT_FOUND Cas de clé inexistante
	 * SUCCESS Cas de suppression avec succés
	 * FAILED Cas de requête échouée sans informations supplémentaires
	 * INVALID_RESPONSE Cas de réponse invalide fournie par le serveur
	 * </pre>
	 * @return Le status de la requête
	 * @throws IOException Jetée lorsque la connection n'est pas active
	 */
	public RequestResult store(String key, int val) throws IOException 
	{
		Request request = RequestFactory.createRequest(CommandType.SET, DataType.INTEGER, key, val);
		String serialObject = request.serialize();
		this.socket.send(serialObject);
		
		String recvMsg = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(recvMsg, Response.class);
			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}
	
	/**
	 * Réalise une demande de stockage d'un objet serialisable après du serveur
	 * @param key La clé de stockage voulue
	 * @param object L'objet serialisable à stocker
	 * @return Le status de la requête
	 * @throws IOException Jetée lorsque la tentative de serialisation de l'objet a échouée, ou qu'un problème est servenu lors de la communication
	 */
	
	public RequestResult store(String key, SerialClass object) throws IOException
	{
		String serialObject = object.serialize();
		return store(key, serialObject);
	}
	
	/**
	 * Supprime l'élément identifié par la clé donnée
	 * @param key La clé de l'élément à supprimer
	 * @return Le status de la requête 
	 * <pre>
	 * KEY_NOT_FOUND Cas de clé inexistante
	 * SUCCESS Cas de suppression avec succés
	 * FAILED Cas de requête échouée sans informations supplémentaires
	 * INVALID_RESPONSE Cas de réponse invalide fournie par le serveur
	 * </pre>
	 * @exception IOException Jetée lorsqu'un problème de communication est survenu
	 */
	public RequestResult clear(String key) throws IOException
	{
		Request request = RequestFactory.createSuppressionRequest(key);
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialResponse = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(serialResponse, Response.class);
			
			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}
	
	/**
	 * @param key cle a laquelle supprimer la valeure
	 * @param index index dans la liste stocke sur laquelle supprimer la valeur
	 * @return RequestResult en fonction de l'etat de la suppression
	 * @throws IOException
	 */
	public RequestResult delete(String key, int index) throws IOException
	{
		Request request = RequestFactory.createRemoveRequest(key, index);
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialResponse = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(serialResponse, Response.class);

			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}

	/**
	 * Obtient l'élément identifiable par la clé spécifiée
	 * @param key La clé de l'élément à obtenir
	 * @param index 
	 * @return L'élément disponible sur le dépôt distant identifié par la clé spécifiée
	 * @throws IOException Jetée lorsqu'une erreur de communication est survenue
	 * @throws InvalidResponseException Jetée lorsque la tentative de convertion du type de l'objet échoue
	 * @throws RequestFailedException 
	 * @throws KeyNotFoundException 
	 */

	public String getString(String key, int index) throws IOException, KeyNotFoundException, RequestFailedException, InvalidResponseException
	{
		Request request = RequestFactory.createGetRequest(key, index);
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialReception = this.socket.receive();
		Response response;
		
		try {
			response = SerialClass.deserialize(serialReception, Response.class);
		
		
			switch(response.getResponseType())
			{
				case NO_ERROR:
					if(response.getData().isEmpty())
						throw new IndexOutOfBoundsException();
					return response.getData().get(0);
				case NO_DATA:
					throw new KeyNotFoundException();
				default:
					throw new RequestFailedException();
			}
		}
		catch (ClassNotFoundException e) {
			throw new InvalidResponseException();
		}
	}

	/**
	 * Obtient l'élément identifiable par la clé spécifiée
	 * @param key La clé de l'élément à obtenir
	 * @return L'élément disponible sur le dépôt distant identifié par la clé spécifiée
	 * @throws IOException Jetée lorsqu'une erreur de communication est survenue
	 * @throws InvalidResponseException Jetée lorsque la tentative de convertion du type de l'objet échoue
	 * @throws RequestFailedException 
	 * @throws KeyNotFoundException 
	 */
	public String getString(String key) throws IOException, KeyNotFoundException, RequestFailedException, InvalidResponseException
	{
		return getString(key,0);
	}
	
	/**
	 * @param key la cle sur laquelle recupere la valeur
	 * @return l'entier associe a cette cle
	 * @throws NumberFormatException
	 * @throws InvalidResponseException
	 * @throws IOException
	 * @throws KeyNotFoundException 
	 * @throws RequestFailedException 
	 */
	public int getInt(String key) throws NumberFormatException, IOException, RequestFailedException, KeyNotFoundException, InvalidResponseException 
	{
		return Integer.parseInt(getString(key));
	}
	
	/**
	 * @param key la cle sur laquelle recupere l'objet
	 * @param objectType la classe associe a l'objet souhaite
	 * @return un objet serializabl stocke a cet emplacement
	 * @throws IOException
	 * @throws InvalidResponseException 
	 * @throws KeyNotFoundException 
	 * @throws RequestFailedException 
	 */

public Object getObject(String key, Class<? extends SerialClass> objectType) throws IOException, RequestFailedException, KeyNotFoundException, InvalidResponseException
	{
		try {
			return SerialClass.deserialize(getString(key), objectType);
		} catch (ClassNotFoundException e) {
			throw new InvalidResponseException();
		}
	}
	
	/**
	 * @param key la cle sur laquelle recupere la valeur
	 * @param index l'index sur lequelle recuperer l'entier
	 * @return l'entier associe a cette cle
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws InvalidResponseException 
	 * @throws RequestFailedException 
	 * @throws KeyNotFoundException 
	 */
	public int getInt(String key, int index) throws NumberFormatException, IOException, KeyNotFoundException, RequestFailedException, InvalidResponseException 
	{
		return Integer.parseInt(getString(key,index));
	}
	
	/**
	 * @param key la cle sur laquelle recupere l'objet
	 * @param objectType la classe associe a l'objet souhaite
	 * @param index l'index de l'objet dans la liste
	 * @return un objet serializabl stocke a cet emplacement
	 * @throws IOException
	 * @throws InvalidResponseException 
	 * @throws RequestFailedException 
	 * @throws KeyNotFoundException 
	 */
	
	public SerialClass getObject(String key, Class<? extends SerialClass> objectType, int index) throws IOException, KeyNotFoundException, RequestFailedException, InvalidResponseException
	{
		try {
			return SerialClass.deserialize(getString(key,index), objectType);
		} catch (ClassNotFoundException e) {
			throw new InvalidResponseException();
		}
	}
	
	/**
	 * @param key la cle sur laquelle recupere la chaine
	 * @return la liste de string present a cet cle
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	
	public List<String> getStringList(String key) throws IOException, ClassNotFoundException
	{
		Request request = RequestFactory.createRequest(CommandType.GET_LIST, DataType.STRING, key);
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialReception = this.socket.receive();
		Response response = SerialClass.deserialize(serialReception, Response.class);
		
		return response.getData();
	}
	
	/**
	 * @param key la cle sur laquelle recupere la liste de int
	 * @return la liste de int present a cet cle
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	
	public List<Integer> getIntList(String key) throws NumberFormatException, ClassNotFoundException, IOException 
	{
		List<String> resList = getStringList(key);
		return SerialClass.getIntegerList(resList);
	}
	
	/**
	 * @param key la cle sur laquelle recupere la liste de objets
	 * @param objectType Classe des objets a deserializer
	 * @return la liste de objet present a cet cle
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	
	public List<SerialClass> getObjectList(String key, Class<? extends SerialClass> objectType) throws ClassNotFoundException, IOException
	{
		Request request = RequestFactory.createRequest(CommandType.GET_LIST, DataType.STRING, key);
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialReception = this.socket.receive();
		Response response = SerialClass.deserialize(serialReception, Response.class);
		
		List<SerialClass> resList = new ArrayList<>();
		
		for(String str : response.getData() ){
			resList.add(SerialClass.deserialize(str, objectType));
		}
		
		return resList;
	}
	
	/**
	 * @param key cle sur laquelle incrementer
	 * @param value valeur a ajouter aux donnees
	 * @return un type de reponse en fonction du resultat
	 * @throws IOException
	 */
	public RequestResult increment(String key, int value) throws IOException
	{
		Request request = RequestFactory.createIncrementRequest(key, value);
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialReception = this.socket.receive();
		
		try 
		{
			Response response = SerialClass.deserialize(serialReception, Response.class);

			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}
	
	/**
	 * @param key cle sur laquelle multiplier
	 * @param value valeur a multiplier aux donnees
	 * @return un type de reponse en fonction du resultat
	 * @throws IOException
	 */
	public RequestResult multiply(String key, int value) throws IOException
	{
		Request request = RequestFactory.createMultiplyRequest(key, value);
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialReception = this.socket.receive();
		try 
		{
			Response response = SerialClass.deserialize(serialReception, Response.class);

			switch(response.getResponseType())
			{
				case NO_ERROR:
					return RequestResult.SUCCESS;
				case NO_DATA:
					return RequestResult.KEY_NOT_FOUND;
				default:
					return RequestResult.FAILED;
			}
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}

	/**
	 * Ferme la communication avec le serveur et libère les ressources utilisées
	 * @throws IOException Jetée lorsqu'une erreur survient lors de la fermeture de la communication ou de la libération des ressources utilisées
	 */
	@Override
	public void close() throws IOException 
	{
		this.socket.close();
	}

	/**
	 * @param key une clé sur laquelle pusher un objet
	 * @param serialObject un objet serializable
	 * @return RequestResult en fonction de la reponse serveur
	 * @throws IOException
	 */
	public RequestResult push(String key, SerialClass serialObject) throws IOException {
		return push(key, serialObject.serialize());
	}
}

	
