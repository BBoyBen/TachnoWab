package fr.noumeme.tachnowab.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.noumeme.tachnowab.models.Utilisateur;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, UUID> {
	
	Optional<Utilisateur> findByLoginAndByMotDePasse(String login, String motDePasse);
	Optional<Utilisateur> findByLogin(String login);
}
