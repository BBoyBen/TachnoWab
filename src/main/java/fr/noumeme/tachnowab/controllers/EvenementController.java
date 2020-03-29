package fr.noumeme.tachnowab.controllers;

import java.time.ZonedDateTime;
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
import fr.noumeme.tachnowab.models.Evenement;
import fr.noumeme.tachnowab.services.EvenementService;

@RestController
public class EvenementController {

	final EvenementService service;
	
	public EvenementController(EvenementService service) {
		this.service = service;
	}
	
	@GetMapping("/evenement/{id}")
	public ResponseEntity<Evenement> getEvenementById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(id == null)
			return ResponseEntity.badRequest().build();
		
		Optional<Evenement> ev = service.getEvenementById(id);
		
		if(ev.isPresent())
			return ResponseEntity.ok(ev.get());
		else
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/evenements/serie/{id}")
	public ResponseEntity<List<Evenement>> getEvenementBySerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(id == null)
			return ResponseEntity.badRequest().build();
		
		List<Evenement> evs = new ArrayList<>();
		service.getEvenementByIdSerie(id).forEach(e -> evs.add(e));
		
		if(evs.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(evs);
	}
	
	@GetMapping("/evenements/{id}/{debut}/{fin}")
	public ResponseEntity<List<Evenement>> getEvenementsEntreDates(@PathVariable UUID id, 
			@PathVariable ZonedDateTime debut, 
			@PathVariable ZonedDateTime fin,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(id == null || debut == null || fin == null)
			return ResponseEntity.badRequest().build();

		List<Evenement> evs = new ArrayList<>();
		service.getEvenementDansInterval(id, debut, fin).forEach(e -> evs.add(e));
		
		if(evs.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(evs);
	}
	
	@PostMapping("/evenement")
	public ResponseEntity<Evenement> ajouterEvenement(@RequestBody Evenement ev,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(ev == null)
			return ResponseEntity.badRequest().build();
		
		Evenement eventAjout = service.ajouterEvenement(ev);
		
		if(eventAjout == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(eventAjout);
	}
	
	@PutMapping("/evenement/{id}")
	public ResponseEntity<Evenement> modifierEvenement(@PathVariable UUID id, @RequestBody Evenement ev,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(id == null || ev == null)
			return ResponseEntity.badRequest().build();
		
		Optional<Evenement> toModif = service.getEvenementById(id);
		if(toModif.equals(Optional.empty()))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		
		Evenement eventModif = service.modifierEvenement(id, ev);
		
		if(eventModif == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(eventModif);
	}
	
	@DeleteMapping("/evenement")
	public ResponseEntity<Integer> supprimerEvent(@RequestBody Evenement ev,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(ev == null)
			return ResponseEntity.badRequest().build();
		
		Integer retour = service.supprimerEvenement(ev);
		
		if(retour == 0)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(retour);
	}
}
