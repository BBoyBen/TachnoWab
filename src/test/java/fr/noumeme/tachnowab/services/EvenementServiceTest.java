package fr.noumeme.tachnowab.services;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import fr.noumeme.tachnowab.repositories.EvenementRepository;

@RunWith(SpringRunner.class)
public class EvenementServiceTest {
	
	@TestConfiguration
    static class EvenementServiceTestContextConfiguration {
  
        @Bean
        public EvenementService evenementService() {
            return new EvenementService();
        }
    }
 
    @Autowired
    private EvenementService service;
 
    @MockBean
    private EvenementRepository repository;

}
