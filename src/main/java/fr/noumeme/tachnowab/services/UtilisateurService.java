package fr.noumeme.tachnowab.services;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;

import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.UtilisateurRepository;

@Service
public class UtilisateurService {

	final UtilisateurRepository repository;
	
	public UtilisateurService(UtilisateurRepository repo) {
		this.repository = repo;
	}
	
	public List<Utilisateur> getAllUtilisateur(){
		try {
			List<Utilisateur> utils = new ArrayList<>();
			repository.findAll().forEach(u -> utils.add(u));
			
			return utils;
		}
		catch(Exception e) {
			return new ArrayList<>();
		}
	}
	
	public Optional<Utilisateur> getUtilisateurById(UUID id) {
		try {
			
			return repository.findById(id);
			
		}
		catch(Exception e) {
			return Optional.empty();
		}
	}
	
	public Optional<Utilisateur> getUtilisateurByLogin(String login) {
		try {
			
			return repository.findByLogin(login);
			
		}
		catch(Exception e) {
			return Optional.empty();
		}
	}
	
	public Optional<Utilisateur> authentifieUtilisateur(String login, String mdp) {
		try {
			String mdpEncode = Hashing.sha256()
									  .hashString(mdp, StandardCharsets.UTF_8)
									  .toString();
			
			return repository.findByLoginAndByMotDePasse(login, mdpEncode);
		}
		catch(Exception e) {
			return Optional.empty();
		}
	}
	
	public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
		try {
			utilisateur.setId(UUID.randomUUID());
			
			String mdpEncode = Hashing.sha256()
					  .hashString(utilisateur.getMotDePasse(), StandardCharsets.UTF_8)
					  .toString();
			utilisateur.setMotDePasse(mdpEncode);
			
			repository.save(utilisateur);
			
			return utilisateur;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Utilisateur modifierUtilisateur(UUID id, Utilisateur util) {
		try {
			Optional<Utilisateur> toModif = getUtilisateurById(id);
			if(!toModif.isPresent())
				return null;
			
			toModif.get().setLogin(util.getLogin());
			toModif.get().setNom(util.getNom());
			toModif.get().setPrenom(util.getPrenom());
			
			repository.save(toModif.get());
			
			return toModif.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Utilisateur changerMotDePasse(UUID id, String ancienMdp, String nveauMdp) {
		try {
			Optional<Utilisateur> toModif = getUtilisateurById(id);
			if(!toModif.isPresent())
				return null;
			
			String ancienMdpEncode = Hashing.sha256()
					  .hashString(ancienMdp, StandardCharsets.UTF_8)
					  .toString();
			
			if(!toModif.get().getMotDePasse().equals(ancienMdpEncode))
				return null;
			
			String nveauMdpEncode = Hashing.sha256()
					  .hashString(nveauMdp, StandardCharsets.UTF_8)
					  .toString();
			
			toModif.get().setMotDePasse(nveauMdpEncode);
			
			repository.save(toModif.get());
			
			return toModif.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public int supprimerUtilisateur(Utilisateur util) {
		try {
			if(util == null)
				return 0;
			
			repository.delete(util);
			
			return 1;
		}
		catch(Exception e) {
			return 0;
		}
	}
}