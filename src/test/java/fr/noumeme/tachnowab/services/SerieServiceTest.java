package fr.noumeme.tachnowab.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.SerieRepository;

@RunWith(SpringRunner.class)
public class SerieServiceTest {
	
	@TestConfiguration
    static class SerieServiceTestContextConfiguration {
  
        @Bean
        public SeriesService seriesService() {
            return new SeriesService();
        }
    }
 
    @Autowired
    private SeriesService service;
 
    @MockBean
    private SerieRepository repository;
    
    private Serie serie;
    private Utilisateur util;
    
    @Before
    public void setup() {
    	
    	util = new Utilisateur("Test", "Test", "login", "supermdp");
    	serie = new Serie("Titre", "Description", util.getId());
    	
    	Mockito.when(repository.findById(serie.getId()))
    		.thenReturn(Optional.ofNullable(serie));
    	
    	Mockito.when(repository.findAll())
    		.thenReturn(Arrays.asList(serie));
    	
    	Mockito.when(repository.findByIdUtilisateur(util.getId()))
    		.thenReturn(Arrays.asList(serie));
    }
    
    @Test
    public void getAllSeries_retourneListeDeSerie() {
    	
    	List<Serie> series = service.getAllSeries();
    	
    	assertNotNull(series);
    	assertEquals(series.size(), 1);
    }
    
    @Test
    public void getSerieById_idExistant() {
    	
    	Optional<Serie> trouve = service.getSerieById(serie.getId());
    	
    	assertEquals(trouve.get().getClass(), Serie.class);
    }
    
    @Test
    public void getSerieById_idInexistant() {
    	
    	Optional<Serie> trouve = service.getSerieById(UUID.randomUUID());
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void getSerieById_idNull() {
    	
    	Optional<Serie> trouve = service.getSerieById(null);
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void getSeriesByUser_idAvecSerie() {
    	
    	List<Serie> trouve = service.getSeriesByUser(util.getId());
    	
    	assertNotNull(trouve);
    	assertEquals(trouve.size(), 1);
    }
    
    @Test
    public void getSeriesByUser_idSansSerie() {
    	
    	List<Serie> trouve = service.getSeriesByUser(UUID.randomUUID());
    	
    	assertNotNull(trouve);
    	assertEquals(trouve.size(), 0);
    }
    
    @Test
    public void getSeriesByUser_idNull() {
    	
    	List<Serie> trouve = service.getSeriesByUser(null);
    	
    	assertNotNull(trouve);
    	assertEquals(trouve.size(), 0);
    }
    
    @Test
    public void ajouterSerie_serieOk() {
    	
    	Serie pourAjout = new Serie("Titre", "Description", util.getId());
    	
    	Serie ajout = service.ajouterSerie(pourAjout);
    	
    	assertNotNull(ajout);
    }
    
    @Test
    public void ajouterSerie_serieNull() {
    	
    	Serie ajout = service.ajouterSerie(null);
    	
    	assertNull(ajout);
    }
    
    @Test
    public void modifierSerie_idExistant_serieOk() {
    	
    	Serie pourModif = new Serie("nouveau titre", "nouvelle description", util.getId());
    	
    	Serie modif = service.modifierSerie(serie.getId(), pourModif);
    	
    	assertNotNull(modif);
    	
    	assertEquals(modif.getTitre(), pourModif.getTitre());
    	assertEquals(modif.getDescription(), pourModif.getDescription());
    }
    
    @Test
    public void modifierSerie_idExistant_serieNull() {
    	
    	Serie modif = service.modifierSerie(serie.getId(), null);
    	
    	assertNull(modif);
    }
    
    @Test
    public void modifierSerie_idInexistant_serieOk() {
    	
    	Serie pourModif = new Serie("nouveau titre", "nouvelle description", util.getId());
    	
    	Serie modif = service.modifierSerie(UUID.randomUUID(), pourModif);
    	
    	assertNull(modif);
    }
    
    @Test
    public void modifierSerie_idInexistant_serieNull() {
    	
    	Serie modif = service.modifierSerie(UUID.randomUUID(), null);
    	
    	assertNull(modif);
    }
    
    @Test
    public void supprimerSerie_serieExistante() {
    	
    	int retour = service.supprimerSerie(serie);
    	
    	assertEquals(retour, 1);
    }
    
    @Test
    public void supprimerSerie_serieInexistante() {
    	
    	Serie aSupp = new Serie ("Titre", "Description", util.getId());
    	
    	int retour = service.supprimerSerie(aSupp);
    	
    	assertEquals(retour, 1);
    }
    
    @Test
    public void supprimerSerie_serieNull() {
    	
    	int retour = service.supprimerSerie(null);
    	
    	assertEquals(retour, 0);
    }

}
