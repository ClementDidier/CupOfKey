package devops.cupofkey;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import devops.cupofkey.client.ClientSocket;

public class ClientSocketTest 
{
	private final static String IP_ADDRESS = "127.0.0.1";
	private EchoServer server;
	
	@Before
	public void load() throws IOException
	{
		this.server = new EchoServer();
	}
	
	@After
	public void unload() throws IOException
	{
		this.server.interrupt();
	}
	
	@Test
	public void connectSocketClient_test() throws IOException
	{
		try (ClientSocket client = new ClientSocket(IP_ADDRESS, this.server.getLocalPort()))
		{
			client.connect();
			assertNotEquals("Le stream de sortie est null", null, client.getOutputStream());
			assertNotEquals("Le stream d'entrée est null", null, client.getInputStream());
		}
	}
	
	@Test(expected = IOException.class)
	public void failConnectSocketClient_test() throws IOException
	{
		try (ClientSocket client = new ClientSocket("WrongAddress", 0))
		{
			client.connect();
			fail("Connection effective sur un serveur invalide");
		}
		
		fail("Exception non jetée");
	}
	
	@Test
	public void sendSocketClient_test() throws IOException
	{
		try (ClientSocket client = new ClientSocket(IP_ADDRESS, this.server.getLocalPort()))
		{
			client.connect();
			client.send("Message");
		}
	}
	
	@Test
	public void sendReceiveSocketClient_test() throws IOException
	{
		try (ClientSocket client = new ClientSocket(IP_ADDRESS, this.server.getLocalPort()))
		{
			client.connect();
			client.send("Message");
			assertEquals("Message reçu non identique à celui envoyé", "Message", client.receive());
		}
	}
}

/**
 * Serveur ECHO avec 1 client simultané maximum
 */
class EchoServer extends Thread
{
	private final static boolean DEBUG = false;
	private ServerSocket server;
	
	public EchoServer() throws IOException
	{
		this.server = new ServerSocket(0);
		this.start();
	}
	
	public int getLocalPort()
	{
		return this.server.getLocalPort();
	}

	@Override
	public void run() 
	{
		while (!this.isInterrupted()) 
		{
            try(Socket clientSocket = server.accept();
	            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true))
            {
	
	            String s;
	            while ((s = in.readLine()) != null) {
	                out.println(s);
	                
	                if(DEBUG)
	                	System.out.println("Message reçu : " + s);
	            }
            } catch (IOException e) {
				if(DEBUG)
					e.printStackTrace();
			}
        }
	}
}
