package fr.noumeme.tachnowab.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.common.hash.Hashing;

import fr.noumeme.tachnowab.TachnowabApplicationTests;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.services.UtilisateurService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.* ;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurControllerTest {
	
    @Mock
    private UtilisateurService service;
	
	@InjectMocks
	private UtilisateurController controller;
    
    private Utilisateur util;
    
    @Before
    public void setup() {
    	
    	String mdpEncode = Hashing.sha256()
				  .hashString("supermdp", StandardCharsets.UTF_8)
				  .toString();
    	
    	util = new Utilisateur("Test", "Test", "login", mdpEncode);
    	
    	given(service.getUtilisateurById(util.getId()))
    		.willReturn(Optional.ofNullable(util));
    	
    	given(service.getUtilisateurByLogin(util.getLogin()))
    		.willReturn(Optional.ofNullable(util));
    	
    	given(service.authentifieUtilisateur(util.getLogin(), "supermdp"))
    		.willReturn(Optional.ofNullable(util));
    }
    
    @Test
    public void getUtilisateurById_idExistant_attends200()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurById(util.getId());
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(Utilisateur.class, rep.getBody().getClass());
    	
    	assertEquals(util.getLogin(), rep.getBody().getLogin());
    	assertEquals(util.getNom(), rep.getBody().getNom());
    	assertEquals(util.getPrenom(), rep.getBody().getPrenom());
    	assertEquals(util.getMotDePasse(), rep.getBody().getMotDePasse());
    }
    
    @Test
    public void getUtilisateurById_idInexistant_attends404() 
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurById(UUID.randomUUID());
    	
    	assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurById_idNull_attends400() 
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurById(null);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurByLogin_loginExistant_attends200()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurByLogin(util.getLogin());
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(Utilisateur.class, rep.getBody().getClass());
    	
    	assertEquals(util.getLogin(), rep.getBody().getLogin());
    	assertEquals(util.getNom(), rep.getBody().getNom());
    	assertEquals(util.getPrenom(), rep.getBody().getPrenom());
    	assertEquals(util.getMotDePasse(), rep.getBody().getMotDePasse());
    }
    
    @Test
    public void getUtilisateurByLogin_loginInexistant_attends404()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurByLogin("pasbonlogin");
    	
    	assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurByLogin_loginVide_attends400()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurByLogin("");
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginOk_mdp_Ok_attends200()
    	throws Exception {
    	
    	Utilisateur pourAuth = new Utilisateur();
    	pourAuth.setLogin("login");
    	pourAuth.setMotDePasse("supermdp");
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(Utilisateur.class, rep.getBody().getClass());
    	
    	assertEquals(util.getLogin(), rep.getBody().getLogin());
    	assertEquals(util.getNom(), rep.getBody().getNom());
    	assertEquals(util.getPrenom(), rep.getBody().getPrenom());
    	assertEquals(util.getMotDePasse(), rep.getBody().getMotDePasse());
    }
    
    @Test
    public void authentification_loginKo_mdpOk_attends401()
    	throws Exception {
    	
    	Utilisateur pourAuth = new Utilisateur();
    	pourAuth.setLogin("pasbonlogin");
    	pourAuth.setMotDePasse("supermdp");
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginOk_mdpKo_attends401()
    	throws Exception {
    	
    	Utilisateur pourAuth = new Utilisateur();
    	pourAuth.setLogin("login");
    	pourAuth.setMotDePasse("pasbonmdp");
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginKo_mdpKo_attends401()
    	throws Exception {
    	
    	Utilisateur pourAuth = new Utilisateur();
    	pourAuth.setLogin("pasbonlogin");
    	pourAuth.setMotDePasse("pasbonmdp");
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_utilNull_attends400() 
    	throws Exception {
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(null, servlet);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void deconnexion_attends200EtCookieNull()
    	throws Exception {
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<Cookie> rep = controller.deconnexion(servlet);
    	
    	assertNull(rep.getBody().getValue());
    	assertEquals("utilisateur", rep.getBody().getName());
    }
    
    @Test
    public void ajouterUtilisateur_utilOk_attends201()
    	throws Exception {
    	
    	Utilisateur pourAjout = new Utilisateur("Nom", "Prenom", "NouveauLogin", "encoreunmdp");
    	
    	when(service.ajouterUtilisateur(any())).thenReturn(pourAjout);
    	
    	ResponseEntity<Utilisateur> rep = controller.ajouterUtilisateur(pourAjout);
    	
    	assertEquals(HttpStatus.CREATED, rep.getStatusCode());
    	assertEquals(Utilisateur.class, rep.getBody().getClass());
    	
    	assertEquals(pourAjout.getLogin(), rep.getBody().getLogin());
    	assertEquals(pourAjout.getMotDePasse(), rep.getBody().getMotDePasse());
    	assertEquals(pourAjout.getNom(), rep.getBody().getNom());
    	assertEquals(pourAjout.getPrenom(), rep.getBody().getPrenom());
    }
    
    @Test
    public void ajouterUtilisateur_utilLogin_ExisteDeja_attends400()
    	throws Exception {
    	
    	Utilisateur pourAjout = new Utilisateur("Nom", "Prenom", "login", "encoreunmdp");
    	
    	ResponseEntity<Utilisateur> rep = controller.ajouterUtilisateur(pourAjout);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void ajouterUtilisateur_utilNull_attends400()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.ajouterUtilisateur(null);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieOk_utilOk_attends200()
    	throws Exception {
    	
    	Utilisateur pourModif = new Utilisateur("Modif", "Modif", "modif", "pasbonmdp");
    	String mdpAvant = util.getMotDePasse();
    	
    	pourModif.setMotDePasse(mdpAvant);
    	when(service.modifierUtilisateur(util.getId(), pourModif)).thenReturn(pourModif);
    	
    	ResponseEntity<Utilisateur> rep = controller.modifierUtilisateur(util.getId().toString(),
    			pourModif);
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(Utilisateur.class, rep.getBody().getClass());
    	
    	assertEquals(pourModif.getLogin(), rep.getBody().getLogin());
    	assertEquals(pourModif.getNom(), rep.getBody().getNom());
    	assertEquals(pourModif.getPrenom(), rep.getBody().getPrenom());
    	assertEquals(mdpAvant, rep.getBody().getMotDePasse());
    }
    
    @Test
    public void modifierUtilisateur_cookieKo_utilOk_attends401()
    	throws Exception {
    	
    	Utilisateur pourModif = new Utilisateur("Modif", "Modif", "modif", "pasbonmdp");
    	
    	ResponseEntity<Utilisateur> rep = controller.modifierUtilisateur("Atta",
    			pourModif);
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieOk_utilNull_attends400()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.modifierUtilisateur(util.getLogin().toString(),
    			null);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieKo_utilNull_attends400() {
    	
    	ResponseEntity<Utilisateur> rep = controller.modifierUtilisateur("Atta",
    			null);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
}