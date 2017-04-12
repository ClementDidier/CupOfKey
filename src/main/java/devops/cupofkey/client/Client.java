package devops.cupofkey.client;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Client 
{
	private Socket socket;
	private String ipAddr;
	private int port;
	
	public Client(String ipAddr, int port) 
	{
		this.ipAddr = ipAddr;
		this.port = port;
	}
	
	public void connect() throws UnknownHostException
	{
		try 
		{
			this.socket = new Socket(this.ipAddr, this.port);
		}
		catch(UnknownHostException e)
		{
			throw new UnknownHostException("Adresse serveur invalide");
		}
		catch (IOException e) 
		{
			// nothing
			System.err.println("Connection impossible");
		}
	}
	
	public boolean isConnected()
	{
		if(this.socket == null)
			return false;
		
		return this.socket.isConnected();
	}
	
	public RequestResult store(String key, String str)
	{
		// good
		return RequestResult.STORED;
	}
}

	
