package pl.tendo;

import java.io.StringReader;

import javax.json.*;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Odpowiada za dekodowanie wiadomoœci wysy³anych z Klienta do Serwera
 * 
 * @author £ukasz Pik
 */

public class ClientDecoder implements Decoder.Text<ClientMessage> {

	 
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
	    public ClientMessage decode(String s) throws DecodeException {
	    	
	        JsonObject jsonObject = Json.createReader(new StringReader(s)).readObject();
	        ClientMessage mail = new ClientMessage();
	        
	        	mail.setPrzy(0 , jsonObject.getBoolean("w"));
	        	mail.setPrzy(1 , jsonObject.getBoolean("a"));
	        	mail.setPrzy(2 , jsonObject.getBoolean("d"));
	        	mail.setPrzy(3 , jsonObject.getBoolean("s"));
	        	mail.setPrzy(4 , jsonObject.getBoolean("t"));
	        	mail.setPrzy(5 , jsonObject.getBoolean("g"));
	        	mail.setPrzy(6 , jsonObject.getBoolean("y"));
	        return mail;
	    }
	 
	    /**
	     * @see javax.websocket.Decoder.Text#willDecode(java.lang.String)
	     */
	    public boolean willDecode(String s) {
	        return true;
	    }
	
}
