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
import devops.cupofkey.core.Response;
import devops.cupofkey.core.ResponseType;
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
	public void ClientPushString_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			
			client.clear("pushKeyTest");
			RequestResult result;
			
			result = client.push("pushKeyTest", "chaine1");
			assertEquals("Erreur retournée par le serveur lors de la tentative de PUSH d'une chaîne de caractères", RequestResult.SUCCESS, result);
			
			result = client.push("pushKeyTest", "chaine2");
			assertEquals("Erreur retournée par le serveur lors de la tentative de PUSH d'une chaîne de caractères", RequestResult.SUCCESS, result);

			result = client.push("pushKeyTest", "chaine3");
			assertEquals("Erreur retournée par le serveur lors de la tentative de PUSH d'une chaîne de caractères", RequestResult.SUCCESS, result);
			
			assertEquals("Verification de la chaine pushee #1", "chaine1", client.getString("pushKeyTest", 0));
			assertEquals("Verification de la chaine pushee #2", "chaine2", client.getString("pushKeyTest", 1));
			assertEquals("Verification de la chaine pushee #3", "chaine3", client.getString("pushKeyTest", 2));
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (KeyNotFoundException e) {
			fail("Erreur de cle non trouve");
		} catch (RequestFailedException e) {
			fail("Erreur de traitement de la requete");
		} catch (InvalidResponseException e) {
			fail("Erreur de reception");
		}
	}
	
	@Test
	public void ClientGetListString_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			
			client.clear("pushKeyTest");

			client.push("pushKeyTest", "chaine1");
			client.push("pushKeyTest", "chaine2");
			client.push("pushKeyTest", "chaine3");
			
			assertEquals("Verification de la chaine pushee #1", "chaine1", client.getString("pushKeyTest", 0));
			assertEquals("Verification de la chaine pushee #2", "chaine2", client.getString("pushKeyTest", 1));
			assertEquals("Verification de la chaine pushee #3", "chaine3", client.getString("pushKeyTest", 2));
			
			List<String> resList = client.getStringList("pushKeyTest");
			
			assertEquals("Verification de la chaine dans la liste #1", "chaine1", resList.get(0));
			assertEquals("Verification de la chaine dans la liste #2", "chaine2", resList.get(1));
			assertEquals("Verification de la chaine dans la liste #3", "chaine3", resList.get(2));
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (KeyNotFoundException e) {
			fail("Erreur de cle non trouve");
		} catch (RequestFailedException e) {
			fail("Erreur de traitement de la requete");
		} catch (InvalidResponseException e) {
			fail("Erreur de reception");
		} catch (ClassNotFoundException e) {
			fail("Erreur de deserialization");
		}
	}
	
	@Test
	public void ClientGetListInt_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			
			client.clear("pushKeyTest");

			client.push("pushKeyTest", 40);
			client.push("pushKeyTest", 41);
			client.push("pushKeyTest", 42);
			
			List<Integer> resList = client.getIntList("pushKeyTest");
			
			assertTrue("Verification de la chaine dans la liste #1", 40 == resList.get(0));
			assertTrue("Verification de la chaine dans la liste #2", 41 == resList.get(1));
			assertTrue("Verification de la chaine dans la liste #3", 42 == resList.get(2));
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (ClassNotFoundException e) {
			fail("Erreur de deserialization");
		}
	}
	
	@Test
	public void ClientSetListInt_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			
			client.clear("ListKeyTest");

			List<Integer> defList = new ArrayList<>();
			defList.add(40);
			defList.add(41);
			defList.add(42);

			assertEquals("Verification de la reponse serveur lors de l'ajout d'une liste de entier", RequestResult.SUCCESS, client.storeIntList("ListKeyTest", defList));
			
			List<Integer> resList = client.getIntList("ListKeyTest");
			
			assertTrue("Verification de l'entier dans la liste #1", 40 == resList.get(0));
			assertTrue("Verification de l'entier dans la liste #2", 41 == resList.get(1));
			assertTrue("Verification de l'entier dans la liste #3", 42 == resList.get(2));
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (ClassNotFoundException e) {
			fail("Erreur de deserialization");
		}
	}
	
	@Test
	public void ClientSetListString_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			
			client.clear("ListKeyTest");

			List<String> defList = new ArrayList<>();
			defList.add("Hubert Bonisseur de La Bath");
			defList.add("Tu n’es pas seulement un lâche, tu es un traître, comme ta petite taille le laissait deviner. ");
			defList.add("En tout cas, on peut dire que le soviet éponge... ");

			assertEquals("Verification de la reponse serveur lors de l'ajout d'une liste de chaine", RequestResult.SUCCESS, client.store("ListKeyTest", defList));
			
			List<String> resList = client.getStringList("ListKeyTest");
			
			assertEquals("Verification de la chaine dans la liste #1", resList.get(0) , "Hubert Bonisseur de La Bath");
			assertEquals("Verification de la chaine dans la liste #2", resList.get(1) , "Tu n’es pas seulement un lâche, tu es un traître, comme ta petite taille le laissait deviner. ");
			assertEquals("Verification de la chaine dans la liste #3", resList.get(2) , "En tout cas, on peut dire que le soviet éponge... ");
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (ClassNotFoundException e) {
			fail("Erreur de deserialization");
		}
	}
	
	@Test
	public void ClientGetListObject_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			
			client.clear("pushKeyTest");

			client.push("pushKeyTest", new CustomSerialObject(40));
			client.push("pushKeyTest", new CustomSerialObject(41));
			client.push("pushKeyTest", new CustomSerialObject(42));
			
			List<SerialClass> resList = client.getObjectList("pushKeyTest", CustomSerialObject.class);
			
			assertTrue("Verification de l'objet dans la liste #1", 40 == ((CustomSerialObject)resList.get(0)).getAttribute());
			assertTrue("Verification de l'objet dans la liste #2", 41 == ((CustomSerialObject)resList.get(1)).getAttribute());
			assertTrue("Verification de l'objet dans la liste #3", 42 == ((CustomSerialObject)resList.get(2)).getAttribute());
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (ClassNotFoundException e) {
			fail("Erreur de deserialization");
		}
	}
	
	@Test
	public void ClientPushDelete_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			
			client.clear("pushKeyTest");

			client.push("pushKeyTest", "chaine1");
			client.push("pushKeyTest", "chaine2");
			client.push("pushKeyTest", "chaine3");
			
			
			assertEquals("Verification de la chaine pushee #1", "chaine1", client.getString("pushKeyTest", 0));
			assertEquals("Verification de la chaine pushee #2", "chaine2", client.getString("pushKeyTest", 1));
			assertEquals("Verification de la chaine pushee #3", "chaine3", client.getString("pushKeyTest", 2));
			
			assertEquals("Suppression de la chaine pushee #2",RequestResult.SUCCESS , client.delete("pushKeyTest",1));
			assertEquals("Verification de la chaine pushee #2->#3", "chaine3", client.getString("pushKeyTest", 1));
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (KeyNotFoundException e) {
			fail("Erreur de cle non trouve");
		} catch (RequestFailedException e) {
			fail("Erreur de traitement de la requete");
		} catch (InvalidResponseException e) {
			fail("Erreur de reception");
		}
	}
	
	@Test
	public void ClientPushInt_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			
			client.clear("pushKeyTestInt");
			
			RequestResult result;
			
			result = client.push("pushKeyTestInt", 40);
			assertEquals("Erreur retournée par le serveur lors de la tentative de PUSH un entier", RequestResult.SUCCESS, result);
			
			result = client.push("pushKeyTestInt", 41);
			assertEquals("Erreur retournée par le serveur lors de la tentative de PUSH d'une chaîne de caractères", RequestResult.SUCCESS, result);

			result = client.push("pushKeyTestInt", 42);
			assertEquals("Erreur retournée par le serveur lors de la tentative de PUSH d'une chaîne de caractères", RequestResult.SUCCESS, result);

			assertEquals("Verification de l'entier pushee #1", 40, client.getInt("pushKeyTestInt", 0));
			assertEquals("Verification de l'entier pushee #2", 41, client.getInt("pushKeyTestInt", 1));
			assertEquals("Verification de l'entier pushee #3", 42, client.getInt("pushKeyTestInt", 2));
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (KeyNotFoundException e) {
			fail("Erreur de cle non trouve");
		} catch (RequestFailedException e) {
			fail("Erreur de traitement de la requete");
		} catch (InvalidResponseException e) {
			fail("Erreur de reception");
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
	
	@Test
	public void ClientStoreCustomSerialClassObject_test()
	{	
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			CustomSerialObject obj = new CustomSerialObject(42);
			RequestResult result = client.store("MaClef", obj);
			assertEquals("Erreur retournée par le serveur lors de la tentative de STORE d'un objet", RequestResult.SUCCESS, result);
			
			CustomSerialObject receivedObject = (CustomSerialObject) client.getObject("MaClef", CustomSerialObject.class);
			assertEquals("Verification d'un champs de l'objet custom stockee", 42, receivedObject.getAttribute());
			
		} catch(SocketException e) {
			e.printStackTrace();
			fail("Erreur lors de la communication");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erreur de l'envoi de la requête STORE du client auprès du serveur");
		} catch (RequestFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientRemoveNotExistingObject_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.clear("NotExistingKeyObject");
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
			RequestResult result = client.clear("ExistingKeyObject");
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
				fail("Erreur requéte");
			}
		} catch (IOException e) {
			fail("Erreur socket");
		}
	}
	
	@Test
	public void ClientGetStringIndex_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCleString", "TEST");
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				String str = client.getString("MaCleString",0);
				assertEquals("La valeur lu est incorrect", "TEST", str);
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				fail("Erreur requéte");
			}
		} catch (IOException e) {
			fail("Erreur socket");
		}
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void ClientGetStringWrongIndex_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCleString", "TEST");
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				String str = client.getString("MaCleString",1);
				assertEquals("La valeur lu est incorrect", "TEST", str);
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				fail("Erreur requéte");
			}
		} catch (IOException e) {
			fail("Erreur socket");
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
	
	@Test
	public void ClientGetIntIndex_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCleInt", 42);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				int val = client.getInt("MaCleInt",0);
				assertEquals("La valeur lue est incorrect", 42, val);
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void ClientGetIntWrongIndex_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCleInt", 42);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				int val = client.getInt("MaCleInt",1);
				assertEquals("La valeur lue est incorrect", 42, val);
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientIncrement_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCleInt", 42);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			result = client.increment("MaCleInt",1);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				int val = client.getInt("MaCleInt",0);
				assertEquals("La valeur lue est incorrect", 43, val);
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientMultiply_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCleInt", 42);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			result = client.multiply("MaCleInt",2);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				int val = client.getInt("MaCleInt",0);
				assertEquals("La valeur lue est incorrect", 84, val);
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientKeyExists_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			RequestResult result = client.store("MaCle", 42);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			boolean b = client.keyExists("MaCle");
			assertTrue("La clé n'a pas été trouvé", b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientKeyExistsFail_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			boolean b = client.keyExists("CleNul");
			assertFalse("La clé n'a pas été trouvé", b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientStoreObject_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			Response r = new Response(ResponseType.TRUE);
			RequestResult result = client.store("CleObjet", r);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientGetObject_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			Response r = new Response(ResponseType.TRUE);
			RequestResult result = client.store("CleObjet", r);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				Object val = client.getObject("CleObjet", Response.class);
				assertEquals("La valeur lue est incorrect", r.getResponseType(), ((Response) val).getResponseType());
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ClientGetObjectIndex_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			Response r = new Response(ResponseType.TRUE);
			RequestResult result = client.store("CleObjet", r);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				Object val = client.getObject("CleObjet", Response.class, 0);
				assertEquals("La valeur lue est incorrect", r.getResponseType(), ((Response) val).getResponseType());
			} catch (RequestFailedException | KeyNotFoundException | InvalidResponseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void ClientGetObjectWrongIndex_test()
	{
		try (Client client = new Client(IP_ADDRESS, this.server.getPort())) {
			assertTrue("La connexion au serveur n'est pas effective", client.isConnected());
			assertFalse("La connexion est fermée", client.isClosed());
			Response r = new Response(ResponseType.TRUE);
			RequestResult result = client.store("CleObjet", r);
			assertEquals("Erreur lors de l'ajout d'un élément", RequestResult.SUCCESS, result);
			try {
				Object val = client.getObject("CleObjet", Response.class, 1);
				assertEquals("La valeur lue est incorrect", r.getResponseType(), ((Response) val).getResponseType());
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
