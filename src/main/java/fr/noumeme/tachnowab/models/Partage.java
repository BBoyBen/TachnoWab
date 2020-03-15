package fr.noumeme.tachnowab.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PARTAGE")
public class Partage {

	@Id
	@Column(name = "ID")
	private UUID id;
	@Column(name = "LECTURE_SEULE")
	private boolean lectureSeule;
	@Column(name = "ID_UTILISATEUR")
	private UUID idUtilisateur;
	@Column(name = "ID_SERIE")
	private UUID idSerie;
	
	public Partage() {
		
	}
	
	public Partage(boolean lectureSeule, UUID idUtilisateur, UUID idSerie) {
		super();
		this.id = UUID.randomUUID();
		this.lectureSeule = lectureSeule;
		this.idUtilisateur = idUtilisateur;
		this.idSerie = idSerie;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public boolean isLectureSeule() {
		return lectureSeule;
	}

	public void setLectureSeule(boolean lectureSeule) {
		this.lectureSeule = lectureSeule;
	}

	public UUID getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(UUID idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public UUID getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(UUID idSerie) {
		this.idSerie = idSerie;
	}
	
	
}
