package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import common.*;
class Serveur 
{
	//Va se mettre en attente d'une requete d'un client (Subscribe() ou Publish())
	@SuppressWarnings("resource")
	public static void main(String[] args)
	{
		ContactsList liste_contacts = new ContactsList();
		ServerSocket socket_serv = null;
		RequestHandler.setContactList(liste_contacts);
		
		
		boolean loop = true;
		while (loop == true){
			try{
				
				socket_serv = new ServerSocket(Integer.parseInt((String)JOptionPane.showInputDialog(null,"Veuillez entrer le port que le serveur de presence doit utiliser: ", "Customized Dialog",JOptionPane.PLAIN_MESSAGE,null,null,"")));
				loop = false;
			}
			catch (NumberFormatException e){
				JOptionPane.showMessageDialog(null, "Port rentre non valable, doit etre un nombre!");
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Impossible d'utiliser ce port");
			}
		}

			
			
			while(true)
			{	
				try {
					// création du socket mise en etat accept == attente d'un message pour connexion
					Socket socket = socket_serv.accept() ;
					
					// lancement d'un thread pour envoyer et recevoir des messages 
					new RequestHandler(socket) ;
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
				

	/*	catch (Exception e)
		{
			e.printStackTrace();
		}*/
	}
		
		

		
}
		
		
		
		
		


