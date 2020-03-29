package fr.noumeme.tachnowab.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.services.PartageService;

@RestController
public class PartageController {

	final PartageService service;
	
	public PartageController(PartageService service) {
		this.service = service;
	}
	
	@GetMapping("/partage/{id}")
	public ResponseEntity<Partage> getPartageById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(id == null)
			return ResponseEntity.badRequest().build();
		
		Optional<Partage> partage = service.getPartageById(id);
		
		if(partage.isPresent())
			return ResponseEntity.ok(partage.get());
		else
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/partages/serie/{id}")
	public ResponseEntity<List<Partage>> getAllPartagesBySerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(id == null)
			return ResponseEntity.badRequest().build();
		
		List<Partage> partages = new ArrayList<>();
		service.getPartageByIdSerie(id).forEach(p -> partages.add(p));
		
		if(partages.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(partages);
	}
	
	@GetMapping("/partages")
	public ResponseEntity<List<Partage>> getAllPartagesByUser(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		UUID id = UUID.fromString(idCookie);
		
		List<Partage> partages = new ArrayList<>();
		service.getPartagesByUtil(id).forEach(p -> partages.add(p));
		
		if(partages.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(partages);
	}
	
	@GetMapping("/partage/serie/{id}/user")
	public ResponseEntity<Partage> getPartageByUserAndSerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		
		if(idCookie.isEmpty()|| idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(id == null)
			return ResponseEntity.badRequest().build();
		
		UUID idUtil = UUID.fromString(idCookie);
		
		Optional<Partage> partage = service.getPartageByUtilAndBySerie(idUtil, id);
			
		if(partage.isPresent())
			return ResponseEntity.ok(partage.get());
		else
			return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/partage")
	public ResponseEntity<Partage> ajouterPartage(Partage partage,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(partage == null)
			return ResponseEntity.badRequest().build();
		
		Partage partageAjout = service.ajouterPartage(partage);
		if(partageAjout == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(partageAjout);
	}
	
	@PutMapping("/partage/{id}")
	public ResponseEntity<Partage> modifierPartage(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(id == null)
			return ResponseEntity.badRequest().build();
		
		Optional<Partage> toModif = service.getPartageById(id);
		if(!toModif.isPresent())
			return ResponseEntity.notFound().build();
		
		Partage partageModif = service.modifierPartage(id);
		if(partageModif == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(partageModif);
	}
	
	@DeleteMapping("/partage")
	public ResponseEntity<Integer> supprimerPartage(@RequestBody Partage partage,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(partage == null)
			return ResponseEntity.badRequest().build();
		
		Integer retour = service.supprimerPartage(partage);
		if(retour == 0)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(retour);
	}
}
