package fr.noumeme.tachnowab.controllers;

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
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.services.SeriesService;

@RestController
public class SeriesController {

	final SeriesService service;
	
	public SeriesController(SeriesService service) {
		this.service = service;
	}
	
	@GetMapping("/series/all")
	public ResponseEntity<List<Serie>> toutesLesSeries(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			List<Serie> series = service.getAllSeries();
			
			if(series.isEmpty())
				return ResponseEntity.noContent().build();
			
			return ResponseEntity.ok(series);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/serie/{id}")
	public ResponseEntity<Serie> serieById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			UUID.fromString(idCookie);
			
			Optional<Serie> serie = service.getSerieById(id);
			
			if(serie.isPresent())
				return ResponseEntity.ok(serie.get());
			else
				return ResponseEntity.notFound().build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/series/user")
	public ResponseEntity<List<Serie>> getSeriesByUser(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID id = UUID.fromString(idCookie);
			
			List<Serie> series = service.getSeriesByUser(id);
			
			if(series.isEmpty())
				return ResponseEntity.noContent().build();
			
			return ResponseEntity.ok(series);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PostMapping("/serie")
	public ResponseEntity<Serie> ajouterNouvelleSerie(@RequestBody Serie serie, 
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(serie == null)
				return ResponseEntity.badRequest().build();
			
			serie.setIdUtilisateur(UUID.fromString(idCookie));
			Serie serieAjouter = service.ajouterSerie(serie);
			
			if(serieAjouter == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.status(HttpStatus.CREATED).body(serieAjouter);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PutMapping("/serie/{id}")
	public ResponseEntity<Serie> modifierSerie(@PathVariable UUID id, @RequestBody Serie serie, 
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(serie == null || id == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Serie> toModif = service.getSerieById(id);
			if(!toModif.isPresent())
				return ResponseEntity.notFound().build();
			
			if(!toModif.get().getIdUtilisateur().equals(UUID.fromString(idCookie)))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			Serie serieModif = service.modifierSerie(id, serie);
			
			if(serieModif == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.ok(serieModif);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@DeleteMapping("/serie")
	public ResponseEntity<Integer> supprimerSerie(@RequestBody Serie serie,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(serie == null)
				return ResponseEntity.badRequest().build();
			
			UUID idUtil = UUID.fromString(idCookie);
			if(serie.getIdUtilisateur().compareTo(idUtil) != 0)
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			Integer retour = service.supprimerSerie(serie);
			
			if(retour.equals(0))
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.ok(retour);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
