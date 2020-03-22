package fr.noumeme.tachnowab.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    
    @Test
    public void getUtilisateurByLogin_loginExistant() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurByLogin(util.getLogin());
    	
    	assertEquals(trouve.get().getClass(), Utilisateur.class);
    }
    
    @Test
    public void getUtilisateurByLogin_loginInexistant() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurByLogin("existepas");
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void authentifieUtilisateur_toutOk() {
    	
    	Optional<Utilisateur> trouve = service.authentifieUtilisateur(util.getLogin(), "supermdp");
    	
    	assertEquals(trouve.get().getClass(), Utilisateur.class);
    }
    
    @Test
    public void authentifieUtilisateur_loginOk_mdpKo() {
    	
    	Optional<Utilisateur> trouve = service.authentifieUtilisateur(util.getLogin(), "pasbonlemdp");
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void authentifieUtilisateur_loginKo_mdpOk() {
    	
    	Optional<Utilisateur> trouve = service.authentifieUtilisateur("pasbonlogin", "supermdp");
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void authentifieUtilisateur_toutKo() {
    	
    	Optional<Utilisateur> trouve = service.authentifieUtilisateur("paslebonlogin", "paslebonmdp");
    	
    	assertEquals(trouve, Optional.empty());
    }
    
    @Test
    public void ajouterUtilisateur_utilOk() {
    	
    	Utilisateur pourAjout = new Utilisateur("nom", "prenom", "login", "motdepasse");
    	
    	Utilisateur ajout = service.ajouterUtilisateur(pourAjout);
    	
    	assertNotNull(ajout);
    }
    
    @Test
    public void modifierUtilisateur_verifDesModif() {
    	
    	Utilisateur pourModif = new Utilisateur("nom", "prenom", "login", "motdepasse");
    	
    	Utilisateur modif = service.modifierUtilisateur(util.getId(), pourModif);
    	
    	assertNotNull(modif);
    	
    	assertEquals(util.getNom(), pourModif.getNom());
    	assertEquals(util.getPrenom(), pourModif.getPrenom());
    	assertEquals(util.getLogin(), pourModif.getLogin());
    }
    
    @Test
    public void changerMotDePasse_idExistant_ancienOk() {
    	
    	Utilisateur changerMdp = service.changerMotDePasse(util.getId(), "supermdp", "nveauMdp");
    	
    	String mdpEncode = Hashing.sha256()
				  .hashString("nveauMdp", StandardCharsets.UTF_8)
				  .toString();
    	
    	assertNotNull(changerMdp);
    	
    	assertEquals(util.getMotDePasse(), mdpEncode);
    }
    
    @Test
    public void changerMotDePasse_idInexistant() {
    	
    	Utilisateur changerMdp = service.changerMotDePasse(UUID.randomUUID(), "supermdp", "nveaumdp");
    	
    	assertNull(changerMdp);
    }
    
    @Test
    public void changerMotDePasse_idExistant_ancienKo() {
    	
    	Utilisateur changerMdp = service.changerMotDePasse(util.getId(), "paslebonmdp", "nveaumdp");
    	
    	assertNull(changerMdp);
    }
    
    @Test
    public void supprimerUtilisateur_utilisateurExistant() {
    	
    	int retour = service.supprimerUtilisateur(util);
    	
    	assertEquals(retour, 1);
    }
    
    @Test
    public void supprimerUtilisateur_utilisateurInexistant() {
    	
    	int retour = service.supprimerUtilisateur(new Utilisateur("nom", "prenom", "login", "motDePasse"));
    	
    	assertEquals(retour, 1);
    }
    
    @Test
    public void supprimerUtilisateur_utiliateurNull() {
    	
    	int retour = service.supprimerUtilisateur(null);
    	
    	assertEquals(retour, 0);
    }

}
