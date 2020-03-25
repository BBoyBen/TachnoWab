package fr.noumeme.tachnowab.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
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

import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.PartageRepository;

@RunWith(SpringRunner.class)
public class PartageServiceTest {
	
	@TestConfiguration
    static class PartageServiceTestContextConfiguration {
  
        @Bean
        public PartageService partageService() {
            return new PartageService();
        }
    }
 
    @Autowired
    private PartageService service;
 
    @MockBean
    private PartageRepository repository;
    
    private Utilisateur auteur;
    private Utilisateur util;
    private Serie serie;
    private Partage partage;
    
    @Before
    public void setup() {
    	
    	auteur = new Utilisateur("Test", "Test", "login", "supermdp");
    	util = new Utilisateur("Test 2", "Test 2", "login", "mdp");
    	serie = new Serie("Titre", "description", auteur.getId());
    	partage = new Partage(false, util.getId(), serie.getId());
    	
    	Mockito.when(repository.findById(partage.getId()))
    		.thenReturn(Optional.ofNullable(partage));
    	
    	Mockito.when(repository.findAllByIdSerie(serie.getId()))
    		.thenReturn(Arrays.asList(partage));
    	
    	Mockito.when(repository.findAllByIdUtilisateur(util.getId()))
    		.thenReturn(Arrays.asList(partage));
    	
    	Mockito.when(repository.findByIdUtilisateurAndByIdSerie(util.getId(), serie.getId()))
    		.thenReturn(Optional.ofNullable(partage));
    }
    
    @Test
    public void getPartageById_idExistant() {
    	
    	Optional<Partage> p = service.getPartageById(partage.getId());
    	
    	assertEquals(p.get().getClass(), Partage.class);
    }
    
    @Test
    public void getPartageById_idInexistant() {
    	
    	Optional<Partage> p = service.getPartageById(UUID.randomUUID());
    	
    	assertEquals(p, Optional.empty());
    }
    
    @Test
    public void getPartageById_idNull() {
    	
    	Optional<Partage> p = service.getPartageById(null);
    	
    	assertEquals(p, Optional.empty());
    }

}
