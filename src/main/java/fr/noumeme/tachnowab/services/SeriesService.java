package fr.noumeme.tachnowab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import fr.noumeme.tachnowab.repositories.SerieRepository;
import fr.noumeme.tachnowab.models.Serie;

@Service
public class SeriesService {

	final SerieRepository repository;
	
	public SeriesService(SerieRepository repo) {
		this.repository = repo;
	}
	
	public List<Serie> getAllSeries() {
		try {
			List<Serie> series = new ArrayList<>();
			repository.findAll().forEach(s -> series.add(s));
			
			return series;
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
	}
	
	@Cacheable(value="serie")
	public Optional<Serie> getSerieById(UUID id) {
		try {
			
			return repository.findById(id);
			
		}
		catch(Exception e) {
			return Optional.empty();
		}
	}
	
	@Cacheable(value="userSeries")
	public List<Serie> getSeriesByUser(UUID id){
		try {
			List<Serie> series = new ArrayList<>();
			repository.findByIdUtilisateur(id).forEach(s -> series.add(s));
			
			return series;
		}
		catch(Exception e) {
			return new ArrayList<>();
		}
	}
	
	@CacheEvict(value="userSeries", beforeInvocation=true)
	@Cacheable(value="serie")
	public Serie  ajouterSerie(Serie serie) {
		try {
			serie.setId(UUID.randomUUID());
			repository.save(serie);
			
			return serie;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@CacheEvict(value= {"serie", "userSeries"}, beforeInvocation=true)
	@Cacheable(value="serie")
	public Serie modifierSerie(UUID id, Serie serie) {
		try {
			Optional<Serie> toModif = getSerieById(id);
			if(!toModif.isPresent())
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
	
	@CacheEvict(value= {"serie", "userSeries"})
	public Integer supprimerSerie(Serie serie) {
		try {
			if(serie == null)
				return 0;
			
			repository.delete(serie);
			
			return 1;
		}
		catch(Exception e) {
			return 0;
		}
	}
}