package fr.noumeme.tachnowab.controllers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import fr.noumeme.tachnowab.dtos.UtilisateurDto;
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
    public void getAllUtilisateur_avecUtil_attends200()
    	throws Exception {
    	
    	given(service.getAllUtilisateur())
    		.willReturn(Arrays.asList(util));
    	
    	ResponseEntity<List<UtilisateurDto>> rep = controller.getAllUtilisateur();
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(1, rep.getBody().size());
    }
    
    @Test
    public void getAllUtilisateur_sansUtil_attends204()
    	throws Exception {
    	
    	given(service.getAllUtilisateur())
    		.willReturn(new ArrayList<>());
    	
    	ResponseEntity<List<UtilisateurDto>> rep = controller.getAllUtilisateur();
    	
    	assertEquals(HttpStatus.NO_CONTENT, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurById_idExistant_attends200()
    	throws Exception {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.getUtilisateurById(util.getId());
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(UtilisateurDto.class, rep.getBody().getClass());
    	
    	assertEquals(util.getLogin(), rep.getBody().getLogin());
    	assertEquals(util.getNom(), rep.getBody().getNom());
    	assertEquals(util.getPrenom(), rep.getBody().getPrenom());
    }
    
    @Test
    public void getUtilisateurById_idInexistant_attends404() 
    	throws Exception {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.getUtilisateurById(UUID.randomUUID());
    	
    	assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurById_idNull_attends400() 
    	throws Exception {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.getUtilisateurById(null);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurByLogin_loginExistant_attends200()
    	throws Exception {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.getUtilisateurByLogin(util.getLogin());
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(UtilisateurDto.class, rep.getBody().getClass());
    	
    	assertEquals(util.getLogin(), rep.getBody().getLogin());
    	assertEquals(util.getNom(), rep.getBody().getNom());
    	assertEquals(util.getPrenom(), rep.getBody().getPrenom());
    }
    
    @Test
    public void getUtilisateurByLogin_loginInexistant_attends404()
    	throws Exception {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.getUtilisateurByLogin("pasbonlogin");
    	
    	assertEquals(HttpStatus.NOT_FOUND, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void getUtilisateurByLogin_loginVide_attends400()
    	throws Exception {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.getUtilisateurByLogin("");
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginOk_mdp_Ok_attends200()
    	throws Exception {
    	
    	UtilisateurDto pourAuth = new UtilisateurDto("osef", "osef", "login");
    	pourAuth.setMotDePasse("supermdp");
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<UtilisateurDto> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(UtilisateurDto.class, rep.getBody().getClass());
    	
    	assertEquals(util.getLogin(), rep.getBody().getLogin());
    	assertEquals(util.getNom(), rep.getBody().getNom());
    	assertEquals(util.getPrenom(), rep.getBody().getPrenom());
    }
    
    @Test
    public void authentification_loginKo_mdpOk_attends401()
    	throws Exception {
    	
		UtilisateurDto pourAuth = new UtilisateurDto("osef", "osef", "pasbonlogin");
		pourAuth.setMotDePasse("supermdp");
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<UtilisateurDto> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginOk_mdpKo_attends401()
    	throws Exception {
    	
		UtilisateurDto pourAuth = new UtilisateurDto("osef", "osef", "login");
		pourAuth.setMotDePasse("pasbonmdp");
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<UtilisateurDto> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_loginKo_mdpKo_attends401()
    	throws Exception {
    	
		UtilisateurDto pourAuth = new UtilisateurDto("osef", "osef", "pasbonlogin");
		pourAuth.setMotDePasse("pasbonmdp");
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<UtilisateurDto> rep = controller.authentification(pourAuth, servlet);
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void authentification_utilNull_attends400() 
    	throws Exception {
    	
    	HttpServletResponse servlet = TachnowabApplicationTests.getHttpServletResponse();
    	
    	ResponseEntity<UtilisateurDto> rep = controller.authentification(null, servlet);
    	
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
    	
		UtilisateurDto pourAjout = new UtilisateurDto("Nom", "Prenom", "NouveauLogin");
		pourAjout.setMotDePasse("encoreunmdp");
    	
    	when(service.ajouterUtilisateur(any())).thenReturn(pourAjout.toModel());
    	
    	ResponseEntity<UtilisateurDto> rep = controller.ajouterUtilisateur(pourAjout);
    	
    	assertEquals(HttpStatus.CREATED, rep.getStatusCode());
    	assertEquals(UtilisateurDto.class, rep.getBody().getClass());
    	
    	assertEquals(pourAjout.getLogin(), rep.getBody().getLogin());
    	assertEquals(pourAjout.getNom(), rep.getBody().getNom());
    	assertEquals(pourAjout.getPrenom(), rep.getBody().getPrenom());
    }
    
    @Test
    public void ajouterUtilisateur_utilLogin_ExisteDeja_attends400()
    	throws Exception {
    	
    	UtilisateurDto pourAjout = new UtilisateurDto("Nom", "Prenom", "login");
		pourAjout.setMotDePasse("encoreunmdp");
    	
    	ResponseEntity<UtilisateurDto> rep = controller.ajouterUtilisateur(pourAjout);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void ajouterUtilisateur_utilNull_attends400()
    	throws Exception {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.ajouterUtilisateur(null);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieOk_utilOk_attends200()
    	throws Exception {
    	
		UtilisateurDto pourModif = new UtilisateurDto("Modif", "Modif", "modif");
		
    	when(service.modifierUtilisateur(util.getId(), pourModif)).thenReturn(pourModif.toModel());
    	
    	ResponseEntity<UtilisateurDto> rep = controller.modifierUtilisateur(pourModif, util.getId().toString());
    	
    	assertEquals(HttpStatus.OK, rep.getStatusCode());
    	assertEquals(UtilisateurDto.class, rep.getBody().getClass());
    	
    	assertEquals(pourModif.getLogin(), rep.getBody().getLogin());
    	assertEquals(pourModif.getNom(), rep.getBody().getNom());
    	assertEquals(pourModif.getPrenom(), rep.getBody().getPrenom());
    }
    
    @Test
    public void modifierUtilisateur_cookieKo_utilOk_attends401()
    	throws Exception {
    	
    	UtilisateurDto pourModif = new UtilisateurDto("Modif", "Modif", "modif");
    	
    	ResponseEntity<UtilisateurDto> rep = controller.modifierUtilisateur(pourModif, "Atta");
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookiePasBonFormat_attends401()
    	throws Exception {
    	
    	UtilisateurDto pourModif = new UtilisateurDto("Modif", "Modif", "modif");
    	
    	ResponseEntity<UtilisateurDto> rep = controller.modifierUtilisateur(pourModif, "pasbonformat");
    	
    	assertEquals(HttpStatus.UNAUTHORIZED, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieOk_utilNull_attends400()
    	throws Exception {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.modifierUtilisateur(null, util.getLogin().toString());
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
    
    @Test
    public void modifierUtilisateur_cookieKo_utilNull_attends400() {
    	
    	ResponseEntity<UtilisateurDto> rep = controller.modifierUtilisateur(null, "Atta");
    	
    	assertEquals(HttpStatus.BAD_REQUEST, rep.getStatusCode());
    	assertNull(rep.getBody());
    }
}