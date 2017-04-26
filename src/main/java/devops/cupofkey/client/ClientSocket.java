package devops.cupofkey.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket client permettant l'échange de message avec un serveur distant
 */
public class ClientSocket extends Socket
{
	/**
	 * Temps avant abandon de la tentative de connexion
	 */
	private final static int CONNEXION_TIMEOUT = 3000;
	
	/**
	 * nom de l'host distant
	 */
	private String hostname;
	/**
	 * port du serveur distant
	 */
	private int port;
	
	/**
	 * PrintWriter sur le socket
	 */
	private PrintWriter out;
	/**
	 * reception des données du socket
	 */
	private BufferedReader in;
	
	/**
	 * @param hostname nom de l'host distant
	 * @param port port du serveur distant
	 */
	public ClientSocket(String hostname, int port)
	{
		super();
		this.hostname = hostname;
		this.port = port;
		
		this.out = null;
		this.in = null;
	}
	
	/**
	 * Connecte le socket au serveur
	 * @throws IOException Jetée lorsque la tentative de connection au serveur a échouée
	 */
	public void connect() throws IOException
	{
		InetSocketAddress inet = new InetSocketAddress(this.hostname,  this.port);
		this.connect(inet, CONNEXION_TIMEOUT);
		
		this.in = new BufferedReader(new InputStreamReader(this.getInputStream()));
		this.out = new PrintWriter(this.getOutputStream(), true);
		
	}
	
	/**
	 * Envoi un message au serveur
	 * @param message Le message à envoyer
	 * @throws IOException Jetée lorsqu'une erreur est survenue lors de l'envoi au serveur
	 */
	public void send(String message) throws IOException
	{	
		this.out.println(message);
	}
	
	/**
	 * Reception bloquante de message depuis le serveur
	 * @return Le message reçu
	 * @throws IOException Jetée lorsqu'une erreur est survenue lors de la reception du message
	 */
	public String receive() throws IOException
	{
		if(this.in != null)
			return this.in.readLine();
		throw new IOException("Le support de communication en entrée est null");
	}
	
	/**
	 * Ferme la communication et libère des objets utilisés
	 */
	@Override
	public synchronized void close() throws IOException
	{
		super.close();
		
		if(this.in != null)
			this.in.close();
		if(this.out != null)
			this.out.close();
	}
}
