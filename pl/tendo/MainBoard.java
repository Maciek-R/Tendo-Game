package pl.tendo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import javax.swing.JPanel;

import pl.tendo.Obiekty.Direction;

/**
 * Klasa abstrakcyjna przechowuj¹ca pola i metody wykorzystywane
 * po stronie serwera oraz klienta tj. obiekty wystêpuj¹ce na planszy,
 * rozmiar planszy i klocków oraz metody odpowiedzialne za rysowanie
 * 
 * @author Maciek Ruszczyk
 * @see Board
 * @see ClientBoard
 */

public abstract class MainBoard extends JPanel {

	protected final static int BOARD_WIDTH_PIX = 1024;
	protected final static int BOARD_HEIGHT_PIX = 820;
	protected final static int WIDTH_KLOCKA = 32;
	protected final static int HEIGHT_KLOCKA = 32;
	
	Player[] player;
	Helper helper;
	Vector<Dzialko> dzialo;
	Vector<Pocisk> pociski;
	
	double time_start;
	double time_elapsed;
	
	int wynik[];
	long Time_end_round;
	boolean GameOver = false;
	
	public MainBoard(){
		//System.out.println("Main");
		player = new Player[2];
		player[0] = new Player(0);
		player[1] = new Player(1);
		
		helper = new Helper();
		dzialo = new Vector<Dzialko>();
		createCannons();
		pociski = new Vector<Pocisk>();
		
		wynik = new int[2];
		wynik[0]=0;
		wynik[1]=0;
		
		Map.init();	
	}
	protected void drawPlayer(Graphics g){
		
		Graphics2D g2 = (Graphics2D)g;
		
		for(int i=0; i<2; ++i){
			if(player[i].look==Direction.Right){
				if(player[i].isMoving)
					g2.drawImage(player[i].im_move, (int)player[i].getPositionX(), (int)player[i].getPositionY(), null);
				else
					g2.drawImage(player[i].im, (int)player[i].getPositionX(), (int)player[i].getPositionY(), null);
				
			}
			else{
				AffineTransform at = AffineTransform.getTranslateInstance(player[i].getPositionX(), player[i].getPositionY());
				at.translate(player[i].WIDTH, 0);
				at.scale(-1, 1);
			
				if(player[i].isMoving){
					g2.drawImage(player[i].im_move, at, null);
				}
				else{
					g2.drawImage(player[i].im, at, null);
				}
			}
		}
	}
	protected void drawBullets(Graphics g){
		for(int i=0; i<pociski.size(); ++i)
			g.drawImage(Pocisk.im, (int)pociski.elementAt(i).getPositionX(), (int)pociski.elementAt(i).getPositionY(), null);
	}
	protected void drawGifts(Graphics g){
		for(int i=0; i<helper.gifts.size(); ++i)
			if(helper.gifts.elementAt(i).gift==0)
				g.drawImage(helper.im_hp, (int)helper.gifts.elementAt(i).getPositionX(), (int)helper.gifts.elementAt(i).getPositionY(), null);
			else if(helper.gifts.elementAt(i).gift==1)
				g.drawImage(helper.im_ammo, (int)helper.gifts.elementAt(i).getPositionX(), (int)helper.gifts.elementAt(i).getPositionY(), null);
	}
	protected void drawHelper(Graphics g){
		if(helper.dir == Direction.Left)
			g.drawImage(helper.im, (int)helper.getPositionX(), (int)helper.getPositionY(), null);
		else{
			
			g.drawImage(helper.im, (int)helper.getPositionX()+ helper.WIDTH, (int)helper.getPositionY(), -helper.WIDTH, helper.HEIGHT, null);
		}
	}
	protected void drawMap(Graphics g){
		//g.setColor(Color.BLACK);
		
		for(int j=0; j<24; ++j){
			for(int i=0; i<32; ++i){
				if(Map.get(i, j)==1){
					g.drawImage(Map.texture_grass, i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA, null);
					//g.fillRect(i*WIDTH_KLOCA, j*HEIGHT_KLOCA, WIDTH_KLOCA, HEIGHT_KLOCA);
				}
				else if(Map.get(i, j)==2){
					g.drawImage(Map.texture_wall, i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA, null);
				}
				else if(Map.get(i, j)==3){
					g.drawImage(Map.texture_kola_arm_r, i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA, null);
				}
				else if(Map.get(i, j)==4){
					//g.fillRect(i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA, WIDTH_KLOCKA, HEIGHT_KLOCKA);
					double kat=0;
					
						for(int k=0; k<dzialo.size(); ++k)
							if(dzialo.get(k).getPositionX() == i*WIDTH_KLOCKA && dzialo.get(k).getPositionY() == j*HEIGHT_KLOCKA)
								kat = dzialo.get(k).kat;
						
						AffineTransform at = AffineTransform.getTranslateInstance(i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA);
						at.rotate(Math.toRadians(kat), 0, Map.texture_lufa_r.getHeight(null)/2 - 4);
						
						Graphics2D g2 = (Graphics2D)g;
						g2.drawImage(Map.texture_lufa_r, at, null);
				}
				else if(Map.get(i, j)==5){
					g.drawImage(Map.texture_kola_arm_l, i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA, null);
				}
				else if(Map.get(i, j)==6){
					
					double kat=0;
					
					for(int k=0; k<dzialo.size(); ++k)
						if(dzialo.get(k).getPositionX() == i*WIDTH_KLOCKA && dzialo.get(k).getPositionY() == j*HEIGHT_KLOCKA)
							kat = dzialo.get(k).kat;
					
					
						AffineTransform at = AffineTransform.getTranslateInstance(i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA);
						at.rotate(Math.toRadians(-kat), Map.texture_lufa_l.getWidth(null), Map.texture_lufa_l.getHeight(null)/2 - 4);
						
						Graphics2D g2 = (Graphics2D)g;
						g2.drawImage(Map.texture_lufa_l, at, null);
				}
				else if(Map.get(i, j)==7){
					g.drawImage(Map.texture_dirt, i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA, null);
				}
			}
		}
	 }
	 protected void drawHealth(Graphics g){
		
		g.setColor(Color.red);
		g.fillRect(50, 780, player[0].HP, 20);
		g.fillRect(1024-50-250, 780, player[1].HP, 20);
		g.setColor(Color.black);
		g.drawRect(50, 780, 100, 20);
		g.drawRect(1024-50-250, 780, 100, 20);
		//ammo
		g.setColor(Color.orange);
		g.fillRect(200, 780, (int)(((double)(player[0].ammo)/30)*100), 20);
		g.fillRect(1024-50-100, 780, (int)(((double)(player[1].ammo)/30)*100), 20);
		g.setColor(Color.black);
		g.drawRect(200, 780, 100, 20);
		g.drawRect(1024-50-100, 780, 100, 20);
		//wynik
		g.setColor(Color.red);
		g.drawString(new Integer(wynik[0]).toString(), 400, 800);
		g.drawString(new Integer(wynik[1]).toString(), 600, 800);
		
	 }
	 protected static void drawGrid(Graphics g){
			g.setColor(Color.GREEN);
	    	
			for(int i=0; i<32; ++i){
				g.drawLine(i*WIDTH_KLOCKA, 0, i*WIDTH_KLOCKA, BOARD_HEIGHT_PIX);
			}
			for(int i=0; i<24; ++i){
				g.drawLine(0, i*HEIGHT_KLOCKA, BOARD_WIDTH_PIX, i*HEIGHT_KLOCKA);
			}
	 }
	 protected void drawNumbers(Graphics g){
	    	g.setColor(Color.RED);
	 
	    	Integer X = new Integer(0);
	    	Integer Y = new Integer(0);
	    	
	    	
	    	for(int j=0; j<24; ++j){
	    		
	    		for(int i=0; i<32; ++i){
	    			
	    			g.drawString(X.toString()+";"+Y.toString(), i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA+20);
	    			++X;
	    		}
	    		X=0;
	    		++Y;
	    	}
	 }
	 public void paint(Graphics g){
			super.paint(g);
			
			g.drawImage(Map.bg, 0, 0, null);
			
			drawMap(g);
			drawHealth(g);
			drawBullets(g);
			drawHelper(g);
			drawGifts(g);
			drawPlayer(g);
	}
	 private void createCannons(){
	    	for(int j=0; j<24; ++j){
				for(int i=0; i<32; ++i){
					if(Map.get(i, j)==4){		//right
						dzialo.addElement(new Dzialko(i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA, Direction.Right));
						//System.out.println("stworzono dzialo r");
						
					}
					else if(Map.get(i, j)==6){	//left
						dzialo.addElement(new Dzialko(i*WIDTH_KLOCKA, j*HEIGHT_KLOCKA, Direction.Left));
						//System.out.println("stworzono dzialo l");
					}
				}
	    	}
	    }
}
