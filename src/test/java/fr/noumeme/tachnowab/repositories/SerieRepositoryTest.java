package fr.noumeme.tachnowab.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SerieRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private SerieRepository repository;
    
    private Utilisateur util;
    
    @Before
    public void setData() {
    	util = new Utilisateur("Test", "Test", "supertest", "supermdp");
    	entityManager.persist(util);
    	entityManager.flush();
    }
    
    @Test
    public void findByIdUtilisateur_idOk_listeRemplie() {
    	
    	Serie serie = new Serie("Serie de test", "Une serie de test", util.getId());
    	entityManager.persist(serie);
    	entityManager.flush();
    	
    	List<Serie> series = repository.findByIdUtilisateur(util.getId());
    	
    	assertNotNull(series);
    	assertEquals(series.size(), 1);
    }
    
    @Test
    public void findByIdUtilisateur_idOk_listeVide() {
    	
    	List<Serie> series = repository.findByIdUtilisateur(util.getId());
    	
    	assertNotNull(series);
    	assertEquals(series.size(), 0);
    }
    
    @Test
    public void findByIdUtilisateur_idKo() {
    	
    	List<Serie> series = repository.findByIdUtilisateur(UUID.randomUUID());
    	
    	assertNotNull(series);
    	assertEquals(series.size(), 0);
    	
    }
}
