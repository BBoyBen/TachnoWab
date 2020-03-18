package fr.noumeme.tachnowab.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.services.PartageService;

@RestController
public class PartageController {

	@Autowired
	private PartageService service;
	
	@GetMapping("/partage/{id}")
	public ResponseEntity<Partage> getPartageById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Partage partage = service.getPartageById(id);
		
		if(partage == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(partage);
	}
	
	@GetMapping("/partages/serie/{id}")
	public ResponseEntity<List<Partage>> getAllPartagesBySerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		List<Partage> partages = new ArrayList<Partage>();
		service.getPartageByIdSerie(id).forEach(p -> partages.add(p));
		
		if(partages.size() == 0)
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(partages);
	}
	
	@GetMapping("/partages")
	public ResponseEntity<List<Partage>> getAllPartagesByUser(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		UUID id = UUID.fromString(idCookie);
		
		List<Partage> partages = new ArrayList<Partage>();
		service.getPartagesByUtil(id).forEach(p -> partages.add(p));
		
		if(partages.size() == 0)
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(partages);
	}
	
	@GetMapping("/partage/serie/{id}/user")
	public ResponseEntity<Partage> getPartageByUserAndSerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		UUID idUtil = UUID.fromString(idCookie);
		
		Partage partage = service.getPartageByUtilAndBySerie(idUtil, id);
		
		if(partage == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(partage);
	}
	
	@PostMapping("/partage")
	public ResponseEntity<Partage> ajouterPartage(Partage partage,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Partage partageAjout = service.ajouterPartage(partage);
		if(partageAjout == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(partageAjout.getId())
                .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/partage/{id}")
	public ResponseEntity<Partage> modifierPartage(@PathVariable UUID id, @RequestBody Partage partage,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Partage partageModif = service.modifierPartage(id,  partage);
		if(partageModif == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(partageModif);
	}
	
	@DeleteMapping("/partage")
	public ResponseEntity<Integer> supprimerPartge(@RequestBody Partage partage,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Integer retour = service.supprimerPartage(partage);
		if(retour == 0)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(retour);
	}
}
