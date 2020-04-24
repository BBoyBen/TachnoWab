package fr.noumeme.tachnowab.dtos;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import fr.noumeme.tachnowab.models.Evenement;

public class EvenementDto {

    private UUID id;
	private ZonedDateTime date;
	private int valeur;
	private String commentaire;
	private List<String> tags;
	private UUID idSerie;
	
	public EvenementDto(UUID id, ZonedDateTime date, int valeur, String commentaire, List<String> tags, UUID idSerie) {
		this.id = id;
		this.date = date;
		this.valeur = valeur;
		this.commentaire = commentaire;
		this.tags = tags;
		this.idSerie = idSerie;
    }
    public EvenementDto(ZonedDateTime date, int valeur, String commentaire, List<String> tags, UUID idSerie) {
		this(null, date, valeur, commentaire, tags, idSerie);
	}

	public UUID getId() {
		return id;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public int getValeur() {
		return valeur;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public List<String> getTags() {
		return tags;
	}

	public UUID getIdSerie() {
		return idSerie;
	}
    
    public Evenement toModel() {
        return new Evenement(this.getDate(), this.getValeur(), this.getCommentaire(), this.getTags(), this.getIdSerie());
    }
}