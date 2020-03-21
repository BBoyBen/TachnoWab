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
	
	public Optional<Serie> getSerieById(UUID id) {
		try {
			Optional<Serie> serie = repository.findById(id);
			
			return serie;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public List<Serie> getSeriesByUser(UUID id){
		try {
			List<Serie> series = new ArrayList<Serie>();
			repository.findByIdUtilisateur(id).forEach(s -> series.add(s));
			
			return series;
		}
		catch(Exception e) {
			return new ArrayList<Serie>();
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
			Optional<Serie> toModif = getSerieById(id);
			if(toModif.equals(Optional.empty()) || toModif == null)
				return null;
			
			toModif.get().setTitre(serie.getTitre());
			toModif.get().setDescription(serie.getDescription());
			
			repository.save(toModif.get());
			
			return toModif.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Integer supprimerSerie(Serie serie) {
		try {
			repository.delete(serie);
			
			return 1;
		}
		catch(Exception e) {
			return 0;
		}
	}
}
