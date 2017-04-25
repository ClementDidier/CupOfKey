package devops.cupofkey;

import java.io.IOException;

import devops.cupofkey.client.Client;

public class Program
{
    public static void main( String[] args )
    {  	
        System.out.println( "Hello World!" );
        
        try (Client client = new Client("localhost"))
        {
        	client.store("myKey", "MyString");
        	client.remove("myKey");
        } 
        catch (IOException e) 
        {
			System.err.println(e.getMessage());
		}
    }
}
