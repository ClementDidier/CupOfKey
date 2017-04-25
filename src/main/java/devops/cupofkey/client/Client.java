package devops.cupofkey.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.ConnectException;

import devops.cupofkey.core.CommandType;
import devops.cupofkey.core.DataType;
import devops.cupofkey.core.ErrorType;
import devops.cupofkey.core.Request;
import devops.cupofkey.core.RequestFactory;
import devops.cupofkey.core.Response;
import devops.cupofkey.core.SerialClass;

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
		// TODO : Revoir Factory
		Request request = RequestFactory.createRequest(CommandType.EMPTY, null, key);
		String serial = request.serialize();
		this.socket.send(serial);
		String rcv = this.socket.receive();
		
		try { 
			Response resp = Response.deserialize(rcv,  Response.class);
		
			switch(resp.getError())
			{
				case FALSE:
					return false;
				case TRUE:
					return true;
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
		//Request request = new Request(CommandType.SET, DataType.STRING, key, str);
		Request request = RequestFactory.createRequest(CommandType.SET, DataType.STRING, key, str);
		this.socket.send(request.serialize());
		
		String recvMsg = this.socket.receive();
		try 
		{
			Response response = Response.deserialize(recvMsg, Response.class);
			switch(response.getError())
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
			Response response = Response.deserialize(recvMsg, Response.class);
			switch(response.getError())
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
	 * @param val L'objet serialisable à stocker
	 * @return Le status de la requête
	 * @throws IOException Jetée lorsque la tentative de serialisation de l'objet a échouée, ou qu'un problème est servenu lors de la communication
	 */
	/*
	public RequestResult store(String key, SerialClass object) throws IOException
	{
		String serialObject = object.serialize();
		
		Request storeRequest = RequestFactory.createRequest(CommandType.SET, DataType.OBJECT, key, serialObject);
		
		this.socket.send(storeRequest.serialize());
		
		String recvMsg = this.socket.receive();
		try 
		{
			Response response = Response.deserialize(recvMsg, Response.class);
			//TODO : Implémenter

			return RequestResult.SUCCESS;
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}*/
	
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
	public RequestResult remove(String key) throws IOException
	{
		Request request = RequestFactory.createSuppressionRequest(key);
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialResponse = this.socket.receive();
		try 
		{
			Response response = Response.deserialize(serialResponse, Response.class);

			switch(response.getError())
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
	 * @param objectType Le type de l'objet retourné
	 * @return L'élément disponible sur le dépôt distant identifié par la clé spécifiée
	 * @throws IOException Jetée lorsqu'une erreur de communication est survenue
	 * @throws ClassNotFoundException Jetée lorsque la tentative de convertion du type de l'objet échoue
	 */
	/*public Object get(String key, Class<? extends SerialClass> objectType) throws IOException, ClassNotFoundException
	{
		Request request = RequestFactory.;
		String serialRequest = request.serialize();
		this.socket.send(serialRequest);
		
		String serialReception = this.socket.receive();
		Response response = Response.deserialize(serialReception, Response.class);
		
		Object obj = SerialClass.deserialize(response.getData().get(0), objectType);
		return obj;
	}*/

	/**
	 * Ferme la communication avec le serveur et libère les ressources utilisées
	 * @throws IOException Jetée lorsqu'une erreur survient lors de la fermeture de la communication ou de la libération des ressources utilisées
	 */
	public void close() throws IOException 
	{
		this.socket.close();
	}
}

	
