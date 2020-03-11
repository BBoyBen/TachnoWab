package fr.noumeme.tachnowab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import fr.noumeme.tachnowab.services.UtilisateurService;

@RestController
public class UtilisateurController {

	@Autowired
	private UtilisateurService service;
}
