package pl.tendo;

import javax.json.*;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import com.google.gson.Gson;

/**
 * Klasa zakodowuj¹ca wiadomoœci wysy³ane z serwera
 * zamienia tablicê liczb danych na Java Script Object Notation,
 * z którego potem tworzony jest string
 * 
 * Do przesy³ania wiadomoœci wykorzystywany jest WebSocket
 * 
 * @author £ukasz Pik 
 */

public class ServerEncoder implements Encoder.Text<ServerMessage> {
	public void init(EndpointConfig config) {
    }
 
    /** 
     * @see javax.websocket.Encoder#destroy()
     */
    public void destroy() {
    }
 
    /**
     * @see javax.websocket.Encoder.Text#encode(java.lang.Object)
     */
    public String encode(ServerMessage mail) throws EncodeException {
    	Gson gson = new Gson();
     /*   return Json.createObjectBuilder()
                        .add("Player1", gson.toJson(mail.getPack(0)))
                   .build().toString();*/
   /* String ticket = Json.createArrayBuilder()
    					.add(mail.getPack(0)[0])
    					.add(mail.getPack(0)[1])
    					.add(mail.getPack(1)[0])
    					.add(mail.getPack(1)[1])
    					.build().toString();*/
    String ticket = Json.createObjectBuilder()
    				.add("P1", gson.toJson(mail.getPack(0))) 
    					/*	Json.createArrayBuilder()
    											.add(mail.getPack(0)[0])
    											.add(mail.getPack(0)[1]))*/
    				.add("P2", gson.toJson(mail.getPack(1))) 
    				/*		Json.createArrayBuilder()
    											.add(mail.getPack(1)[0])
    											.add(mail.getPack(1)[1]))*/
    				.add("Can", gson.toJson(mail.getPack(2)))
    						//Json.createArrayBuilder()
    										//	.add(gson.toJson(mail.getPack(2))))
    										/*	.add(mail.getPack(2)[0])
    											.add(mail.getPack(2)[1])
    											.add(mail.getPack(2)[2])
    											.add(mail.getPack(2)[3]))*/
    				.add("Helper", gson.toJson(mail.getPack(3)))
    				/*		Json.createArrayBuilder()
    											.add(mail.getPack(3)[0])
    											.add(mail.getPack(3)[1]))*/
    				.add("Gifts", gson.toJson(mail.getPack(4)))
    				.add("Poc", gson.toJson(mail.getPack(5)))
    				.add("K", gson.toJson(mail.getPack(6)))
    				.build().toString();
    				
    				
    				
    				
    				
  //  System.out.println("ticket: "+ticket);
    return ticket;
    }
}
