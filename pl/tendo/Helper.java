package pl.tendo;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

/**
 * Klasa opisuj¹ca pomocnika rozrzucaj¹cego prezenty
 * 
 * @author Maciek Ruszczyk
 * @see Obiekty
 * @see Gift
 */

public class Helper extends Obiekty{
	
	final static int ITEM_SPAWN_TIME_FROM = 1000;
	final static int ITEM_SPAWN_TIME_TO = 2000;
	final static int MAX_NUMBER_OF_GIFTS = 10;
	
	Direction dir;
	
	Image im;
	Image im_hp;
	Image im_ammo;
	
	double item_time;		//czas po ktorym utworzy sie przedmiot
	long time_elapsed;
	
	
	
	Vector<Gift> gifts;
	
	public Helper(){
		
		HEIGHT = 30;
		WIDTH = 30;
		
		dir = Direction.Right;
		im = new ImageIcon("helper.gif").getImage();
		im_hp = new ImageIcon("aid-kit.png").getImage();
		im_ammo = new ImageIcon("ammo.png").getImage();
		
		gifts = new Vector<Gift>();
		
		POS_X=250;
		POS_Y=20;
		
		time_elapsed = 0;
		item_time = Math.random()*2000+3000;
		//item_time = (Math.random()*ITEM_SPAWN_TIME_TO - ITEM_SPAWN_TIME_FROM) + ITEM_SPAWN_TIME_FROM;		
		//System.out.println(item_time);
	}
	public void add_gift(){
		gifts.addElement(new Gift(POS_X, POS_Y));
	}
	public void add_gift(int x, int y, int z){
		gifts.addElement(new Gift(x, y, z));
	}
	public int[] getGifts(){
		int[] table = new int[3*gifts.size()+1];
		table[0] = gifts.size();
		for(int i = 0; i < gifts.size() ; ++i){
			table[3*i+1] = (int)gifts.get(i).POS_X;
			table[3*i+2] = (int)gifts.get(i).POS_Y;
			table[3*i+3] = gifts.get(i).gift;
		}
		return table;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
