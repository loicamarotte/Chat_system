package common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message_presence implements Serializable
{
	private static final long serialVersionUID = 1;
	// Attributs
	private int type ;
	private Object contenu ;
	
	// Constructeurs
	// constructeur classique
	public Message_presence (int type,Object contenu)
	{
		this.type = type ;
		this.contenu = contenu ;
	}
	
	//constructeur avec des bytes recus
	public Message_presence(byte[] b)throws Exception
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		ObjectInput in = null;
		
		  
		in = new ObjectInputStream(bis);
		Message_presence o = (Message_presence)in.readObject(); 
		in.close();
		this.type = o.getType() ;
		this.contenu = o.getContenu() ;
	}
	
	
	// Getters
	public int getType()
	{
		return this.type ;
	}
	
	public Object getContenu()
	{
		return this.contenu;
	}
	
	// Methodes
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
	
}
