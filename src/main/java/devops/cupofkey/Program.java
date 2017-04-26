package devops.cupofkey;

import java.util.Scanner;

import devops.cupofkey.client.Client;
import devops.cupofkey.server.Dispatcher;
import devops.cupofkey.server.master.MasterDispatcher;
import devops.cupofkey.server.master.MasterServer;

/**
 * Programme utilisateur
 */
public class Program
{
    public static void main( String[] args )
    {  
        try
        {
        	System.out.println("Lancement des serveurs secondaires");
        	Dispatcher 	firstSecondaryServer = new Dispatcher(4242);
        	firstSecondaryServer.start();
        	Dispatcher 	secondSecondaryServer = new Dispatcher(2424);
        	secondSecondaryServer.start();
        	System.out.println("serveurs secondaires lancés\n\n");
        	
        	System.out.println("Lancement du serveur principal");
        	MasterDispatcher masterServer = MasterServer.getDefaultMasterServer();
        	masterServer.start();
        	System.out.println("serveur principal lancé\n\n");
        	
        	while(masterServer.getPort() == -1){
        		Thread.sleep(100);
        	}
        	
        	Client client = new Client("localhost", masterServer.getPort());
        	
        	Scanner scan = new Scanner(System.in);
        	
        	StringBuilder build = new StringBuilder();
        	
        	build.append("CupofKey client \n");
        	build.append("Commanndes possibles : \n");
        	build.append("get, set, exist, incr, mult, clear, delete, push \n"); 
        	
        	System.out.println(build.toString());
        	
        	String cmd; 
        	while((cmd = scan.nextLine())!="quit"){
        		String[] linecut = cmd.split(" ");
        		switch(linecut[0]){
        		case "set" :
        			if (linecut.length==4){
        				if(linecut[3].equals("Integer")){
        					client.store(linecut[1], Integer.valueOf(linecut[2]));
        				}
        				else{
        					client.store(linecut[1], linecut[2]);
        				}
        			}
        			else{
        				System.out.println("syntaxe : set <key> <value> <type=Integer||String>");
        			}
        			break;
        		case "get" :
        			if (linecut.length==2){
        				System.out.println(client.getString(linecut[1]));
        			}
        			else if(linecut.length==3){
        				System.out.println(client.getString(linecut[1],Integer.valueOf(linecut[2])));
        			}
        			else{
        				System.out.println("syntaxe : get <key> [index]");
        			}
        			break;
        		case "incr" :
        			if (linecut.length==3){
        				client.increment(linecut[1],Integer.valueOf(linecut[2]));
        			}
        			else{
        				System.out.println("syntaxe : incr <key> <value>");
        			}
        			break;
        		case "exist":
        			if (linecut.length==2){
        				System.out.println("existance de la cle : " + linecut[1] + " = " + client.keyExists(linecut[1]));
        			}
        			else{
        				System.out.println("syntaxe : exist <key>");
        			}
        			break;
        		case "mult" :
        			if (linecut.length==3){
        				client.multiply(linecut[1],Integer.valueOf(linecut[2]));
        			}
        			else{
        				System.out.println("syntaxe : mult <key> <value>");
        			}
        			break;
        		case "push" :
        			if (linecut.length==4){
        				if(linecut[3].equals("Integer")){
        					client.push(linecut[1], Integer.valueOf(linecut[2]));
        				}
        				else{
        					client.push(linecut[1], linecut[2]);
        				}
        			}
        			else{
        				System.out.println("syntaxe : push <key> <value> <type=Integer||String>");
        			}
        			break;
        		case "delete" :
        			if (linecut.length==3){
        				client.delete(linecut[1],Integer.valueOf(linecut[2]));
        			}
        			else{
        				System.out.println("syntaxe : delete <key> <index>");
        			}
        			break;
        		case "clear" :
        			if (linecut.length==2){
        				client.clear(linecut[1]);
        			}
        			else{
        				System.out.println("syntaxe : clear <key>");
        			}
        			break;
        		default :
        			build.append("CupofKey client \n");
                	build.append("Commanndes possibles : \n");
                	build.append("get, set, exist, incr, mult \n"); 
        		}
        	}
        	scan.close();
        	client.close();
        } 
        catch (Exception e) 
        {
			System.err.println(e.getMessage());
		}    	
    }


}
