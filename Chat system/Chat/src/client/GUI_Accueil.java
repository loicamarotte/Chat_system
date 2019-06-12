package client;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import common.Contact;
import common.ContactsList;

public class GUI_Accueil extends Thread
{
	// composants
	private JFrame fenetre ;
	private JTextField zone_pseudo ;
	private JLabel labelpseudo ;
	
	private boolean GUI_ouv;
	
	// attributs liés aux conv
	private Contact user_local ;
	private ContactsList liste_contacts;
	private ConversationList liste_conv;
	private Interface_TCP TCP;
	private Interface_Presence UDP;
	private Parametres param;
	
	// Constructeur
	public GUI_Accueil(Contact user_local,ContactsList liste_contacts,Interface_TCP TCP,Interface_Presence UDP,ConversationList liste_conv,Parametres param)
	{
		this.user_local = user_local ;
		this.liste_contacts = liste_contacts ;
		this.liste_conv = liste_conv;
		this.TCP = TCP;
		this.UDP = UDP;
		this.GUI_ouv = false;
		this.param = param;
	}
	
	
	private WindowListener wl()
	{
		return new WindowListener()
			{
				public void windowClosing(WindowEvent evt)
				{
					Application.deconnexion(user_local, TCP, UDP, liste_conv);
				}
				public void windowClosed(WindowEvent e) {
			    }

			    public void windowOpened(WindowEvent e) {
			    }

			    public void windowIconified(WindowEvent e) {
			    }

			    public void windowDeiconified(WindowEvent e) {
			    }

			    public void windowActivated(WindowEvent e) {
			    }

			    public void windowDeactivated(WindowEvent e) {
			    }
			};
	}
	
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
	// -------------------------- ActionListener ---------------------------

	private ActionListener listener_bouton()
	{
		return new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				
				String pseudo = zone_pseudo.getText();
				if (liste_contacts.pseudo_existe(pseudo))
				{
					labelpseudo.setText("Pseudo déjà pris");		
				}
				else
				{

					user_local.setPseudo(pseudo);
					user_local.setConnecte(true);
					param.setNom(pseudo);
					Database.save_parametres(param);
					UDP.publish();
					if (GUI_ouv == false) {
						GUI gui = new GUI(liste_contacts,liste_conv,user_local,TCP,UDP);
						gui.start();
						GUI_ouv = true;
					}
					
					
				}
				
			}
				
		} ;
	}

	

	// ---------------------- Composants --------------------------
	public Component createComponents()
	{
		// ----- Creation d'un bouton
		JButton bouton_envoyer = new JButton ("Connexion") ;
		// S'active avec un clic ou entrée 
		bouton_envoyer.setMnemonic(KeyEvent.VK_ENTER);
		// associe l'action bouton à notre bouton
		bouton_envoyer.addActionListener(listener_bouton());
		
		
		// ----- Creation d'une zone de saisie de message
		this.zone_pseudo = new JTextField(20);
		
		
		// ----- Creation de labels
		JLabel label = new JLabel ( "----- Choisissez votre pseudo pour vous connecter ------") ;
		this.labelpseudo = new JLabel("");
		
		// creer une bordure dans le panel
		JPanel panel = new JPanel(new GridLayout(2,2)) ;
		// ajouter tous les composants
		panel.add(bouton_envoyer);
		panel.add(zone_pseudo);
		panel.add(label);
		panel.add(labelpseudo);
		
		
		
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30)); //top,left,bottom,right
		
		return panel ;		
	}
	

	// -------------------- Créer la fenetre
	private void createAndShowGUI()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		}
		catch (ClassNotFoundException e) {
			System.out.println("Avertissement: GTKLookAndFeel non trouv�");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// Déco fenetre
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		// création de la fenetre
		this.fenetre = new JFrame("GUI") ;
		this.fenetre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		Component contenu = this.createComponents() ;
		this.fenetre.getContentPane().add(contenu, BorderLayout.PAGE_END);
		this.fenetre.pack();
		//this.fenetre.setBounds(0, 0, 800, 800);
		this.zone_pseudo.setText(param.getNom());
		this.fenetre.addWindowListener(wl());
		this.fenetre.setVisible(true);
	}
	
	

	public void run()
	{
		createAndShowGUI();
		/*
		ContactsList liste_contacts = new ContactsList() ;
		ConversationList liste_conv = new ConversationList() ;
		*/

		connexion(user_local,TCP,UDP) ;
		
		//UDP.broadcast_user_local();
		
		
	}
	
}
