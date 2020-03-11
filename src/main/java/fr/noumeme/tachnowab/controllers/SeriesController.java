package fr.noumeme.tachnowab.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.services.SeriesService;

@RestController
public class SeriesController {
	
	@Autowired
	private SeriesService service;
	
	@GetMapping("/series")
	public ResponseEntity<List<Serie>> ToutesLesSeries(){
		List<Serie> series = service.getAllSeries();
		
		return ResponseEntity.ok(series);
	}

}
