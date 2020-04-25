package fr.noumeme.tachnowab.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.noumeme.tachnowab.dtos.UtilisateurDto;

@Entity
@Table(name  = "UTILISATEUR")
public class Utilisateur {

	@Id
	@Column(name = "ID")
	private UUID id;
	@Column(name = "NOM")
	private String nom;
	@Column(name = "PRENOM")
	private String prenom;
	@Column(name = "LOGIN")
	private String login;
	@Column(name = "MOT_DE_PASSE")
	private String motDePasse;
	
	public Utilisateur() { }
	
	public Utilisateur(String nom, String prenom, String login, String motDePasse) {
		super();
		this.id = UUID.randomUUID();
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.motDePasse = motDePasse;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	public UtilisateurDto toDto() {
		return new UtilisateurDto(getId(), getNom(), getPrenom(), getLogin());
	}
}
