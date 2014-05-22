package eventbus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class EventBusServerThread extends Thread {
	private int port;
	private ServerSocket serverSocket;
	private IEventBusThread eventBus;
	
	public EventBusServerThread(int port, IEventBusThread eventBus)
	{
		this.port = port;
		this.eventBus = eventBus;
	}
	
	public void run()
	{ 
		try { 
			serverSocket = new ServerSocket(port); 
        } 
		catch (IOException e) 
        { 
			System.err.println("On ne peut pas écouter au  port: " + port + "."); 
			System.exit(1); 
        } 

		while(true) {
			Socket clientSocket = null; 
			System.out.println ("Le serveur " + port + " est en marche, Attente de la connexion.....");
			
			//id du client
			int id=0;
			
			try { 
				clientSocket = serverSocket.accept(); 
				
				//get client ID
				BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream())); 
				id = Integer.parseInt(in.readLine());
				System.out.println("LE ID EST : "+id);
				//in.close();

			} 
			catch (IOException e) 
	        { 
				System.err.println("Accept de " + port + " a échoué."); 
				System.exit(1); 
	        } 
			
			
			//event but for client id
			EventBusCommunicator ebc = new EventBusCommunicator(clientSocket, eventBus, id );
			ebc.start();
			eventBus.attachCommunicator(ebc);
		}
	}
}
