package fr.noumeme.tachnowab.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Serie {
	
	@Id
	private UUID id;
	private String titre;
	private String description;
	private UUID idUtilisateur;
	
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
	
	
	
}
