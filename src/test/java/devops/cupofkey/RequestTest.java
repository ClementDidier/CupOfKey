package devops.cupofkey;


import static org.junit.Assert.*;

import org.junit.Test;

import devops.cupofkey.core.CommandType;
import devops.cupofkey.core.DataType;
import devops.cupofkey.core.Request;

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
}
