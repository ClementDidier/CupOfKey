package devops.cupofkey.client;

import java.io.IOException;
import java.net.ConnectException;

import devops.cupofkey.core.CommandType;
import devops.cupofkey.core.DataType;
import devops.cupofkey.core.Request;
import devops.cupofkey.core.Response;
import devops.cupofkey.core.SerialClass;

public class Client
{
	private ClientSocket connecter;
	
	public Client(String hostname, int port) 
	{	
		this.connecter = new ClientSocket(hostname, port);
	}
	
	/**
	 * Connecte le client au serveur de stockage
	 * @throws IOException Jetée lorsque la tentative de connection au serveur a échouée
	 */
	public void connect() throws IOException
	{
		this.connecter.connect();
	}
	
	/**
	 * Obtient le status de la connection
	 * @return Le status actuel de la connection, True si connecté, False dans le cas contraire
	 */
	public boolean isConnected()
	{
		return this.connecter.isConnected();
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
		Request request = new Request(CommandType.SET, DataType.STRING, key, str);
		this.connecter.send(request.serialize());
		
		String recvMsg = this.connecter.receive();
		try 
		{
			@SuppressWarnings("unused")
			Response response = Response.deserialize(recvMsg, Response.class);
			//TODO : Implémenter

			return RequestResult.STORED_SUCCESSFULY;
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
	 * @return Le status de la requête
	 * @throws IOException Jetée lorsque la connection n'est pas active
	 */
	public RequestResult store(String key, int val) throws IOException 
	{
		Request request = new Request(CommandType.SET, DataType.INTEGER, key, val);
		this.connecter.send(request.serialize());
		
		String recvMsg = this.connecter.receive();
		try 
		{
			@SuppressWarnings("unused")
			Response response = Response.deserialize(recvMsg, Response.class);
			//TODO : Implémenter

			return RequestResult.STORED_SUCCESSFULY;
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
	 * @throws IOException Jetée lorsque la tentative de serialisation de l'objet a échouée
	 * @throws ConnectException Jetée lorsque la connection n'est pas active
	 */
	public RequestResult store(String key, SerialClass object) throws IOException
	{
		String so = object.serialize();
		
		Request storeRequest = new Request(CommandType.SET, DataType.OBJECT, key, so);
		this.connecter.send(storeRequest.serialize());
		
		String recvMsg = this.connecter.receive();
		try 
		{
			@SuppressWarnings("unused")
			Response response = Response.deserialize(recvMsg, Response.class);
			//TODO : Implémenter

			return RequestResult.STORED_SUCCESSFULY;
		} 
		catch (ClassNotFoundException e) 
		{
			return RequestResult.INVALID_RESPONSE;
		}
	}

	/**
	 * Déconnecte le client du serveur de stockage auquel il est connecté, ne fait rien s'il n'est pas connecté
	 */
	public void disconnect() 
	{
		this.connecter.close();
	}
}

	
