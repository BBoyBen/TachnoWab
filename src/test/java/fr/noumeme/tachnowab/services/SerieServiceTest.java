package fr.noumeme.tachnowab.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import fr.noumeme.tachnowab.dtos.SerieDto;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.SerieRepository;

@RunWith(MockitoJUnitRunner.class)
public class SerieServiceTest {
	
    @Mock
    private SerieRepository repository;
 
    @InjectMocks
    private SeriesService service;
    
    private Serie serie;
    private Utilisateur util;
    
    @Before
    public void setup() {
    	
    	util = new Utilisateur("Test", "Test", "login", "supermdp");
    	serie = new Serie("Titre", "Description", util.getId());
    	
    	when(repository.findById(serie.getId()))
    		.thenReturn(Optional.ofNullable(serie));
    	
    	when(repository.findAll())
    		.thenReturn(Arrays.asList(serie));
    	
    	when(repository.findByIdUtilisateur(util.getId()))
    		.thenReturn(Arrays.asList(serie));
    }
    
    @Test
    public void getAllSeries_retourneListeDeSerie() {
    	
    	List<Serie> series = service.getAllSeries();
    	
    	assertNotNull(series);
    	assertEquals(1, series.size());
    }
    
    @Test
    public void getSerieById_idExistant() {
    	
    	Optional<Serie> trouve = service.getSerieById(serie.getId());
    	
    	assertEquals(Serie.class, trouve.get().getClass());
    }
    
    @Test
    public void getSerieById_idInexistant() {
    	
    	Optional<Serie> trouve = service.getSerieById(UUID.randomUUID());
    	
    	assertEquals(Optional.empty(), trouve);
    }
    
    @Test
    public void getSerieById_idNull() {
    	
    	Optional<Serie> trouve = service.getSerieById(null);
    	
    	assertEquals(Optional.empty(), trouve);
    }
    
    @Test
    public void getSeriesByUser_idAvecSerie() {
    	
    	List<Serie> trouve = service.getSeriesByUser(util.getId());
    	
    	assertNotNull(trouve);
    	assertEquals(1, trouve.size());
    }
    
    @Test
    public void getSeriesByUser_idSansSerie() {
    	
    	List<Serie> trouve = service.getSeriesByUser(UUID.randomUUID());
    	
    	assertNotNull(trouve);
    	assertEquals(0, trouve.size());
    }
    
    @Test
    public void getSeriesByUser_idNull() {
    	
    	List<Serie> trouve = service.getSeriesByUser(null);
    	
    	assertNotNull(trouve);
    	assertEquals(0, trouve.size());
    }
    
    @Test
    public void ajouterSerie_serieOk() {
    	
    	SerieDto pourAjout = new SerieDto("Titre", "Description", util.getId());
    	
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
    	
    	SerieDto pourModif = new SerieDto("nouveau titre", "nouvelle description", util.getId());
    	
    	Serie modif = service.modifierSerie(serie.getId(), pourModif);
    	
    	assertNotNull(modif);
    	
    	assertEquals(pourModif.getTitre(), modif.getTitre());
    	assertEquals(pourModif.getDescription(), modif.getDescription());
    }
    
    @Test
    public void modifierSerie_idExistant_serieNull() {
    	
    	Serie modif = service.modifierSerie(serie.getId(), null);
    	
    	assertNull(modif);
    }
    
    @Test
    public void modifierSerie_idInexistant_serieOk() {
    	
    	SerieDto pourModif = new SerieDto("nouveau titre", "nouvelle description", util.getId());
    	
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
    	
    	assertEquals(1, retour);
    }
    
    @Test
    public void supprimerSerie_serieInexistante() {
    	
    	Serie aSupp = new Serie ("Titre", "Description", util.getId());
    	
    	int retour = service.supprimerSerie(aSupp);
    	
    	assertEquals(1, retour);
    }
    
    @Test
    public void supprimerSerie_serieNull() {
    	
    	int retour = service.supprimerSerie(null);
    	
    	assertEquals(0, retour);
    }
}