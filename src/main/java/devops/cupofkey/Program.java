package devops.cupofkey;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

import devops.cupofkey.client.Client;

public class Program
{
    public static void main( String[] args )
    {  
        try (Client client = new Client("localhost"))
        {
        	Scanner scan = new Scanner(System.in);
        	
        	StringBuilder build = new StringBuilder();
        	
        	build.append("CupofKey client \n");
        	build.append("Commanndes possibles : \n");
        	build.append("get, set, exist, incr, mult \n");    	
        	System.out.println(build.toString());
        	
        	String cmd; 
        	while((cmd = scan.nextLine())!="quit"){
        		String[] linecut = cmd.split(" ");
        		switch(linecut[0]){
        		case "set" :
        			if (linecut.length==2){
        				
        			}else if(linecut.length==3){
        				
        			}else{
        				
        			}
        			break;
        		case "get" :
        			if (linecut.length==2){
        			}else if(linecut.length==3){
    				
        			}else{
    				
        			}
        			break;
        		case "incr" :
        			if (linecut.length==2){
        			}else if(linecut.length==3){
    				
        			}else{
    				
        			}
        			break;
        		case "exist":
        			if (linecut.length==2){
        			}else if(linecut.length==3){
    				
        			}else{
    				
        			}
        			break;
        		case "mult" :
        			if (linecut.length==2){
        			}else if(linecut.length==3){
    				
        			}else{
    				
        			}
        			break;
        		default :
        			
        		}
        	}
        	
        } 
        catch (IOException e) 
        {
			System.err.println(e.getMessage());
		}
    	
        //System.out.println( "Hello World!" );
    }


}
