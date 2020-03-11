package fr.noumeme.tachnowab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.noumeme.tachnowab.repositories.PartageRepository;

@Service
public class PartageService {

	@Autowired
	private PartageRepository repository;
}
