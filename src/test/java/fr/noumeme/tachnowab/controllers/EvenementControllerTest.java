package fr.noumeme.tachnowab.controllers;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.hash.Hashing;

import fr.noumeme.tachnowab.models.Evenement;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.services.EvenementService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EvenementControllerTest {
	
	@Mock
	private EvenementService service;
	
	@InjectMocks
	private EvenementController controller;
	
	private Utilisateur util;
	private Serie serie;
	private Evenement ev;
	
	@Before
	public void setup() {
		
    	String mdpEncode = Hashing.sha256()
				  .hashString("supermdp", StandardCharsets.UTF_8)
				  .toString();
		
		util = new Utilisateur("Nom", "Prenom", "login", mdpEncode);
		serie = new Serie("Titre", "Description", util.getId());
		ev = new Evenement(ZonedDateTime.now(), 1, "Commentaire", new ArrayList<String>(), serie.getId());
		
		given(service.getEvenementById(ev.getId()))
			.willReturn(Optional.ofNullable(ev));
	}
	
	@Test
	public void getEvenementById_idExistant_attends200()
		throws Exception {
		
		ResponseEntity<Evenement> rep = controller.getEvenementById(ev.getId(),
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(Evenement.class, rep.getBody().getClass());
		
		assertEquals(ev.getDate(), rep.getBody().getDate());
		assertEquals(ev.getCommentaire(), rep.getBody().getCommentaire());
		assertEquals(ev.getValeur(), rep.getBody().getValeur());
		assertEquals(ev.getIdSerie(), rep.getBody().getIdSerie());
	}
	
	@Test
	public void getEvenementById_idInexistant_attends404()
		throws Exception {
		
		ResponseEntity<Evenement> rep = controller.getEvenementById(UUID.randomUUID(),
				util.getId().toString());
		
		assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementById_idNull_attends400()
		throws Exception {
		
		ResponseEntity<Evenement> rep = controller.getEvenementById(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementById_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<Evenement> rep = controller.getEvenementById(ev.getId(),
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementBySerie_serieAvecEvent_attends200()
		throws Exception {
		
		given(service.getEvenementByIdSerie(serie.getId())).willReturn(Arrays.asList(ev));
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementBySerie(serie.getId(),
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(1, rep.getBody().size());
	}
	
	@Test
	public void getEvenementBySerie_serieSansEvent_attends204()
		throws Exception {
		
		Serie sansEv = new Serie("Vide", "Vide", util.getId());
		
		given(service.getEvenementByIdSerie(sansEv.getId())).willReturn(new ArrayList<Evenement>());
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementBySerie(sansEv.getId(),
				util.getId().toString());
		
		assertEquals(HttpStatus.NO_CONTENT, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementBySerie_idNull_attends400()
		throws Exception {
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementBySerie(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementBySerie_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementBySerie(serie.getId(),
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementsEntreDates_intervalPlein_attends200()
		throws Exception {
		
		ZonedDateTime debut = ZonedDateTime.now().minusDays(2);
		ZonedDateTime fin = ZonedDateTime.now().plusDays(2);
		
		given(service.getEvenementDansInterval(serie.getId(), debut, fin))
			.willReturn(Arrays.asList(ev));
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementsEntreDates(serie.getId(),
				debut, fin,
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(1, rep.getBody().size());
	}
	
	@Test
	public void getEvenementsEntreDates_intervalVide_attends204()
		throws Exception {
		
		ZonedDateTime debut = ZonedDateTime.now().minusDays(4);
		ZonedDateTime fin = ZonedDateTime.now().minusDays(2);
		
		given(service.getEvenementDansInterval(serie.getId(), debut, fin))
			.willReturn(new ArrayList<Evenement>());
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementsEntreDates(serie.getId(),
				debut, fin,
				util.getId().toString());
		
		assertEquals(HttpStatus.NO_CONTENT, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementsEntreDates_idNull_attends400()
		throws Exception {
		
		ZonedDateTime debut = ZonedDateTime.now().minusDays(2);
		ZonedDateTime fin = ZonedDateTime.now().plusDays(2);
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementsEntreDates(null,
				debut, fin,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementsEntreDates_debutNull_attends400()
		throws Exception {
		
		ZonedDateTime fin = ZonedDateTime.now().plusDays(2);
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementsEntreDates(serie.getId(),
				null, fin,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementsEntreDates_finNull_attends400()
		throws Exception {
		
		ZonedDateTime debut = ZonedDateTime.now().minusDays(2);
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementsEntreDates(serie.getId(),
				debut, null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getEvenementsEntreDates_cookieKo_attends401()
		throws Exception {
		
		ZonedDateTime debut = ZonedDateTime.now().minusDays(2);
		ZonedDateTime fin = ZonedDateTime.now().plusDays(2);
		
		ResponseEntity<List<Evenement>> rep = controller.getEvenementsEntreDates(serie.getId(),
				debut, fin,
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterEvenement_eventOk_attends201()
		throws Exception {
		
		Evenement pourAjout = new Evenement(ZonedDateTime.now(), 1, "Ajout", new ArrayList<String>(), serie.getId());
		
		given(service.ajouterEvenement(pourAjout))
			.willReturn(pourAjout);
		
		ResponseEntity<Evenement> rep = controller.ajouterEvenement(pourAjout,
				util.getId().toString());
		
		assertEquals(HttpStatus.CREATED, rep.getStatusCode());
		assertEquals(Evenement.class, rep.getBody().getClass());
		
		assertEquals(pourAjout.getCommentaire(), rep.getBody().getCommentaire());
		assertEquals(pourAjout.getDate(), rep.getBody().getDate());
		assertEquals(pourAjout.getValeur(), rep.getBody().getValeur());
		assertEquals(pourAjout.getIdSerie(), rep.getBody().getIdSerie());
	}
	
	@Test
	public void ajouterEvenement_eventNull_attends400()
		throws Exception {
		
		ResponseEntity<Evenement> rep = controller.ajouterEvenement(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterEvenement_cookieKo_attends401()
		throws Exception {
		
		Evenement pourAjout = new Evenement(ZonedDateTime.now(), 1, "Ajout", new ArrayList<String>(), serie.getId());
		
		ResponseEntity<Evenement> rep = controller.ajouterEvenement(pourAjout,
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterEvenement_erreurService_attends500()
		throws Exception {
		
		Evenement pourAjout = new Evenement(ZonedDateTime.now(), 1, "Ajout", new ArrayList<String>(), serie.getId());
		
		given(service.ajouterEvenement(pourAjout))
			.willReturn(null);
		
		ResponseEntity<Evenement> rep =controller.ajouterEvenement(pourAjout,
				util.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierEvenement_idExistant_attends200()
		throws Exception {
		
		Evenement pourModif = new Evenement(ZonedDateTime.now(), 5, "Modif", new ArrayList<String>(), serie.getId());
		
		given(service.modifierEvenement(ev.getId(), pourModif))
			.willReturn(pourModif);
		
		ResponseEntity<Evenement> rep = controller.modifierEvenement(ev.getId(), pourModif,
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(Evenement.class, rep.getBody().getClass());
		
		assertEquals(pourModif.getCommentaire(), rep.getBody().getCommentaire());
		assertEquals(pourModif.getDate(), rep.getBody().getDate());
		assertEquals(pourModif.getIdSerie(), rep.getBody().getIdSerie());
		assertEquals(pourModif.getValeur(), rep.getBody().getValeur());
	}
	
	@Test
	public void modifierEvenement_idInexistant_attends404()
		throws Exception {
		
		Evenement pourModif = new Evenement(ZonedDateTime.now(), 5, "Modif", new ArrayList<String>(), serie.getId());
		
		ResponseEntity<Evenement> rep = controller.modifierEvenement(UUID.randomUUID(), pourModif,
				util.getId().toString());
		
		assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierEvenement_idNull_attends400()
		throws Exception {
		
		Evenement pourModif = new Evenement(ZonedDateTime.now(), 5, "Modif", new ArrayList<String>(), serie.getId());
		
		ResponseEntity<Evenement> rep = controller.modifierEvenement(null, pourModif,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierEvenement_eventNull_attends400()
		throws Exception {
		
		ResponseEntity<Evenement> rep = controller.modifierEvenement(ev.getId(), null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierEvenement_cookieKo_attends401()
		throws Exception {
		
		Evenement pourModif = new Evenement(ZonedDateTime.now(), 5, "Modif", new ArrayList<String>(), serie.getId());
		
		ResponseEntity<Evenement> rep = controller.modifierEvenement(ev.getId(), pourModif,
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierEvenement_erreurService_attends500()
		throws Exception {
		
		Evenement pourModif = new Evenement(ZonedDateTime.now(), 5, "Modif", new ArrayList<String>(), serie.getId());
		
		given(service.modifierEvenement(ev.getId(), pourModif))
			.willReturn(null);
		
		ResponseEntity<Evenement> rep = controller.modifierEvenement(ev.getId(), pourModif,
				util.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerEvent_evOk_attends200()
		throws Exception {
		
		given(service.supprimerEvenement(ev))
			.willReturn(1);
		
		ResponseEntity<Integer> rep = controller.supprimerEvent(ev,
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(1, rep.getBody().intValue());
	}
	
	@Test
	public void supprimerEvent_evNull_attends400()
		throws Exception {
		
		ResponseEntity<Integer> rep = controller.supprimerEvent(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerEvent_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<Integer> rep = controller.supprimerEvent(ev, "Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerEvent_erreurService_attends500()
		throws Exception {
		
		given(service.supprimerEvenement(ev))
			.willReturn(0);
		
		ResponseEntity<Integer> rep = controller.supprimerEvent(ev,
				util.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}

}
