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

import fr.noumeme.tachnowab.dtos.PartageDto;
import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.services.PartageService;
import fr.noumeme.tachnowab.services.SeriesService;

@RestController
public class PartageController {

	final PartageService service;
	final SeriesService seriesService;
	
	public PartageController(PartageService service, SeriesService seriesService) {
		this.service = service;
		this.seriesService = seriesService;
	}
	
	@GetMapping("/partage/{id}")
	public ResponseEntity<PartageDto> getPartageById(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Partage> partage = service.getPartageById(id);
			
			if(partage.isPresent()) {
				Optional<Serie> serie = seriesService.getSerieById(partage.get().getIdSerie());
				if(!serie.isPresent())
					return ResponseEntity.notFound().build();
				return ResponseEntity.ok(partage.get().toDto(serie.get()));
			}
			else
				return ResponseEntity.notFound().build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	private List<PartageDto> recoverSerie(List<Partage> partages) {
		List<PartageDto> dtos = new ArrayList<>();
		partages.forEach(p -> {
			Optional<Serie> serie = seriesService.getSerieById(p.getIdSerie());
			if(serie.isPresent())
				dtos.add(p.toDto(serie.get()));
		});
		return dtos;
	}
	
	@GetMapping("/partages/serie/{id}")
	public ResponseEntity<List<PartageDto>> getAllPartagesBySerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			List<PartageDto> partages = recoverSerie(service.getPartageByIdSerie(id));
			
			if(partages.isEmpty())
				return ResponseEntity.noContent().build();
			
			return ResponseEntity.ok(partages);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/partages")
	public ResponseEntity<List<PartageDto>> getAllPartagesByUser(@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID id = UUID.fromString(idCookie);
			
			List<PartageDto> partages = recoverSerie(service.getPartagesByUtil(id));
			
			if(partages.isEmpty())
				return ResponseEntity.noContent().build();
			
			return ResponseEntity.ok(partages);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/partage/serie/{id}/user")
	public ResponseEntity<PartageDto> getPartageByUserAndSerie(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			UUID idUtil = UUID.fromString(idCookie);
			
			Optional<Partage> partage = service.getPartageByUtilAndBySerie(idUtil, id);
				
			if(partage.isPresent()) {
				Optional<Serie> serie = seriesService.getSerieById(partage.get().getIdSerie());
				if(!serie.isPresent())
					return ResponseEntity.notFound().build();
				return ResponseEntity.ok(partage.get().toDto(serie.get()));
			}
			else
				return ResponseEntity.notFound().build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PostMapping("/partage")
	public ResponseEntity<PartageDto> ajouterPartage(@RequestBody PartageDto partage,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(partage == null)
				return ResponseEntity.badRequest().build();
			
			Partage partageAjout = service.ajouterPartage(partage);
			if(partageAjout == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			Optional<Serie> serie = seriesService.getSerieById(partageAjout.getIdSerie());
			if(!serie.isPresent())
				return ResponseEntity.notFound().build();

			return ResponseEntity.status(HttpStatus.CREATED).body(partageAjout.toDto(serie.get()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PutMapping("/partage/{id}")
	public ResponseEntity<PartageDto> modifierPartage(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie){
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			UUID.fromString(idCookie);
			
			if(id == null)
				return ResponseEntity.badRequest().build();
			
			Optional<Partage> toModif = service.getPartageById(id);
			if(!toModif.isPresent())
				return ResponseEntity.notFound().build();
			
			Partage partageModif = service.modifierPartage(id);
			if(partageModif == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			Optional<Serie> serie = seriesService.getSerieById(partageModif.getIdSerie());
			if(!serie.isPresent())
				return ResponseEntity.notFound().build();

			return ResponseEntity.ok(partageModif.toDto(serie.get()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@DeleteMapping("/partage/{id}")
	public ResponseEntity<Integer> supprimerPartage(@PathVariable UUID id,
			@CookieValue(value="utilisateur", defaultValue="Atta") String idCookie) {
		try {
			if(idCookie.contentEquals("Atta"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
			if(id == null)
				return ResponseEntity.badRequest().build();

			Optional<Partage> partage = service.getPartageById(id);
			if(!partage.isPresent())
				return ResponseEntity.notFound().build();

			UUID idUtil = UUID.fromString(idCookie);

			Optional<Serie> serie = seriesService.getSerieById(partage.get().getIdSerie());
			if(!serie.isPresent())
				return ResponseEntity.notFound().build();

			if(serie.get().getIdUtilisateur().compareTo(idUtil) != 0)
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

			Integer retour = service.supprimerPartage(partage.get());
			if(retour == 0)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
			return ResponseEntity.ok(retour);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
