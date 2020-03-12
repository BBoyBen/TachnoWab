package fr.noumeme.tachnowab.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.noumeme.tachnowab.models.Evenement;

@Repository
public interface EvenementRepository extends CrudRepository<Evenement, UUID> {
	
	@Query("from Evenement e where e.idSerie = ?1")
	List<Evenement> findByIdSerie(UUID idSerie);
}
