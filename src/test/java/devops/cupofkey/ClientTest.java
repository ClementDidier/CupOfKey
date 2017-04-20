package devops.cupofkey;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import devops.cupofkey.client.Client;

public class ClientTest 
{
	private final static String IP_ADDRESS = "127.0.0.1";
	private final static int PORT = 4000;
	
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
	
	@Test(expected = IOException.class)
	public void ClientFailConnect_test() throws Exception
	{
		Client client = new Client("WrongAddress", PORT);
		client.connect();
		
		// Cas de non exception
		fail("Exception attendue non jetée");
	}
	
	@Test
	public void ClientFailIsConnect_test()
	{
		Client client = new Client(IP_ADDRESS, -1);
		
		assertFalse("Connection non effective", client.isConnected());
	}
	
	@Test
	public void ClientConnect_test()
	{
		// TODO : Implémenter (@Before serveur)
		/*Client client = new Client(IP_ADDRESS, PORT);
		try {
			client.connect();
		} catch (IOException e) {
			fail("Connection au serveur échouée");
		}
		assertTrue("Connection effective", client.isConnected());*/
	}
	
	@Test
	public void ClientStoreString_test()
	{
		// TODO : Implémenter (@Before serveur)
		/*Client client = new Client("wrongAddr", 6666);
		client.connect();
		RequestResult result = client.store("MaClef", "MaChaineDeCaractères");
		assertEquals(result, RequestResult.STORED_SUCCESSFULY);
		*/
	}
	
	@Test
	public void ClientStoreInt_test()
	{
		// TODO : Implémenter (@Before serveur)
		/*Client client = new Client("wrongAddr", 6666);
		client.connect();
		RequestResult result = client.store("MaClef", 123456);
		assertEquals(result, RequestResult.STORED_SUCCESSFULY);
		*/
	}
	
	@Test
	public void ClientStoreSerialClassObject_test()
	{
		// TODO : Implémenter (@Before serveur)
		/*
		// CustomObject extends SerialClass
		CustomObject obj = new CustomObject(...);
		 
		Client client = new Client("wrongAddr", 6666);
		client.connect();
		RequestResult result = client.store("MaClef", obj);
		assertEquals(result, RequestResult.STORED_SUCCESSFULY);
		*/
	}
	
	@Test
	public void ClientRemoveNotExistingObject_test()
	{
		// TODO : Implémenter + @Before serveur
		/*Client client = new Client(IP_ADDRESS, PORT);
		try {
			client.connect();
		} catch (IOException e) {
			fail("Tentative de connection échouée");
		}
		
		client.remove("NotExistingKeyObject");*/
	}
	
	@Test
	public void ClientRemoveStoredObject_test()
	{
		// TODO : Implémenter + @Before serveur
		/*Client client = new Client(IP_ADDRESS, PORT);
		client.connect();
		assertEquals("Résultat de la requête de stockage", client.store("MaClef", "MaChaineDeCaractères"), RequestResult.STORED_SUCCESSFULY);
		client.disconnect();
		
		client = new Client(IP_ADDRESS, PORT);
		client.connect();
		assertTrue(client.remove("MaClef"));*/
	}
	
	@Test
	public void ClientDisconnect_test()
	{	
		// TODO : Implémenter (@Before serveur)
		/*Client client = new Client(IP_ADDRESS, PORT);
		try 
		{
			client.connect();
		} catch (IOException e) {
			fail("Connection au serveur échouée");
		}
		
		assertTrue("Connection effective", client.isConnected());
		client.disconnect();
		assertFalse("Deconnexion effective", client.isConnected());
		*/
	}
}
