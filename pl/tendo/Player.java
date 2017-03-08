package pl.tendo;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Klasa opisuj¹ca gracza, odpowiedzialna
 * za fizykê(spadanie i skakanie)
 * 
 * @author Maciek Ruszczyk
 * @see Obiekty
 */

public class Player extends Obiekty{
	
	Direction dir;
	Direction look;
	
	Image im;
	Image im_move;
	Image im_l;
	
	boolean isFalling;
	boolean isJumping;
	boolean isRising;
	boolean isMoving;
	
	boolean isDead;
	int HP;
	long timeOfFalling;
	
	double acceleration;
	final static double gravity_factor = 0.2;
	
	boolean press[];
	
	int nr;		//numer gracza
	int ammo; //ilosc amunicji
	
	public Player(int numer){
		
		POS_X=100;
		POS_Y=0;
		
		dir = Direction.None;
		look = Direction.Right;
		
		if(numer == 0){
			im = new ImageIcon("rycerz.png").getImage();
			im_move = new ImageIcon("rycerz.gif").getImage();
		}
		else if(numer == 1){
			im = new ImageIcon("rycerzP2.png").getImage();
			im_move = new ImageIcon("rycerz_P2.gif").getImage();
			setPositionX(924);
			look=Direction.Left;
		}
		HP = 100;
		isDead = false;
		//im = new ImageIcon("rycerz.png").getImage();
		//im_l = im_l.getScaledInstance(im_l.getWidth(null), im_l.getHeight(null), Image.)
		//		im_l.
		
		HEIGHT=61;	//in pixels
		WIDTH=39;
		
		isFalling = true;
		isJumping = false;
		isRising = false;
		isMoving = false;
		
		acceleration = 0;
		timeOfFalling = 0;
		
		ammo = 15;
		nr = numer;
		
		press = new boolean[] {false, false, false, false};
	}
	
	
	
	public int getState(){
		if(isMoving){
			if(look == Direction.Right)
				return 1;
			else
				return 2;
		}
		else
			return 0;
			
	}
	/**
	 * Metoda sluzaca do poinformowania, ¿e gracz podskoczy³
	 */
	public void jump(){
		isJumping=true;
		isRising=true;
		isFalling=false;
		acceleration=8;
	}
	
	private void stopRising(){
		isFalling=true; 
		isRising=false;
		acceleration=0; 
		isJumping=true; 
		timeOfFalling=0;
	}
	
	private void stopFalling(){
		isFalling=false; 
		acceleration=0; 
		isJumping=false; 
		timeOfFalling=0;
	}
	/**
	 * Metoda sluzaca do "kwadratowego" opadania sie gracza podczas skoku
	 * 
	 * @param time_elap - czas ktory minal od poprzedniego cyklu timera
	 * @param time_of_fall- calkowity czas spadania gracza
	 * @return
	 */
	public double func(double time_elap, double time_of_fall){	
		
		double time_of_fa = time_of_fall/1000;
		double time_ela = time_elap/1000;
		
		double time_2 = 1000*((time_of_fa + time_ela)*(time_of_fa + time_ela))/2 ;
		
		double time_1 = 1000*(time_of_fa * time_of_fa)/2;
		
		//System.out.println(time_2-time_1);
		return time_2-time_1;
	}
	/**
	 * Metoda sluzaca do "kwadratowego" wznoszenia sie gracza podczas skoku
	 * 
	 * @param time_elap - czas ktory minal od poprzedniego cyklu timera
	 * @param time_of_fall - calkowity czas wznoszenia sie gracza
	 * @return
	 */
	public double func_2(double time_elap, double time_of_fall){	
		
		double time_of_fa = time_of_fall/1000;
		double time_ela = time_elap/1000;
		
		double time_2 = (600*(time_of_fa + time_ela)) -(1000*((time_of_fa + time_ela)*(time_of_fa + time_ela))/2) ;
		
		double time_1 = (600*(time_of_fa))     -(1000*(time_of_fa * time_of_fa))/2;
		
		//System.out.println(time_2-time_1);
		if(time_2-time_1 <= 0){
			stopRising();
			return 0;
		}
		
		return time_2-time_1;
	}
	
	/**
	 * Metoda sprawdzajaca czy gracz mo¿e jeszcze siê wznosiæ
	 * 
	 * @return true, jeœli mo¿e siê wznosiæ
	 */
	public boolean tryRise(){
		double pos_x = getPositionX();
		double pos_y = getPositionY();
		int height = HEIGHT;
		int width  = WIDTH;
		boolean isCollide = false;
		
		//if(acceleration < 0){
		//	stopRising();
		//	return false;
		//}
		
		
		if((pos_y-4) < 0) {				//poza mape
			//setPositionY(0);
			stopRising();
			isCollide = true;		
		}	
		
		else if (checkCollisionY(0, width-1, -4, 0, pos_x, pos_y)){
			stopRising();
			isCollide = true;
		}
			
		
		if(isCollide){
			setPositionY((int)((pos_y+10)/32) * 32);		//+10 na zapas
			return false;
		}
		else{
			isRising = true;		
			acceleration-=Player.gravity_factor;
			return true;
		}
	}
	/**
	 * Metoda sprawdzajaca, czy gracz mo¿e zacz¹æ lub nadal spadaæ
	 * 
	 * @return true, jeœli mo¿e spadaæ
	 */
	public boolean tryFall(){
		
		double pos_x = getPositionX();
		double pos_y = getPositionY();
		int height = HEIGHT;
		int width  = WIDTH;
		boolean isCollide = false;
		
		if(((pos_y+4+height-1) / 32) >= 24) {		//spada poza mape
			stopFalling();
			isCollide = true;
			setPositionY((int)((pos_y+4)/32) * 32);
			//setPositionY(1024-height-3);
			return false;
		}
		else if(checkCollisionY(0, width-1, 4, height-1, pos_x, pos_y)){
			stopFalling();
			isCollide = true;
		}
		
		if(isCollide){
			setPositionY((int)((pos_y+4)/32) * 32);
			return false;
		}
		else{
			isFalling = true;
			acceleration+=Player.gravity_factor;
			return true;
		}
	}
	
	
	
}
