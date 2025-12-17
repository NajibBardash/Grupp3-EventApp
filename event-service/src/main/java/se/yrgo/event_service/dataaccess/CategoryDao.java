package se.yrgo.event_service.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yrgo.event_service.domain.Category;

public interface CategoryDao extends JpaRepository<Category, Long> {
}
