package pl.tendo;

/**
 * Klasa abstrakcyjna, po której dziedzicz¹ wszystkie obiekty,
 * które s¹ widoczne na planszy(po³o¿enie i rozmiar obiektów)
 * oraz posiadaj¹ca metody odpowiedzialne za sprawdzanie kolizji
 * 
 * @author Maciek Ruszczyk
 * @see Player
 * @see Helper
 * @see Dzialko
 * @see Gift
 * @see Pocisk
 */

public abstract class Obiekty {

	enum Direction{
		None, Right, Left, Up, Down;
	}
	
	protected double POS_X;
	protected double POS_Y;
	
	protected int HEIGHT;	//in pixels
	protected int WIDTH;
	
	public void setPositionX(double pos_x){
		POS_X = pos_x;
	}
	public void setPositionY(double pos_y){
		POS_Y = pos_y;
	}
	public double getPositionX(){
		return POS_X;
	}
	public double getPositionY(){
		return POS_Y;
	}
	
	/**
	 * Metoda sprawdzajaca kolizje w poziomie
	 * 
	 * @param przesX - przesuniecie w poziomie(o ile przesuwa sie obiekt)
	 * @param offsetX - przesuniecie wzgledem ktorego sprawdzana jest kolizja w poziomie
	 * @param przesY - przesuniecie w pionie(o ile przesuwa sie obiekt)
	 * @param offsetY - przesuniecie wzgledem ktorego sprawdzana jest kolizja w pionie
	 * @param pos_x - aktualna pozycja obiektu w poziomie
	 * @param pos_y - aktualna pozycja obiektu w pionie
	 * @return true, jeœli by³a kolizja, false w przeciwnym przypadku
	 */
	public boolean checkCollisionX(double przesX, double offsetX, double przesY, double offsetY, double pos_x, double pos_y){
		if((0 < Map.get((pos_x+przesX+offsetX) / 32, (pos_y+przesY) / 32)))
			return true;
		if((0 < Map.get((pos_x+przesX+offsetX) / 32, (pos_y+przesY+offsetY) / 32)))
			return true;
		if((0 < Map.get((pos_x+przesX+offsetX) / 32, (pos_y+przesY+offsetY/2) / 32)))
			return true;
		
		return false;	
	}
	
	/**
	 * Metoda sprawdzajaca kolizje w pionie
	 * 
	 * @param przesX - przesuniecie w poziomie(o ile przesuwa sie obiekt)
	 * @param offsetX - przesuniecie wzgledem ktorego sprawdzana jest kolizja w poziomie
	 * @param przesY - przesuniecie w pionie(o ile przesuwa sie obiekt)
	 * @param offsetY - przesuniecie wzgledem ktorego sprawdzana jest kolizja w pionie
	 * @param pos_x - aktualna pozycja obiektu w poziomie
	 * @param pos_y - aktualna pozycja obiektu w pionie
	 * @return true, jeœli by³a kolizja, false w przeciwnym przypadku
	 */
	
	public boolean checkCollisionY(double przesX, double offsetX, double przesY, double offsetY, double pos_x, double pos_y){
		if((0 < Map.get((pos_x+przesX) / 32, (pos_y+przesY+offsetY) / 32)))
			return true;
		if((0 < Map.get((pos_x+przesX+offsetX) / 32, (pos_y+przesY+offsetY) / 32)))
			return true;
		if((0 < Map.get((pos_x+przesX+offsetX/2) / 32, (pos_y+przesY+offsetY) / 32)))
			return true;
		
		return false;	
	}
	/**
	 * Metoda sprawdzajaca, czy obiekt jest poza map¹ w poziomie
	 * 
	 * @param dir - kierunek przesuwajacego sie obiektu
	 * @param przes - przesuniecie obiektu w poziomie
	 * @return true, jeœli obiekt jest poza mapa, false w przeciwnym przypadku
	 */
	public boolean isOutOfMapX(Direction dir, double przes){
		
		if(dir == Direction.Left){
			if((POS_X-przes) < 0) 
				return true;
		}
		else if(dir == Direction.Right){
			if((POS_X+przes+WIDTH-1) / 32 >= 32) 
				return true;
		}
		
		return false;
	}
	/**
	 * Metoda sprawdzajaca, czy obiekt jest poza map¹ w pionie
	 * 
	 * @param przes - przesuniecie obiektu w pionie
	 * @return true, jeœli obiekt jest poza mapa, false w przeciwnym przypadku
	 */
	public boolean isOutOfMapY(double przes){
		
		if(((POS_Y+przes+HEIGHT-1) / 32) >= 24)
				return true;
		
		return false;
	}
	
}
