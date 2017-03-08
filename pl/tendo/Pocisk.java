package pl.tendo;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Klasa opisuj¹ca pociski wystêpuj¹ce na planszy
 * posiada przedefiniowan¹ metodê sprawdzania kolizji z blokami na planszy
 * 
 * @author Maciek Ruszczyk
 * @see Obiekty
 */

public class Pocisk extends Obiekty{
	
	Direction dir;
	
	static Image im = new ImageIcon("pocisk.png").getImage();
	double kat;
	
	/**
	 * Konstruktor tworzacy pocisk uwzgledniajacy kierunek i kat pocisku
	 * 
	 * @param kat - kat lotu pocisku
	 * @param pos_x - pozycja pocisku w poziomie
	 * @param pos_y - pozycja pocisku w pionie
	 * @param di - kierunek lotu pocisku
	 */
	public Pocisk(double kat, double pos_x, double pos_y, Direction di){
		HEIGHT = 8;
		WIDTH = 8;
		
		dir = di;
		this.kat = kat;
		
		if(di == Direction.Right){
			POS_X = pos_x + 32;
			POS_Y = (int)(pos_y + 16 + (kat/30)*14);
		}
		else if(di == Direction.Left){
			POS_X = pos_x;
			POS_Y = (int)(pos_y + 16 + (kat/30)*14);
		}
	}
	/**
	 * Konstruktor tworzacy pocisk, bez uwzgledniania kierunku i kata pocisku
	 * wykorzystywana tylko przez klienta
	 * 
	 * @param pos_x - pozycja pocisku w poziomie
	 * @param pos_y - pozycja pocisku w pionie
	 */
	public Pocisk(double pos_x, double pos_y){	//konstruktor dla klienta
		POS_X = pos_x;
		POS_Y = pos_y;
	}
	
	public double getKat(){
		return kat;
	}
	/**
	 * Metoda sprawdzajaca kolizje pocisku w poziomie
	 * 
	 * @param przesX - przesuniecie w poziomie(o ile przesuwa sie pocisk)
	 * @param offsetX - przesuniecie wzgledem ktorego sprawdzana jest kolizja w poziomie
	 * @param przesY - przesuniecie w pionie(o ile przesuwa sie pocisk)
	 * @param offsetY - przesuniecie wzgledem ktorego sprawdzana jest kolizja w pionie
	 * @param pos_x - aktualna pozycja pocisku w poziomie
	 * @param pos_y - aktualna pozycja pocisku w pionie
	 * @param pos - tablica zawieraj¹ca wspolrzedne x, y planszy, w które uderzy³ pocisk
	 * @return true, jeœli by³a kolizja, false w przeciwnym przypadku
	 */
	public boolean checkCollisionX(double przesX, double offsetX, double przesY, double offsetY, double pos_x, double pos_y, int pos[]){
		int x = (int)(pos_x+przesX+offsetX) / 32;
		
		if((0 < Map.get(x, (pos_y+przesY) / 32))){
			pos[0] = x;
			pos[1] = (int)((pos_y+przesY) / 32);
			return true;
		}
		if((0 < Map.get(x, (pos_y+przesY+offsetY) / 32))){
			pos[0] = x;
			pos[1] = (int)((pos_y+przesY+offsetY) / 32);
			return true;
		}
		if((0 < Map.get(x, (pos_y+przesY+offsetY/2) / 32))){
			pos[0] = x;
			pos[1] = (int)((pos_y+przesY+offsetY/2) / 32);
			return true;
		}
		
		return false;	
	}
	
}
