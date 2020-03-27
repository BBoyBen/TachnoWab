package fr.noumeme.tachnowab.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.google.common.hash.Hashing;

import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.services.UtilisateurService;
import fr.noumeme.tachnowab.util.HttpServletPerso;
import fr.noumeme.tachnowab.controllers.UtilisateurController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.* ;

@RunWith(SpringRunner.class)
public class UtilisateurControllerTest {
	
	private UtilisateurController controller;
 
    @MockBean
    private UtilisateurService service;
    
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
    	
    	controller = new UtilisateurController(service);
    }
    
    @Test
    public void getUtilisateurById_idExistant_attends200()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurById(util.getId());
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.OK);
    	assertEquals(rep.getBody().getClass(), Utilisateur.class);
    	
    	assertEquals(rep.getBody().getLogin(), util.getLogin());
    	assertEquals(rep.getBody().getNom(), util.getNom());
    	assertEquals(rep.getBody().getPrenom(), util.getPrenom());
    	assertEquals(rep.getBody().getMotDePasse(), util.getMotDePasse());
    }
    
    @Test
    public void getUtilisateurById_idInexistant_attends404() 
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurById(UUID.randomUUID());
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.NOT_FOUND);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurById_idNull_attends400() 
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurById(null);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.BAD_REQUEST);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurByLogin_loginExistant_attends200()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurByLogin(util.getLogin());
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.OK);
    	assertEquals(rep.getBody().getClass(), Utilisateur.class);
    	
    	assertEquals(rep.getBody().getLogin(), util.getLogin());
    	assertEquals(rep.getBody().getNom(), util.getNom());
    	assertEquals(rep.getBody().getPrenom(), util.getPrenom());
    	assertEquals(rep.getBody().getMotDePasse(), util.getMotDePasse());
    }
    
    @Test
    public void getUtilisateurByLogin_loginInexistant_attends404()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurByLogin("pasbonlogin");
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.NOT_FOUND);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurByLogin_loginVide_attends400()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.getUtilisateurByLogin("");
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.BAD_REQUEST);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginOk_mdp_Ok_attends200()
    	throws Exception {
    	
    	Utilisateur pourAuth = new Utilisateur();
    	pourAuth.setLogin("login");
    	pourAuth.setMotDePasse("supermdp");
    	
    	HttpServletResponse servlet = new HttpServletPerso();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.OK);
    	assertEquals(rep.getBody().getClass(), Utilisateur.class);
    	
    	assertEquals(rep.getBody().getLogin(), util.getLogin());
    	assertEquals(rep.getBody().getNom(), util.getNom());
    	assertEquals(rep.getBody().getPrenom(), util.getPrenom());
    	assertEquals(rep.getBody().getMotDePasse(), util.getMotDePasse());
    }
    
    @Test
    public void authentification_loginKo_mdpOk_attends401()
    	throws Exception {
    	
    	Utilisateur pourAuth = new Utilisateur();
    	pourAuth.setLogin("pasbonlogin");
    	pourAuth.setMotDePasse("supermdp");
    	
    	HttpServletResponse servlet = new HttpServletPerso();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.UNAUTHORIZED);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginOk_mdpKo_attends401()
    	throws Exception {
    	
    	Utilisateur pourAuth = new Utilisateur();
    	pourAuth.setLogin("login");
    	pourAuth.setMotDePasse("pasbonmdp");
    	
    	HttpServletResponse servlet = new HttpServletPerso();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.UNAUTHORIZED);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginKo_mdpKo_attends401()
    	throws Exception {
    	
    	Utilisateur pourAuth = new Utilisateur();
    	pourAuth.setLogin("pasbonlogin");
    	pourAuth.setMotDePasse("pasbonmdp");
    	
    	HttpServletResponse servlet = new HttpServletPerso();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.UNAUTHORIZED);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_utilNull_attends400() 
    	throws Exception {
    	
    	HttpServletResponse servlet = new HttpServletPerso();
    	
    	ResponseEntity<Utilisateur> rep = controller.authentification(null, servlet);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.BAD_REQUEST);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void deconnexion_attends200EtCookieNull()
    	throws Exception {
    	
    	HttpServletResponse servlet = new HttpServletPerso();
    	
    	ResponseEntity<Cookie> rep = controller.deconnexion(servlet);
    	
    	assertNull(rep.getBody().getValue());
    	assertEquals(rep.getBody().getName(), "utilisateur");
    }
    
    @Test
    public void ajouterUtilisateur_utilOk_attends201()
    	throws Exception {
    	
    	Utilisateur pourAjout = new Utilisateur("Nom", "Prenom", "NouveauLogin", "encoreunmdp");
    	
    	ResponseEntity<Utilisateur> rep = controller.ajouterUtilisateur(pourAjout);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.CREATED);
    	assertEquals(rep.getBody().getClass(), Utilisateur.class);
    	
    	assertEquals(rep.getBody().getLogin(), pourAjout.getLogin());
    	assertEquals(rep.getBody().getMotDePasse(), pourAjout.getMotDePasse());
    	assertEquals(rep.getBody().getNom(), pourAjout.getNom());
    	assertEquals(rep.getBody().getPrenom(), pourAjout.getPrenom());
    }
    
    @Test
    public void ajouterUtilisateur_utilLogin_ExisteDeja_attends400()
    	throws Exception {
    	
    	Utilisateur pourAjout = new Utilisateur("Nom", "Prenom", "login", "encoreunmdp");
    	
    	ResponseEntity<Utilisateur> rep = controller.ajouterUtilisateur(pourAjout);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.BAD_REQUEST);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void ajouterUtilisateur_utilNull_attends400()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.ajouterUtilisateur(null);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.BAD_REQUEST);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieOk_utilOk_attends200()
    	throws Exception {
    	
    	Utilisateur pourModif = new Utilisateur("Modif", "Modif", "modif", "pasbonmdp");
    	String mdpAvant = util.getMotDePasse();
    	
    	ResponseEntity<Utilisateur> rep = controller.modifierUtilisateur(util.getId().toString(),
    			pourModif);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.OK);
    	assertEquals(rep.getBody().getClass(), Utilisateur.class);
    	
    	assertEquals(util.getLogin(), pourModif.getLogin());
    	assertEquals(util.getNom(), pourModif.getNom());
    	assertEquals(util.getPrenom(), pourModif.getPrenom());
    	assertEquals(util.getMotDePasse(), mdpAvant);
    }
    
    @Test
    public void modifierUtilisateur_cookieKo_utilOk_attends401()
    	throws Exception {
    	
    	Utilisateur pourModif = new Utilisateur("Modif", "Modif", "modif", "pasbonmdp");
    	
    	ResponseEntity<Utilisateur> rep = controller.modifierUtilisateur("Atta",
    			pourModif);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.UNAUTHORIZED);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieOk_utilNull_attends400()
    	throws Exception {
    	
    	ResponseEntity<Utilisateur> rep = controller.modifierUtilisateur(util.getLogin().toString(),
    			null);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.BAD_REQUEST);
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieKo_utilNull_attends400() {
    	
    	ResponseEntity<Utilisateur> rep = controller.modifierUtilisateur("Atta",
    			null);
    	
    	assertEquals(rep.getStatusCode(), HttpStatus.BAD_REQUEST);
    	assertNull(rep.getBody());
    }
    
    
}
