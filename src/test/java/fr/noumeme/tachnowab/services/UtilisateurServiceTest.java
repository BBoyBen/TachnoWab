package fr.noumeme.tachnowab.services;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
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

import com.google.common.hash.Hashing;

import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.UtilisateurRepository;

@RunWith(SpringRunner.class)
public class UtilisateurServiceTest {
	
	@TestConfiguration
    static class UtilisateurServiceTestContextConfiguration {
  
        @Bean
        public UtilisateurService employeeService() {
            return new UtilisateurService();
        }
    }
 
    @Autowired
    private UtilisateurService service;
 
    @MockBean
    private UtilisateurRepository repository;
    
    private Utilisateur util;
    
    @Before
    public void setup() {
    	
    	String mdpEncode = Hashing.sha256()
				  .hashString("supermdp", StandardCharsets.UTF_8)
				  .toString();
    	
    	util = new Utilisateur("Test", "Test", "supertest", mdpEncode);
    	
    	Mockito.when(repository.findById(util.getId()))
    		.thenReturn(Optional.ofNullable(util));
    	
    	Mockito.when(repository.findByLogin(util.getLogin()))
    		.thenReturn(Optional.ofNullable(util));
    	
    	Mockito.when(repository.findByLoginAndByMotDePasse(util.getLogin(), mdpEncode))
    		.thenReturn(Optional.ofNullable(util));
    	
    }
    
    @Test
    public void getUtilisateurById_idOk() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurById(util.getId());
    	
    	assertEquals(trouve.get().getClass(), Utilisateur.class);
    }
    
    @Test
    public void getUtilisateurById_idKo() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurById(UUID.randomUUID());
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    

}
