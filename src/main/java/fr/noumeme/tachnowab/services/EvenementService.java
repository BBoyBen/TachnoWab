package fr.noumeme.tachnowab.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.noumeme.tachnowab.models.Evenement;
import fr.noumeme.tachnowab.repositories.EvenementRepository;

@Service
public class EvenementService {
	
	@Autowired
	private EvenementRepository repository;
	
	public List<Evenement> getAllEvenement(){
		try {
			List<Evenement> events = new ArrayList<Evenement>();
			repository.findAll().forEach(e -> events.add(e));
			
			return events;
		}
		catch(Exception e) {
			return new ArrayList<Evenement>();
		}
	}
	
	public Evenement getEvenementById(UUID id) {
		try {
			Optional<Evenement> ev = repository.findById(id);
			
			return ev.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public List<Evenement> getEvenementByIdSerie(UUID id){
		try {
			List<Evenement> events = new ArrayList<Evenement>();
			repository.findAllByIdSerie(id).forEach(e -> events.add(e));
			
			return events;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public List<Evenement> getEvenementDansInterval(UUID id, ZonedDateTime debut, ZonedDateTime fin){
		try {
			List<Evenement> events = new ArrayList<Evenement>();
			List<Evenement> all = getEvenementByIdSerie(id);
			
			for (Evenement e : all) {
				if(e.getDate().compareTo(debut) >= 0 && e.getDate().compareTo(fin) <= 0)
					events.add(e);
			}
			
			return events;
		}
		catch(Exception e) {
			return new ArrayList<Evenement>();
		}
	}
	
	public Evenement ajouterEvenement(Evenement ev) {
		try {
			repository.save(ev);
			
			return ev;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Evenement modifierEvenement(UUID id, Evenement ev) {
		try {
			Evenement toModif = getEvenementById(id);
			if(toModif == null)
				return null;
			
			toModif.setCommentaire(ev.getCommentaire());
			toModif.setTags(ev.getTags());
			
			repository.save(toModif);
			
			return toModif;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public int supprimerEvenement(Evenement ev) {
		try {
			repository.delete(ev);
			
			return 1;
		}
		catch(Exception e) {
			return 0;
		}
	}
}
