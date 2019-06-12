package client;
import java.io.Serializable;
import java.util.Date;

import common.Contact;

public class Message implements Serializable
{
	
	private static final long serialVersionUID = 1;
	private Contact dest;
	private Contact sender;
	private String contenu;
	private Date heure;
	
	public Message(Contact u1, Contact u2, String contenu)
	{
		this.dest=u1;
		this.sender=u2;
		this.contenu=contenu;
	}
	
	// setter
	public void setHeure(Date heure)
	{
		this.heure=heure;
	}
	
	//getter
	public Contact getDest()
	{
		return this.dest;
	}

	public Contact getSender()
	{
		return this.sender;
	}
	
	public String getContenu()
	{
		return this.contenu;
	}
	
	public Date getHeure()
	{
		return this.heure;
	}
	
}
