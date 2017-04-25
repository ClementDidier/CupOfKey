package devops.cupofkey;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import devops.cupofkey.core.CommandType;
import devops.cupofkey.core.DataType;
import devops.cupofkey.core.Request;
import devops.cupofkey.core.RequestFactory;
import devops.cupofkey.core.Response;
import devops.cupofkey.core.SerialClass;

public class RequestTest {
	
	@Test
	public void testconstructeur1()
	{
		Request req = RequestFactory.createRequest(CommandType.GET, DataType.INTEGER,"toto",5,"42");
		assertEquals("Type de commande de la requête", CommandType.GET, req.getCommandType());
		assertEquals("Type de données de la requête", DataType.INTEGER, req.getDataType());
		assertEquals("Clé des données", "toto", req.getKey());
		assertEquals("Indice des données dans la structure serveur", 5, req.getIndice());
		assertEquals("Données de la requête", "42", req.getData(0));
	}
	
	@Test(expected = ClassNotFoundException.class)
	public void serializeDeserializeFail_test() throws ClassNotFoundException
	{
		Request req = RequestFactory.createRequest(CommandType.SET, DataType.INTEGER, "key", 12);
		String serial = null;
		try {
			serial = req.serialize();
		} catch (IOException e) {
			fail("Erreur lors de la lecture des données pendant la serialisation");
		}
		
		try {
			Response.deserialize(serial, Response.class);
			
			fail("Exception attendue non jetée");
			
		} catch (IOException e) {
			fail("Erreur lors de la lecture des données pendant la deserialisation");
		}
	}
	
	@Test
	public void serializeDeserialize_test()
	{
		Request req = RequestFactory.createRequest(CommandType.SET, DataType.INTEGER, "key", 12);
		String serial = null;
		try {
			serial = req.serialize();
		} catch (IOException e) {
			fail("Erreur lors de la lecture des données pendant la serialisation");
		}
		
		try {
			Request deserial = Request.deserialize(serial, Request.class);
			assertEquals("Type de commande de la requête", CommandType.SET, deserial.getCommandType());
			assertEquals("Type des données de la requête", DataType.INTEGER, deserial.getDataType());
			assertEquals("Clé des données à stocker", "key", deserial.getKey());
			assertEquals("Données à stocker", "12", deserial.getData().get(0));
			
		} catch (IOException | ClassNotFoundException e) {
			fail("Erreur lors de la deserialisation");
		}
	}
}
