package fr.noumeme.tachnowab.dtos;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;

import fr.noumeme.tachnowab.models.*;

import static org.junit.Assert.assertEquals;

public class DtosTests {
	
	@Test
	public void convert_evenementDto_toModel() {
		ZonedDateTime date = ZonedDateTime.now();
		UUID idSerie = UUID.randomUUID();
		EvenementDto dto = new EvenementDto(date, 1, "commentaire", Arrays.asList("t1", "t2"), idSerie);

		assertEquals(date, dto.getDate());
		assertEquals(1, dto.getValeur());
		assertEquals("commentaire", dto.getCommentaire());
		assertEquals(Arrays.asList("t1", "t2"), dto.getTags());
		assertEquals(idSerie, dto.getIdSerie());

		Evenement model = dto.toModel();

		assertEquals(model.getDate(), dto.getDate());
		assertEquals(model.getValeur(), dto.getValeur());
		assertEquals(model.getCommentaire(), dto.getCommentaire());
		assertEquals(model.getTags(), dto.getTags());
		assertEquals(model.getIdSerie(), dto.getIdSerie());
	}

	@Test
	public void convert_evenement_toDto() {
		ZonedDateTime date = ZonedDateTime.now();
		UUID idSerie = UUID.randomUUID();
		Evenement model = new Evenement(date, 1, "commentaire", Arrays.asList("t1", "t2"), idSerie);

		assertEquals(date, model.getDate());
		assertEquals(1, model.getValeur());
		assertEquals("commentaire", model.getCommentaire());
		assertEquals(Arrays.asList("t1", "t2"), model.getTags());
		assertEquals(idSerie, model.getIdSerie());

		EvenementDto dto = model.toDto();

		assertEquals(dto.getDate(), model.getDate());
		assertEquals(dto.getValeur(), model.getValeur());
		assertEquals(dto.getCommentaire(), model.getCommentaire());
		assertEquals(dto.getTags(), model.getTags());
		assertEquals(dto.getIdSerie(), model.getIdSerie());
	}

	@Test
	public void convert_partageDto_toModel() {
		UUID idUtilisateur = UUID.randomUUID();
		UUID idSerie = UUID.randomUUID();
		PartageDto dto = new PartageDto(true, idUtilisateur, "loginUtilisateur", idSerie);

		assertEquals(true, dto.isLectureSeule());
		assertEquals(idUtilisateur, dto.getIdUtilisateur());
		assertEquals("loginUtilisateur", dto.getLoginUtilisateur());
		assertEquals(idSerie, dto.getIdSerie());

		Partage model = dto.toModel();

		assertEquals(model.isLectureSeule(), dto.isLectureSeule());
		assertEquals(model.getIdUtilisateur(), dto.getIdUtilisateur());
		assertEquals(model.getLoginUtilisateur(), dto.getLoginUtilisateur());
		assertEquals(model.getIdSerie(), dto.getIdSerie());
	}

	@Test
	public void convert_partage_toDto() {
		UUID idUtilisateur = UUID.randomUUID();

		Serie serie = new Serie("titre", "description", idUtilisateur);
		Partage model = new Partage(true, idUtilisateur, serie.getId(), "loginUtilisateur");

		assertEquals(true, model.isLectureSeule());
		assertEquals(idUtilisateur, model.getIdUtilisateur());
		assertEquals("loginUtilisateur", model.getLoginUtilisateur());
		assertEquals(serie.getId(), model.getIdSerie());

		PartageDto dto = model.toDto(serie);

		assertEquals(dto.isLectureSeule(), model.isLectureSeule());
		assertEquals(dto.getIdUtilisateur(), model.getIdUtilisateur());
		assertEquals(dto.getLoginUtilisateur(), model.getLoginUtilisateur());
		assertEquals(dto.getIdSerie(), model.getIdSerie());
	}

	@Test
	public void convert_serieDto_toModel() {
		UUID idUtilisateur = UUID.randomUUID();
		SerieDto dto = new SerieDto("titre", "description", idUtilisateur);

		assertEquals("titre", dto.getTitre());
		assertEquals("description", dto.getDescription());
		assertEquals(idUtilisateur, dto.getIdUtilisateur());

		Serie model = dto.toModel();

		assertEquals(model.getTitre(), dto.getTitre());
		assertEquals(model.getDescription(), dto.getDescription());
		assertEquals(model.getIdUtilisateur(), dto.getIdUtilisateur());
	}

	@Test
	public void convert_serie_toDto() {
		UUID idUtilisateur = UUID.randomUUID();
		Serie model = new Serie("titre", "description", idUtilisateur);

		assertEquals("titre", model.getTitre());
		assertEquals("description", model.getDescription());
		assertEquals(idUtilisateur, model.getIdUtilisateur());

		SerieDto dto = model.toDto();

		assertEquals(dto.getTitre(), model.getTitre());
		assertEquals(dto.getDescription(), model.getDescription());
		assertEquals(dto.getIdUtilisateur(), model.getIdUtilisateur());
	}

	@Test
	public void convert_utilisateurDto_toModel() {
		UtilisateurDto dto = new UtilisateurDto("nom", "prenom", "login");

		assertEquals("nom", dto.getNom());
		assertEquals("prenom", dto.getPrenom());
		assertEquals("login", dto.getLogin());

		dto.setMotDePasse("motdePasse");
		assertEquals("motdePasse", dto.getMotDePasse());

		Utilisateur model = dto.toModel();

		assertEquals(model.getNom(), dto.getNom());
		assertEquals(model.getPrenom(), dto.getPrenom());
		assertEquals(model.getLogin(), dto.getLogin());
	}

	@Test
	public void convert_utilisateur_toDto() {
		Utilisateur model = new Utilisateur("nom", "prenom", "login", "motdePasse");

		assertEquals("nom", model.getNom());
		assertEquals("prenom", model.getPrenom());
		assertEquals("login", model.getLogin());
		assertEquals("motdePasse", model.getMotDePasse());

		UtilisateurDto dto = model.toDto();

		assertEquals(dto.getNom(), model.getNom());
		assertEquals(dto.getPrenom(), model.getPrenom());
		assertEquals(dto.getLogin(), model.getLogin());
	}
}