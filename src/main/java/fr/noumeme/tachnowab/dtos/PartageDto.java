package fr.noumeme.tachnowab.dtos;

import java.util.UUID;

public class PartageDto {
    private UUID id;
    private boolean lectureSeule;
    private UUID idUtilisateur;
    private String loginUtilisateur;
    private UUID idSerie;
    private String titre;
    private String description;

    public PartageDto(final UUID id, final boolean lectureSeule, final UUID idUtilisateur,
            final String loginUtilisateur, final UUID idSerie, final String titre, final String description) {
        this.id = id;
        this.lectureSeule = lectureSeule;
        this.setIdUtilisateur(idUtilisateur);
        this.setLoginUtilisateur(loginUtilisateur);
        this.idSerie = idSerie;
        this.titre = titre;
        this.description = description;
    }

    public UUID getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(UUID idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getLoginUtilisateur() {
        return loginUtilisateur;
    }

    public void setLoginUtilisateur(String loginUtilisateur) {
        this.loginUtilisateur = loginUtilisateur;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public boolean isLectureSeule() {
        return lectureSeule;
    }

    public void setLectureSeule(final boolean lectureSeule) {
        this.lectureSeule = lectureSeule;
    }

    public UUID getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(final UUID idSerie) {
        this.idSerie = idSerie;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(final String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}