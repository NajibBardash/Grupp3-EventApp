package se.yrgo.event_service.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yrgo.event_service.domain.Category;

import java.util.Optional;

/**
 * This is the dao for categories
 */
public interface CategoryDao extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryId(String categoryId);
}
