package client;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import common.Contact;

public class Database 
{
	
	private static void createDir(){
		new File(System.getProperty("user.dir") + File.separator + "ConvDB").mkdirs();
	}
	
	public static void save_conv(Conversation conv, boolean recu) 
	{	
		createDir();
		try
		{
			// identification de la conv à stocker
			String path = System.getProperty("user.dir") + File.separator + "ConvDB"+ File.separator + conv.getUser2().getMachine();
			
			// ecriture du message dans le fichier associé a la conv
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream messOut = new ObjectOutputStream(fileOut);
			messOut.writeObject(conv.getMessages());
			messOut.close();
			fileOut.close();
		    
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void save_parametres(Parametres p){
		try
		{
			// identification de la conv à stocker
			String path = System.getProperty("user.dir") + File.separator + "_param";
			
			// ecriture du message dans le fichier associé a la conv
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream messOut = new ObjectOutputStream(fileOut);
			messOut.writeObject(p);
			messOut.close();
			fileOut.close();
		    
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Parametres getParametresSaved(){
		try{
			return (Parametres)getObjectSaved(System.getProperty("user.dir")  + File.separator + "_param");

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
// retourne les messages stockés ou une liste vide
public static ArrayList<Message> getMessagesSaved(Contact c )
{
	createDir();

	// on verifie si le fichier existe ==  il y a des messages stockés

		try{
			@SuppressWarnings("unchecked")
			ArrayList<Message> liste_mess = (ArrayList<Message>)getObjectSaved( System.getProperty("user.dir") + File.separator + "ConvDB" + File.separator + c.getMachine());
			if (liste_mess == null){
				return new ArrayList<Message>();
			}
			else{
				return liste_mess;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<Message>();
}
	
private static Object getObjectSaved(String path) throws Exception{
	Object ret;
	File fichier = new File(path) ;
	
	if(fichier.exists() && !fichier.isDirectory())
	{

			FileInputStream fis = new FileInputStream(fichier);
			ObjectInputStream ois = new ObjectInputStream(fis);

			ret = ois.readObject();
			ois.close();
			fis.close();
			return ret;
	}
	else
	{
		return null;
	}
}
	
}	

