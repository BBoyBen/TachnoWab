package fr.noumeme.tachnowab.dtos;

import java.util.UUID;

import fr.noumeme.tachnowab.models.Serie;

public class SerieDto {
    private UUID id;
	private String titre;
	private String description;
    private UUID idUtilisateur;

    public SerieDto() { }

    public SerieDto(String titre, String description, UUID idUtilisateur) {
        this(null, titre, description, idUtilisateur);
    }

    public SerieDto(UUID id, String titre, String description, UUID idUtilisateur) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.idUtilisateur = idUtilisateur;
    }

    public UUID getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public UUID getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(UUID idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Serie toModel() {
        return new Serie(this.getTitre(), this.getDescription(), this.getIdUtilisateur());
    }
}