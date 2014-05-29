/******************************************************
	Cours :           LOG730
	Session :         �t� 2010
	Groupe :          01
	Projet :          Laboratoire #2
	Date cr�ation :   2010-05-21
******************************************************
Thread qui achemine les �v�nements contenus sur le bus
d'�v�nements aux Communicators qui sont enregistr�s.
******************************************************/ 
package eventbus;

import java.util.ArrayList;
import java.util.List;

import events.EventSyncResponse;
import events.EventThatShouldBeSynchronized;
import events.IEvent;

public class EventBusThread extends Thread implements IEventBusThread {
	private List<IEventBusCommunicator> lstComm = new ArrayList<IEventBusCommunicator>();
	private EventBusServerThread server;
	private List<IEvent> eventsToSend = new ArrayList<IEvent>();

	public EventBusThread(int port) {
		server = new EventBusServerThread(port, this);
		server.start();
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				synchronized(eventsToSend) {
					if (eventsToSend.size() > 0) {
						System.out.println("Envoie de l'�v�nement " + eventsToSend.get(0).toString());
						if (eventsToSend.get(0) instanceof EventThatShouldBeSynchronized){
							//Synchrone

							int last =0;
							int currentID=0;
							IEventBusCommunicator currentIEBus = null; 

							for (int i = 0; i < lstComm.size(); i++) {
								int min = Integer.MAX_VALUE;
								for(IEventBusCommunicator ievc : lstComm){
									currentID = ievc.getID();
									if( ( currentID < min ) && ( currentID > last) ){
										min = currentID;
										currentIEBus = ievc;
									}
								}
								last = currentIEBus.getID();
								currentIEBus.sendToListener(eventsToSend.get(0));
								System.out.println("envoie a l<application id: "+ currentIEBus.getID()+ " message : "+ eventsToSend.get(0).getMessage());

								IEvent event;
								while ( !( ( event = eventsToSend.get(eventsToSend.size()-1) ) instanceof EventSyncResponse) ){
									Thread.sleep(100);
								}
								eventsToSend.remove(event);

							}


							eventsToSend.remove(0);


						}else{
							//Asynchrone

							for(IEventBusCommunicator ievc : lstComm)
								ievc.sendToListener(eventsToSend.get(0));

							eventsToSend.remove(0);
						}
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void addEvent(IEvent ie) {
		eventsToSend.add(ie);
	}

	public void attachCommunicator(IEventBusCommunicator iebc) {
		lstComm.add(iebc);
	}

}
