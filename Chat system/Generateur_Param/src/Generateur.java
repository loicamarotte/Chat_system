

import javax.swing.JOptionPane;
import client.Parametres;
import client.Database;

class Generateur 
{



	
	
	public static void main(String[] args)
	{
	
		
		try {
			
				//Demande infos a l'utilisateur
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
				
				//cree parametres + enregistre
				Parametres param = new Parametres(adserv,port,"",null);
				Database.save_parametres(param);
				
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		

		
	}
	
}
