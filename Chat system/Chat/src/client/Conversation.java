package client;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import common.Contact;
import common.ContactsList;

public class Conversation extends Thread implements Serializable
{
	
	private static final long serialVersionUID = 1;
	// ---------------------- attributs ------------------------
	private Contact user_local;
	private Contact user2;
	private ArrayList<Message> messages;
	private Socket socket_TCP ;
	private int nb_new_mess=0;
	
	private ConversationList conv_list; // devrait etre evite, appels statiques seraient mieux que passages en argument

	
	// ---------------------- Constructeurs -------------------------
	// constructeur quand on initie la conv
	public Conversation(String contenu,Contact user2, Contact user_local, ConversationList cl)
	{				
		// recreer le message
		Message m = new Message(user2,user_local,contenu);
		try
		{
			// ------ envoi du message initiateur via la conv -----
			// creer socket
			this.socket_TCP = new Socket(m.getDest().getAdresse(),m.getDest().getPort_TCP());
			this.user_local=user_local;
			this.user2=user2;
			this.conv_list=cl;
			// cree une conv en cherchant si il y a déjà des messages dans la database
			this.messages = Database.getMessagesSaved(user2);
			// ajout message
			this.messages.add(m);
			//this.reader = new BufferedReader(new InputStreamReader(socket_TCP.getInputStream())) ;
			
			// creation du message tcp
			Message_TCP m_TCP = new Message_TCP(m) ;

			
			this.envoyer_message_TCP(m_TCP);
			// sauvegarde des messages
			Database.save_conv(this, false);

			this.start();
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	// constructeur quand on recoit un premier message
	public Conversation(Socket socket, Contact user_local, ContactsList liste_contacts,ConversationList cl)
	{
		try
		{

			//initialisation de la conversation
			this.socket_TCP = socket ;
			this.messages = new ArrayList<Message>() ;
			this.user_local = user_local;
			this.conv_list=cl;
			
			// reception du premier message 
			Message_TCP m_TCP = this.recevoir_message_TCP();
			
			// recup user_2
			this.user2=liste_contacts.getContact(m_TCP.getMachine());
			
			
			// cree une conv en cherchant si il y a déjà des messages dans la database
			this.messages = Database.getMessagesSaved(user2);
			
			// recreer un type message
			Message message_recu = new Message(user_local,user2,m_TCP.getContenu()) ;
							
			// Mettre à jour la conversation synchronized et afficher
			this.ajouter_message(message_recu);
			

			
			// nb de nouveau message dans la conv
			this.nb_new_mess=1;
			
			//ajout a la database
			Database.save_conv(this, true);
			
			this.start();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
				
	}
	
	// ----------------------- Methodes ------------------------------
	// ajoute un message à un conv = mise à jour de la conv => synchonisée pour ne pas lire et ecrire en meme temps
	private synchronized void ajouter_message (Message message)
	{
		this.messages.add(message);
		this.nb_new_mess++;
	}
	
	// affiche tous les messages de la conv => synchonisée pour ne pas lire et ecrire en meme temps
	public String toString()
	{
		Iterator<Message> iter = this.messages.iterator() ;
		String s = " ----------- Conversation avec " + this.user2.getPseudo() + "-------------- \n";
		
		while(iter.hasNext())
		{
			Message m = iter.next() ;
			s = s + "De : " + m.getSender().getPseudo()  + "\n";
			s = s + m.getContenu() + "\n" ;
		}
		return s ;
	}
	
	
	public synchronized void afficher_conversation()
	{
		System.out.println(this.toString());
	}
	
	// envoyer message
	public synchronized void envoyer_message(String contenu)
	{
		try
		{
			// creer le message
			Message m = new Message(this.user2,this.user_local,contenu);
			
			//creation du mesage_TCP
			Message_TCP m_TCP = new Message_TCP(m);
			
			envoyer_message_TCP(m_TCP);
			
			// ajout à la conv
			this.ajouter_message(m);
			
			// sauvegarde des messages
			Database.save_conv(this, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void envoyer_message_TCP(Message_TCP m_TCP){
		// ecriture dans le socket
		try{
		OutputStream os = socket_TCP.getOutputStream();
		os.write(m_TCP.getBytesMes());
		os.flush();
		//Database.save_conv(this, false);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Message_TCP recevoir_message_TCP() throws Exception
	{
		// transformation en byte[]
		byte[] byte_recu = new byte[1000];
		this.socket_TCP.getInputStream().read(byte_recu);
		
		Message_TCP m = new Message_TCP(byte_recu);
		System.out.println(m);
		return m;
	}
	
	
	
	// fermer le thread
	public void close_conv()
	{
		try
		{
			this.socket_TCP.close();
			this.interrupt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	//--------------------------------------- getter
	public Contact getUser2()
	{
		return this.user2 ;
	}
	
	public synchronized boolean getBoolNewMess(){
		if (this.nb_new_mess==0){
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public synchronized int getNbNewMess(){
		return this.nb_new_mess;
	}
	
	public synchronized void resetNewMess(){
		this.nb_new_mess=0;
	}
	
	public ArrayList<Message> getMessages()
	{
		return this.messages;
	}
	
	//  ------------------------------------ RUN QUI RECOIT DES MESSAGES SUR LE MEME SOCKET -----------------------------------
	public void run()
	{
		try
		{
			System.out.println("CONV LANCEE");
			// -------------- reception des messages -----------------

			while(true)
			{
	
				Message_TCP m_TCP = this.recevoir_message_TCP();


				
				// recreer un type message
				Message message_recu = new Message(user_local,user2,m_TCP.getContenu()) ;
								
				// Mettre à jour la conversation synchronized et afficher
				this.ajouter_message(message_recu);
				
				//ajout a la database
				Database.save_conv(this, true);
			}
			
		}///////
		catch (StreamCorruptedException e) {
			this.conv_list.trouve_et_close(this.user2);
			
		}
		catch(SocketException e) {
			this.conv_list.trouve_et_close(this.user2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
}
