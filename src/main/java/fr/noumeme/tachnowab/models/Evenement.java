package fr.noumeme.tachnowab.models;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Evenement {
	
	@Id
	private UUID id;
	private ZonedDateTime date;
	private int valeur;
	private String commentaire;
	private ArrayList<String> tags;
	private UUID idSerie;
	
	public Evenement(ZonedDateTime date, int valeur, String commentaire, ArrayList<String> tags, UUID idSerie) {
		super();
		this.id = UUID.randomUUID();
		this.date = date;
		this.valeur = valeur;
		this.commentaire = commentaire;
		this.tags = tags;
		this.idSerie = idSerie;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
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

	public UUID getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(UUID idSerie) {
		this.idSerie = idSerie;
	}
	
	
	
	
}
