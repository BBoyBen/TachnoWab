package fr.noumeme.tachnowab.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PartageRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private PartageRepository repository;
    
    private Utilisateur util;
    private Serie serie;
    
    @Before
    public void setData() {
    	util = new Utilisateur("Test", "Test", "supertest", "supermdp");
    	entityManager.persist(util);
    	entityManager.flush();
    	
    	serie = new Serie("Serie de test", "Une serie de test", util.getId());
    	entityManager.persist(serie);
    	entityManager.flush();
    }
    
    @Test
    public void findAllByIdUtilisateur_idOk_listeRemplie() {
    	
    	Partage partage = new Partage(true, util.getId(), serie.getId());
    	entityManager.persist(partage);
    	entityManager.flush();
    	
    	List<Partage> partages = repository.findAllByIdUtilisateur(util.getId());
    	
    	assertNotNull(partages);
    	assertEquals(partages.size(), 1);
    }
    
    @Test
    public void findAllByIdUtilisateur_idOk_listeVide() {
    	
    	List<Partage> partages = repository.findAllByIdUtilisateur(util.getId());
    	
    	assertNotNull(partages);
    	assertEquals(partages.size(), 0);
    }
    
    @Test
    public void findAllByIdUtilisateur_idKo() {
    	
    	List<Partage> partages = repository.findAllByIdUtilisateur(UUID.randomUUID());
    	
    	assertNotNull(partages);
    	assertEquals(partages.size(), 0);
    }
    
    @Test
    public void findAllByIdSerie_idOk_listeRemplie() {
    	
    	Partage partage = new Partage(true, util.getId(), serie.getId());
    	entityManager.persist(partage);
    	entityManager.flush();
    	
    	List<Partage> partages = repository.findAllByIdSerie(serie.getId());
    	
    	assertNotNull(partages);
    	assertEquals(partages.size(), 1);
    }
    
    @Test
    public void findAllByIdSerie_idOk_listeVide() {
    	
    	List<Partage> partages = repository.findAllByIdSerie(serie.getId());
    	
    	assertNotNull(partages);
    	assertEquals(partages.size(), 0);
    }
    
    @Test
    public void findAllByIdSerie_idKo() {
    	
    	List<Partage> partages = repository.findAllByIdSerie(UUID.randomUUID());
    	
    	assertNotNull(partages);
    	assertEquals(partages.size(), 0);
    }
    
    @Test
    public void findByIdUtilisateurAndByIdSerie_idUtilOk_idSerieOk() {
    	
    	Partage partage = new Partage(true, util.getId(), serie.getId());
    	entityManager.persist(partage);
    	entityManager.flush();
    	
    	Optional<Partage> trouve = repository.findByIdUtilisateurAndByIdSerie(util.getId(), serie.getId());
    	
    	assertEquals(trouve.get().getClass(), Partage.class);
    }
    
    @Test
    public void findByIdUtilisateurAndByIdSerie_idUtilKo_idSerieOk() {
    	
    	Partage partage = new Partage(true, util.getId(), serie.getId());
    	entityManager.persist(partage);
    	entityManager.flush();
    	
    	Optional<Partage> trouve = repository.findByIdUtilisateurAndByIdSerie(UUID.randomUUID(), serie.getId());
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void findByIdUtilisateurAndByIdSerie_idUtilOk_idSerieKo() {
    	
    	Partage partage = new Partage(true, util.getId(), serie.getId());
    	entityManager.persist(partage);
    	entityManager.flush();
    	
    	Optional<Partage> trouve = repository.findByIdUtilisateurAndByIdSerie(util.getId(), UUID.randomUUID());
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void findByIdUtilisateurAndByIdSerie_udUtilKo_idSerieKo() {
    	
    	Partage partage = new Partage(true, util.getId(), serie.getId());
    	entityManager.persist(partage);
    	entityManager.flush();
    	
    	Optional<Partage> trouve = repository.findByIdUtilisateurAndByIdSerie(UUID.randomUUID(), UUID.randomUUID());
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void findByIdUtilisateurAndByIdSerie_idUtilOk_idSerieOk_partageInexistant() {
    	
    	Optional<Partage> trouve = repository.findByIdUtilisateurAndByIdSerie(util.getId(), serie.getId());
    	
    	assertEquals(trouve, Optional.empty());
    }

}
