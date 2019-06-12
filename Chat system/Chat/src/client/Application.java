package client;
import java.util.UUID;

import javax.swing.JOptionPane;

import common.Contact;
import common.ContactsList;

public class Application 
{

	
	// --------------------------- CONNEXION = ECOUTE TCP ET UDP ------------------------------
	// se connecte et envoi coucou à tout le monde pour le signifier
	// et ecoute en permanence pour connaitre les connectés
	public static void connexion(Contact user_local, Interface_TCP TCP, Interface_Presence UDP)
	{	
		
		// ecoute TCP (appel fonction dans un thread)
		TCP.start();
		System.out.println("Thread TCP server (ecoute) lancé avec comme port ecoute: " + user_local.getPort_TCP());
		
		
		// ecoute UDP + mise a jour contact	

		UDP.start();
		System.out.println("Thread UDP lancé avec le port : "+user_local.getPort_presence());
		
	}
	
	// ------------------------ DECONNEXION -----------------------------------------------
	public static void deconnexion(Contact user_local,Interface_TCP TCP, Interface_Presence presence, ConversationList liste_conv)
	{
		// Changement d'etat
		user_local.setConnecte(false);
		// publish notre etat de deconnexion
		presence.publish();
		// Fermeture UDP
		//presence.close_UDP() ;
		
		deconnexionSansServ(user_local,TCP,liste_conv);
	}
	
	public static void deconnexionSansServ(Contact user_local,Interface_TCP TCP, ConversationList liste_conv)
	{
		
		// Fermeture TCP
		TCP.close_TCP();
		
		// Fermeture des threads TCP
		liste_conv.close_all_conv();
		
		// Fermeture fenetre
		java.lang.System.exit(0);
	}
	
	
	public static void main(String[] args)
	{
	
		Contact user_local;
		
		try {
			
			ContactsList liste_contacts = new ContactsList() ;
			ConversationList liste_conv = new ConversationList() ;
			
			// Recuperations des parametres si ils existent, sinon null
			Parametres param = Database.getParametresSaved();
			
			// test si null
			if ((param != null) && (param.getUuid()!=null)){				
				//user_local = new Contact("","localhost",param.getUuid(),5000,2000,true);			
			}
			else if (param!=null){
				//manque l'uuid, on le cree, l'ajoute et le save. CAS OU ON INSTALLE DIRECTEMENT AVEC UN FICHIER CONTENANT ADRESSE SERVEUR
				UUID uuid = UUID.randomUUID();
				param.setUUID(uuid);
				Database.save_parametres(param);

			}
			else{
				//Demande infos a l'utilisateur
				JOptionPane.showMessageDialog(null, "Aucun fichier de parametre trouv�, parametrage prealable necessaire");
				String adserv = (String)JOptionPane.showInputDialog(null,"Veuillez entrer l'adresse du serveur de presence: ", "Customized Dialog",JOptionPane.PLAIN_MESSAGE,null,null,"");
				int port = 0;
				boolean loop = true;
				while (loop == true){
					try{
						port = Integer.parseInt((String)JOptionPane.showInputDialog(null,"Veuillez entrer le port du serveur de presence: ", "Customized Dialog",JOptionPane.PLAIN_MESSAGE,null,null,""));
						loop = false;
					}
					catch (NumberFormatException e){
						JOptionPane.showMessageDialog(null, "Port rentre non valable, doit etre un nombre!");
					}
				}
				UUID uuid = UUID.randomUUID();
				
				//cree parametres + enregistre
				param = new Parametres(adserv,port,"",uuid);
				Database.save_parametres(param);
				
			}
			
			user_local = new Contact("","localhost",param.getUuid(),5000,2000,true);
			/////////////////////OVERRIDE USER POUR TEST/ DOIT ETRE COMMENTE EN USAGE NORMAL
			//	UUID uuid = UUID.fromString("54944df8-0e9e-4471-a2f9-9af509fb5889");
			//	user_local = new Contact("","localhost",uuid,5001,2001,true) ;
			//////////////////////
			
			Interface_TCP TCP = new Interface_TCP(user_local,liste_contacts,liste_conv);
			
			Interface_Presence presence = new Interface_Presence(user_local,liste_contacts,param.getServeur(), param.getPortserv(),liste_conv,TCP);			
			GUI_Accueil gui_a = new GUI_Accueil(user_local,liste_contacts,TCP,presence,liste_conv,param);
			gui_a.start();

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		

		
	}
	
}
