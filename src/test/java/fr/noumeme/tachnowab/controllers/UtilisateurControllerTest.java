package fr.noumeme.tachnowab.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.common.hash.Hashing;

import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.services.UtilisateurService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.* ;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UtilisateurController.class)
public class UtilisateurControllerTest {
	
	@Autowired
    private MockMvc mvc;
 
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
    }
    
    @Test
    public void getUtilisateurById_idExistant_attends200()
    	throws Exception {
    	
    	mvc.perform(get("/utilisateur/" + util.getId())
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk());
    }
    
    @Test
    public void getUtilisateurById_idInexistant_attends404() 
    	throws Exception {
    	
    	mvc.perform(get("/utilisateur/" + UUID.randomUUID())
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNotFound());
    }
    
    @Test
    public void getUtilisateurById_idNull_attends400() 
    	throws Exception {
    	
    	mvc.perform(get("/utilisateur/" + null)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isBadRequest());
    }
    
    @Test
    public void getUtilisateurById_sansId_attends405() 
    	throws Exception {
    	
    	mvc.perform(get("/utilisateur/")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isMethodNotAllowed());
    }
    
    @Test
    public void getUtilisateurById_idPasBonFormat_attends400() 
    	throws Exception {
    	
    	mvc.perform(get("/utilisateur/pasbonformat")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isBadRequest());
    }
    
    @Test
    public void getUtilisateurByLogin_loginExistant_attends200()
    	throws Exception {
    	
    	mvc.perform(get("/utilisateur/login/" + util.getLogin())
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk());
    }
    
    @Test
    public void getUtilisateurByLogin_loginInexistant_attends404()
    	throws Exception {
    	
    	mvc.perform(get("/utilisateur/login/pasbon")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNotFound());
    }
    
    @Test
    public void getUtilisateurByLogin_loginVide_attends400()
    	throws Exception {
    	
    	mvc.perform(get("/utilisateur/login/")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isBadRequest());
    }
    
    
}
