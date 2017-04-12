package devops.cupofkey;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import devops.cupofkey.client.Client;

public class ClientTest 
{
	@Before
	public void init()
	{
		/*Client client = new Client("127.0.0.1", 4000);
		client.connect();
		client.isConnected();
		client.store("key", "string");
		client.store("key2", 12);
		client.store("key3", new );
		client.remove("key");
		client.get("key");
		client.disconnect();*/
	}
	
	@Test(expected = UnknownHostException.class)
	public void ClientFailConnect_test() throws Exception
	{
		Client client = new Client("wrongAddr", 6666);
		client.connect();
		
	}
	
	@Test
	public void ClientFailIsConnect_test() throws Exception
	{
		Client client = new Client("127.0.0.1", 6666);
		client.connect();
		assertFalse(client.isConnected());
	}
	
	@Test
	public void ClientConnect_test()
	{
		//Client client = new Client("127.0.0.1", 4000);
		
	}
	
	@Test
	public void ClientStoreString_test()
	{
		/*Client client = new Client("wrongAddr", 6666);
		client.connect();
		RequestResult result = client.store("MaClef", "MaChaineDeCaractères");
		assertEquals(result, RequestResult.STORED);
		*/
	}
	
	@Test
	public void ClientStoreInt_test()
	{
		
	}
	
	@Test
	public void ClientRemoveStoredObject_test()
	{
		
	}
	
	@Test
	public void ClientDisconnect_test()
	{
		
	}
}
