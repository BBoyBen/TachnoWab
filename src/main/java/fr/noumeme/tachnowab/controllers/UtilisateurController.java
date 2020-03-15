package fr.noumeme.tachnowab.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import fr.noumeme.tachnowab.util.ChangerMdp;

@RestController
public class UtilisateurController {

	@Autowired
	private UtilisateurService service;
	
	@GetMapping("/utilisateur/{id}")
	public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable UUID id){
		Utilisateur util = service.getUtilisateurById(id);
		
		if(util == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(util);
	}
	
	@GetMapping("/utilisateur/login/{login}")
	public ResponseEntity<Utilisateur> getUtilisateurByLogin(@PathVariable String login){
		Utilisateur util = service.getUtilisateurByLogin(login);
		
		if(util == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(util);
	}
	
	@PostMapping("/utilisateur/auth")
	public ResponseEntity<Utilisateur> authentification(@RequestBody Utilisateur util){
		Utilisateur authUtil = service.authentifieUtilisateur(util.getLogin(), util.getMotDePasse());
		
		if(authUtil == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		return ResponseEntity.ok(authUtil);
	}
	
	@PostMapping("/utilisateur")
	public ResponseEntity<Utilisateur> ajouterUtilisateur(@RequestBody Utilisateur util){
		Utilisateur existeDeja = service.getUtilisateurByLogin(util.getLogin());
		if(existeDeja != null)
			return ResponseEntity.badRequest().build();
		
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
	
	@PutMapping("/utilisateur/{id}")
	public ResponseEntity<Utilisateur> modifierUtilisateur(@PathVariable UUID id, @RequestBody Utilisateur util){
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
	public ResponseEntity<Utilisateur> changerMotDePasse(@RequestBody ChangerMdp change){
		Utilisateur util = service.changerMotDePasse(change.getId(), change.getOdlMdp(), change.getNewMdp());
		
		if(util == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(util);
	}
}
