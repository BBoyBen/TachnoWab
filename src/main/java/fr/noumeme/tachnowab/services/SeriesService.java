package fr.noumeme.tachnowab.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.noumeme.tachnowab.repositories.SerieRepository;
import fr.noumeme.tachnowab.models.Serie;

@Service
public class SeriesService {
	
	@Autowired
	private SerieRepository repository;
	
	public List<Serie> getAllSeries() {
		try {
			List<Serie> series = new ArrayList<Serie>();
			repository.findAll().forEach(s -> series.add(s));
			
			return series;
		}
		catch (Exception e) {
			throw e;
		}
	}
}
