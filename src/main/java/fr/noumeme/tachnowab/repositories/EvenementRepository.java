package fr.noumeme.tachnowab.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.noumeme.tachnowab.models.Evenement;

@Repository
public interface EvenementRepository extends CrudRepository<Evenement, UUID> {
	
	List<Evenement> findAllByIdSerie(UUID idSerie);
}
