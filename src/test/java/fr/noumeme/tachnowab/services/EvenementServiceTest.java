package fr.noumeme.tachnowab.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import fr.noumeme.tachnowab.models.Evenement;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.EvenementRepository;

@RunWith(SpringRunner.class)
public class EvenementServiceTest {
	
	@TestConfiguration
    static class EvenementServiceTestContextConfiguration {
  
        @Bean
        public EvenementService evenementService() {
            return new EvenementService();
        }
    }
 
    @Autowired
    private EvenementService service;
 
    @MockBean
    private EvenementRepository repository;
    
    private Utilisateur util;
    private Serie serie;
    private Evenement ev;
    
    @Before
    public void setup() {
    	
    	util = new Utilisateur("Test", "Test", "login", "supermdp");
    	serie = new Serie("Titre", "Description", util.getId());
    	ev = new Evenement(ZonedDateTime.now(), 2, "Commentaire", new ArrayList<String>(), serie.getId());
    	
    	Mockito.when(repository.findById(ev.getId()))
    		.thenReturn(Optional.ofNullable(ev));
    	
    	Mockito.when(repository.findAll())
    		.thenReturn(Arrays.asList(ev));
    	
    	Mockito.when(repository.findByIdSerie(serie.getId()))
    		.thenReturn(Arrays.asList(ev));
    }
    
    @Test
    public void getAllEvenement_retournerListEvent() {
    	
    	List<Evenement> evs = service.getAllEvenement();
    	
    	assertNotNull(evs);
    	assertEquals(evs.size(), 1);
    }
    
    @Test
    public void getEvenementById_idExistant() {
    	
    	Optional<Evenement> e = service.getEvenementById(ev.getId());
    	
    	assertEquals(e.get().getClass(), Evenement.class);
    }
    
    @Test
    public void getEvenementById_idInexistant() {
    	
    	Optional<Evenement> e = service.getEvenementById(UUID.randomUUID());
    	
    	assertEquals(e, Optional.empty());
    }
    
    @Test
    public void getEvenementById_idNull() {
    	
    	Optional<Evenement> e = service.getEvenementById(null);
    	
    	assertEquals(e, Optional.empty());
    }
    
    @Test
    public void getEvenementByIdSerie_idExistant() {
    	
    	List<Evenement> evs = service.getEvenementByIdSerie(serie.getId());
    	
    	assertNotNull(evs);
    	assertEquals(evs.size(), 1);
    }
    
    @Test
    public void getEvenementByIdSerie_idInexistant() {
    	
    	List<Evenement> evs = service.getEvenementByIdSerie(UUID.randomUUID());
    	
    	assertNotNull(evs);
    	assertEquals(evs.size(), 0);
    }
    
    @Test
    public void getEvenementByIdSerie_idNull() {
    	
    	List<Evenement> evs = service.getEvenementByIdSerie(null);
    	
    	assertNotNull(evs);
    	assertEquals(evs.size(), 0);
    }
    
    @Test
    public void getEvenementDansInterval_idExistant_intervallePlein() {
    	
    	ZonedDateTime dateDebut = ZonedDateTime.now().minusDays(2);
    	ZonedDateTime dateFin = ZonedDateTime.now().plusDays(2);
    	
    	List<Evenement> evs = service.getEvenementDansInterval(serie.getId(), 
    			dateDebut, dateFin);
    	
    	assertNotNull(evs);
    	assertEquals(evs.size(), 1);
    }
    
    @Test
    public void getEvenementDansInterval_idExistant_intervalVide() {
    	
    	ZonedDateTime dateDebut = ZonedDateTime.now().minusDays(4);
    	ZonedDateTime dateFin = ZonedDateTime.now().minusDays(2);
    	
    	List<Evenement> evs = service.getEvenementDansInterval(serie.getId(), 
    			dateDebut, dateFin);
    	
    	assertNotNull(evs);
    	assertEquals(evs.size(), 0);
    }
    
    @Test
    public void getEvenementDansInterval_idInexistant() {
    	
    	ZonedDateTime dateDebut = ZonedDateTime.now().minusDays(2);
    	ZonedDateTime dateFin = ZonedDateTime.now().plusDays(2);
    	
    	List<Evenement> evs = service.getEvenementDansInterval(UUID.randomUUID(), 
    			dateDebut, dateFin);
    	
    	assertNotNull(evs);
    	assertEquals(evs.size(), 0);
    }
    
    @Test
    public void getEvenementDansInterval_idNull() {
    	ZonedDateTime dateDebut = ZonedDateTime.now().minusDays(2);
    	ZonedDateTime dateFin = ZonedDateTime.now().plusDays(2);
    	
    	List<Evenement> evs = service.getEvenementDansInterval(null, 
    			dateDebut, dateFin);
    	
    	assertNotNull(evs);
    	assertEquals(evs.size(), 0);
    }
    
    @Test
    public void ajouterEvenement_evenementValide() {
    	
    	Evenement pourAjout = new Evenement(ZonedDateTime.now(), 2, "Commentaire 2", new ArrayList<String>(), serie.getId());
    	
    	Evenement ajout = service.ajouterEvenement(pourAjout);
    	
    	assertNotNull(ajout);
    }
    
    @Test
    public void ajouterEvenement_evenementNull() {
    	
    	Evenement ajout = service.ajouterEvenement(null);
    	
    	assertNull(ajout);
    }
    
    @Test
    public void modifierEvenement_idExistant_eventOk() {
    	
    	Evenement pourModif = new Evenement(ZonedDateTime.now(), 2, "Commentaire modif", new ArrayList<String>(), serie.getId());
    	
    	Evenement modif = service.modifierEvenement(ev.getId(), pourModif);
    	
    	assertNotNull(modif);
    	
    	assertEquals(ev.getCommentaire(), pourModif.getCommentaire());
    }
    
    @Test
    public void modifierEvenement_idInexistant_eventOK() {
    	
    	Evenement pourModif = new Evenement(ZonedDateTime.now(), 2, "Commentaire modif", new ArrayList<String>(), serie.getId());
    	
    	Evenement modif = service.modifierEvenement(UUID.randomUUID(), pourModif);
    	
    	assertNull(modif);
    }
    
    @Test
    public void modifierEvenement_idNull_eventOk() {
    	
    	Evenement pourModif = new Evenement(ZonedDateTime.now(), 2, "Commentaire modif", new ArrayList<String>(), serie.getId());
    	
    	Evenement modif = service.modifierEvenement(null, pourModif);
    	
    	assertNull(modif);
    }
    
    @Test
    public void modifierEvenement_idExistant_eventNull() {
    	
    	Evenement modif = service.modifierEvenement(ev.getId(), null);
    	
    	assertNull(modif);
    }
    
    @Test
    public void supprimerEvenement_eventExistant() {
    	
    	int retour = service.supprimerEvenement(ev);
    	
    	assertEquals(retour, 1);
    }
    
    @Test
    public void supprimerEvenement_eventInexistant() {
    	
    	Evenement pourSupp = new Evenement(ZonedDateTime.now(), 2, "Commentaire modif", new ArrayList<String>(), serie.getId());
    	
    	int retour = service.supprimerEvenement(pourSupp);
    	
    	assertEquals(retour, 1);
    }
    
    @Test
    public void supprimerEvenement_eventNull() {
    
    	int retour = service.supprimerEvenement(null);
    	
    	assertEquals(retour, 0);
    }
}
