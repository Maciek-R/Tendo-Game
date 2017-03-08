package pl.tendo;

/**
 * Klasa opisuj¹ca dzia³ka, za pomoc¹ których
 * mo¿na wystrzeliwaæ pociski
 * posiada metodê sprawdzaj¹c¹, czy jakiœ gracz jest w pobli¿u
 * 
 * @author Maciek Ruszczyk
 * @see Obiekty
 */

public class Dzialko extends Obiekty {

	enum Podnoszenie{
		None, gora, dol;
	}
	
	Direction di;
	Podnoszenie dir;
	double kat;
	
	public Dzialko(double pos_x, double pos_y, Direction d){
		//POS_X = 192;
		//POS_Y = 512;
		POS_X = pos_x;
		POS_Y = pos_y;
		
		dir = Podnoszenie.None;
		this.di = d;
		kat = 0;
	}
	
	/**
	 * Metoda sprawdzajaca, czy jakiœ gracz jest blisko dzia³ka
	 * 
	 * @param pos_x - pozycja gracza w poziomie
	 * @param pos_y - pozycja gracza w pionie
	 * @return true, jeœli jakiœ gracz jest blisko
	 */
	public boolean isNearbyPlayer(double pos_x, double pos_y){
		
		double x=0;
		
		if(di == Direction.Right)
			x = (POS_X - pos_x - 32) * (POS_X - pos_x - 32);
		else if(di == Direction.Left)
			x = (POS_X - pos_x + 32) * (POS_X - pos_x + 32);
		double y = (POS_Y - pos_y) * (POS_Y - pos_y);
		
		if(Math.sqrt(x+y) < 55)
			return true;
		else 
			return false;
	}
}
