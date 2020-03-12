package fr.noumeme.tachnowab.controllers;

import java.net.URI;
import java.util.List;
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

import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.services.SeriesService;

@RestController
public class SeriesController {
	
	@Autowired
	private SeriesService service;
	
	@GetMapping("/series")
	public ResponseEntity<List<Serie>> toutesLesSeries(){
		List<Serie> series = service.getAllSeries();
		
		if(series.size() == 0)
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(series);
	}
	
	@GetMapping("/serie/{id}")
	public ResponseEntity<Serie> serieById(@PathVariable UUID id){
		Serie serie = service.getSerieById(id);
		
		if(serie == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(serie);
	}
	
	@GetMapping("/series/user/{id}")
	public ResponseEntity<List<Serie>> getSeriesByUser(@PathVariable UUID id){
		List<Serie> series = service.getSeriesByUser(id);
		
		if(series.size() == 0)
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(series);
	}
	
	@PostMapping("/serie")
	public ResponseEntity<Serie> addNewSerie(@RequestBody Serie serie){
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
	public ResponseEntity<Serie> modifSerie(@PathVariable UUID id, @RequestBody Serie serie) {
		Serie serieModif = service.modifierSerie(id, serie);
		
		if(serieModif == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(serieModif);
	}
	
	@DeleteMapping("/serie")
	public ResponseEntity<Integer> supprimerSerie(@RequestBody Serie serie){
		Integer retour = service.supprimerSerie(serie);
		
		if(retour.equals(0))
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
		return ResponseEntity.ok(retour);
	}

}
