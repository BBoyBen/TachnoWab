package fr.noumeme.tachnowab.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import fr.noumeme.tachnowab.models.Partage;
import fr.noumeme.tachnowab.models.Serie;
import fr.noumeme.tachnowab.models.Utilisateur;
import fr.noumeme.tachnowab.repositories.PartageRepository;

@RunWith(MockitoJUnitRunner.class)
public class PartageServiceTest {
 
    @Mock
    private PartageRepository repository;
	
    @InjectMocks
    private PartageService service;
    
    private Utilisateur auteur;
    private Utilisateur util;
    private Serie serie;
    private Partage partage;
    
    @Before
    public void setup() {
    	
    	auteur = new Utilisateur("Test", "Test", "login", "supermdp");
    	util = new Utilisateur("Test 2", "Test 2", "login", "mdp");
    	serie = new Serie("Titre", "description", auteur.getId());
    	partage = new Partage(false, util.getId(), serie.getId());
    	
    	when(repository.findById(partage.getId()))
    		.thenReturn(Optional.ofNullable(partage));
    	
    	when(repository.findAllByIdSerie(serie.getId()))
    		.thenReturn(Arrays.asList(partage));
    	
    	when(repository.findAllByIdUtilisateur(util.getId()))
    		.thenReturn(Arrays.asList(partage));
    	
    	when(repository.findByIdUtilisateurAndByIdSerie(util.getId(), serie.getId()))
    		.thenReturn(Optional.ofNullable(partage));
    }
    
    @Test
    public void getPartageById_idExistant() {
    	
    	Optional<Partage> p = service.getPartageById(partage.getId());
    	
    	assertEquals(Partage.class, p.get().getClass());
    }
    
    @Test
    public void getPartageById_idInexistant() {
    	
    	Optional<Partage> p = service.getPartageById(UUID.randomUUID());
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void getPartageById_idNull() {
    	
    	Optional<Partage> p = service.getPartageById(null);
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void ajouterPartage_partageOk() {
    	Serie nvelle = new Serie("Titre", "description", auteur.getId());
    	Partage pourAjout = new Partage(true, util.getId(), nvelle.getId());
    	
    	Partage ajout = service.ajouterPartage(pourAjout);
    	
    	assertNotNull(ajout);
    	
    	
    }
    
    @Test
    public void ajouterPartage_partageNull() {
    	
    	Partage ajout = service.ajouterPartage(null);
    	
    	assertNull(ajout);
    }
    
    @Test
    public void modifierPartage_idExistant() {
    	
    	boolean oldValue = partage.isLectureSeule();
    	
    	Partage modif = service.modifierPartage(partage.getId());
    	
    	assertNotNull(modif);
    	
    	assertEquals(oldValue, !partage.isLectureSeule());
    }
    
    @Test
    public void modifierPartage_idInexistant() {
    	
    	Partage modif = service.modifierPartage(UUID.randomUUID());
    	
    	assertNull(modif);
    }
    
    @Test
    public void modifierPartage_idNull() {
    	
    	Partage modif = service.modifierPartage(null);
    	
    	assertNull(modif);
    }
    
    @Test
    public void getPartagesByUtil_idExistant_avecPartage() {
    	
    	List<Partage> partages = service.getPartagesByUtil(util.getId());
    	
    	assertEquals(1, partages.size());
    }
    
    @Test
    public void getPartagesByUtil_idExistant_sansPartage() {
    	
    	List<Partage> partages = service.getPartagesByUtil(auteur.getId());
    	
    	assertEquals(0, partages.size());
    }
    
    @Test
    public void getPartagesByUtil_idInexistant() {
    	
    	List<Partage> partages = service.getPartagesByUtil(UUID.randomUUID());
    	
    	assertEquals(0, partages.size());
    }
    
    @Test
    public void getPartagesByUtil_idNull() {
    	
    	List<Partage> partages = service.getPartagesByUtil(null);
    	
    	assertEquals(0, partages.size());
    }
    
    @Test
    public void getPartageByIdSerie_idExistant() {
    	
    	List<Partage> partages = service.getPartageByIdSerie(serie.getId());
    	
    	assertEquals(1, partages.size());
    }
    
    @Test
    public void getPartageByIdSerie_idInexitant() {
    	
    	List<Partage> partages = service.getPartageByIdSerie(UUID.randomUUID());
    	
    	assertEquals(0, partages.size());
    }
    
    @Test
    public void getPartageByIdSerie_idNull() {
    	
    	List<Partage> partages = service.getPartageByIdSerie(null);
    	
    	assertEquals(0, partages.size());
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilOk_idSerieOK() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(util.getId(), serie.getId());
    	
    	assertEquals(Partage.class, p.get().getClass());
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilOk_idSerieKo() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(util.getId(), UUID.randomUUID());
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilOk_idSerieNull() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(util.getId(), null);
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilKo_idSerieOk() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(UUID.randomUUID(), serie.getId());
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilKo_idSerieKo() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(UUID.randomUUID(), UUID.randomUUID());
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilKo_idSerieNull() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(UUID.randomUUID(), null);
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilNull_idSerieOk() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(null, serie.getId());
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilNull_idSerieKo() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(null, UUID.randomUUID());
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void getPartageByUtilAndBySerie_idUtilNull_idSerieNull() {
    	
    	Optional<Partage> p = service.getPartageByUtilAndBySerie(null, null);
    	
    	assertEquals(Optional.empty(), p);
    }
    
    @Test
    public void supprimerPartage_partageExistant() {
    	
    	int retour = service.supprimerPartage(partage);
    	
    	assertEquals(1, retour);
    }
    
    @Test
    public void supprimerPartage_partageInexistant() {
    	
    	int retour = service.supprimerPartage(new Partage(true, UUID.randomUUID(), UUID.randomUUID()));
    	
    	assertEquals(1, retour);
    }
    
    @Test
    public void supprimerPartage_partageNull() {
    	
    	int retour = service.supprimerPartage(null);
    	
    	assertEquals(0, retour);
    }
}