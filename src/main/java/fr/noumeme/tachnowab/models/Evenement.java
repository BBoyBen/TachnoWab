package fr.noumeme.tachnowab.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Evenement {
	
	@Id
	private UUID id;
	private Date date;
	private int valeur;
	private String commentaire;
	private ArrayList<String> tags;
	
	public Evenement(Date date, int valeur, String commentaire, ArrayList<String> tags) {
		super();
		this.id = UUID.randomUUID();
		this.date = date;
		this.valeur = valeur;
		this.commentaire = commentaire;
		this.tags = tags;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	
}
