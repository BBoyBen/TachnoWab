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
	
	public Utilisateur getUtilisateurById(UUID id) {
		try {
			Optional<Utilisateur> util = repository.findById(id);
			
			return util.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Utilisateur getUtilisateurByLogin(String login) {
		try {
			Optional<Utilisateur> util = repository.findByLogin(login);
			
			return util.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Utilisateur authentifieUtilisateur(String login, String mdp) {
		try {
			String mdpEncode = Hashing.sha256()
									  .hashString(mdp, StandardCharsets.UTF_8)
									  .toString();
			Optional<Utilisateur> util = repository.findByLoginAndByMotDePasse(login, mdpEncode);
			
			return util.get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
		try {
			repository.save(utilisateur);
			
			return utilisateur;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Utilisateur modifierUtilisateur(UUID id, Utilisateur util) {
		try {
			Utilisateur toModif = getUtilisateurById(id);
			if(toModif == null)
				return null;
			
			toModif.setLogin(util.getLogin());
			toModif.setNom(util.getNom());
			toModif.setPrenom(util.getPrenom());
			
			repository.save(toModif);
			
			return toModif;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Utilisateur changerMotDePasse(UUID id, String ancienMdp, String nveauMdp) {
		try {
			Utilisateur toModif = getUtilisateurById(id);
			if(toModif == null)
				return null;
			
			String ancienMdpEncode = Hashing.sha256()
					  .hashString(ancienMdp, StandardCharsets.UTF_8)
					  .toString();
			
			if(!toModif.getMotDePasse().equals(ancienMdpEncode))
				return null;
			
			String nveauMdpEncode = Hashing.sha256()
					  .hashString(nveauMdp, StandardCharsets.UTF_8)
					  .toString();
			
			toModif.setMotDePasse(nveauMdpEncode);
			
			repository.save(toModif);
			
			return toModif;
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
		catch(Exception e) {
			return 0;
		}
	}
}
