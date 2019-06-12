package client;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import common.*;

public class Interface_Presence extends Thread
{
	// Attributs
	private Contact user_local;
	private ContactsList liste_contacts;
	private ServerSocket socket_serv ;
	private String adresse_server ;
	private int port_server ;
	private ConversationList liste_conv;
	private Interface_TCP TCP;
	
	// Constructeur
	public Interface_Presence (Contact user_local,ContactsList liste_contacts, String adresse_server, int port_server,ConversationList liste_conv, Interface_TCP TCP)
	{
		this.user_local=user_local;
		this.liste_contacts=liste_contacts;
		this.adresse_server = adresse_server ;
		this.port_server = port_server ;
		this.liste_conv = liste_conv;
		this.TCP = TCP;
		
		try
		{
			this.socket_serv = new ServerSocket(user_local.getPort_presence());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	// -------------- Methodes de création de message Presence

	// Message "voila mes infos connecte ou pas""
	public void publish()
	{
		Message_presence m = new Message_presence(2,user_local);
		envoyer_message_presence(m.getBytesMes()) ;	
	}
	// Message "coucou envoie moi ta liste de contacts"
	public void subscribe()
	{
		Message_presence m = new Message_presence(1,user_local);
		envoyer_message_presence(m.getBytesMes()) ;	
	}
		
	// envoi message au server
	public void envoyer_message_presence(byte[] b)
	{
		try
		{
			// creation du socket
			Socket s = new Socket(adresse_server,port_server);
			// ecriture dans le socket
			OutputStream os = s.getOutputStream();
			os.write(b);
			os.flush();
			s.close();
		}
		catch(ConnectException e) {
			JOptionPane.showMessageDialog(null, "Impossible de se connecter au serveur, peut-etre celui-ci est deconnecte, ou les parametres ne sont pas les bons. Le programme va se fermer.");
			Application.deconnexionSansServ(user_local, TCP, liste_conv);
			java.lang.System.exit(0);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	// reception message
	public Message_presence reception_request(Socket s) throws Exception
	{
		// transformation en byte[]
		byte[] byte_recu = new byte[1000];
		s.getInputStream().read(byte_recu);
		
		Message_presence m = new Message_presence(byte_recu);
		System.out.println(m);
		return m;
	}
	
	
	public void run()
	{
		try 
		{
			// envoi du message subscribe au server
			subscribe() ;
			
			//ecoute EN TCP = Mise à jour des actifs	
			while(true)
			{
				// recoi un message
				Message_presence message_recu = reception_request(socket_serv.accept()) ;
				
				// on verifie qu'il s'agit bien d'un message notify
				if (message_recu.getType() == 3)
				{
					ContactsList liste_comp = (ContactsList)message_recu.getContenu();
					liste_contacts.mise_a_jour_liste(liste_comp,user_local);
			
				}
				
				
			}	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
/*	
	
	// Fonction run
	public void run()
	{	
		try 
		{
		//---- 1 : envoi coucou
			// créé le message coucou
			String message = "0" + user_local.getPort_TCP() + user_local.getPseudo() ;
			
			// parcours des ports de 5000 à 5050
			// envoi du coucou EN UDP
			for (int i=5000;i<5050;i++)
			{
				DatagramPacket sonde = new DatagramPacket(message.getBytes(),message.length(),InetAddress.getByName("255.255.255.255"),i);
				socket.send(sonde);		
			}
				
			//---- 2 : ecoute EN UDP = Mise à jour des actifs
			byte[] buffer = new byte[256];
			DatagramPacket reponse= new DatagramPacket(buffer,buffer.length);
			
			while(true)
			{
				socket.receive(reponse);
				
				// -->Traiter la reponse : recup le port TCP et le pseudo et le type de message
				String message_recu = new String(reponse.getData(),StandardCharsets.UTF_8);
				char type_message = message_recu.charAt(0);
					
				int port_TCP = Integer.parseInt(message_recu.substring(1,5));
				String pseudo = message_recu.substring(5,reponse.getLength());

				
				if (pseudo.equals(user_local.getPseudo()) == false)
				{
					boolean connecte = true;
	
					// si coucou je suis là, et toi es tu là? : ajouter ou modifier l'etat du contact en actif et envoie réponse
					if(type_message == '0')
					{
						connecte = true;
						// envoi message
						String message_reponse = "1" + user_local.getPort_TCP() + user_local.getPseudo() ;
						DatagramPacket paquet_reponse = new DatagramPacket(message_reponse.getBytes(),message_reponse.length(),reponse.getAddress(),reponse.getPort());
						socket.send(paquet_reponse);
						
						
					}
					// coucou je suis là : mise à jour contact
					else if(type_message =='1')
					{
						connecte = true;
	
					}
					// si bye : modifier l'etat du contact en pas actif
					else if (type_message == '2')
					{
						connecte = false;
					}
					else {
						throw new Exception() ;
					}
					liste_contacts.ajouteContact(pseudo, reponse.getAddress().getHostAddress(), reponse.getPort(), port_TCP, connecte);
				}
			}
				
				
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		
		
		//TODO: Deconnexion UDP = envoi de message bye
		
	}
	
	
	
*/	
	
	
	
	
}
