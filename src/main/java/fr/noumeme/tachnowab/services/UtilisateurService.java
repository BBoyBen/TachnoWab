package fr.noumeme.tachnowab.services;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;

import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.UtilisateurRepository;

@Service
public class UtilisateurService {

	@Autowired
	private UtilisateurRepository repository;
	
	public Optional<Utilisateur> getUtilisateurById(UUID id) {
		try {
			Optional<Utilisateur> util = repository.findById(id);
			
			return util;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Optional<Utilisateur> getUtilisateurByLogin(String login) {
		try {
			Optional<Utilisateur> util = repository.findByLogin(login);
			
			return util;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Optional<Utilisateur> authentifieUtilisateur(String login, String mdp) {
		try {
			String mdpEncode = Hashing.sha256()
									  .hashString(mdp, StandardCharsets.UTF_8)
									  .toString();
			Optional<Utilisateur> util = repository.findByLoginAndByMotDePasse(login, mdpEncode);
			
			return util;
		}
		catch(Exception e) {
			return null;
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
			if(toModif.equals(Optional.empty()) || toModif == null)
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
			if(toModif.equals(Optional.empty()))
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
			repository.delete(util);
			
			return 1;
		}
		catch(IllegalArgumentException e) {
			return 0;
		}
		catch(Exception e) {
			return 0;
		}
	}
}
