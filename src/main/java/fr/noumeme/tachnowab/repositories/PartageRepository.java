package fr.noumeme.tachnowab.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.noumeme.tachnowab.models.Partage;

@Repository
public interface PartageRepository extends CrudRepository<Partage, UUID> {
	
	@Query("from Partage p where p.idUtilisateur = ?1")
	List<Partage> findAllByIdUtilisateur(UUID idUtilisateur);
	
	@Query("from Partage p where p.idSerie = ?1")
	List<Partage> findAllByIdSerie(UUID idSerie);
	
	@Query("from Partage p where p.idUtilisateur = ?1 and p.idSerie = ?2")
	Optional<Partage> findByIdUtilisateurAndByIdSerie(UUID idUtilisateur, UUID idSerie);

}
