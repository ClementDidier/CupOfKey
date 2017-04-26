package devops.cupofkey;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import devops.cupofkey.client.Client;
import devops.cupofkey.client.InvalidResponseException;
import devops.cupofkey.client.KeyNotFoundException;
import devops.cupofkey.client.RequestFailedException;
import devops.cupofkey.client.RequestResult;
import devops.cupofkey.core.SerialClass;
import devops.cupofkey.server.Dispatcher;
import devops.cupofkey.server.master.DistantServer;
import devops.cupofkey.server.master.MasterDispatcher;

public class ClientTest 
{
	private final static boolean DEBUG = false;
	private final static String IP_ADDRESS = "127.0.0.1";
	private final static long TIMEOUT_SECONDS = 10;
	
	private MasterDispatcher server;
	public List<Dispatcher> disps;
	
	@Rule
	public Timeout globalTimeout = Timeout.seconds(TIMEOUT_SECONDS);
	
	@Before
	public void load() throws InterruptedException
	{
		List<DistantServer> servers = new ArrayList<DistantServer>();
		disps = new ArrayList<Dispatcher>();
		for(int i = 0; i < 3; i++){
			Dispatcher serv = new Dispatcher(0);
			serv.start();
			disps.add(serv);
			while(serv.getPort() == -1)
			{
				if(DEBUG)
					System.out.println("Recherche d'un port de lecture disponible...");
				Thread.sleep(100);
			}
			DistantServer distServ = new DistantServer(IP_ADDRESS, serv.getPort());
			
			if (DEBUG)
				System.out.println(distServ.getHostName()+ " : "+ distServ.getPort() );
			servers.add(distServ);
		}
		
		this.server = new MasterDispatcher(servers,0);
		this.server.start();
		while(this.server.getPort() == -1)
		{
			if(DEBUG)
				System.out.println("Recherche d'un port de lecture disponible server maitre...");
			Thread.sleep(100);
		}
	}
	
	@After
	public void stop()
	{
		this.server.interrupt();
		for(Dispatcher disp : disps){
			disp.interrupt();
		}
		assertTrue("Erreur lors de la fermeture du serveur", this.server.isInterrupted());
	}
	
	@Test(expected = IOException.class)
	public void ClientFailConnect_test() throws IOException
	{
		try (Client client = new Client("WrongAddress", this.server.getPort()))
		{
			// Nothing
		}
		
		// Cas de non exception
		fail("Exception attendue non jetée");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ClientConnectInvalidPort_test() throws IOException
	{
		try (Client client = new Client(IP_ADDRESS, -1))
		{
			// Nothing
		}
		
		// Cas de non exception
		fail("Exception attendue non jetée");
	}
	
	@Test
	public void ClientConnect_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("Connection non effective", client.isConnected());
		} catch (IOException e) {
			fail("Connection au serveur échouée\n" + e.getMessage());
		}
	}
	
	@Test
	public void ClientDisconnect_test() throws IOException
	{
		Client client = null;
		try {
			client = new Client(IP_ADDRESS, this.server.getPort());
		} catch (IOException e) {
			fail("Connection au serveur échouée");
		}
		finally
		{
			assertTrue("Connection non effective", client.isConnected());
			client.close();
			assertTrue("Deconnexion non effective", client.isClosed());
		}
	}
	
	@Test
	public void ClientStoreString_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaClef", "MaChaineDeCaractères");
			assertEquals("Erreur retournée par le serveur lors de la tentative de STORE d'une chaîne de caractères", RequestResult.SUCCESS, result);
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
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaClef", 42);
			assertEquals("Erreur retournée par le serveur lors de la tentative de STORE d'un entier", RequestResult.SUCCESS, result);
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		}
	}
	
	/*@Test
	public void ClientStoreCustomSerialClassObject_test()
	{	
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			CustomSerialObject obj = new CustomSerialObject(42);
			RequestResult result = client.store("MaClef", obj);
			assertEquals("Erreur retournée par le serveur lors de la tentative de STORE d'un objet", RequestResult.SUCCESS, result);
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		}
	}*/
	
	@Test
	public void ClientRemoveNotExistingObject_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.remove("NotExistingKeyObject");
			assertEquals("Valeur invalide retournée par le serveur lors d'une tentative de suppression d'un objet inextistant", RequestResult.KEY_NOT_FOUND, result);
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête auprès du serveur");
		}
	}
	
	@Test
	public void ClientRemoveStoredObject_test()
	{
		// Store
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("ExistingKeyObject", "Value");
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête auprès du serveur");
		}
		
		// Suppression
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.remove("ExistingKeyObject");
			assertEquals("Valeur invalide retournée par le serveur lors d'une tentative de suppression d'un objet inextistant", RequestResult.SUCCESS, result);
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête auprès du serveur");
		}
	}
	
	@Test
	public void ClientGetString_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCleString", "TEST");
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				String str = client.getString("MaCleString");
				assertEquals("La valeur lue est incorrect", "TEST", str);
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientGetInt_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCleInt", 42);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				int val = client.getInt("MaCleInt");
				assertEquals("La valeur lue est incorrect", 42, val);
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * Classe objet serialisable pour les tests
 */
final class CustomSerialObject extends SerialClass
{
	private static final long serialVersionUID = 1L;
	private int attribute;
	
	public CustomSerialObject(int attribute)
	{
		this.attribute = attribute;
	}
	
	public int getAttribute()
	{
		return this.attribute;
	}
}
