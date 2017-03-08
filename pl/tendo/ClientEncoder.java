package pl.tendo;

import javax.json.*;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Transluje wiadomoœci, które maj¹ zostaæ wys³ane do serwera od Klienta
 * 
 * @author £ukasz Pik
 */
public class ClientEncoder implements Encoder.Text<ClientMessage> {
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
	    public String encode(ClientMessage mail) throws EncodeException {
	    	
	        return Json.createObjectBuilder()
	                        .add("w", mail.getPrzy(0))
	                        .add("a", mail.getPrzy(1))
	                        .add("d", mail.getPrzy(2))
	                        .add("s", mail.getPrzy(3))
	                        .add("t", mail.getPrzy(4))
	                        .add("g", mail.getPrzy(5))
	                        .add("y", mail.getPrzy(6))
	                   .build().toString();
	    }
}
