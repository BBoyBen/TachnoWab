package fr.noumeme.tachnowab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import fr.noumeme.tachnowab.dtos.PartageDto;
import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.repositories.PartageRepository;

@Service
public class PartageService {

	final PartageRepository repository;
	
	public PartageService(PartageRepository repo) {
		this.repository = repo;
	}
	
	public Optional<Partage> getPartageById(UUID id) {
		try {
			
			return repository.findById(id);
			
		}
		catch(Exception e) {
			return Optional.empty();
		}
	}
	
	@Caching(evict = {
		    @CacheEvict(value="utilPartages", allEntries=true),
		    @CacheEvict(value="seriePartages", allEntries=true)
		})
	public Partage ajouterPartage(PartageDto partage) {
		try {
			if(partage == null)
				return null;
			
			Partage model = partage.toModel();
			repository.save(model);
			
			return model;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@Caching(evict = {
		    @CacheEvict(value="utilPartages", allEntries=true),
		    @CacheEvict(value="seriePartages", allEntries=true)
		})
	public Partage modifierPartage(UUID id) {
		try {
			Optional<Partage> toModif = getPartageById(id);
			if(!toModif.isPresent())
				return null;
			
			toModif.get().setLectureSeule(!toModif.get().isLectureSeule());
			
			repository.save(toModif.get());
			
			return toModif.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@Cacheable(value="utilPartages", key="#id")
	public List<Partage> getPartagesByUtil(UUID id){
		try {
			List<Partage> partages = new ArrayList<>();
			repository.findAllByIdUtilisateur(id).forEach(partages::add);
			
			return partages;
		}
		catch(Exception e) {
			return new ArrayList<>();
		}
	}
	
	@Cacheable(value="seriePartages", key="#id")
	public List<Partage> getPartageByIdSerie(UUID id){
		try {
			List<Partage> partages = new ArrayList<>();
			repository.findAllByIdSerie(id).forEach(partages::add);
			
			return partages;
		}
		catch(Exception e) {
			return new ArrayList<>();
		}
	}
	
	public Optional<Partage> getPartageByUtilAndBySerie(UUID idUtil, UUID idSerie) {
		try {
			
			return repository.findByIdUtilisateurAndByIdSerie(idUtil, idSerie);
			
		}
		catch(Exception e) {
			return Optional.empty();
		}
	}
	
	@Caching(evict = {
		    @CacheEvict(value="utilPartages", key="#partage.idUtilisateur"),
		    @CacheEvict(value="seriePartages", key="#partage.idSerie")
		})
	public int supprimerPartage(Partage partage) {
		try {
			if(partage == null)
				return 0;
			
			repository.delete(partage);
			
			return 1;
		}
		catch(Exception e) {
			return 0;
		}
	}
}