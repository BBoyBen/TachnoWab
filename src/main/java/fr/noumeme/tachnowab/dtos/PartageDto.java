package fr.noumeme.tachnowab.dtos;

import java.util.UUID;

import fr.noumeme.tachnowab.models.Partage;

public class PartageDto {
    private UUID id;
    private boolean lectureSeule;
    private UUID idUtilisateur;
    private String loginUtilisateur;
    private UUID idSerie;
    private String titre;
    private String description;

    public PartageDto() { }

    public PartageDto(final UUID id, final boolean lectureSeule, final UUID idUtilisateur,
            final String loginUtilisateur, final UUID idSerie, final String titre, final String description) {
        this.id = id;
        this.lectureSeule = lectureSeule;
        this.idUtilisateur = idUtilisateur;
        this.loginUtilisateur = loginUtilisateur;
        this.idSerie = idSerie;
        this.titre = titre;
        this.description = description;
    }

    public PartageDto(final boolean lectureSeule, final UUID idUtilisateur, final String loginUtilisateur, final UUID idSerie) {
        this(null, lectureSeule, idUtilisateur, loginUtilisateur, idSerie, null ,null);
    }

    public UUID getIdUtilisateur() {
        return idUtilisateur;
    }

    public String getLoginUtilisateur() {
        return loginUtilisateur;
    }

    public UUID getId() {
        return id;
    }

    public boolean isLectureSeule() {
        return lectureSeule;
    }

    public UUID getIdSerie() {
        return idSerie;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public Partage toModel() {
        return new Partage(this.isLectureSeule(), this.getIdUtilisateur(), this.getIdSerie(), this.getLoginUtilisateur());
    }
}