package fr.noumeme.tachnowab.controllers;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		List<Serie> series = service.getAllSeries();
		
		if(series.size() == 0)
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(series);
	}
	
	@GetMapping("/serie/{id}")
	public ResponseEntity<Serie> serieById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Optional<Serie> serie = service.getSerieById(id);
		
		if(serie == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		if(serie.equals(Optional.empty()))
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(serie.get());
	}
	
	@GetMapping("/series/user")
	public ResponseEntity<List<Serie>> getSeriesByUser(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		UUID id = UUID.fromString(idCookie);
		
		List<Serie> series = service.getSeriesByUser(id);
		
		if(series.size() == 0)
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(series);
	}
	
	@PostMapping("/serie")
	public ResponseEntity<Serie> addNewSerie(@RequestBody Serie serie, 
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		serie.setIdUtilisateur(UUID.fromString(idCookie));
		Serie serieAjouter = service.ajouterSerie(serie);
		
		if(serieAjouter == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(serieAjouter.getId())
                .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/serie/{id}")
	public ResponseEntity<Serie> modifierSerie(@PathVariable UUID id, @RequestBody Serie serie, 
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Optional<Serie> toModif = service.getSerieById(id);
		if(!toModif.equals(Optional.empty()) && toModif != null && !toModif.get().getIdUtilisateur().equals(UUID.fromString(idCookie)))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Serie serieModif = service.modifierSerie(id, serie);
		
		if(serieModif == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(serieModif);
	}
	
	@DeleteMapping("/serie")
	public ResponseEntity<Integer> supprimerSerie(@RequestBody Serie serie,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		
		if(idCookie.isEmpty() || idCookie == null || idCookie.contentEquals("Atta"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if(!serie.getIdUtilisateur().equals(idCookie))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Integer retour = service.supprimerSerie(serie);
		
		if(retour.equals(0))
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(retour);
	}

}
