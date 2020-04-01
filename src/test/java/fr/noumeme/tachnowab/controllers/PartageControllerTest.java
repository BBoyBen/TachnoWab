package fr.noumeme.tachnowab.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.services.PartageService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.* ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class PartageControllerTest {
	
	@Mock
	private PartageService service;
	
	@InjectMocks
	private PartageController controller;
	
	private Utilisateur proprietaire;
	private Utilisateur util;
	private Serie serie;
	private Partage partage;
	
	@Before
	public void setup() {
		
		proprietaire = new Utilisateur("Proprio", "Proprio", "loginproprio", "mdpproprio");
		util = new Utilisateur("Nom", "Prenom", "login", "supermdp");
		serie = new Serie("Titre", "Description", proprietaire.getId());
		partage = new Partage(true, util.getId(), serie.getId());
		
		given(service.getPartageById(partage.getId()))
			.willReturn(Optional.ofNullable(partage));
	}
	
	@Test
	public void getPartageById_idExistant_attends200()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.getPartageById(partage.getId(),
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(Partage.class, rep.getBody().getClass());
		
		assertEquals(partage.getIdSerie(), rep.getBody().getIdSerie());
		assertEquals(partage.getIdUtilisateur(), rep.getBody().getIdUtilisateur());
		assertEquals(partage.isLectureSeule(), rep.getBody().isLectureSeule());
	}
	
	@Test
	public void getPartageById_idInexistant_attends404()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.getPartageById(UUID.randomUUID(),
				util.getId().toString());
		
		assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getPartageById_idNull_attends400()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.getPartageById(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getPartageById_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.getPartageById(partage.getId(),
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getPartageById_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.getPartageById(partage.getId(),
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getAllPartagesBySerie_avecPartage_attends200()
		throws Exception {
		
		given(service.getPartageByIdSerie(serie.getId()))
			.willReturn(Arrays.asList(partage));
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesBySerie(serie.getId(),
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(1, rep.getBody().size());
	}
	
	@Test
	public void getAllPartagesBySerie_sansPartage_attends204()
		throws Exception {
		
		Serie sansPartage = new Serie("Vide", "Vide", util.getId());
		
		given(service.getPartageByIdSerie(sansPartage.getId()))
			.willReturn(new ArrayList<Partage>());
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesBySerie(sansPartage.getId(),
				util.getId().toString());
		
		assertEquals(HttpStatus.NO_CONTENT, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getAllPartagesBySerie_idNull_attends400()
		throws Exception {
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesBySerie(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getAllPartagesBySerie_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesBySerie(serie.getId(),
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getAllPartagesBySerie_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesBySerie(serie.getId(),
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getAllPartagesByUser_utilAvecPartage_attends200()
		throws Exception {
		
		given(service.getPartagesByUtil(util.getId()))
			.willReturn(Arrays.asList(partage));
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesByUser(util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(1, rep.getBody().size());
	}
	
	@Test
	public void getAllPartagesByUser_utilSansPartage_attends204()
		throws Exception {
		
		given(service.getPartagesByUtil(proprietaire.getId()))
			.willReturn(new ArrayList<Partage>());
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesByUser(proprietaire.getId().toString());
		
		assertEquals(HttpStatus.NO_CONTENT, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getAllPartagesByUser_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesByUser("Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getAllPartagesByUser_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<List<Partage>> rep = controller.getAllPartagesByUser("pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getPartageByUserAndSerie_avecPartage_attends200()
		throws Exception {
		
		given(service.getPartageByUtilAndBySerie(util.getId(), serie.getId()))
			.willReturn(Optional.ofNullable(partage));
		
		ResponseEntity<Partage> rep = controller.getPartageByUserAndSerie(serie.getId(),
				util.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(Partage.class, rep.getBody().getClass());
		
		assertEquals(partage.isLectureSeule(), rep.getBody().isLectureSeule());
		assertEquals(partage.getIdSerie(), rep.getBody().getIdSerie());
		assertEquals(partage.getIdUtilisateur(), rep.getBody().getIdUtilisateur());
	}
	
	@Test
	public void getPartageByUserAndSerie_sansPartage_attends404()
		throws Exception {
		
		given(service.getPartageByUtilAndBySerie(proprietaire.getId(), serie.getId()))
			.willReturn(Optional.empty());
		
		ResponseEntity<Partage> rep = controller.getPartageByUserAndSerie(serie.getId(),
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getPartageByUserAndSerie_idNull_attends400()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.getPartageByUserAndSerie(null,
				util.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getPartageByUserAndSerie_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.getPartageByUserAndSerie(serie.getId(),
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void getPartageByUserAndSerie_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.getPartageByUserAndSerie(serie.getId(),
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterPartage_partageOk_attends201()
		throws Exception {
		
		Utilisateur encoreUn = new Utilisateur("Encore", "Unautre", "autrelogin", "encoreunmdp");
		Partage pourAjout = new Partage(false, encoreUn.getId(), serie.getId());
		
		given(service.ajouterPartage(pourAjout))
			.willReturn(pourAjout);
		
		ResponseEntity<Partage> rep = controller.ajouterPartage(pourAjout,
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.CREATED, rep.getStatusCode());
		assertEquals(Partage.class, rep.getBody().getClass());
		
		assertEquals(pourAjout.getIdSerie(), rep.getBody().getIdSerie());
		assertEquals(pourAjout.getIdUtilisateur(), rep.getBody().getIdUtilisateur());
		assertEquals(pourAjout.isLectureSeule(), rep.getBody().isLectureSeule());
	}
	
	@Test
	public void ajouterPartage_partageNull_attends400()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.ajouterPartage(null,
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterPartage_cookieKo_attends401()
		throws Exception {
		
		Utilisateur encoreUn = new Utilisateur("Encore", "Unautre", "autrelogin", "encoreunmdp");
		Partage pourAjout = new Partage(false, encoreUn.getId(), serie.getId());
		
		ResponseEntity<Partage> rep = controller.ajouterPartage(pourAjout,
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterPartage_cookiePasBonFormat_attends401()
		throws Exception {
		
		Utilisateur encoreUn = new Utilisateur("Encore", "Unautre", "autrelogin", "encoreunmdp");
		Partage pourAjout = new Partage(false, encoreUn.getId(), serie.getId());
		
		ResponseEntity<Partage> rep = controller.ajouterPartage(pourAjout,
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void ajouterPartage_erreurService_attends500()
		throws Exception {
		
		Utilisateur encoreUn = new Utilisateur("Encore", "Unautre", "autrelogin", "encoreunmdp");
		Partage pourAjout = new Partage(false, encoreUn.getId(), serie.getId());
		
		given(service.ajouterPartage(pourAjout))
			.willReturn(null);
		
		ResponseEntity<Partage> rep = controller.ajouterPartage(pourAjout,
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierPartage_idExistant_attends200()
		throws Exception {
		
		Partage modif = partage;
		modif.setLectureSeule(false);
		
		given(service.modifierPartage(partage.getId()))
			.willReturn(modif);
		
		ResponseEntity<Partage> rep = controller.modifierPartage(partage.getId(),
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(Partage.class, rep.getBody().getClass());
		
		assertEquals(modif.isLectureSeule(), rep.getBody().isLectureSeule());
	}
	
	@Test
	public void modifierPartage_idInexistant_attends404()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.modifierPartage(UUID.randomUUID(),
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierPartage_idNull_attends400()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.modifierPartage(null,
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierPartage_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.modifierPartage(partage.getId(),
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierPartage_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<Partage> rep = controller.modifierPartage(partage.getId(),
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void modifierPartage_erreurService_attends500()
		throws Exception {
		
		given(service.modifierPartage(partage.getId()))
			.willReturn(null);
		
		ResponseEntity<Partage> rep = controller.modifierPartage(partage.getId(),
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerPartage_partageOk_attends200()
		throws Exception {
		
		given(service.supprimerPartage(partage))
			.willReturn(1);
		
		ResponseEntity<Integer> rep = controller.supprimerPartage(partage,
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.OK, rep.getStatusCode());
		assertEquals(1, rep.getBody().intValue());
	}
	
	@Test
	public void supprimerPartage_partageNull_attends400()
		throws Exception {
		
		ResponseEntity<Integer> rep = controller.supprimerPartage(null,
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerPartage_cookieKo_attends401()
		throws Exception {
		
		ResponseEntity<Integer> rep = controller.supprimerPartage(partage,
				"Atta");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerPartage_cookiePasBonFormat_attends401()
		throws Exception {
		
		ResponseEntity<Integer> rep = controller.supprimerPartage(partage,
				"pasbonformat");
		
		assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
		assertNull(rep.getBody());
	}
	
	@Test
	public void supprimerPartage_erreurService_attends500()
		throws Exception {
		
		given(service.supprimerPartage(partage))
			.willReturn(0);
		
		ResponseEntity<Integer> rep = controller.supprimerPartage(partage,
				proprietaire.getId().toString());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rep.getStatusCode());
		assertNull(rep.getBody());
	}
}