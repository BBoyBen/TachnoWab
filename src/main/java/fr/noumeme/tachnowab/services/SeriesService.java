package fr.noumeme.tachnowab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
			return new ArrayList<Serie>();
		}
	}
	
	public Serie getSerieById(UUID id) {
		try {
			Optional<Serie> serie = repository.findById(id);
			
			return serie.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Serie  ajouterSerie(Serie serie) {
		try {
			repository.save(serie);
			
			return serie;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Serie modifierSerie(UUID id, Serie serie) {
		try {
			Serie toModif = getSerieById(id);
			if(toModif == null)
				return null;
			
			toModif.setTitre(serie.getTitre());
			toModif.setDescription(serie.getDescription());
			
			repository.save(toModif);
			
			return toModif;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public int supprimerSerie(Serie serie) {
		try {
			repository.delete(serie);
			
			return 1;
		}
		catch(Exception e) {
			return 0;
		}
	}
}
