package fr.noumeme.tachnowab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import fr.noumeme.tachnowab.services.EvenementService;

@RestController
public class EvenementController {

	@Autowired
	private EvenementService service;
}
