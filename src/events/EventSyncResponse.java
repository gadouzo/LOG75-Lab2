/******************************************************
Cours :           LOG730
Session :         
Groupe :          01
Projet :          Laboratoire #2
Date cr�ation :   
******************************************************
Classe d�crivant la base des �v�nements transmis.
******************************************************/ 
package events;

public class EventSyncResponse implements IEvent {

private static final long serialVersionUID = 5483268778788190809L;

private String message;

public EventSyncResponse(String message){
	this.message = message;
}

public String getMessage() { return message; }
}
