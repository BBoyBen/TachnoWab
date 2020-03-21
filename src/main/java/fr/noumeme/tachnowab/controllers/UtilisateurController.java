package fr.noumeme.tachnowab.controllers;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.services.UtilisateurService;

@RestController
public class UtilisateurController {

	@Autowired
	private UtilisateurService service;
	
	@GetMapping("/utilisateur/{id}")
	public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable UUID id){
		Optional<Utilisateur> util = service.getUtilisateurById(id);
		
		if(util == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		if(util.equals(Optional.empty()))
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(util.get());
	}
	
	@GetMapping("/utilisateur/login/{login}")
	public ResponseEntity<Utilisateur> getUtilisateurByLogin(@PathVariable String login){
		Optional<Utilisateur> util = service.getUtilisateurByLogin(login);
		
		if(util == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		if(util.equals(Optional.empty()))
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(util.get());
	}
	
	@PostMapping("/utilisateur/auth")
	public ResponseEntity<Utilisateur> authentification(@RequestBody Utilisateur util, HttpServletResponse reponse){
		Optional<Utilisateur> authUtil = service.authentifieUtilisateur(util.getLogin(), util.getMotDePasse());
		
		if(authUtil == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		if(authUtil.equals(Optional.empty()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Cookie cookieAuth = new Cookie("utilisateur", authUtil.get().getId().toString());
		cookieAuth.setMaxAge(7*24*60*60);
		cookieAuth.setSecure(false);
		cookieAuth.setHttpOnly(true);
		cookieAuth.setPath("/");
		
		reponse.addCookie(cookieAuth);
		
		return ResponseEntity.ok(authUtil.get());
	}
	
	@GetMapping("/utilisateur/deconnexion")
	public ResponseEntity<Integer> deconnexion(HttpServletResponse reponse) {
		
		Cookie cookieAuth = new Cookie("utilisateur", null);
		cookieAuth.setMaxAge(0);
		cookieAuth.setSecure(false);
		cookieAuth.setHttpOnly(true);
		cookieAuth.setPath("/");
		
		reponse.addCookie(cookieAuth);
		
		return ResponseEntity.ok(1);
	}
	
	@PostMapping("/utilisateur")
	public ResponseEntity<Utilisateur> ajouterUtilisateur(@RequestBody Utilisateur util){
		Optional<Utilisateur> existeDeja = service.getUtilisateurByLogin(util.getLogin());
		
		if(!existeDeja.equals(Optional.empty()) || existeDeja != null)
			return ResponseEntity.badRequest().build();
		
		@SuppressWarnings("unused")
		Utilisateur utilAjout = service.ajouterUtilisateur(util);
		
		if(utilAjout == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(utilAjout.getId())
                .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/utilisateur")
	public ResponseEntity<Utilisateur> modifierUtilisateur(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie, 
			@RequestBody Utilisateur util){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		UUID id = UUID.fromString(idCookie);
		
		Utilisateur utilModif = service.modifierUtilisateur(id,  util);
		
		if(utilModif == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(utilModif);
	}
	
	@DeleteMapping("/utilisateur")
	public ResponseEntity<Integer> supprimerUtilisateur(@RequestBody Utilisateur util){
		Integer retour = service.supprimerUtilisateur(util);
		
		if(retour == 0)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(retour);
	}
	
	@PutMapping("/utilisateur/changemdp")
	public ResponseEntity<Utilisateur> changerMotDePasse(@RequestBody String data){
		String[] tab = data.split(";");
		
		UUID idUtil = UUID.fromString(tab[0]);
		String oldMdp = tab[1];
		String nveauMdp = tab[2];
		
		Utilisateur util = service.changerMotDePasse(idUtil, oldMdp, nveauMdp);
		
		if(util == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(util);
	}
}
