package csd230.s26.lab1;

import com.github.javafaker.Faker;
import csd230.s26.lab1.entities.*;
import csd230.s26.lab1.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootApplication
public class Lab1Application implements CommandLineRunner {

	// Final fields for Constructor Injection
	private final BookRepository bookRepository;
	private final MagazineRepository magazineRepository;
	private final DiscMagRepository discMagRepository;
	private final TicketRepository ticketRepository;
	private final ProductRepository productRepository;
	private final CartRepository cartRepository;
	private final ActionGameRepository actionGameRepository;
	private final RPGgameRepository rpggameRepository;

	// Hardened Constructor Injection (Standard for S26)
	public Lab1Application(BookRepository bookRepository,
						   MagazineRepository magazineRepository,
						   DiscMagRepository discMagRepository,
						   TicketRepository ticketRepository,
						   ProductRepository productRepository,
						   CartRepository cartRepository,
						   ActionGameRepository actionGameRepository,
						   RPGgameRepository rpggameRepository) {
		this.bookRepository = bookRepository;
		this.magazineRepository = magazineRepository;
		this.discMagRepository = discMagRepository;
		this.ticketRepository = ticketRepository;
		this.productRepository = productRepository;
		this.cartRepository = cartRepository;
		this.actionGameRepository = actionGameRepository;
		this.rpggameRepository = rpggameRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(Lab1Application.class, args);
	}

	/**
	 * The run method executes after the application context is loaded.
	 * @Transactional ensures the Hibernate Session remains open for the entire
	 * duration of the method, preventing LazyInitializationExceptions when
	 * accessing polymorphic collections or relationships.
	 */
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Faker faker = new Faker();

		// --- 1. Create Books ---
		System.out.println("Generating Books...");
		for (int i = 0; i < 3; i++) {
			BookEntity book = new BookEntity(
					faker.book().author(),
					faker.book().title(),
					Double.parseDouble(faker.commerce().price(10.0, 50.0)),
					faker.number().numberBetween(1, 100)
			);
			bookRepository.save(book);
		}

		// --- 2. Create Magazines ---
		System.out.println("Generating Magazines...");
		for (int i = 0; i < 3; i++) {
			MagazineEntity mag = new MagazineEntity(
					faker.number().numberBetween(10, 100),       // Order Qty
					LocalDateTime.now().minusDays(i),            // Current Issue Date
					faker.book().genre() + " Magazine",          // Title
					Double.parseDouble(faker.commerce().price(5.0, 20.0)), // Price
					faker.number().numberBetween(10, 500)        // Copies
			);
			magazineRepository.save(mag);
			System.out.println("Saved Magazine: " + mag.getTitle());
		}

		// --- 3. Create Tickets ---
		System.out.println("Generating Tickets...");
		for (int i = 0; i < 3; i++) {
			String eventName = faker.commerce().department() + " " + faker.company().suffix();
			TicketEntity ticket = new TicketEntity(
					eventName + " Ticket",
					Double.parseDouble(faker.commerce().price(5.0, 100.0))
			);
			ticketRepository.save(ticket);
			System.out.println("Saved Ticket: " + ticket.getDescription());
		}

		// --- 4. Create DiscMags ---
		System.out.println("Generating DiscMags...");
		for (int i = 0; i < 3; i++) {
			DiscMagEntity discMag = new DiscMagEntity(
					faker.bool().bool(),                         // Has Disc?
					faker.number().numberBetween(10, 100),       // Order Qty
					LocalDateTime.now().minusDays(i),            // Current Issue Date
					faker.book().title() + " (with Disc)",       // Title
					Double.parseDouble(faker.commerce().price(10.0, 30.0)), // Price
					faker.number().numberBetween(5, 50)          // Copies
			);
			discMagRepository.save(discMag);
			System.out.println("Saved DiscMag: " + discMag.getTitle());
		}

		// --- 5. Create Action Games ---
		System.out.println("Generating Action Games...");
		String[] platforms = {"PC", "PlayStation 5", "Xbox Series X", "Nintendo Switch"};
		String[] subGenres = {"FPS", "Hack and Slash", "Metroidvania", "Battle Royale"};
		for (int i = 0; i < 3; i++) {
			ActionGameEntity actionGame = new ActionGameEntity(
					faker.superhero().name() + "Chronicles",
					platforms[faker.number().numberBetween(0, platforms.length)],
					Double.parseDouble(faker.commerce().price(30.0, 70.0)),
					faker.number().numberBetween(5, 40),
					subGenres[faker.number().numberBetween(0, subGenres.length)]
			);
			actionGameRepository.save(actionGame);
			System.out.println("Saved Action Game: " + actionGame.getName());
		}

		// --- 6. Create RPG Games ---
		System.out.println("Generating RPG Games...");
		for (int i = 0; i < 3; i++) {
			RPGgameEntity rpgGame = new RPGgameEntity(
					"Legend of " + faker.elderScrolls().firstName(),
					platforms[faker.number().numberBetween(0, platforms.length)],
					Double.parseDouble(faker.commerce().price(40.0, 80.0)),
					faker.number().numberBetween(2, 25),
					faker.bool().bool()
			);
			rpggameRepository.save(rpgGame);
			System.out.println("Saved RPG Game: " + rpgGame.getName());
		}

		System.out.println("\nDatabase initialization complete.");

		// --- 5. List All Products (Polymorphic Retrieval) ---
		System.out.println("\n--- Listing All Products from ProductRepository ---");
		productRepository.findAll().forEach(product -> {
			// Polymorphism in action: calling toString() on the base type
			// executes the specific implementation for the subclass.
			System.out.println(product.toString());
		});

		CartEntity cart = new CartEntity();
		cartRepository.save(cart);
		BookEntity someBook = bookRepository.findAll().get(0);
		cart.addProduct(someBook);
		ActionGameEntity someActionGame = actionGameRepository.findAll().get(0);
		cart.addProduct(someActionGame);
		cartRepository.save(cart);
		System.out.println("\n--- Cart Verification ---");
		cartRepository.findAll().forEach(c -> {
			System.out.println("Cart ID: " + c.getId());
			c.getProducts().forEach(p-> System.out.println(" - Comtains " + p.toString()));
		});

		System.out.println("\n--- Query Testing ---");

// 1. Test findByAuthor (Search for one of your faker-generated authors)
		String authorToFind = bookRepository.findAll().get(0).getAuthor();
		System.out.println("Searching for books by: " + authorToFind);
		bookRepository.findByAuthor(authorToFind).forEach(System.out::println);

// 2. Test findByTitleContaining
		System.out.println("\nSearching for titles containing 'Magazine':");
		bookRepository.findByTitleContaining("Magazine").forEach(System.out::println);

// 3. Test findByPriceLessThan
		System.out.println("\nSearching for cheap items (under $20.00):");
		productRepository.findByPriceLessThan(20.0).forEach(p ->
				System.out.println(p.getProductId() + ": " + p.getClass().getSimpleName()));

// 4. Test Custom Range Query
		System.out.println("\nTesting Custom Price Range Query ($15 - $45):");
		productRepository.findProductsInPriceRange(15.0, 45.0).forEach(System.out::println);
	}
}