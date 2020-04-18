package fr.noumeme.tachnowab.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.noumeme.tachnowab.models.Evenement;
import fr.noumeme.tachnowab.repositories.EvenementRepository;

@Service
public class EvenementService {

	final EvenementRepository repository;
	
	public EvenementService(EvenementRepository repo) {
		this.repository = repo;
	}
	
	public List<Evenement> getAllEvenement(){
		try {
			List<Evenement> events = new ArrayList<>();
			repository.findAll().forEach(e -> events.add(e));
			
			return events;
		}
		catch(Exception e) {
			return new ArrayList<>();
		}
	}
	
	public Optional<Evenement> getEvenementById(UUID id) {
		try {
			
			return repository.findById(id);
			
		}
		catch(Exception e) {
			return Optional.empty();
		}
	}
	
	public List<Evenement> getEvenementByIdSerie(UUID id){
		try {
			List<Evenement> events = new ArrayList<>();
			repository.findByIdSerie(id).forEach(e -> events.add(e));
			
			return events;
		}
		catch(Exception e) {
			return new ArrayList<>();
		}
	}
	
	public List<Evenement> getEvenementDansInterval(UUID id, ZonedDateTime debut, ZonedDateTime fin){
		try {
			List<Evenement> events = new ArrayList<>();
			List<Evenement> all = getEvenementByIdSerie(id);
			
			for (Evenement e : all) {
				if(e.getDate().isAfter(debut) && e.getDate().isBefore(fin))
					events.add(e);
			}
			
			return events;
		}
		catch(Exception e) {
			return new ArrayList<>();
		}
	}
	
	public Evenement ajouterEvenement(Evenement ev) {
		try {
			if(ev == null)
				return null;
			
			ev.setId(UUID.randomUUID());
			ev.setDate(ZonedDateTime.now());
			repository.save(ev);
			
			return ev;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Evenement modifierEvenement(UUID id, Evenement ev) {
		try {
			if(ev == null)
				return null;
			
			Optional<Evenement> toModif = getEvenementById(id);
			if(!toModif.isPresent())
				return null;
			
			toModif.get().setCommentaire(ev.getCommentaire());
			toModif.get().setTags(ev.getTags());
			
			repository.save(toModif.get());
			
			return toModif.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public int supprimerEvenement(Evenement ev) {
		try {
			if(ev == null)
				return 0;
			
			repository.delete(ev);
			
			return 1;
		}
		catch(Exception e) {
			return 0;
		}
	}
}