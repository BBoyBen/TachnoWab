package fr.noumeme.tachnowab.dtos;

import java.util.UUID;

import fr.noumeme.tachnowab.models.Utilisateur;

public class UtilisateurDto {
    private UUID id;
    private String prenom;
    private String nom;
    private String login;
    private String motDePasse;

    public UtilisateurDto(UUID id, String nom, String prenom, String login) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
    }

    public UtilisateurDto(String nom, String prenom, String login) {
        this(null, nom, prenom, login);
    }

    public UUID getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getLogin() {
        return login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motdePasse) {
        this.motDePasse = motdePasse;
    }

    public Utilisateur toModel() {
        return new Utilisateur(this.getNom(), this.getPrenom(), this.getLogin(), this.getMotDePasse());
    }
}