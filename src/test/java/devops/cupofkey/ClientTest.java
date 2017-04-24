package devops.cupofkey;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.SocketException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import devops.cupofkey.client.Client;
import devops.cupofkey.client.RequestResult;
import devops.cupofkey.server.Dispatcher;

public class ClientTest 
{
	private final static String IP_ADDRESS = "127.0.0.1";
	private final static int PORT = 0;
	private final static long TIMEOUT_SECONDS = 10;
	
	private Dispatcher server;
	
	@Rule
	public Timeout globalTimeout = Timeout.seconds(TIMEOUT_SECONDS);
	
	@Before
	public void load() throws InterruptedException
	{
		this.server = new Dispatcher(PORT);
		this.server.start();
		
		while(this.server.getPort() == -1)
		{
			System.out.println("Recherche d'un port de lecture disponible...");
			Thread.sleep(100);
		}
		
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
	
	@After
	public void stop()
	{
		this.server.interrupt();
		assertTrue("Erreur lors de la fermeture du serveur", this.server.isInterrupted());
	}
	
	@Test(expected = IOException.class)
	public void ClientFailConnect_test() throws Exception
	{
		try (Client client = new Client("WrongAddress", PORT))
		{
			client.connect();
		}
		
		// Cas de non exception
		fail("Exception attendue non jetée");
	}
	
	@Test
	public void ClientFailIsConnect_test() throws IOException
	{
		try (Client client = new Client(IP_ADDRESS, -1))
		{
			assertFalse("Connection non effective", client.isConnected());
		}
	}
	
	@Test
	public void ClientConnect_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			client.connect();
			assertTrue("Connection non effective", client.isConnected());
		} catch (IOException e) {
			fail("Connection au serveur échouée\n" + e.getMessage());
		}
	}
	
	@Test
	public void ClientStoreString_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			client.connect();
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaClef", "MaChaineDeCaractères");
			assertEquals("Erreur retournée par le serveur lors de la tentative de STORE d'une chaîne de caractères", RequestResult.STORED_SUCCESSFULY, result);
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		}
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
