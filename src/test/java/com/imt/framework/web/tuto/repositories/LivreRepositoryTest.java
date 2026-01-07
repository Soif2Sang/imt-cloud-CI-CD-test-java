package com.imt.framework.web.tuto.repositories;

import com.imt.framework.web.tuto.entities.Livre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LivreRepositoryTest {

    @Autowired
    private LivreRepository livreRepository;

    @Test
    public void testGetBooksWithMaxPrice() {
        // Arrange
        Livre cheapBook = new Livre();
        cheapBook.setTitre("Cheap Book");
        cheapBook.setAuteur("Author A");
        cheapBook.setPrice(10.0);
        livreRepository.save(cheapBook);

        Livre expensiveBook = new Livre();
        expensiveBook.setTitre("Expensive Book");
        expensiveBook.setAuteur("Author B");
        expensiveBook.setPrice(100.0);
        livreRepository.save(expensiveBook);

        Livre mediumBook = new Livre();
        mediumBook.setTitre("Medium Book");
        mediumBook.setAuteur("Author C");
        mediumBook.setPrice(50.0);
        livreRepository.save(mediumBook);

        // Act
        List<Livre> result = livreRepository.getBooksWithMaxPrice(50.0);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Livre::getTitre)
                .containsExactlyInAnyOrder("Cheap Book", "Medium Book");
        
        assertThat(result).extracting(Livre::getTitre)
                .doesNotContain("Expensive Book");
    }
}