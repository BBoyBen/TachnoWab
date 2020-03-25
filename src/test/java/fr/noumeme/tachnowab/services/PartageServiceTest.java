package fr.noumeme.tachnowab.services;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import fr.noumeme.tachnowab.repositories.PartageRepository;

@RunWith(SpringRunner.class)
public class PartageServiceTest {
	
	@TestConfiguration
    static class PartageServiceTestContextConfiguration {
  
        @Bean
        public PartageService partageService() {
            return new PartageService();
        }
    }
 
    @Autowired
    private PartageService service;
 
    @MockBean
    private PartageRepository repository;

}
