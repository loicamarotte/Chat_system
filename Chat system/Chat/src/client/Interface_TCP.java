package client;
import java.net.ServerSocket;
import java.net.Socket;

import common.Contact;
import common.ContactsList;

public class Interface_TCP extends Thread
{
	// Attributs
	private Contact user_local;
	private ContactsList liste_contacts ;
	private ConversationList liste_conv ;
	private ServerSocket socket_serv ;

	
	// Constructeur
	public Interface_TCP(Contact user_local, ContactsList liste_contacts,ConversationList liste_conv )
	{
		this.user_local=user_local;
		this.liste_contacts=liste_contacts;
		this.liste_conv = liste_conv ;
		try
		{
			this.socket_serv = new ServerSocket(user_local.getPort_TCP());
			//this.user_local.setAdresse(this.socket_serv.getInetAddress().getHostAddress());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// Methode pour la deconnexion
	public void close_TCP()
	{
		try
		{
			// fermeture des threads
			this.interrupt();
		
			// fermeture du socket
			this.socket_serv.close();			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Fonction run = se mettre en attente de message et créé des conversation
	public void run()
	{
		try
		{			
			while(true)
			{	
				// création du socket mise en etat accept == attente d'un message pour connexion
				Socket socket = this.socket_serv.accept() ;
				
				// Creation d'une conv dans un thread qui recevra et enverra les messages
				liste_conv.creer_conv(socket, user_local,  liste_contacts) ;
			}
			
		}
		catch (Exception e)
		{
			
		}
	}
	
	

	
}
	
	
	
	
	
