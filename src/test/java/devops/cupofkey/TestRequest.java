package devops.cupofkey;


import static org.junit.Assert.*;

import org.junit.Test;

import devops.cupofkey.core.Request;
import devops.cupofkey.core.Request.*;

public class TestRequest {
	
	@Test
	public void testconstructeur1(){
		Request toto = new Request(commandType.GET, dataType.INTEGER,"toto",5,"42");
		assertEquals(toto.getCmdtype(),commandType.GET);
		assertEquals(toto.getdType(),dataType.INTEGER);
		assertEquals(toto.getKey(),"toto");
		assertEquals(toto.getIndice(),5);
		assertEquals(toto.getData(),"42");
		
		
	}

}
