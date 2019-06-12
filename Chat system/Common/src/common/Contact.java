package common;
import java.io.Serializable;
import java.util.UUID;

public class Contact implements Serializable 
{
	private static final long serialVersionUID = 1;
	private String pseudo ;
	private String adresse ;
	private UUID machine ;
	private int port_presence ;
	private int port_TCP ;
	private boolean connecte ;
	
	// ATTENTION : VERIF PAS DE PSEUDO DEJA EXISTANT
	public Contact (String pseudo,String adresse,UUID machine, int port_presence, int port_TCP, boolean connecte)
	{
		this.pseudo=pseudo;
		this.adresse=adresse;
		this.machine = machine ;
		this.port_presence=port_presence;
		this.port_TCP=port_TCP;
		this.connecte = connecte ;
	}
	public Contact (String adresse,UUID machine, int port_presence, int port_TCP, boolean connecte)
	{
		this.pseudo="NoPseudo";
		this.adresse=adresse;
		this.machine = machine ;
		this.port_presence=port_presence;
		this.port_TCP=port_TCP;
		this.connecte = connecte ;
	}
	// Afficheur
	public String toString()
	{
		return this.pseudo +" "+ this.adresse + " " + this.getMachine() + " " +  port_presence + " "+port_TCP + " "+connecte ;
	}
	
	// setter
	public void setPseudo(String x)
	{
		this.pseudo=x;
	}
	public void setAdresse(String x)
	{
		this.adresse = x;
	}
	public void setMachine(UUID x)
	{
		this.machine = x;
	}
	public void setPort_presence(int x)
	{
		this.port_presence = x;
	}
	public void setPort_TCP(int x)
	{
		this.port_TCP = x;
	}
	public void setConnecte(boolean x)
	{
		this.connecte = x;
	}
	
	
	
	//getter
	public String getPseudo()
	{
		return this.pseudo;
	}
	public String getAdresse()
	{
		return this.adresse;
	}
	public UUID getMachine()
	{
		return this.machine;
	}
	public int getPort_presence()
	{
		return this.port_presence;
	}
	public int getPort_TCP()
	{
		return this.port_TCP;
	}
	public boolean getConnecte()
	{
		return this.connecte;
	}


}
