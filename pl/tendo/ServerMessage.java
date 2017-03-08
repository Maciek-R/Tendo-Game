package pl.tendo;

import java.util.Vector;

/**
 * Klasa u�atwiaj�ca przetrzymywanie danych, kt�re zostaj� wysy�ane do klienta
 *
 * @author �ukasz Pik
 */

public class ServerMessage {
	Vector<int[]> obj;
	//double[] Pack;

	ServerMessage(){
		obj = new Vector<int[]>();
	}
	ServerMessage(int[] pos){
		
		obj = new Vector<int[]>();
		
		obj.add(pos);
		
	}
	
	
	public void addPack(int[] pack){
		obj.add(pack);
		
	}
	
	public int[] getPack(int index){
		return obj.get(index);
	}
	
	public Vector<int[]> getMess(){
		return obj;
	}
	
	
}
