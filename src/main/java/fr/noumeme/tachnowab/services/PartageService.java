package fr.noumeme.tachnowab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
			Optional<Partage> partage = repository.findById(id);
			
			return partage;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Partage ajouterPartage(Partage partage) {
		try {
			if(partage == null)
				return null;
			
			partage.setId(UUID.randomUUID());
			
			repository.save(partage);
			
			return partage;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Partage modifierPartage(UUID id) {
		try {
			Optional<Partage> toModif = getPartageById(id);
			if(toModif.equals(Optional.empty()) || toModif == null)
				return null;
			
			toModif.get().setLectureSeule(!toModif.get().isLectureSeule());
			
			repository.save(toModif.get());
			
			return toModif.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public List<Partage> getPartagesByUtil(UUID id){
		try {
			List<Partage> partages = new ArrayList<Partage>();
			repository.findAllByIdUtilisateur(id).forEach(p -> partages.add(p));
			
			return partages;
		}
		catch(Exception e) {
			return new ArrayList<Partage>();
		}
	}
	
	public List<Partage> getPartageByIdSerie(UUID id){
		try {
			List<Partage> partages = new ArrayList<Partage>();
			repository.findAllByIdSerie(id).forEach(p -> partages.add(p));
			
			return partages;
		}
		catch(Exception e) {
			return new ArrayList<Partage>();
		}
	}
	
	public Optional<Partage> getPartageByUtilAndBySerie(UUID idUtil, UUID idSerie) {
		try {
			Optional<Partage> partage = repository.findByIdUtilisateurAndByIdSerie(idUtil, idSerie);
			
			return partage;
		}
		catch(Exception e) {
			return null;
		}
	}
	
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
