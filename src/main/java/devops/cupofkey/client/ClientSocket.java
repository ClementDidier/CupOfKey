package devops.cupofkey.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocket extends Socket
{
	private final static int CONNEXION_TIMEOUT = 3000;
	
	private String hostname;
	private int port;
	
	public ClientSocket(String hostname, int port)
	{
		super();
		this.hostname = hostname;
		this.port = port;
	}
	
	/**
	 * Connecte le socket au serveur
	 * @throws IOException Jetée lorsque la tentative de connection au serveur a échouée
	 */
	public void connect() throws IOException
	{
		InetSocketAddress inet = new InetSocketAddress(this.hostname,  this.port);
		this.connect(inet, CONNEXION_TIMEOUT);
	}
	
	/**
	 * Envoi un message au serveur
	 * @param message Le message à envoyer
	 * @throws IOException Jetée lorsqu'une erreur est survenue lors de l'envoi au serveur
	 */
	public void send(String message) throws IOException
	{
		if(!this.isConnected())
			throw new IOException("La connection n'est pas active");
		
		PrintWriter out = new PrintWriter(this.getOutputStream(), true);
		out.println(message);
		out.close();
	}
	
	/**
	 * Reception bloquante de message depuis le serveur
	 * @return Le message reçu
	 * @throws IOException Jetée lorsqu'une erreur est survenue lors de la reception du message
	 */
	public String receive() throws IOException
	{
		if(!this.isConnected())
			throw new IOException("Impossible de recevoir un message, la connection n'est pas effective");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(this.getInputStream()));
		String res = in.readLine();
		in.close();
		
		return res;
	}
	
	/**
	 * Ferme la communication, ne fait rien si déjà fermée
	 */
	@Override
	public synchronized void close()
	{
		if(this.isConnected() && !this.isClosed())
			this.close();
	}
}
