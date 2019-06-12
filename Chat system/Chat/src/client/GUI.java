package client;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import common.Contact;
import common.ContactsList;

import java.awt.event.WindowListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class GUI extends Thread
{
	// composants
	private JFrame fenetre ;
	private JTextArea zone_conversation ;
	private JList<String> zone_liste ;
	private JTextField zone_message ;
	
	// attributs liés aux conv
	private Contact user_local ;
	private ContactsList liste_contacts ;
	private ConversationList liste_conv ;
	private boolean conv_select = false; 
	private Conversation conv_actuelle ;
	private Interface_TCP TCP;
	private Interface_Presence UDP;
	

	
	
	
	public GUI(ContactsList l_contacts, ConversationList l_conv, Contact user_local, Interface_TCP TCP, Interface_Presence UDP )
	{
		this.liste_contacts = l_contacts ;
		this.liste_conv = l_conv ;
		this.user_local = user_local ;
		this.TCP = TCP;
		this.UDP = UDP;
	}
	
	
	// -------------------------- ActionListener ---------------------------

	private ActionListener listener_bouton()
	{
		return new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				action_bouton() ;
				
			}
				
		} ;
	}
	
	private ActionListener listener_bouton_deco()
	{
		return new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				action_bouton_deco() ;
				
			}
				
		} ;
	}
	
	
	
	private ListSelectionListener listener_liste()
	{
		return new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent evt)
			{
				changement_valeur_liste(evt);
			}
		};
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

	
	
	
	// -------------------- Modele de la liste ------------------------------
	private AbstractListModel<String> modele_liste()
	{
		//this.liste_contacts_actifs = liste_contacts.getContacts_actifs() ;
		return new AbstractListModel<String>() 
		{
			private static final long serialVersionUID = 1;
			//Contact[] actifs  = liste_contacts_actifs;
			ArrayList<Contact> l_contacts = liste_contacts.getListe();
			
			public int getSize()
			{
				return l_contacts.size();
			}
			
			public String getElementAt(int i)
			{
				String s = "";
				try {
					int a = liste_conv.trouve_conv(l_contacts.get(i)).getNbNewMess();
					if (a!=0){
						s = " : (" + a + ")";
					}
				}
				catch(NoSuchElementException e){
					
				}
				if (l_contacts.get(i).getConnecte()) {
					return l_contacts.get(i).getPseudo() + s;
				}
				else {
					return l_contacts.get(i).getPseudo() + s;
				}
				
			}
		};
		
	}


		
	// --------- Action suite à un evenement ----------------
	private void changement_valeur_liste(ListSelectionEvent evt)
	{
			int i = zone_liste.getSelectedIndex();
			if (i!=-1){
				
				Contact c = liste_contacts.getListe().get(i);
				try
				{
					conv_actuelle = liste_conv.trouve_conv(c) ;
					zone_conversation.setText(conv_actuelle.toString());
					conv_actuelle.resetNewMess();
					conv_select = true ;
				}
				catch (NoSuchElementException e)
				{
					ArrayList<Message> liste_mess = Database.getMessagesSaved(c);
					if(liste_mess.isEmpty())
					{
						zone_conversation.setText("Commencez à clavarder avec "+ c.getPseudo());
					}
					else
					{
						Iterator<Message> iter = liste_mess.iterator() ;
						String s = " ----------- Conversation avec " + c.getPseudo() + "-------------- \n";
						
						while(iter.hasNext())
						{
							Message m = iter.next() ;
							s = s + "De : " + m.getSender().getPseudo()  + "\n";
							s = s + m.getContenu() + "\n" ;
						}
						zone_conversation.setText(s);
					}
					
					
					conv_select = false ;
	
				}
		}
	}
	
	private void action_bouton()
	{
		if (conv_select)
		{
			if (this.conv_actuelle.getUser2().getConnecte()) {
			this.conv_actuelle.envoyer_message(zone_message.getText());
			zone_conversation.setText(conv_actuelle.toString());
			}
		}
		else
		{
			int i = zone_liste.getSelectedIndex();
			Contact userdest = liste_contacts.getListe().get(i);
			if (userdest.getConnecte()) {
			this.conv_actuelle = liste_conv.creer_conv_et_envoyer_message(zone_message.getText(),userdest,user_local );
			conv_select=true;
			zone_conversation.setText(conv_actuelle.toString());
			}
		}
		

		
	}
	private void action_bouton_deco()
	{
		Application.deconnexion(user_local, TCP, UDP, liste_conv);
	}
	
	
	
	// ---------------------- Composants --------------------------
	public Component createComponents()
	{
		// ----- Creation d'un panel et des contraintes
		JPanel panel = new JPanel(new GridBagLayout()) ;

		
		// ----- Creation d'un bouton envoi de message
		JButton bouton_envoyer = new JButton ("Clavarder") ;
		// S'active avec un clic ou entrée 
		bouton_envoyer.setMnemonic(KeyEvent.VK_ENTER);
		// associe l'action bouton à notre bouton
		bouton_envoyer.addActionListener(listener_bouton());
		// contrainte du layout
		GridBagConstraints c_bouton = new GridBagConstraints() ;
		c_bouton.fill= GridBagConstraints.BOTH;
		c_bouton.gridx = 2;
		c_bouton.gridy = 1 ;
		c_bouton.weightx = 0.15;
		c_bouton.weighty = 0.1;
		
		// ----- Creation d'un bouton deconnexion
		JButton bouton_deco = new JButton ("Deconnexion") ;
		// associe l'action bouton à notre bouton
		bouton_deco.addActionListener(listener_bouton_deco());
		// contrainte du layout
		GridBagConstraints c_bouton_deco = new GridBagConstraints() ;
		c_bouton_deco.fill= GridBagConstraints.BOTH;
		c_bouton_deco.gridx = 0;
		c_bouton_deco.gridy = 1 ;
		c_bouton_deco.weightx = 0.25 ;
		c_bouton_deco.weighty = 0.1 ;

		
		// ----- Creation d'une liste
		this.zone_liste = new JList<String>();		
		//TODO: remplacer la Contact[] qui se trouve dans la fonction modele_liste par un passage en argument de liste_contacts_actifs
		this.zone_liste.setModel(this.modele_liste());
		this.zone_liste.addListSelectionListener(this.listener_liste());		
		// contrainte du layout
		GridBagConstraints c_liste = new GridBagConstraints() ;
		c_liste.fill= GridBagConstraints.BOTH;
		c_liste.gridheight = 1;
		c_liste.gridx = 0;
		c_liste.gridy = 0 ;
		c_liste.weightx = 0.40;
		c_liste.weighty = 1;
		
		
		zone_liste.setCellRenderer(new DefaultListCellRenderer() {
			
			private static final long serialVersionUID = 1;
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
               Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
               setText((String)value);
               if (liste_contacts.getListe().get(index).getConnecte()) {

                         setBackground(Color.GREEN);

               } else {
                   setBackground(Color.RED);
               }
               return c;
          }
		
		});
		
		
		// ----- Creation de l'affichage de la conversation
		this.zone_conversation = new JTextArea();
		this.zone_conversation.setColumns(180);
		this.zone_conversation.setRows(10);
		this.zone_conversation.setLineWrap(true);
		this.zone_conversation.setWrapStyleWord(true);
		// contrainte du layout
		GridBagConstraints c_conv = new GridBagConstraints() ;
		c_conv.fill= GridBagConstraints.BOTH;
		c_conv.gridwidth = 2;
		c_conv.gridx = 1;
		c_conv.gridy = 0 ;
		c_conv.weightx = 0.75;
		c_conv.weighty = 0.9;
		
		// ----- Creation d'une zone de saisie de message
		this.zone_message = new JTextField(40);
		// contrainte du layout
		GridBagConstraints c_message = new GridBagConstraints() ;
		c_message.fill= GridBagConstraints.BOTH;
		c_message.gridx = 1;
		c_message.gridy = 1 ;
		c_message.weightx = 0.75;
		c_message.weighty = 0.1;
		
		
		// ----- Creation des barres de scroll
		JScrollPane scroll_conv = new JScrollPane(zone_conversation) ;
		scroll_conv.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll_conv.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollPane scroll_mess = new JScrollPane(zone_message) ;
		scroll_mess.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scroll_mess.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		

		// ajouter tous les composants
		panel.add(zone_liste,c_liste);
		panel.add(scroll_conv,c_conv);
		panel.add(bouton_envoyer,c_bouton);
		panel.add(scroll_mess,c_message);
		panel.add(bouton_deco,c_bouton_deco);
		
		
		
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30)); //top,left,bottom,right
		
		return panel ;		
	}
	
	

	
	// ------------------ Créer la fenetre
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
		this.fenetre.addWindowListener(wl());
		
		Component contenu = this.createComponents() ;
		this.fenetre.getContentPane().add(contenu, BorderLayout.CENTER);
		this.fenetre.pack();
		this.fenetre.setBounds(0, 0, 800, 800);
		this.fenetre.setVisible(true);
	}
	
	

	public void run()
	{

		createAndShowGUI();

		while(true){
				try{
				Thread.sleep(500);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				
				//TODO: remplacer ca par un rafraichissement de liste plus propre, qui ne perd pas l'index... et qui est declenche par l'arriv�e d'un nouveau message si possible -> dans la fonction de reception message des conv, appeller une fonction de GUI qui rafraichit.
				int save = this.zone_liste.getSelectedIndex();
				this.zone_liste.setModel(this.modele_liste());
				this.zone_liste.setSelectedIndex(save);
				
				// Ce qu'il y a ici sert normalement a rafraichir la discussion actuelle. Seulement, le fait que l'on setSelected declenche l evenement changeselected et donc le fait tout seul, on garde pour s'inspirer si on change la methode de rafraichissement de la liste pour quelque chose de plus propre.
				/*if (this.conv_select){
					if (this.conv_actuelle.getBoolNewMess()){
						this.conv_actuelle.resetNewMess();
						zone_conversation.setText(conv_actuelle.toString());
					}
					System.out.println("conv select : " + conv_select + "nb_new:" + conv_actuelle.getNbNewMess());
				}*/ 
			
		}
	}


	
	
	
	
	
	
	

}
