package fr.noumeme.tachnowab.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

import fr.noumeme.tachnowab.dtos.SerieDto;
import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.services.PartageService;
import fr.noumeme.tachnowab.services.SeriesService;

@RestController
public class SeriesController {

	final SeriesService service;
	final PartageService partageService;
	
	public SeriesController(SeriesService service, PartageService partageService) {
		this.service = service;
		this.partageService = partageService;
	}
	
	@GetMapping("/series/all")
	public ResponseEntity<List<SerieDto>> toutesLesSeries(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			List<SerieDto> series = service.getAllSeries().stream()
														  .map(Serie::toDto)
														  .collect(Collectors.toList());
			
			if(series.isEmpty())
				return ResponseEntity.noContent().build();
			
			return ResponseEntity.ok(series);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/serie/{id}")
	public ResponseEntity<SerieDto> serieById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Serie> serie = service.getSerieById(id);
			
			if(serie.isPresent()) {
				UUID mwa = UUID.fromString(idCookie);
				List<Partage> partages = partageService.getPartageByIdSerie(id);
				if (!serie.get().getIdUtilisateur().equals(mwa) && partages.stream().noneMatch(p -> p.getIdUtilisateur().equals(mwa)))
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
          
				return ResponseEntity.ok(serie.get().toDto());
			}
			else
				return ResponseEntity.notFound().build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/series/user")
	public ResponseEntity<List<SerieDto>> getSeriesByUser(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID id = UUID.fromString(idCookie);
			
			List<SerieDto> series = service.getSeriesByUser(id).stream().map(Serie::toDto).collect(Collectors.toList());
			
			if(series.isEmpty())
				return ResponseEntity.noContent().build();
			
			return ResponseEntity.ok(series);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PostMapping("/serie")
	public ResponseEntity<SerieDto> ajouterNouvelleSerie(@RequestBody SerieDto serie, 
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
			
			return ResponseEntity.status(HttpStatus.CREATED).body(serieAjouter.toDto());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PutMapping("/serie/{id}")
	public ResponseEntity<SerieDto> modifierSerie(@PathVariable UUID id, @RequestBody SerieDto serie, 
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(serie == null || id == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Serie> toModif = service.getSerieById(id);
			if(!toModif.isPresent())
				return ResponseEntity.notFound().build();
			
			UUID mwa = UUID.fromString(idCookie);
			List<Partage> partages = partageService.getPartageByIdSerie(id);
			if (!toModif.get().getIdUtilisateur().equals(mwa) && partages.stream().noneMatch(p -> p.getIdUtilisateur().equals(mwa)))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			Serie serieModif = service.modifierSerie(id, serie);
			
			if(serieModif == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.ok(serieModif.toDto());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@DeleteMapping("/serie/{id}")
	public ResponseEntity<Integer> supprimerSerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Serie> serie = service.getSerieById(id);
			if(!serie.isPresent())
				return ResponseEntity.notFound().build();

			UUID idUtil = UUID.fromString(idCookie);
			if(serie.get().getIdUtilisateur().compareTo(idUtil) != 0)
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			Integer retour = service.supprimerSerie(serie.get());
			
			if(retour.equals(0))
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.ok(retour);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
