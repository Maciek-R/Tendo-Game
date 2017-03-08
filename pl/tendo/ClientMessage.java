package pl.tendo;

/**
 * Klasa u�atwiaj�ca przetrymywanie wiadomo�ci wysy�anych z Klienta
 *
 * @author �ukasz Pik
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
