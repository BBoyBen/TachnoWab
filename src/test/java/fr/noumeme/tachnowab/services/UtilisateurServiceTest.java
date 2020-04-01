package fr.noumeme.tachnowab.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
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
import com.google.common.hash.Hashing;

import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.UtilisateurRepository;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurServiceTest {
 
    @Mock
    private UtilisateurRepository repository;
	
    @InjectMocks
    private UtilisateurService service;
    
    private Utilisateur util;
    
    @Before
    public void setup() {
    	
    	String mdpEncode = Hashing.sha256()
				  .hashString("supermdp", StandardCharsets.UTF_8)
				  .toString();
    	
    	util = new Utilisateur("Test", "Test", "supertest", mdpEncode);
    	
    	when(repository.findAll())
    		.thenReturn(Arrays.asList(util));
    	
    	when(repository.findById(util.getId()))
    		.thenReturn(Optional.ofNullable(util));
    	
    	when(repository.findByLogin(util.getLogin()))
    		.thenReturn(Optional.ofNullable(util));
    	
    	when(repository.findByLoginAndByMotDePasse(util.getLogin(), mdpEncode))
    		.thenReturn(Optional.ofNullable(util));
    	
    }
    
    @Test
    public void getAllUtilisateur_retourneListeUtilisateur() {
    	
    	List<Utilisateur> trouve = service.getAllUtilisateur();
    	
    	assertNotNull(trouve);
    	assertEquals(1, trouve.size());
    }
    
    @Test
    public void getUtilisateurById_idOk() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurById(util.getId());
    	
    	assertEquals(Utilisateur.class, trouve.get().getClass());
    }
    
    @Test
    public void getUtilisateurById_idKo() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurById(UUID.randomUUID());
    	
    	assertEquals(Optional.empty(), trouve);
    }
    
    @Test
    public void getUtilisateurById_idNull() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurById(null);
    	
    	assertEquals(Optional.empty(), trouve);
    }
    
    @Test
    public void getUtilisateurByLogin_loginExistant() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurByLogin(util.getLogin());
    	
    	assertEquals(Utilisateur.class, trouve.get().getClass());
    }
    
    @Test
    public void getUtilisateurByLogin_loginInexistant() {
    	
    	Optional<Utilisateur> trouve = service.getUtilisateurByLogin("existepas");
    	
    	assertEquals(Optional.empty(), trouve);
    }
    
    @Test
    public void authentifieUtilisateur_toutOk() {
    	
    	Optional<Utilisateur> trouve = service.authentifieUtilisateur(util.getLogin(), "supermdp");
    	
    	assertEquals(Utilisateur.class, trouve.get().getClass());
    }
    
    @Test
    public void authentifieUtilisateur_loginOk_mdpKo() {
    	
    	Optional<Utilisateur> trouve = service.authentifieUtilisateur(util.getLogin(), "pasbonlemdp");
    	
    	assertEquals(Optional.empty(), trouve);
    }
    
    @Test
    public void authentifieUtilisateur_loginKo_mdpOk() {
    	
    	Optional<Utilisateur> trouve = service.authentifieUtilisateur("pasbonlogin", "supermdp");
    	
    	assertEquals(Optional.empty(), trouve);
    }
    
    @Test
    public void authentifieUtilisateur_toutKo() {
    	
    	Optional<Utilisateur> trouve = service.authentifieUtilisateur("paslebonlogin", "paslebonmdp");
    	
    	assertEquals(Optional.empty(), trouve);
    }
    
    @Test
    public void ajouterUtilisateur_utilOk() {
    	
    	Utilisateur pourAjout = new Utilisateur("nom", "prenom", "login", "motdepasse");
    	
    	Utilisateur ajout = service.ajouterUtilisateur(pourAjout);
    	
    	assertNotNull(ajout);
    }
    
    @Test
    public void ajouterUtilisateur_utilNull() {
    	
    	Utilisateur ajout = service.ajouterUtilisateur(null);
    	
    	assertNull(ajout);
    }
    
    @Test
    public void modifierUtilisateur_utilExistant_modifOk() {
    	
    	Utilisateur pourModif = new Utilisateur("nom", "prenom", "login", "motdepasse");
    	
    	Utilisateur modif = service.modifierUtilisateur(util.getId(), pourModif);
    	
    	assertNotNull(modif);
    	
    	assertEquals(pourModif.getNom(), util.getNom());
    	assertEquals(pourModif.getPrenom(), util.getPrenom());
    	assertEquals(pourModif.getLogin(), util.getLogin());
    }
    
    @Test
    public void modifierUtilisateur_utilExistant_modifNull() {
    	
    	Utilisateur modif = service.modifierUtilisateur(util.getId(), null);
    	
    	assertNull(modif);
    }
    
    @Test
    public void modifierUtilisateur_utilInexistant_modifOk() {
    	
    	Utilisateur pourModif = new Utilisateur("nom", "prenom", "login", "motdepasse");
    	
    	Utilisateur modif = service.modifierUtilisateur(UUID.randomUUID(), pourModif);
    	
    	assertNull(modif);
    }
    
    @Test
    public void modifierUtilisateur_utilInexistant_modifNull() {
    	
    	Utilisateur modif = service.modifierUtilisateur(UUID.randomUUID(), null);
    	
    	assertNull(modif);
    }
    
    @Test
    public void changerMotDePasse_idExistant_ancienOk() {
    	
    	Utilisateur changerMdp = service.changerMotDePasse(util.getId(), "supermdp", "nveauMdp");
    	
    	String mdpEncode = Hashing.sha256()
				  .hashString("nveauMdp", StandardCharsets.UTF_8)
				  .toString();
    	
    	assertNotNull(changerMdp);
    	
    	assertEquals(mdpEncode, util.getMotDePasse());
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
    	
    	assertEquals(1, retour);
    }
    
    @Test
    public void supprimerUtilisateur_utilisateurInexistant() {
    	
    	int retour = service.supprimerUtilisateur(new Utilisateur("nom", "prenom", "login", "motDePasse"));
    	
    	assertEquals(1, retour);
    }
    
    @Test
    public void supprimerUtilisateur_utiliateurNull() {
    	
    	int retour = service.supprimerUtilisateur(null);
    	
    	assertEquals(0, retour);
    }
}