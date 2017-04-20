package devops.cupofkey;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import devops.cupofkey.core.CommandType;
import devops.cupofkey.core.DataType;
import devops.cupofkey.core.Request;
import devops.cupofkey.core.Response;

public class RequestTest {
	
	@Test
	public void testconstructeur1(){
		Request toto = new Request(CommandType.GET, DataType.INTEGER,"toto",5,"42");
		assertEquals(toto.getCommandType(), CommandType.GET);
		assertEquals(toto.getDataType(), DataType.INTEGER);
		assertEquals(toto.getKey(),"toto");
		assertEquals(toto.getIndice(),5);
		assertEquals(toto.getData(),"42");
	}
	
	@Test(expected = ClassNotFoundException.class)
	public void serializeDeserializeFail_test() throws ClassNotFoundException
	{
		Request req = new Request(CommandType.SET, DataType.INTEGER, "key", 12);
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
		Request req = new Request(CommandType.SET, DataType.INTEGER, "key", 12);
		String serial = null;
		try {
			serial = req.serialize();
		} catch (IOException e) {
			fail("Erreur lors de la lecture des données pendant la serialisation");
		}
		
		try {
			Request deserial = Request.deserialize(serial, Request.class);
			assertEquals("Type de commande de la requête", deserial.getCommandType(), CommandType.SET);
			assertEquals("Type des données de la requête", deserial.getDataType(), DataType.INTEGER);
			assertEquals("Clé des données à stocker", deserial.getKey(),"key");
			assertEquals("Données à stocker", deserial.getData(), "12");
			
		} catch (IOException | ClassNotFoundException e) {
			fail("Erreur lors de la deserialisation");
		}
	}
}
