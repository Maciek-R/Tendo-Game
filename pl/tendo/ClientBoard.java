package pl.tendo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;

import pl.tendo.Obiekty.Direction;

/**
 * 
 *
 * Klasa generuj¹ca widok po stronie klienta,
 * a tak¿e odbieraj¹ca sygna³y nadawane przez gracza,
 * które s¹ potem wysy³ane do serwera,
 * a tak¿e aktualizuje dane, które zosta³y odebrane z serwera
 *
 * @author £ukasz Pik
 */
public class ClientBoard extends MainBoard implements ActionListener {
	
	ClientMessage mess;
	
	SluchaczKey adap;
	javax.swing.Timer timer;
	

	
	public ClientBoard() {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setPreferredSize(new Dimension(BOARD_WIDTH_PIX, BOARD_HEIGHT_PIX));
		
		mess = new ClientMessage();
		
		adap = new SluchaczKey();	
		this.addKeyListener(adap);
		
		timer = new Timer(12, this);
	}
	
	
	
	public void start(){
			
			timer.start();
			time_start = System.currentTimeMillis();
	}
		
		
		
	public void actionPerformed(ActionEvent act) {
			
			long time = System.currentTimeMillis();
			
			if(GameOver){
				if(time-Time_end_round > 5000){
					GameOver = false;
				}
			}			
			
			repaint();
	}	
	
	public void newFrame(){
		repaint();
	}
	
	private void updateCannons(int[] table){
		for(int i=0 ; i < dzialo.size() ; ++i ){
			dzialo.get(i).kat = (double)table[i];
		}
	}
	private void updateHelper(int[] table){
		helper.POS_X = (double)table[0];
		helper.POS_Y = (double)table[1];
		if(table[0] > 990)
			helper.dir = Direction.Left;
		if(table[0] < 4)
			helper.dir = Direction.Right;
	}
	private void updateGifts(int[] table){
		//helper.gifts.removeAllElements();
		helper.gifts.clear();
		
		
		for(int i = 0 ; i < table[0] ; ++i){
			helper.add_gift(table[3*i+1], table[3*i+2], table[3*i+3]);
		}
	}
	
	private void updateBullets(int[] table){
		/*pociski[0] = table[0];
		for(int i = 0 ; i < table[0] ; ++i){
			pociski[2*i+1] = table[2*i+1];
			pociski[2*i+2] = table[2*i+2];
		}*/
		pociski.clear();
		
		for(int i = 0 ; i < table[0] ; ++i){
			pociski.add(new Pocisk(table[2*i+1], table[2*i+2]));
		}
		
	}

	private void updateTerrain(int[] table){
		int[] pos = new int[2];
		if(table[0] != 40){
			pos[0] = table[0];
			pos[1] = table[1];
			Map.destroy(pos);
			
		}
		if(table[2] != 40){
			pos[0] = table[2];
			pos[1] = table[3];
			Map.destroy(pos);
		}
	}


	private void setPlayer(int i, int[] table){
		player[i].setPositionX(table[0]);
		player[i].setPositionY(table[1]);
		player[i].HP = table[2];
		player[i].ammo = table[3];
		
		switch(table[4]){
		case 0: player[i].isMoving = false; break;
		case 1: player[i].isMoving = true; player[i].look = Direction.Right; break;
		case 2: player[i].isMoving = true; player[i].look = Direction.Left; break;
		}
		
		if(player[i].HP<=0){
			if(GameOver==false){
				
				wynik[0] = (i==1)?wynik[0]+1:wynik[0];
				wynik[1] = (i==0)?wynik[1]+1:wynik[1];
				
				GameOver=true;
				Time_end_round = System.currentTimeMillis();
			}
		}
	}
	
	public void setAll(ServerMessage mail){
		setPlayer(0, mail.getPack(0));
		setPlayer(1, mail.getPack(1));
		updateCannons(mail.getPack(2));
		updateHelper(mail.getPack(3));
		updateGifts(mail.getPack(4));
		updateBullets(mail.getPack(5));
		updateTerrain(mail.getPack(6));
	}


	class SluchaczKey implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
			
			int keyCode = e.getKeyCode();
			
			switch(keyCode){
			/*case 'w': przyciski[0] = true; break;
			case 'a': przyciski[1] = true; break;
			case 'd': przyciski[2] = true; break;
			case 's': przyciski[3] = true; break;*/
			case KeyEvent.VK_W: mess.setPrzy(0, true); break;
			case KeyEvent.VK_A: mess.setPrzy(1, true); break;
			case KeyEvent.VK_D: mess.setPrzy(2, true); break;
			case KeyEvent.VK_S: mess.setPrzy(3, true); break;
			case KeyEvent.VK_T: mess.setPrzy(4, true); break;
			case KeyEvent.VK_G: mess.setPrzy(5, true); break;
			case KeyEvent.VK_Y: mess.setPrzy(6, true); break;
			
			}
			
			/*for(int i = 0 ; i < 4 ; ++i){
				mess.setPrzy(i, przyciski[i]);
			}*/
			Client.send(mess);
		}
	
		@Override
		public void keyReleased(KeyEvent e) {
		
			int keyCode = e.getKeyCode();
			
			switch(keyCode){
				case KeyEvent.VK_W: mess.setPrzy(0, false); break;
				case KeyEvent.VK_A: mess.setPrzy(1, false); break;
				case KeyEvent.VK_D: mess.setPrzy(2, false); break;
				case KeyEvent.VK_S: mess.setPrzy(3, false); break;
				case KeyEvent.VK_T: mess.setPrzy(4, false); break;
				case KeyEvent.VK_G: mess.setPrzy(5, false); break;
				case KeyEvent.VK_Y: mess.setPrzy(6, false); break;
			}
	
	
			Client.send(mess);
		}
	
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
			
		}
		
	}





}
