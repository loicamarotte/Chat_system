package serveur;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import common.*;

public class RequestHandler extends Thread
{
	//Attributs
	private Socket socket;
	static private ContactsList liste_contacts; 
	
	
	// Constructeur
	public RequestHandler(Socket socket)
	{
		this.socket = socket;
		this.start();
	}
	
	
	// reception message
	public Message_presence reception_request() throws Exception
	{
		// transformation en byte[]
		byte[] byte_recu = new byte[1000];
		this.socket.getInputStream().read(byte_recu);
		
		Message_presence m = new Message_presence(byte_recu);
		System.out.println(m);
		return m;
	}
	
	// Handler selon le type de message recu
	public void handler(Message_presence m)
	{
		// Message Subscribe type 1
		if (m.getType() == 1)
		{
			//recupere le user
			Contact user = (Contact)m.getContenu() ;
			user.setAdresse(socket.getInetAddress().getHostAddress());
			//on met a jour la liste de contact
			liste_contacts.ajouteContact(user);		
			// on lui répond en lui envoyant la liste des contacts 
			envoyer_liste(user) ;
			// on broadcast le contact
			envoyer_contact(user);
			}
		// Message Publish type 2
		else if (m.getType() == 2)
		{
			//recupere le user
			Contact user = (Contact)m.getContenu() ;
			user.setAdresse(socket.getInetAddress().getHostAddress());
			//on met a jour la liste de contact
			liste_contacts.ajouteContact(user);		
			// on broadcast le contact
			envoyer_contact(user);
		}
		
	}
	
	
	// Envoi de la liste de contacts à un seul contact (quand il vient de subscribe)
	public void envoyer_liste(Contact user)
	{
		// Creation d'un message Notify type 3
		Message_presence m = new Message_presence(3,liste_contacts);
		// Envoi
		envoyer_message_presence(m,user) ;
	}
	
	// Envoi d'un user à toute la liste (après un subscribe ou un pusblish)
	public void envoyer_contact(Contact user)
	{
		// Creation d'une ContactsList
		ContactsList liste = new ContactsList();
		liste.ajouteContact(user);
		// Creation d'un message Notify type 3
		Message_presence m = new Message_presence(3,liste);
		// Envoi à toute la liste
		Iterator<Contact> iter = liste_contacts.getListe().iterator();
		while(iter.hasNext())
		{
			Contact c = iter.next();
			if(c.getConnecte()) {
			envoyer_message_presence(m,c) ;
			}
		}
	}
		

	// envoi message à un autre contact
	public void envoyer_message_presence(Message_presence m, Contact c)
	{
		try
		{
			// creation du socket
			Socket s = new Socket(c.getAdresse(),c.getPort_presence());
			// ecriture dans le socket
			OutputStream os = s.getOutputStream();
			os.write(m.getBytesMes());
			os.flush();
			s.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//envoi une réponse à un message
	public void envoyer_message_presence(Message_presence m)
	{
		try
		{
			// ecriture dans le socket
			OutputStream os = socket.getOutputStream();
			os.write(m.getBytesMes());
			os.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// Setter	
	public static void setContactList(ContactsList liste) {
		liste_contacts = liste;
	}
	
	
	
	
	// ------------------ RUN ---------------------
	public void run()
	{
		try
		{
			// reception message
			Message_presence m = this.reception_request();
			// gere le message
			this.handler(m);
			// fermeture du thread
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
}
