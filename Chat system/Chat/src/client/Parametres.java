package client;

import java.io.Serializable;
import java.util.UUID;

public class Parametres implements Serializable{
	
	private static final long serialVersionUID = 1;
	String serveur;
	int portserv;
	String nom;
	UUID uuid;
	
	public Parametres(String serveur, int portserv, String nom, UUID uuid){
		this.serveur = serveur;
		this.portserv = portserv;
		this.nom = nom;
		this.uuid=uuid;
	}

	public String getServeur() {
		return serveur;
	}

	public int getPortserv() {
		return portserv;
	}

	public String getNom() {
		return nom;
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public void setUUID(UUID uuid){
		this.uuid = uuid;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
