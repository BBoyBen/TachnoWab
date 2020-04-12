package fr.noumeme.tachnowab.controllers;

import java.nio.charset.StandardCharsets;
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

import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.services.SeriesService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.* ;

@RunWith(MockitoJUnitRunner.class)
public class SeriesControllerTest {
	
	@Mock
	private SeriesService service;
	
	@InjectMocks
	private SeriesController controller;
	
	private Utilisateur util;
	private Serie serie;
	
	@Before
	public void setup() {
		
    	String mdpEncode = Hashing.sha256()
				  .hashString("supermdp", StandardCharsets.UTF_8)
				  .toString();
		
    	util = new Utilisateur("Test", "Test", "login", mdpEncode);
    	serie = new Serie("Titre", "Description", util.getId());
    	
    	given(service.getSerieById(serie.getId()))
    		.willReturn(Optional.ofNullable(serie));
	}
	
	@Test
	public void toutesLesSeries_seriePresente_attends200()
		throws Exception {
		
    	given(service.getAllSeries())
			.willReturn(Arrays.asList(serie));
    	
    	ResponseEntity<List<Serie>> rep = controller.toutesLesSeries(util.getId().toString());
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(1, rep.getBody().size());
	}
	
	@Test
	public void toutesLesSeries_pasDeSerie_attends204()
		throws Exception {
		
		ResponseEntity<List<Serie>> rep = controller.toutesLesSeries(util.getId().toString());
		
		assertEquals(HttpStatus.NO_CONTENT, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void toutesLesSeries_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<List<Serie>> rep = controller.toutesLesSeries("Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void toutesLesSeries_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<List<Serie>> rep = controller.toutesLesSeries("pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void serieById_idExistant_attends200()
		throws Exception {
		
		ResponseEntity<Serie> rep = controller.serieById(serie.getId(),
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(Serie.class, rep.getBody().getClass());
		
		assertEquals(serie.getTitre(), rep.getBody().getTitre());
		assertEquals(serie.getDescription(), rep.getBody().getDescription());
	}
	
	@Test
	public void serieById_idInexistant_attends404()
		throws Exception {
		
		ResponseEntity<Serie> rep = controller.serieById(UUID.randomUUID(),
				util.getId().toString());
		
		assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void serieById_idNull_attends400()
		throws Exception {
		
		ResponseEntity<Serie> rep = controller.serieById(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void serieById_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<Serie> rep = controller.serieById(serie.getId(),
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void serieById_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<Serie> rep = controller.serieById(serie.getId(),
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getSeriesByUser_userAvecSerie_attends200()
		throws Exception {
		
    	given(service.getSeriesByUser(util.getId()))
			.willReturn(Arrays.asList(serie));
    	
    	ResponseEntity<List<Serie>> rep = controller.getSeriesByUser(util.getId().toString());
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(1, rep.getBody().size());
	}
	
	@Test
	public void getSeriesByUser_userSansSerie_attends204()
		throws Exception {
		
		Utilisateur sansSerie = new Utilisateur("Vide", "Rien", "nada", "vraimentRien");
		given(service.getSeriesByUser(sansSerie.getId()))
			.willReturn(new ArrayList<Serie>());
		
		ResponseEntity<List<Serie>> rep = controller.getSeriesByUser(sansSerie.getId().toString());
		
		assertEquals(HttpStatus.NO_CONTENT, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getSeriesByUser_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<List<Serie>> rep = controller.getSeriesByUser("Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getSeriesByUser_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<List<Serie>> rep = controller.getSeriesByUser("pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterNouvelleSerie_serieOk_attends201()
		throws Exception {
		
		Serie pourAjout = new Serie("Nouveau", "Des", util.getId());
		given(service.ajouterSerie(pourAjout)).willReturn(pourAjout);
		
		ResponseEntity<Serie> rep = controller.ajouterNouvelleSerie(pourAjout,
				util.getId().toString());
		
		assertEquals(HttpStatus.CREATED, rep.getStatusCode());
		assertEquals(Serie.class, rep.getBody().getClass());
		
		assertEquals(pourAjout.getTitre(), rep.getBody().getTitre());
		assertEquals(pourAjout.getDescription(), rep.getBody().getDescription());
	}
	
	@Test
	public void ajouterNouvelleSerie_serieNull_attends400()
		throws Exception {
		
		ResponseEntity<Serie> rep = controller.ajouterNouvelleSerie(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterNouvelleSerie_erreurService_attends500()
		throws Exception {
		
		Serie pourAjout = new Serie("Erreur", "Erreur", util.getId());
		given(service.ajouterSerie(pourAjout)).willReturn(null);
		
		ResponseEntity<Serie> rep = controller.ajouterNouvelleSerie(pourAjout,
				util.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterNouvelleSerie_cookieKo_attends401()
		throws Exception {
		
		Serie pourAjout = new Serie("Titre", "Description", util.getId());
		
		ResponseEntity<Serie> rep = controller.ajouterNouvelleSerie(pourAjout,
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterNouvelleSerie_cookiePasBonFormat_attends401()
		throws Exception {
		
		Serie pourAjout = new Serie("Titre", "Description", util.getId());
		
		ResponseEntity<Serie> rep = controller.ajouterNouvelleSerie(pourAjout,
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierSerie_serieOk_attends200()
		throws Exception {
		
		Serie pourModif = new Serie("Modif", "Modif", util.getId());
		given(service.modifierSerie(serie.getId(), pourModif)).willReturn(pourModif);
		
		ResponseEntity<Serie> rep = controller.modifierSerie(serie.getId(), pourModif,
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(Serie.class, rep.getBody().getClass());
		
		assertEquals(pourModif.getTitre(), rep.getBody().getTitre());
		assertEquals(pourModif.getDescription(), rep.getBody().getDescription());
		assertEquals(pourModif.getIdUtilisateur(), rep.getBody().getIdUtilisateur());
	}
	
	@Test
	public void modifierSerie_mauvaisUtilisateur_attends401()
		throws Exception {
		
		Serie pourModif = new Serie("Modif", "Modif", util.getId());
		
		ResponseEntity<Serie> rep = controller.modifierSerie(serie.getId(), pourModif,
				UUID.randomUUID().toString());
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierSerie_serieNull_attends400()
		throws Exception {
		
		ResponseEntity<Serie> rep = controller.modifierSerie(serie.getId(), null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierSerie_idInexistant_attends404()
		throws Exception {
		
		Serie pourModif = new Serie("Modif", "Modif", util.getId());
		
		ResponseEntity<Serie> rep = controller.modifierSerie(UUID.randomUUID(), pourModif,
				util.getId().toString());
		
		assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierSerie_idNull_attends400()
		throws Exception {
		
		Serie pourModif = new Serie("Modif", "Modif", util.getId());
		
		ResponseEntity<Serie> rep = controller.modifierSerie(null, pourModif,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierSerie_erreurService_attends500()
		throws Exception {
		
		Serie pourModif = new Serie("Erreur", "Erreur", util.getId());
		given(service.modifierSerie(serie.getId(), pourModif)).willReturn(null);
		
		ResponseEntity<Serie> rep = controller.modifierSerie(serie.getId(), pourModif,
				util.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierSerie_cookieKo_attends401() 
		throws Exception {
		
		Serie pourModif = new Serie("Modif", "Modif", util.getId());
		
		ResponseEntity<Serie> rep = controller.modifierSerie(serie.getId(), pourModif,
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierSerie_cookiePasBonFormat_attends401() 
		throws Exception {
		
		Serie pourModif = new Serie("Modif", "Modif", util.getId());
		
		ResponseEntity<Serie> rep = controller.modifierSerie(serie.getId(), pourModif,
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerSerie_serieOk_attends200()
		throws Exception {
		
		Serie toSupp = new Serie("Supp", "Supp", util.getId());
		when(service.getSerieById(toSupp.getId())).thenReturn(Optional.of(toSupp));
		given(service.supprimerSerie(toSupp)).willReturn(1);
		
		ResponseEntity<Integer> rep = controller.supprimerSerie(toSupp.getId(), util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(1, rep.getBody().intValue());
	}
	
	@Test
	public void supprimerSerie_serieNull_attends400()
		throws Exception {
		
		ResponseEntity<Integer> rep = controller.supprimerSerie(null, util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}

	@Test
	public void supprimerSerie_newUuid_attends404()
		throws Exception {
		
		ResponseEntity<Integer> rep = controller.supprimerSerie(UUID.randomUUID(), util.getId().toString());
		
		assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerSerie_cookieKo_attends401()
		throws Exception {
		
		Serie toSupp = new Serie("Supp", "Supp", util.getId());
		
		ResponseEntity<Integer> rep = controller.supprimerSerie(toSupp.getId(), "Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerSerie_cookiePasBonFormat_attends401()
		throws Exception {
		
		Serie toSupp = new Serie("Nouveau", "Des", util.getId());
		when(service.getSerieById(toSupp.getId())).thenReturn(Optional.of(toSupp));
		ResponseEntity<Integer> rep = controller.supprimerSerie(toSupp.getId(), "pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerSerie_pasBonUtil_attends401()
		throws Exception {
		
		Serie toSupp = new Serie("Supp", "Supp", util.getId());
		when(service.getSerieById(toSupp.getId())).thenReturn(Optional.of(toSupp));

		ResponseEntity<Integer> rep = controller.supprimerSerie(toSupp.getId(), UUID.randomUUID().toString());
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerSerie_erreurService_attends500()
		throws Exception {
		
		Serie toSupp = new Serie("Supp", "Supp", util.getId());
		when(service.getSerieById(toSupp.getId())).thenReturn(Optional.of(toSupp));
		given(service.supprimerSerie(toSupp)).willReturn(0);
		
		ResponseEntity<Integer> rep = controller.supprimerSerie(toSupp.getId(), util.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}
}
