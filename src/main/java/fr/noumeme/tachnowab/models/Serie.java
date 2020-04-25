package fr.noumeme.tachnowab.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.noumeme.tachnowab.dtos.SerieDto;


@Entity
@Table(name = "SERIE")
public class Serie {
	
	@Id
	@Column(name = "ID")
	private UUID id;
	@Column(name = "TITRE")
	private String titre;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "ID_UTILISATEUR")
	private UUID idUtilisateur;
	
	public Serie() { }
	
	public Serie(String titre, String description, UUID idUtilisateur) {
		super();
		this.id = UUID.randomUUID();
		this.titre = titre;
		this.description = description;
		this.idUtilisateur = idUtilisateur;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UUID getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(UUID idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	
	public SerieDto toDto() {
		return new SerieDto(this.getId(), this.getTitre(), this.getDescription(), this.getIdUtilisateur());
	}
}
