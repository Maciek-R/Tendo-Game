package pl.tendo;

/**
 * Klasa opisuj¹ca prezenty rozrzucane na planszy
 * 
 * @author Maciek Ruszczyk
 * @see Obiekty
 */

public class Gift extends Obiekty{

	final static int NUMBER_OF_GIFTS = 2;
	int gift;
	
	public Gift(double pos_x, double pos_y){
		HEIGHT = 12;
		WIDTH = 12;
		
		POS_X = pos_x;
		POS_Y = pos_y;
		
		gift = (int) Math.floor(Math.random()*NUMBER_OF_GIFTS);		
		//System.out.println("prezent: "+gift);
	}
	public Gift(double pos_x, double pos_y, int x){
		HEIGHT = 12;
		WIDTH = 12;
		
		POS_X = pos_x;
		POS_Y = pos_y;
		
		gift = x;	//todo 0..2
		//System.out.println("prezent: "+gift);
	}
	
	
}
