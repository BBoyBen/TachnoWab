package fr.noumeme.tachnowab.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.noumeme.tachnowab.models.Serie;

@Repository
public interface SerieRepository extends CrudRepository<Serie, Long> {

}
