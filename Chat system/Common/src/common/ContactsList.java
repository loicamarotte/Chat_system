package common;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ContactsList implements Serializable
{
	private static final long serialVersionUID = 1;
	// Attributs
	private ArrayList<Contact> liste_contacts;
	
	// Constructeur
	public ContactsList() 
	{
		this.liste_contacts = new ArrayList<Contact>();
	}

	public ContactsList(ContactsList l)
	{
		this.liste_contacts = l.getListe();
	}
	
	
	// Getter
	public synchronized Contact getContact(UUID machine)
	{
		Iterator<Contact> iter = this.liste_contacts.iterator();
		while(iter.hasNext())
		{
			Contact x = iter.next();
			if (x.getMachine().equals(machine))
			{
				System.out.println("CONTACT DEJA DANS LISTE");
				return x;
			}
		}
		throw new NoSuchElementException() ;
	}
	
	public synchronized ArrayList<Contact> getListe()
	{
		return this.liste_contacts ;
	}
	
	 
	// --------------------- Methodes

	
	// ajoute un contact par adresse machine
	public synchronized void ajouteContact(String pseudo,String adresse,UUID machine, int port_UDP, int port_TCP, boolean connecte )
	{
		// si le contacte existe, on met à jour les infos
		try
		{
			Contact x = getContact(machine);
			x.setPseudo(pseudo);
			x.setAdresse(adresse);
			x.setPort_TCP(port_TCP);
			x.setPort_presence(port_UDP);
			x.setConnecte(connecte);
		}
		// si le contact n'existe pas, on le créé
		catch (NoSuchElementException e)
		{
			Contact y = new Contact(pseudo,adresse,machine, port_UDP, port_TCP,connecte) ;
			this.liste_contacts.add(y);
		}
	}	
	// ajoute un contact avec un objet contact
	public void ajouteContact(Contact c)
	{
		this.ajouteContact(c.getPseudo(), c.getAdresse(), c.getMachine(),c.getPort_presence(), c.getPort_TCP(), c.getConnecte());	
	}
		
		
	
	// retourne les pseudo des contacts actifs
	public synchronized Contact[] getContacts_actifs()
	{
		Iterator<Contact> iter = this.liste_contacts.iterator();
		Contact[] liste = new Contact[this.getNb_Actifs()];
		int i = 0;
		
		while(iter.hasNext())
		{
			Contact c = iter.next() ;
			if (c.getConnecte())
			{
				liste[i] = c ;
				i++;
			}
			
		}
		return liste ;
	}
	
	// compte les contacts actifs
	private synchronized int getNb_Actifs()
	{
		Iterator<Contact> iter = this.liste_contacts.iterator();
		int i = 0;
		while(iter.hasNext())
		{
			Contact c = iter.next() ;
			if (c.getConnecte())
			{
				i++;
			}
		}
		return i ;
	}
	
	
	
	//affiche la liste des contacts
	public synchronized String toString()
	{
		Iterator<Contact> iter = this.liste_contacts.iterator();
		String liste = " --- Liste de Contacts --- \n";
		
		while(iter.hasNext())
		{
			Contact c = iter.next() ;
			liste = liste + c.toString() + "\n" ;
		}
		return liste ;
	}
	
	public void afficheListeContacts()
	{
		System.out.println(this.toString());
	}
	
	
	// transforme en byteArray (pour envoi UDP)
	public byte[] getListe_byte()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try 
		{
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(this);
		  out.flush();
		  byte[] liste = bos.toByteArray();
		  bos.close();
		  return liste ;
		} 
		catch (IOException ex) {
		    ex.printStackTrace();
		    return null ;
		  }
		
	}
	
	// Mise à jour de la liste par comparaison avec une autre
	public void mise_a_jour_liste(ContactsList autre_liste, Contact user_local)
	{
		Iterator<Contact> iter = autre_liste.getListe().iterator() ;
		
		while(iter.hasNext())
		{
			Contact x = iter.next() ;
			if(x.getMachine().equals(user_local.getMachine())==false)
			{
				try
				{
					Contact notreContact = this.getContact(x.getMachine()) ;
					notreContact.setPseudo(x.getPseudo());
					notreContact.setConnecte(x.getConnecte());
					notreContact.setPort_TCP(x.getPort_TCP());
					notreContact.setPort_presence(x.getPort_presence());
					notreContact.setAdresse(x.getAdresse());
				}
				catch (NoSuchElementException e)
				{
					this.ajouteContact(x);
				}
			}
		}
		
		
	}
	public void mise_a_jour_contact(Contact x, Contact user_local){
		if(x.getMachine().equals(user_local.getMachine())==false)
		{
			try
			{
				Contact notreContact = this.getContact(x.getMachine()) ;
				notreContact.setPseudo(x.getPseudo());
				notreContact.setConnecte(x.getConnecte());
				notreContact.setPort_TCP(x.getPort_TCP());
				notreContact.setPort_presence(x.getPort_presence());
				notreContact.setAdresse(x.getAdresse());
			}
			catch (NoSuchElementException e)
			{
				this.ajouteContact(x);
			}
		}
	}
	
	// Verifie si un pseudo est pris
	public boolean pseudo_existe(String pseudo)
	{
		Iterator<Contact> iter = this.liste_contacts.iterator() ;
		
		while(iter.hasNext())
		{
			Contact x = iter.next() ;
			if (x.getPseudo().equals(pseudo))
			{
				return true ;
			}
		}
		return false ;
		
	}
	
	
	
	// -------------------- Cimetiere (feu les fonctions inutiles)
	/*	public synchronized Contact getContact(String pseudo)
	{
		Iterator<Contact> iter = this.liste_contacts.iterator();
		while(iter.hasNext())
		{
			Contact x = iter.next();
			if (x.getPseudo().equals(pseudo))
			{
				return x;
			}
		}
		throw new NoSuchElementException() ;
	}
	
	
	
	// ajoute un contact par ses infos
	public synchronized void ajouteContact(String pseudo,String adresse,String machine, int port_UDP, int port_TCP, boolean connecte )
	{
		// si le contacte existe, on met à jour les infos
		try
		{
			Contact x = getContact(pseudo);
			x.setAdresse(adresse);
			x.setMachine(machine);
			x.setPort_TCP(port_TCP);
			x.setPort_UDP(port_UDP);
			x.setConnecte(connecte);
		}
		// si le contact n'existe pas, on le créé
		catch (NoSuchElementException e)
		{
			Contact y = new Contact(pseudo,adresse,machine, port_UDP, port_TCP,connecte) ;
			this.liste_contacts.add(y);
		}
		
	}
	
	// ajoute un contact par le type contact
	public synchronized void ajouteContact(Contact contact)
	{
		ajouteContact(contact.getPseudo(),contact.getAdresse(),contact.getMachine(),contact.getPort_UDP(),contact.getPort_TCP(),contact.getConnecte());
	}
	
	
	
	
	
	
	
	
	
	
*/	
	
}
