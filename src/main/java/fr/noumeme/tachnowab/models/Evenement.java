package fr.noumeme.tachnowab.models;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.noumeme.tachnowab.dtos.EvenementDto;

@Entity
@Table(name = "EVENEMENT")
public class Evenement {
	
	@Id
	@Column(name = "ID")
	private UUID id;
	@Column(name = "DATE")
	private ZonedDateTime date;
	@Column(name = "VALEUR")
	private int valeur;
	@Column(name = "COMMENTAIRE")
	private String commentaire;
	@ElementCollection
	@Column(name = "TAGS")
	private List<String> tags = new ArrayList<String>();
	@Column(name = "ID_SERIE")
	private UUID idSerie;

	public Evenement() { }
	
	public Evenement(ZonedDateTime date, int valeur, String commentaire, List<String> tags, UUID idSerie) {
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public UUID getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(UUID idSerie) {
		this.idSerie = idSerie;
	}
    
    public EvenementDto toDto() {
        return new EvenementDto(this.getId(), this.getDate(), this.getValeur(), this.getCommentaire(), this.getTags(), this.getIdSerie());
    }
}
