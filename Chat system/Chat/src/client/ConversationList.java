package client;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import common.Contact;
import common.ContactsList;

public class ConversationList 
{
	// ------------------------- attributs
	private ArrayList<Conversation> liste_conversation ;
	
	// --------------------------- constructeur
	public ConversationList ()
	{
		this.liste_conversation = new ArrayList<Conversation>(); 
	}
	
	// ---------------------------- Methodes
	//trouver une conv a partir de user2
	public Conversation trouve_conv (Contact user2)
	{
		Iterator<Conversation> iter = this.liste_conversation.iterator();
		while(iter.hasNext())
		{
			Conversation x = iter.next();
			if (x.getUser2().getMachine().equals(user2.getMachine()))
			{
				return x;
			}
		}
		throw new NoSuchElementException() ;
		
	}
	
	// affiche toutes les conversations de la liste
	public void afficher_conversations()
	{
		Iterator<Conversation> iter = this.liste_conversation.iterator() ;
		while(iter.hasNext())
		{
			Conversation conv = iter.next() ;
			System.out.println("(--------------- Conversation -------------)");
			conv.afficher_conversation();
		}
	}
	
	// ajoute une conv a la liste
	public synchronized void ajoute_conv (Conversation conv)
	{
		this.liste_conversation.add(conv);
	}
	
	
	// ferme la conversation voulue (utile quand un user se déconnecte)
	public void trouve_et_close (Contact user2)
	{
		Conversation conv = trouve_conv(user2) ;
		this.liste_conversation.remove(conv);
		conv.close_conv();
	}
	
	
	// fermeture de toutes les conversations
	public void close_all_conv()
	{
		Iterator<Conversation> iter = this.liste_conversation.iterator() ;
		while(iter.hasNext())
		{
			Conversation conv = iter.next() ;
			conv.close_conv();
		}
	}
	
	public Conversation creer_conv_et_envoyer_message(String mess,Contact cible, Contact user_local){
		Conversation conv = new Conversation(mess,cible,user_local,this );
		this.ajoute_conv(conv);
		return conv;
	}
	
	public Conversation creer_conv(Socket socket, Contact user_local, ContactsList liste_contacts){
		Conversation conv = new Conversation(socket,user_local,liste_contacts,this);
		this.ajoute_conv(conv);
		return conv;
	}
	
	public synchronized boolean getBoolNewMess(){ //renvoie true si une des conv a un nouveau mess
		Iterator<Conversation> iter = this.liste_conversation.iterator() ;
		boolean bool = false;
		while(iter.hasNext())
		{
			Conversation conv = iter.next();
			if (conv.getBoolNewMess()){
				bool = true;
			}
			
		}
		return bool;
	}
	
/*	//Ajouter message (créé conv ou ajoute à conv existanteà
	public void ajouter_message (Message m, boolean recu)
	{
		Iterator<Conversation> iter = this.liste_conversation.iterator();
		Contact user2 ;
		
		
		// si c'est un message recu 
		if (recu)
		{
			user2 = m.getSender() ;
		}
		else
		{
			user2 = m.getDest() ;
		}
	
		// on cherche si la conversation existe
		try
		{
			// on ajoute le message
			trouve_conv(user2).ajouter_message(m);
		}
		catch (Exception e)
		{
			// creer conv, ajoute message, ajoute conv à la liste
			Conversation conv = new Conversation(user2);
			conv.ajouter_message(m);
			this.liste_conversation.add(conv);
		}
	}
	
	*/
}
