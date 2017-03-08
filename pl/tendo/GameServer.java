package pl.tendo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.glassfish.tyrus.server.Server;
/** 
 * Klasa slu¿¹ca do uruchomienia serwera
 *
 * @author £ukasz Pik
 */


public class GameServer {
	
	
	public static void main(String[] args){
		runServer();
	}
	
	public static void runServer(){
		//Server server = new Server("192.168.0.3", 59234, "/websockets", GameServerEndpoint.class );
		//Server server = new Server("169.254.199.117", 59234, "/websockets", GameServerEndpoint.class );
		Server server = new Server("localhost", 59234, "/websockets", GameServerEndpoint.class );
		try{
			server.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
			
		}
		catch (Exception e) {
            e.printStackTrace();
		}
		finally{
			server.stop();
		}
		
	}

}
