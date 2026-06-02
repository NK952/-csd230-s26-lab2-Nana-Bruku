package csd230.s26.lab1.repositories;

import com.github.javafaker.Faker;
import csd230.s26.lab1.entities.BookEntity;
import csd230.s26.lab1.entities.ActionGameEntity;
import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.AccessOptions;
import org.springframework.transaction.annotation.Propagation;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use real MySQL, not H2
//@Transactional(propagation = Propagation.NOT_SUPPORTED) // Don't rollback so data persists for inspection
class ProductRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ActionGameRepository actionGameRepository;

    @Test
    void testSaveAndRetrieveBook(){
        Faker faker = new Faker();

        //1. Create a fake book
        BookEntity book = new BookEntity(
                faker.book().author(),
                faker.book().title(),
                29.99,
                10
        );

        //2. Save to database
        bookRepository.save(book);
        Long saveId = book.getId();
        assertNotNull(saveId, "ID should be generated upon saving");

        //3. Retrieve and verify
        BookEntity foundBook = bookRepository.findById(saveId).orElseThrow();
        assertEquals(book.getTitle(), foundBook.getTitle());
        assertEquals(book.getAuthor(), foundBook.getAuthor());

        System.out.println("Successfully verified book" +  foundBook.getTitle());
    }

    @Test
    void testCRUD(){
        Faker faker = new Faker();
        BookEntity book = new BookEntity(
                faker.book().author(),
                faker.book().title(),
                19.99,
                5
        );
        bookRepository.save(book);
        Long id = book.getId();
        assertNotNull(id, "ID should be generated upon saving");
        bookRepository.delete(book);
        Optional<BookEntity> deletedBook = bookRepository.findById(id);
        assertTrue(deletedBook.isEmpty(), "Book with id " + id + " not found");
        System.out.println("Successfully verified book" +  book.getTitle());
    }

    @Test
    void testDerivedQuery(){
        Faker faker = new Faker();
        String uniqueAuthor = "Special Test Author" + faker.crypto().md5().substring(0,5);
        BookEntity book = new BookEntity(
                uniqueAuthor,
                faker.book().title(),
                15.00,
                12
        );
        bookRepository.save(book);
        List<BookEntity> foundBooks = bookRepository.findByAuthor(uniqueAuthor);
        assertFalse(foundBooks.isEmpty(),"Should find at least one book by author");
        assertEquals(uniqueAuthor, foundBooks.get(0).getAuthor());
        System.out.println("Successfully verified derived query for" +  uniqueAuthor);
    }

    @Test
    void testNiche(){
        Faker faker = new Faker();
        String gameName = faker.superhero().name() + "Quest";
        String genre = "Metroidvania";
        ActionGameEntity actionGame = new ActionGameEntity(
                gameName,
                "Nintendo Switch",
                59.99,
                20,
                genre
        );
        actionGameRepository.save(actionGame);
        Long gameId = actionGame.getId();
        assertNotNull(gameId, "Game ID should be generated upon saving");
        ActionGameEntity foundGame = (ActionGameEntity) actionGameRepository.findById(gameId).orElseThrow();
        assertEquals(gameId, foundGame.getId());
        System.out.println("Successfully verified niche");
    }


}