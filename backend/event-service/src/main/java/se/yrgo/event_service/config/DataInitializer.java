package se.yrgo.event_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.yrgo.event_service.dataaccess.CategoryDao;
import se.yrgo.event_service.dataaccess.EventDao;
import se.yrgo.event_service.domain.Category;
import se.yrgo.event_service.domain.Event;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CategoryDao categoryDao, EventDao eventDao) {
        return args -> {
            // Check if data already exists
            if (categoryDao.count() > 0) {
                System.out.println("Database already initialized with categories");
                return;
            }

            System.out.println("Initializing database with categories and events...");

            // Create Categories
            Category music = new Category("CAT-MUSIC", "Music");
            Category theater = new Category("CAT-THEATER", "Theater");
            Category sports = new Category("CAT-SPORTS", "Sports");

            categoryDao.save(music);
            categoryDao.save(theater);
            categoryDao.save(sports);

            System.out.println("Created 3 categories: Music, Theater, Sports");

            // Create Music Events
            Event event1 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Tchajkovski: Svansjön",
                    "Experience the timeless beauty of Swan Lake, Tchaikovsky's masterpiece. The Royal Swedish Ballet performs this enchanting tale of love and magic.",
                    "Royal Opera House, Stockholm",
                    music,
                    "Royal Swedish Ballet Orchestra",
                    5000,
                    5000,
                    LocalDateTime.of(2025, 12, 20, 19, 0)
            );

            Event event2 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Arctic Monkeys - Live in Stockholm",
                    "The British rock sensation returns to Stockholm! Experience their latest hits and classic favorites in this electrifying concert.",
                    "Tele2 Arena, Stockholm",
                    music,
                    "Arctic Monkeys",
                    30000,
                    30000,
                    LocalDateTime.of(2026, 2, 15, 20, 0)
            );

            Event event3 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Taylor Swift - The Eras Tour",
                    "Don't miss Taylor Swift's spectacular Eras Tour! A journey through her entire discography with stunning visuals and unforgettable performances.",
                    "Friends Arena, Stockholm",
                    music,
                    "Taylor Swift",
                    50000,
                    50000,
                    LocalDateTime.of(2026, 5, 18, 19, 30)
            );

            Event event4 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Swedish Symphony Orchestra - New Year's Gala",
                    "Welcome 2026 with a spectacular gala concert featuring classical masterpieces and festive favorites.",
                    "Stockholm Concert Hall",
                    music,
                    "Swedish Symphony Orchestra",
                    1800,
                    1800,
                    LocalDateTime.of(2025, 12, 31, 21, 0)
            );

            // Create Theater Events
            Event event5 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Hamlet - A Modern Adaptation",
                    "Shakespeare's timeless tragedy reimagined for the modern era. A powerful production exploring themes of revenge, madness, and mortality.",
                    "Royal Dramatic Theatre, Stockholm",
                    theater,
                    "National Theater Company",
                    800,
                    800,
                    LocalDateTime.of(2026, 1, 10, 19, 0)
            );

            Event event6 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "The Lion King - Musical",
                    "Disney's award-winning musical comes to Stockholm! Experience the Circle of Life in this spectacular theatrical production.",
                    "Cirkus Theatre, Stockholm",
                    theater,
                    "Disney Theatrical Productions",
                    1500,
                    1500,
                    LocalDateTime.of(2026, 3, 5, 18, 30)
            );

            Event event7 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "A Midsummer Night's Dream",
                    "Shakespeare's magical comedy about love and mischief in an enchanted forest. Perfect for the whole family!",
                    "Drottningholm Palace Theatre",
                    theater,
                    "Stockholm Theatre Ensemble",
                    600,
                    600,
                    LocalDateTime.of(2026, 6, 21, 20, 0)
            );

            // Create Sports Events
            Event event8 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Sweden vs Norway - Ice Hockey Championship",
                    "The ultimate Scandinavian rivalry! Watch Sweden take on Norway in this crucial championship match.",
                    "Globen Arena, Stockholm",
                    sports,
                    "Swedish National Ice Hockey Team",
                    13850,
                    13850,
                    LocalDateTime.of(2026, 2, 8, 19, 0)
            );

            Event event9 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "AIK vs Hammarby - Derby Match",
                    "The most intense football derby in Sweden! Experience the passion and rivalry of Stockholm's biggest clubs.",
                    "Friends Arena, Stockholm",
                    sports,
                    "AIK vs Hammarby IF",
                    50000,
                    50000,
                    LocalDateTime.of(2026, 4, 20, 17, 0)
            );

            Event event10 = new Event(
                    "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Stockholm Marathon 2026",
                    "Join thousands of runners in Sweden's biggest marathon! Spectators welcome to cheer on participants along the scenic route.",
                    "Stockholm City Center",
                    sports,
                    "Stockholm Marathon Association",
                    2000,
                    2000,
                    LocalDateTime.of(2026, 6, 6, 10, 0)
            );

            // Save all events
            eventDao.save(event1);
            eventDao.save(event2);
            eventDao.save(event3);
            eventDao.save(event4);
            eventDao.save(event5);
            eventDao.save(event6);
            eventDao.save(event7);
            eventDao.save(event8);
            eventDao.save(event9);
            eventDao.save(event10);

            System.out.println("Created 10 events:");
            System.out.println("- 4 Music events");
            System.out.println("- 3 Theater events");
            System.out.println("- 3 Sports events");
            System.out.println("Database initialization complete!");
        };
    }
}
