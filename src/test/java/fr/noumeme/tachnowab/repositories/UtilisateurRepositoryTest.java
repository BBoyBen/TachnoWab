package fr.noumeme.tachnowab.repositories;

import static org.junit.Assert.assertEquals;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import fr.noumeme.tachnowab.models.Utilisateur;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UtilisateurRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private UtilisateurRepository repository;
    
    private Utilisateur util;
    
    @Before
    public void setData() {
    	util = new Utilisateur("Test", "Test", "supertest", "supermdp");
    	entityManager.persist(util);
    	entityManager.flush();
    }
    
    @Test
    public void findByLoginAndByMotDePasse_toutEstOk_UtilisateurNonNull() {
    	
    	Optional<Utilisateur> trouve = repository.findByLoginAndByMotDePasse(util.getLogin(), util.getMotDePasse());
    	
    	assertEquals(trouve.get().getClass(), Utilisateur.class);
    }
    
    @Test
    public void findByLoginAndByMotDePasse_loginKo_utilisateurNull() {
    	
    	Optional<Utilisateur> trouve = repository.findByLoginAndByMotDePasse("loginKO", util.getMotDePasse());
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void findByLoginAndByMotDePasse_MdpKo_utilisateurNull() {
    	
    	Optional<Utilisateur> trouve = repository.findByLoginAndByMotDePasse(util.getLogin(), "mdpKO");
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void findByLoginAndByMotDePasse_toutEstKo_utilisateurNull() {
    	
    	Optional<Utilisateur> trouve = repository.findByLoginAndByMotDePasse("loginKO", "mdpKO");
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void findByLogin_loginExistant_utilisateurNonNull() {
    	
    	Optional<Utilisateur> trouve = repository.findByLogin(util.getLogin());
    	
    	assertEquals(trouve.get().getClass(), Utilisateur.class);
    }
    
    @Test
    public void findByLogin_loginNonExistant_utilisateurNull() {
    	
    	Optional<Utilisateur> trouve = repository.findByLogin("existepas");
    	
    	assertEquals(trouve, Optional.empty());
    }

}
