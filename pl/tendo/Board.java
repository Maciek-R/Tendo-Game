package pl.tendo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;
import pl.tendo.Dzialko.Podnoszenie;
import pl.tendo.Obiekty.Direction;

/**
 * Klasa obs³uguj¹ca zadania wykonywane po stronie serwera tj.
 * zmiana wszystkich ruszaj¹cych siê obiektów zarówno pierwszego jak i drugiego gracza
 * (ruch pocisków, poruszanie siê gracza)
 * oraz odpowiedzialna za przesy³anie informacji do klienta
 * 
 * @author Maciek Ruszczyk
 * @see MainBoard
 * @see ServerEncoder
 * @see ServerMessage
 */

public class Board extends MainBoard implements ActionListener{
	
	//1. Prezentacja
	//2. Dokumentacja
	//	a) Java doc
	//	b) Architektura
	//3. Trudnosci
	
	//31.05 wtorek 17:00 - 19:00 sala: 302
	
	private final static double PIXELS_PER_SECONDS=400;	
	private final static double PIXELS_FOR_BULLETS=1000;
	private final static double PIXELS_FOR_GIFTS=200;
	
	Adapter adap;
	javax.swing.Timer timer;
	int terrain[];
	
	
	public Board() {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setPreferredSize(new Dimension(BOARD_WIDTH_PIX, BOARD_HEIGHT_PIX));
		
		adap = new Adapter();	
		this.addKeyListener(adap);
		
		timer = new Timer(12, this);
		
		terrain = new int[4];
		terrain[0] = 40;
		terrain[2] = 40;
	}
	
	public void start(){
		
		timer.start();
		time_start = System.currentTimeMillis();
	}

	//@Override
	public void actionPerformed(ActionEvent act) {
		
		long time = System.currentTimeMillis();
		time_elapsed = time-time_start;
		time_start = time;	
		double time_elapsed_in_sec = time_elapsed/1000;
		
		double przes = (((time_elapsed_in_sec)) * PIXELS_PER_SECONDS);     
		double przes_bull = (((time_elapsed_in_sec)) * PIXELS_FOR_BULLETS); 
		double przes_gift = (((time_elapsed_in_sec)) * PIXELS_FOR_GIFTS);   
		
		if(GameOver){
			if(time-Time_end_round > 5000){
				GameOver = false;
				player[0]=new Player(0);
				player[1]=new Player(1);
				player[1].setPositionX(924);
				player[1].look=Direction.Left;
			}
		}
		

		playerServe(przes);
		helperServe(przes);
		giftServe(przes_gift);
		bulletServe(przes_bull);
		dzialoServe();
				
		createMessage();
		repaint();
	}
	
	
private void createMessage(){
	
	ServerMessage ticket = new ServerMessage();
	int[]y = new int[5];
	y[0] = (int)player[0].getPositionX();
	y[1] = (int)player[0].getPositionY();
	y[2] = player[0].HP;
	y[3] = player[0].ammo;
	y[4] = player[0].getState();
	ticket.addPack(y);
	
	int[] z = new int[5];
	z[0] = (int)player[1].getPositionX();
	z[1] = (int)player[1].getPositionY();
	z[2] = player[1].HP;
	z[3] = player[1].ammo;
	z[4] = player[1].getState();
	
		
		
		
		ticket.addPack(z);
		ticket.addPack(cannonAngles());
		ticket.addPack(helperPos());
		ticket.addPack(giftsPos());
		ticket.addPack(bulletPos());
		ticket.addPack(terrain);
		GameServerEndpoint.send(ticket);
		terrain[0] = 40;
		terrain[2] = 40;
	}
	
	
	
	
	private int[] bulletPos(){
		int[] table = new int[2*pociski.size()+1];
		table[0] = pociski.size();
		for(int i = 0 ; i < pociski.size(); ++i){
			table[2*i+1] = (int)pociski.get(i).POS_X;
			table[2*i+2] = (int)pociski.get(i).POS_Y;
		}
		return table;
	}
	
	
	private int[] giftsPos(){
		int[] table = new int[3*helper.gifts.size()+1];
		table[0] = helper.gifts.size();
		for(int i = 0  ; i< helper.gifts.size(); ++i){
			table[3*i+1] = (int)helper.gifts.get(i).POS_X;
			table[3*i+2] = (int)helper.gifts.get(i).POS_Y;
			table[3*i+3] = helper.gifts.get(i).gift;
		}
		return table;
	}
	
	
	
	private int[] helperPos(){
		int[] table = new int[2];
		table[0] = (int)helper.POS_X;
		table[1] = (int)helper.POS_Y;
		return table;
	}
	private int[] cannonAngles(){
		int[] table = new int[dzialo.size()];
		for(int i = 0  ; i< dzialo.size(); ++i){
			table[i] = (int)dzialo.get(i).kat;
		}
		return table;
	}

	
	
	
	
	

	

	private void playerServe(double przes){
		
		for(int i=0; i<2; ++i){
			double pos_x = player[i].getPositionX();
			double pos_y = player[i].getPositionY();
			int height = player[i].HEIGHT;
			int width  = player[i].WIDTH;
			boolean isCollide = false;
			
			//if(pos_x<0)
				//player[i].setPositionX(0);
			
			//if(pos_y<0)
			//	player[i].setPositionY(0);
			
			//if(pos_x>BOARD_WIDTH_PIX-width)
			//	player[i].setPositionX(BOARD_WIDTH_PIX-width);
			
		//	if(pos_y>BOARD_HEIGHT_PIX-height+1)
			//	player[i].setPositionY(BOARD_HEIGHT_PIX-height+1);
				
					
			if(player[i].dir == Direction.Left){
				isCollide = player[i].isOutOfMapX(Direction.Left, przes);		//czy poza mapa
				
				if(!isCollide && !player[i].checkCollisionX(-przes, 0, 0, height-1, pos_x, pos_y))
					player[i].setPositionX(pos_x-przes);
			}
					
			else if(player[i].dir == Direction.Right){
				isCollide = player[i].isOutOfMapX(Direction.Right, przes);	//czy poza mapa
				
				if(!isCollide && !player[i].checkCollisionX(przes, width-1, 0, height-1, pos_x, pos_y))
					player[i].setPositionX(pos_x+przes);
			}
		}
		
		for(int i=0; i<2; ++i){
			if(!player[i].isRising)
				player[i].tryFall();
			
			if(player[i].isRising){
				
				if(player[i].tryRise()){
				
					player[i].setPositionY(player[i].getPositionY()-player[i].func_2(time_elapsed, player[i].timeOfFalling));
					player[i].timeOfFalling+=time_elapsed;
				}
			}
			
			if(player[i].isFalling){
			
				if(player[i].tryFall()){
					
					player[i].setPositionY(player[i].getPositionY()+player[i].func(time_elapsed, player[i].timeOfFalling));
					player[i].timeOfFalling+=time_elapsed;
				}
			}
		}
	}
    private void helperServe(double przes){
    	
    	helper.time_elapsed+=time_elapsed;
    	
    	if (helper.gifts.size() < Helper.MAX_NUMBER_OF_GIFTS){
			if(helper.time_elapsed >= helper.item_time){
				helper.time_elapsed = 0;
				helper.item_time = Math.random()*2000+3000;
				helper.add_gift();
			}
    	}
		
		boolean isCollide = false;
		double pos_x = helper.getPositionX();
		double pos_y = helper.getPositionY();
		int height = helper.HEIGHT;
		int width  = helper.WIDTH;
		
		
		if(helper.dir == Direction.Left){
			if(isCollide = helper.isOutOfMapX(Direction.Left, przes)) {	//czy poza mapa
				helper.dir = Direction.Right;
			}
			
			if(!isCollide && !helper.checkCollisionX(-przes, 0, 0, height-1, pos_x, pos_y))
				helper.setPositionX(pos_x-przes);
			else
				helper.dir = Direction.Right;
		}
		else if(helper.dir == Direction.Right){
			if(isCollide = helper.isOutOfMapX(Direction.Right, przes)) { //czy poza mapa
				helper.dir = Direction.Left;
			}
			
			if(!isCollide && !helper.checkCollisionX(przes, width-1, 0, height-1, pos_x, pos_y))
				helper.setPositionX(pos_x+przes);
			else
				helper.dir = Direction.Left;
		}	
    }
    private void giftServe(double przes){
    	
    	for(int i=0; i<helper.gifts.size(); ++i){
    	
			boolean isCollide = false;
			double pos_x = helper.gifts.get(i).getPositionX();
			double pos_y = helper.gifts.get(i).getPositionY();
			int height = helper.gifts.get(i).HEIGHT;
			int width  = helper.gifts.get(i).WIDTH;
			
			if(helper.gifts.get(i).isOutOfMapY(przes)) {		//spada poza mape
				//stopFalling();
				isCollide = true;
				helper.gifts.get(i).setPositionY((int)((pos_y+przes)/32) * 32 + 20);
			}
			else if(helper.checkCollisionY(0, width-1, przes, height-1, pos_x, pos_y)){
				isCollide = true;
			}
			
			if(isCollide){
				helper.gifts.get(i).setPositionY((int)((pos_y+4)/32) * 32 + 20);
			}
			else{
				helper.gifts.get(i).setPositionY(pos_y+przes);
			}
			
			//collisions with player
			for(int j=0; j<2; ++j){
				if(helper.gifts.get(i).getPositionX() +4 >= player[j].getPositionX() && helper.gifts.get(i).getPositionX() <= player[j].getPositionX()+player[j].WIDTH-1){
					if(helper.gifts.get(i).getPositionY() >= player[j].getPositionY() && helper.gifts.get(i).getPositionY() <= player[j].getPositionY()+player[j].HEIGHT-1){
						if(helper.gifts.get(i).gift == 0){
							if(player[j].HP >= 90)
								player[j].HP=100;
							else
								player[j].HP+=10;
						}
						else{
							if(player[j].ammo >= 15)
								player[j].ammo = 30;
							else
								player[j].ammo+=15;
						}
						helper.gifts.remove(i);
						break;
					}
				}
			//-------
			}
    	}
    }
    private void bulletServe(double przes_bull){
    	
    			Vector<Boolean> pom;
    			boolean flag = false;
    			pom = new Vector<Boolean>();
    			for(int i =0; i<pociski.size(); ++i)
    				pom.addElement(new Boolean(false));
    			
    		//	System.out.println("ilosc bullets: "+ pociski.size());
    			
    			for(int i=0; i<pociski.size(); ++i){
    				
    				
    				double wsp_X = Math.cos(Math.toRadians(pociski.get(i).getKat()));
    				double wsp_Y = Math.sin(Math.toRadians(pociski.get(i).getKat()));
    				//System.out.println("wsp: "+wsp_X);
    				//System.out.println("wsp: "+wsp_Y);
    				
    				boolean isCollide = false;
    				double poss_x = pociski.get(i).getPositionX();
    				double poss_y = pociski.get(i).getPositionY();
    				int height = pociski.get(i).HEIGHT;
    				int width  = pociski.get(i).WIDTH;
    				int pos[] = new int[2];
    				
    				if(pociski.get(i).dir == Direction.Left){
    					if((poss_x-przes_bull*wsp_X) < 0) {isCollide = true;}		//czy poza mapa}
    					if((poss_y-przes_bull*wsp_Y) < 0) {isCollide = true;}		//czy poza mapa}
    					
    					if(!isCollide && !pociski.get(i).checkCollisionX(-(przes_bull*wsp_X), 0, wsp_Y*przes_bull, height-1, poss_x, poss_y, pos)){
    						
    						  
    						  pociski.get(i).setPositionX(poss_x-(przes_bull*wsp_X));
    						  pociski.get(i).setPositionY(poss_y+(przes_bull*wsp_Y));
    						}	
    					else{
    						pom.set(i, true);
    						if(!flag){
    							terrain[0] = pos[0];
    							terrain[1] = pos[1];
    							flag = true;
    						}
    						else{
    							terrain[2] = pos[0];
    							terrain[3] = pos[1];
    						}
    						Map.destroy(pos);
    					}
    				}
    				else if(pociski.get(i).dir == Direction.Right){
    					if((poss_x+przes_bull*wsp_X+width-1) / 32 >= 32) {isCollide = true; }	//czy poza mapa
    					if((poss_y+przes_bull*wsp_Y+width-1) /32 >=24) {isCollide = true;}		//czy poza mapa}
    					
    					
    					if(!isCollide && !pociski.get(i).checkCollisionX((przes_bull*wsp_X), width-1, wsp_Y*przes_bull, height-1, poss_x, poss_y, pos)){
    						pociski.get(i).setPositionX(poss_x+(przes_bull*wsp_X));
    						 pociski.get(i).setPositionY(poss_y+(przes_bull*wsp_Y));
    						}
    					else{
    						pom.set(i, true);
    						if(!flag){
    							terrain[0] = pos[0];
    							terrain[1] = pos[1];
    							flag = true;
    						}
    						else{
    							terrain[2] = pos[0];
    							terrain[3] = pos[1];
    						}
    						Map.destroy(pos);
    					}
    					
    				}
    				//collisions with player
    				
    				for(int j=0; j<2; ++j){
	    				if(pociski.get(i).getPositionX() >= player[j].getPositionX() && pociski.get(i).getPositionX() <= player[j].getPositionX()+player[j].WIDTH-1){
							if(pociski.get(i).getPositionY() >= player[j].getPositionY() && pociski.get(i).getPositionY() <= player[j].getPositionY()+player[j].HEIGHT-1){
								player[j].HP-=20;
								pom.set(i, true);
								if(player[j].HP<=0){
									if(GameOver==false){
										
										wynik[0] = (j==1)?wynik[0]+1:wynik[0];
										wynik[1] = (j==0)?wynik[1]+1:wynik[1];
										
										//wynik[j]++;
										//System.out.println(wynik[j]);
										GameOver=true;
										Time_end_round = System.currentTimeMillis();
									}
									
								}
									
							}
						}
    				}
    				//-------
    			}
    			
    			for(int i=pociski.size()-1; i>=0; --i)
    				if(i<pociski.size())
    				if(pom.get(i).equals(true))
    					pociski.remove(i);
    			
    			pom.clear();
    }
    private void dzialoServe(){
    	
    	for(int i=0; i<dzialo.size(); ++i){
	    
		    	if(dzialo.get(i).dir == Podnoszenie.dol && dzialo.get(i).kat < 30)
					dzialo.get(i).kat+=0.5;
				else if(dzialo.get(i).dir == Podnoszenie.gora && dzialo.get(i).kat > -30)
					dzialo.get(i).kat-=0.5;
	    	
    	}
	
    }
    
    private void nowy_pocisk(int nr_gracza){
    	
    	if(player[nr_gracza].ammo-- > 0)
    	for(int i=0; i<dzialo.size(); ++i){
    		
    			if(dzialo.get(i).isNearbyPlayer(player[nr_gracza].POS_X, player[nr_gracza].POS_Y))
    				pociski.addElement(new Pocisk(dzialo.get(i).kat, dzialo.get(i).POS_X, dzialo.get(i).POS_Y, dzialo.get(i).di));
    		
    	}
    	else
    		player[nr_gracza].ammo=0;
    }
	
    
    private void cannonUpOrDown(Podnoszenie dir, int nr_gracza){
    	
    	for(int i=0; i<dzialo.size(); ++i){
	    	if(dzialo.get(i).isNearbyPlayer(player[nr_gracza].POS_X, player[nr_gracza].POS_Y)){
	    		if(dir == Podnoszenie.dol)
	    			dzialo.get(i).dir = Podnoszenie.dol;
	    		else if(dir == Podnoszenie.gora)
	    			dzialo.get(i).dir = Podnoszenie.gora;
	    		else if(dir == Podnoszenie.None)
	    			dzialo.get(i).dir = Podnoszenie.None;
	    	}
    	}
    }
    /**
     * Metoda obs³uguj¹ca zdarzenia odebrane od klienta
     * (sterowanie)
     * 
     * @param mail - wiadomoœæ odebrana od klienta
     * @param board
     */
    public void obsluga_gracza2(ClientMessage mail, Board board){
    	boolean flag = false;
    	
    	if(mail.getPrzy(1)){
    		player[1].dir = player[1].look = Direction.Left;
    		player[1].isMoving = true;
    		player[1].press[2] = true; 
    		flag = true;
    	}
    	else if(mail.getPrzy(2)){
    		player[1].dir = player[1].look = Direction.Right;
    		player[1].isMoving = true;
    		player[1].press[3] = true;
    		flag = true;
    	}
    	
    	if(flag==false){
    		player[1].isMoving=false;
			player[1].dir = Direction.None;
    	}
    	
    	if(mail.getPrzy(0)){
    		if(!player[1].isJumping) player[1].jump();
    	}
    	if(mail.getPrzy(4)){
    		board.cannonUpOrDown(Podnoszenie.gora, 1);
    	}
    	if(mail.getPrzy(5)){
    		board.cannonUpOrDown(Podnoszenie.dol, 1);
    	}
    	if(!mail.getPrzy(4) && !mail.getPrzy(5)){
    		board.cannonUpOrDown(Podnoszenie.None, 1);
    	}
    	if(mail.getPrzy(6)){
    		board.nowy_pocisk(1);
    	}
    	
    	
    	//if(!mail.getPrzy(1)){
		//	player[1].press[2] = false;
			//player.isMoving = false;
			
	//	}
		//if( !mail.getPrzy(2)){
			//player[1].press[3] = false;
			//player.isMoving = false;   
		//}
	/*	if(keyCode==KeyEvent.VK_G || keyCode==KeyEvent.VK_T){
			for(int i=0; i<dzialo.size(); ++i)
				dzialo.get(i).dir=Podnoszenie.None;
		}*/
    	
    	
    }
    
    
    
    
    //tylko obsluga gracza nr 1
	class Adapter implements KeyListener{
		
		
		public void keyPressed(KeyEvent e) {
			
			//char keyCode = e.getKeyChar();
			int keyCode = e.getKeyCode();
			//System.out.println(keyCode);
			
			switch(keyCode){
			case KeyEvent.VK_SPACE:		
			case KeyEvent.VK_UP: if(!player[0].isJumping) player[0].jump(); break;
			case KeyEvent.VK_LEFT: player[0].dir=player[0].look=Direction.Left; player[0].isMoving=true; player[0].press[2] = true; break;
			case KeyEvent.VK_RIGHT: player[0].dir=player[0].look=Direction.Right;player[0].isMoving=true;player[0].press[3] = true; break;
			case KeyEvent.VK_J: cannonUpOrDown(Podnoszenie.dol, 0); break;
			case KeyEvent.VK_U: cannonUpOrDown(Podnoszenie.gora, 0); break;
			case KeyEvent.VK_P: nowy_pocisk(0); break;
			case KeyEvent.VK_ESCAPE: System.exit(0); break;	
			// gracz 2
		/*	case KeyEvent.VK_W: if(!player[1].isJumping) player[1].jump(); break;
			case KeyEvent.VK_A: player[1].dir=player[1].look=Direction.Left; player[1].isMoving=true; player[1].press[2] = true; break;
			case KeyEvent.VK_D: player[1].dir=player[1].look=Direction.Right;player[1].isMoving=true;player[1].press[3] = true; break;
			case KeyEvent.VK_G: cannonUpOrDown(Podnoszenie.dol, 1); break;
			case KeyEvent.VK_T: cannonUpOrDown(Podnoszenie.gora, 1); break;
			case KeyEvent.VK_Y: nowy_pocisk(1); break;*/
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			int keyCode = e.getKeyCode();
			
			//if(press[0] == true && keyCode=='w'){
			//	press[0]=false;
			//}
			/*if(press[1] == true && keyCode=='s'){
				press[1]=false;
			}*/
			if(player[0].press[2] == true && keyCode==KeyEvent.VK_LEFT){
				player[0].press[2]=false;
				//player.isMoving = false;
			}
			if(player[0].press[3] == true && keyCode==KeyEvent.VK_RIGHT){
				player[0].press[3]=false;
				//player.isMoving = false;
			}
			if(keyCode==KeyEvent.VK_U || keyCode==KeyEvent.VK_J){
				for(int i=0; i<dzialo.size(); ++i)
					dzialo.get(i).dir=Podnoszenie.None;
			}
		/*	//2gracz
			if(player[1].press[2] == true && keyCode==KeyEvent.VK_A){
				player[1].press[2]=false;
				//player.isMoving = false;
			}
			if(player[1].press[3] == true && keyCode==KeyEvent.VK_D){
				player[1].press[3]=false;
				//player.isMoving = false;   
			}
			if(keyCode==KeyEvent.VK_G || keyCode==KeyEvent.VK_T){
				for(int i=0; i<dzialo.size(); ++i)
					dzialo.get(i).dir=Podnoszenie.None;
			}*/
			
			//---------
			
		//	for(int j=0; j<2; ++j){
				boolean flag = false;
				for(int i=0; i<4; ++i){
					if(player[0].press[i]==true){
						flag = true;
						     //if(i==0) dir=Direction.Up; 
						if(i==1) player[0].dir=Direction.Down; 
						else if(i==2) {player[0].dir=Direction.Left; player[0].isMoving=true; player[0].look=Direction.Left;}
						else if(i==3) {player[0].dir=Direction.Right; player[0].isMoving=true; player[0].look=Direction.Right;}
					}
				}
				if(flag == false){
					player[0].isMoving=false;
					player[0].dir = Direction.None;
				}
		//	}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
			
		}
			
		
	}
	
	/*private void createBackground(){
		try{
			b_grass = ImageIO.read(new File("grass.png"));
			b_wall = ImageIO.read(new File("wall.png"));
			b_kola_arm = ImageIO.read(new File("kola_arm.png"));
			b_tlo = ImageIO.read(new File("tlo.png"));
		}
		catch(IOException e){
				
		}
		
		try{
			File image = new File("aaa.png");
			
			for(int i=0; i<24; ++i){
				for(int j=0; j<32; ++j){
					for(int k=0; k<32; ++k){
						for(int w=0; w<32; ++w){
							
							if(Map.get(j, i) == 1)
								Map.tekstura.setRGB(j*WIDTH_KLOCKA+w, i*HEIGHT_KLOCKA+k, b_grass.getRGB(w, k));
							else if(Map.get(j, i) == 2)
								Map.tekstura.setRGB(j*WIDTH_KLOCKA+w, i*HEIGHT_KLOCKA+k, b_wall.getRGB(w, k));
							else{
								Map.tekstura.setRGB(j*WIDTH_KLOCKA+w, i*HEIGHT_KLOCKA+k, b_tlo.getRGB(j*WIDTH_KLOCKA+w, i*HEIGHT_KLOCKA+k));
							}
						}
					}
				}
			}
			ImageIO.write(Map.tekstura, "png", image);
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}*/
	
	
}
