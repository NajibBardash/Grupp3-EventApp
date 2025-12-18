package se.yrgo.event_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.event_service.dataaccess.CategoryDao;
import se.yrgo.event_service.domain.Category;
import se.yrgo.event_service.dtos.CategoryCreateDTO;
import se.yrgo.event_service.dtos.CategoryResponseDTO;
import se.yrgo.event_service.exceptions.CategoryNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CategoryService
 */
@Service
@Transactional(readOnly = true)
public class CategoryServiceProdImpl implements CategoryService {
    private final CategoryDao categoryDao;

    public CategoryServiceProdImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * Creates a category
     * @param dto with data of the created category
     * @return the category as a response-dto
     */
    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryCreateDTO dto) {
        Category category = mapToEntity(dto);
        Category saved = categoryDao.save(category);
        return mapToResponse(saved);
    }

    /**
     * Update a category
     * @param id of the category to update
     * @param dto with new data for the category
     * @return category as response-dto
     */
    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryCreateDTO dto) {
        Category toBeUpdated = categoryDao.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id));
        toBeUpdated.setType(dto.type());

        return mapToResponse(toBeUpdated);
    }

    /**
     * Delete a category
     * @param id of the category to delete
     */
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryDao.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        categoryDao.deleteById(category.getId());
    }

    /**
     * Finds a category
     * @param id of the category to find
     * @return found category as response-dto
     */
    @Override
    public CategoryResponseDTO getCategory(Long id) {
        Category foundCategory = categoryDao.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        return mapToResponse(foundCategory);
    }

    /**
     * Find all categories
     * @return all found categories
     */
    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> foundCategories = categoryDao.findAll();
        List<CategoryResponseDTO> categoryResponseDTOs = new ArrayList<>();

        for (Category category : foundCategories) {
            categoryResponseDTOs.add(mapToResponse(category));
        }

        return categoryResponseDTOs;
    }

    /**
     * Helper-function that maps create-dto to entity
     * @param dto to map
     * @return entity of the dto
     */
    private Category mapToEntity(CategoryCreateDTO dto) {
        return new Category(generateCategoryId(), dto.type());
    }

    /**
     * Helper-function that maps entity to response-dto
     * @param category to map
     * @return response-dto of entity
     */
    private CategoryResponseDTO mapToResponse(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getCategoryId(),
                category.getType()
        );
    }

    /**
     * Helper-function that generates a categoryId
     * @return a string with a unique categoryId
     */
    private String generateCategoryId() {
        return "CTG-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
