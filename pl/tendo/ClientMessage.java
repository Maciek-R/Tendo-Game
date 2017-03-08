package pl.tendo;

/**
 * Klasa ułatwiająca przetrymywanie wiadomości wysyłanych z Klienta
 *
 * @author Łukasz Pik
 */

public class ClientMessage {
	boolean[] przyciski;
	
	public ClientMessage(){			//w   ,a    , d,     ,s		t,		g,		y
		przyciski = new boolean[] { false, false, false, false, false, false, false};
	}
	public void setPrzy(int i, boolean war){
		przyciski[i] = war;
	}
	public boolean getPrzy(int i){
		return przyciski[i];
	}


}
