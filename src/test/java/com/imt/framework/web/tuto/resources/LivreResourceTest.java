package com.imt.framework.web.tuto.resources;

import com.imt.framework.web.tuto.entities.Livre;
import com.imt.framework.web.tuto.repositories.LivreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LivreResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LivreRepository livreRepository;

    @AfterEach
    public void cleanup() {
        livreRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetBook() {
        Livre livre = new Livre();
        livre.setTitre("Integration Test Book");
        livre.setAuteur("Tester");
        livre.setPrice(15.0);

        // Test POST (Create)
        // The resource maps to /books, but JerseyConfig maps to /librairy
        ResponseEntity<Void> postResponse = restTemplate.postForEntity("/librairy/books", livre, Void.class);
        
        // JAX-RS void return typically results in 204 No Content
        assertThat(postResponse.getStatusCode()).isIn(HttpStatus.NO_CONTENT, HttpStatus.OK);

        // Test GET (Read)
        ResponseEntity<Livre[]> getResponse = restTemplate.getForEntity("/librairy/books", Livre[].class);
        
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().length).isGreaterThan(0);

        boolean found = false;
        for (Livre l : getResponse.getBody()) {
            if ("Integration Test Book".equals(l.getTitre()) && "Tester".equals(l.getAuteur())) {
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }
}