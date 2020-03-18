package fr.noumeme.tachnowab.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import fr.noumeme.tachnowab.models.Evenement;
import fr.noumeme.tachnowab.services.EvenementService;

@RestController
public class EvenementController {

	@Autowired
	private EvenementService service;
	
	@GetMapping("/evenement/{id}")
	public ResponseEntity<Evenement> getEvenementById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Evenement ev = service.getEvenementById(id);
		
		if(ev == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(ev);
	}
	
	@GetMapping("/evenements/serie/{id}")
	public ResponseEntity<List<Evenement>> getEvenementBySerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		List<Evenement> evs = new ArrayList<Evenement>();
		service.getEvenementByIdSerie(id).forEach(e -> evs.add(e));
		
		if(evs.size() == 0)
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(evs);
	}
	
	@GetMapping("/evenements/{id}/{debut}/{fin}")
	public ResponseEntity<List<Evenement>> getEvenementsEntreDates(@PathVariable UUID id, 
			@PathVariable String debut, 
			@PathVariable String fin,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Pattern p = Pattern.compile("([0-3][0-9]-[0-1][0-9]-[1-2][0-9][0-9][0-9])");
		Matcher md = p.matcher(debut);
		Matcher mf = p.matcher(fin);
		
		if(!md.matches() || !mf.matches())
			return ResponseEntity.badRequest().build();
		
		List<Evenement> evs = new ArrayList<Evenement>();
		service.getEvenementDansInterval(id, debut, fin).forEach(e -> evs.add(e));
		
		if(evs.size() == 0)
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(evs);
	}
	
	@PostMapping("/evenement")
	public ResponseEntity<Evenement> ajouterEvenement(@RequestBody Evenement ev,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Evenement eventAjout = service.ajouterEvenement(ev);
		
		if(eventAjout == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eventAjout.getId())
                .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/evenement/{id}")
	public ResponseEntity<Evenement> modifierEvenement(@PathVariable UUID id, @RequestBody Evenement ev,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Evenement eventModif = service.modifierEvenement(id, ev);
		
		if(eventModif == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(eventModif);
	}
	
	@DeleteMapping("/evenement")
	public ResponseEntity<Integer> supprimerEvent(@RequestBody Evenement ev,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Integer retour = service.supprimerEvenement(ev);
		
		if(retour == 0)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(retour);
	}
}
