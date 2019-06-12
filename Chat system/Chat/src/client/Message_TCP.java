package client;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

// evite d'envoyer trop d'infos inutilement (contenues dans la classe Message) et ainsi reduit l'utilisation des buffers du réseaux
public class Message_TCP implements Serializable
{
	private static final long serialVersionUID = 1;
	private UUID machine ;
	private String contenu ;
	
	// Constructeurs
	public Message_TCP(Message m)
	{
		this.machine = m.getSender().getMachine() ;
		this.contenu = m.getContenu() ;
	}
	
	public Message_TCP (byte[] b) throws Exception
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		ObjectInput in = null;
		
		  
		in = new ObjectInputStream(bis);
		Message_TCP o = (Message_TCP)in.readObject(); 
		in.close();
		this.machine = o.getMachine() ;
		this.contenu = o.getContenu() ;


	}
	
	

	// methodes
	public byte[] getBytesMes()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try 
		{
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(this);
		  out.flush();
		  byte[] mes = bos.toByteArray();
		  bos.close();
		  return mes;

		} 
		catch (IOException ex) {
		    ex.printStackTrace();
		  }
		return null ;
		
	}
	
	
	
	// getter
	public String getContenu()
	{
		return this.contenu ;
	}
	public UUID getMachine()
	{
		return this.machine ;
	}
}
