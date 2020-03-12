package fr.noumeme.tachnowab.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.noumeme.tachnowab.models.Serie;

@Repository
public interface SerieRepository extends CrudRepository<Serie, UUID> {

	@Query("from Serie s where s.idUtilisateur = ?1")
	List<Serie> findByIdUtilisateur(UUID idUtilisateur);
}
