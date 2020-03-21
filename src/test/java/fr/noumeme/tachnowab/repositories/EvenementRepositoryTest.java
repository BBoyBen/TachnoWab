package fr.noumeme.tachnowab.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import fr.noumeme.tachnowab.models.Evenement;
import fr.noumeme.tachnowab.models.Serie;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EvenementRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private EvenementRepository repository;
    
    private Serie serie;
    
    @Before
    public void setData() {
    	serie = new Serie("Serie de test", "une serie pour les test", UUID.randomUUID());
    	entityManager.persist(serie);
    	entityManager.flush();
    }
    
    @Test
    public void findByIdSerie_idOk_listeRemplie() {
    	
    	Evenement ev = new Evenement(ZonedDateTime.now(), 10, "Ceci est un commentaire", new ArrayList<String>(), serie.getId());
    	entityManager.persist(ev);
    	entityManager.flush();
    	
    	List<Evenement> evenements = repository.findByIdSerie(serie.getId());
    	
    	assertNotNull(evenements);
    	assertEquals(evenements.size(), 1);
    }
    
    @Test
    public void findByIdSerie_idOk_listeVide() {
    	
    	List<Evenement> evenements = repository.findByIdSerie(serie.getId());
    	
    	assertNotNull(evenements);
    	assertEquals(evenements.size(), 0);
    }
    
    @Test
    public void findByIdSerie_idKo() {
    	
    	List<Evenement> evenements = repository.findByIdSerie(UUID.randomUUID());
    	
    	assertNotNull(evenements);
    	assertEquals(evenements.size(), 0);
    }
}
