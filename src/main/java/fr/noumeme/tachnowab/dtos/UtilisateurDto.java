package fr.noumeme.tachnowab.dtos;

import java.util.UUID;

public class UtilisateurDto {
    private UUID id;
    private String prenom;
    private String nom;
    private String login;

    public UtilisateurDto(UUID id, String nom, String prenom, String login) {
        this.setId(id);
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setLogin(login);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}