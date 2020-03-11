package fr.noumeme.tachnowab.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Partage {

	@Id
	private UUID id;
	private boolean lectureSeule;
	private UUID idUtilisateur;
	private UUID idSerie;
	
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
