package pl.tendo;

import javax.websocket.Decoder;

import java.io.StringReader;

import javax.json.*;
import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;
import com.google.gson.Gson;

/**
 * Klasa dekoduj¹ca wiadomoœci przesy³ane z serwera,
 * aby spowrotem by³y tablicê liczb, które przechowywane s¹ w specjalnych 
 * strukturach danych 
 *
 * @author £ukasz Pik
 * @see ServerMessage
 */

public class ServerDecoder implements Decoder.Text<ServerMessage>{

	 
    /**
     * @see javax.websocket.Decoder#init(javax.websocket.EndpointConfig)
     */
    public void init(EndpointConfig config) {
    }
 
    /**
     * @see javax.websocket.Decoder#destroy()
     */
    public void destroy() {
    }
 
    /**
     * @see javax.websocket.Decoder.Text#decode(java.lang.String)
     */
    public ServerMessage decode(String s) throws DecodeException {
    	
    	Gson gson = new Gson();
    	//System.out.println("S: "+s);
        //JsonObject jsonObject = Json.createReader(new StringReader(s)).readObject();
        
       /* System.out.println("przed kons s: "+jsonObject.toString());
        String buffor = jsonObject.toString();
        buffor = buffor.replace("{\"Player1\":", "");
        buffor = buffor.replace("}", "");
        buffor = buffor.replace("\"", "");
        System.out.println(buffor);
        
        double[] test = gson.fromJson(buffor, double[].class);   //////////
        System.out.println("X: "+test[0]+" Y: "+test[1]);
        ServerMessage mail = new ServerMessage(gson.fromJson(buffor, double[].class));
        System.out.println("po kons");*/
        
        
      /*  System.out.println("przed add pack");
        	mail.addPack(gson.fromJson(jsonObject.toString(), double[].class));
        	System.out.println("po add pack");
        	double[] response = mail.getPack(0);
    		System.out.println("X: "+response[0]+"Y: "+response[1]);*/
        //System.out.println("test");
        //double[] test;
       // System.out.println("Json "+jsonObject);
    	//System.out.println("X: "+test[0]+" Y: "+test[1]);
        
    	
    	
    	
    	//double[] test = gson.fromJson(s, double[].class);                   to dzialalo
    	
    	JsonObject json = Json.createReader(new StringReader(s)).readObject();
    	//String p1 = json.getString("P1");
    	int[] player1 = gson.fromJson(json.getString("P1"), int[].class);
    	int[] player2 = gson.fromJson(json.getString("P2"), int[].class);
    //	System.out.println("Test: "+json.getString("Can"));
    	int[] can = gson.fromJson(json.getString("Can"), int[].class);
    	int[] helper = gson.fromJson(json.getString("Helper"), int[].class);
    	int[] gifts = gson.fromJson(json.getString("Gifts"), int[].class);
    	int[] bullets = gson.fromJson(json.getString("Poc"), int[].class);
    	int[] terrain = gson.fromJson(json.getString("K"), int[].class);
        
        ServerMessage mail = new ServerMessage(player1);
        mail.addPack(player2);
        mail.addPack(can);
        mail.addPack(helper);
        mail.addPack(gifts);
        mail.addPack(bullets);
        mail.addPack(terrain);
        return mail;
    }
 
    /**
     * @see javax.websocket.Decoder.Text#willDecode(java.lang.String)
     */
    public boolean willDecode(String s) {
        return true;
    }

}
