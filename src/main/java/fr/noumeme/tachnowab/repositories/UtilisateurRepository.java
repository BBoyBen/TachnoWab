package fr.noumeme.tachnowab.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.noumeme.tachnowab.models.Utilisateur;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, UUID> {
	
	@Query("from Utilisateur u where u.login = ?1 and u.motDePasse = ?2")
	Optional<Utilisateur> findByLoginAndByMotDePasse(String login, String motDePasse);
	
	@Query("from Utilisateur u where u.login = ?1")
	Optional<Utilisateur> findByLogin(String login);
}
