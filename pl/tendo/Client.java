package pl.tendo;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.Decoder;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import pl.tendo.Obiekty.Direction;


@ClientEndpoint(encoders={ClientEncoder.class},
decoders={ServerDecoder.class})

/**
 * 
 * 
 * Klasa s³u¿¹ca do uruchomienia aplikacji w wersji klienckiej
 * tworzy obraz przystosowany dla klienta
 * odbiera wiadomosci przychodzace od serwera
 * i przekazuje je do obslugi przez ClientBoard
 * 
 * 
 * @author £ukasz Pik
 * 
 */
public class Client extends JFrame {
	private static CountDownLatch latch;
	//boolean[] przyciski;
	//SluchaczKey akcja;
	//javax.swing.Timer tim;
	static Session ses;
	ClientMessage mess;
	ClientBoard board;
	ClientFrejm frame = new ClientFrejm();
	
	
	public Client(){
		//setFocusable(true);
		//przyciski = new boolean[]{false, false, false, false};
		//akcja = new SluchaczKey();
		mess = new ClientMessage();
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.addKeyListener(akcja);
		//tim = new Timer(30, this);
		
		//setVisible(true);
		
	}
	
	@OnOpen
	public void onOpen(Session session){
		System.out.println("Connected to server");
		ses = session;
		//this.addKeyListener(akcja);
		
		//tim.start();
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
		board = frame.getBoard();
	}
	public static void send(ClientMessage mess){
		
		try {
			ses.getBasicRemote().sendObject(mess);
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public  void onMessage(ServerMessage mail, Session session){
		//System.out.println("New message: ");
		
		board.setAll(mail);
		
	}
	/*public void actionPerformed(ActionEvent act) {
		ClientMessage mess = new ClientMessage();
		for(int i = 0 ; i < 4 ; ++i){
			if(przyciski[i]){
				System.out.print("1 ");
				
			}
			else{	
				System.out.print("0 ");
				
			}
			mess.setPrzy(i, przyciski[i]);
		}
		//System.out.println();
		send(mess);
		
		
	}*/
	
	
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
       System.out.println(String.format("Session %s close because of %s", session.getId(), closeReason));
        latch.countDown();
        System.exit(0);
    } 
	
	
	public static void main(String[] args){
		
	
		
		latch = new CountDownLatch(1);
		
		ClientManager client = ClientManager.createClient();
		try {
            //client.connectToServer(Client.class, new URI("ws://192.168.0.3:59234/websockets/game"));
			client.connectToServer(Client.class, new URI("ws://localhost:59234/websockets/game"));
			//client.connectToServer(Client.class, new URI("ws://169.254.199.117:59234/websockets/game"));
            latch.await();

        } catch (DeploymentException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
		
		
	}
	/*class SluchaczKey implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
			
			int keyCode = e.getKeyCode();
			
			switch(keyCode){
			/*case 'w': przyciski[0] = true; break;
			case 'a': przyciski[1] = true; break;
			case 'd': przyciski[2] = true; break;
			case 's': przyciski[3] = true; break;*/
		/*	case KeyEvent.VK_W: mess.setPrzy(0, true); break;
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
	/*		send(mess);
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
	

			send(mess);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
			
		}
		
	}*/
	
}
class ClientFrejm extends JFrame{
	ClientBoard board;
	public ClientFrejm(){
		//setSize(Board.BOARD_WIDTH_PIX, Board.BOARD_HEIGHT_PIX);
		
				setTitle("Client");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				board = new ClientBoard();
				add(board);
				pack();
				
				board.start();
	}
	public ClientBoard getBoard(){
		return board;
	}
}




