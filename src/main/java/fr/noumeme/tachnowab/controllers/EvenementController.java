package fr.noumeme.tachnowab.controllers;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
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

import fr.noumeme.tachnowab.dtos.EvenementDto;
import fr.noumeme.tachnowab.models.Evenement;
import fr.noumeme.tachnowab.services.EvenementService;

@RestController
public class EvenementController {

	final EvenementService service;
	
	public EvenementController(EvenementService service) {
		this.service = service;
	}
	
	@GetMapping("/evenement/{id}")
	public ResponseEntity<EvenementDto> getEvenementById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Evenement> ev = service.getEvenementById(id);
			
			if(ev.isPresent())
				return ResponseEntity.ok(ev.get().toDto());
			else
				return ResponseEntity.notFound().build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/evenements/serie/{id}")
	public ResponseEntity<List<EvenementDto>> getEvenementBySerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			List<EvenementDto> evs = new ArrayList<>();
			service.getEvenementByIdSerie(id).forEach(e -> evs.add(e.toDto()));
			
			if(evs.isEmpty())
				return ResponseEntity.noContent().build();
			
			return ResponseEntity.ok(evs);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/evenements/serie/{id}/{debut}/{fin}")
	public ResponseEntity<List<EvenementDto>> getEvenementsEntreDates(@PathVariable UUID id, 
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime debut, 
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fin,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(id == null || debut == null || fin == null)
				return ResponseEntity.badRequest().build();
	
			List<EvenementDto> evs = new ArrayList<>();
			service.getEvenementDansInterval(id, debut, fin).forEach(e -> evs.add(e.toDto()));
			
			if(evs.isEmpty())
				return ResponseEntity.noContent().build();
			
			return ResponseEntity.ok(evs);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PostMapping("/evenement")
	public ResponseEntity<EvenementDto> ajouterEvenement(@RequestBody EvenementDto ev,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(ev == null)
				return ResponseEntity.badRequest().build();
			
			Evenement eventAjout = service.ajouterEvenement(ev);
			
			if(eventAjout == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.status(HttpStatus.CREATED).body(eventAjout.toDto());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PutMapping("/evenement/{id}")
	public ResponseEntity<EvenementDto> modifierEvenement(@PathVariable UUID id, @RequestBody EvenementDto ev,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(id == null || ev == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Evenement> toModif = service.getEvenementById(id);
			if(toModif.equals(Optional.empty()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			
			Evenement eventModif = service.modifierEvenement(id, ev);
			
			if(eventModif == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.ok(eventModif.toDto());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@DeleteMapping("/evenement/{id}")
	public ResponseEntity<Integer> supprimerEvent(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

			UUID.fromString(idCookie);
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Evenement> evenement = service.getEvenementById(id);
			if(!evenement.isPresent())
				return ResponseEntity.notFound().build();
			
			Integer retour = service.supprimerEvenement(evenement.get());
			
			if(retour.equals(0))
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.ok(retour);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
