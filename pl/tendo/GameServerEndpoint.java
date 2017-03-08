package pl.tendo;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.OnError;
import javax.websocket.server.ServerEndpoint;
import org.glassfish.tyrus.server.Server;


@ServerEndpoint(value="/game", 
//configurator=ChatServerEndPointConfigurator.class,
encoders={ServerEncoder.class},
decoders={ClientDecoder.class})

/** 
 * Klasa odpowiadaj¹ca za odbieranie wiadomoœci od Klienta
 * i przekazuj¹ca dane z wiadomoœci do obs³ugi gracza 2
 * 
 * @author £ukasz Pik 
 * 
 */

public class GameServerEndpoint {
	
	private boolean[] tablica;
	double[] Player;
	static Session ses;
	Frejm frame = new Frejm();
	
	
	@OnOpen
	public void OnOpen(Session session){
		System.out.println(" Client connected to the server");
		ses = session;
		Player = new double[] {100, 100};
		
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				
				
				
			}
		});
	
		
	}
	
	
	
	
	
	
	public static void send(ServerMessage mess){
		try {
			
			ses.getBasicRemote().sendObject(mess);
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	@OnMessage
	public void OnMessage(ClientMessage mail, Session session ){//boolean tab[], Session session){
		/*System.out.print("New Message: ");
		for(int i=0; i < 4; ++i){
				System.out.print(mail.getPrzy(i)+" ");
			
		}
		System.out.println();/*
		ServerMessage response = calculate(mail);
		try {
			ses.getBasicRemote().sendObject(response);
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		frame.getBoard().obsluga_gracza2(mail, frame.getBoard());
		
    }
		

	
	
	@OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(String.format("Session %s closed because of %s", session.getId(), closeReason));
        System.exit(0);
        
    }

	class Frejm extends JFrame{
		Board board;
		public Frejm(){
			//setSize(Board.BOARD_WIDTH_PIX, Board.BOARD_HEIGHT_PIX);
			
					setTitle("Server");
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					board = new Board();
					add(board);
					pack();
					
					board.start();
		}
		public Board getBoard(){
			return board;
		}
		
	}
    
	
	
	
}