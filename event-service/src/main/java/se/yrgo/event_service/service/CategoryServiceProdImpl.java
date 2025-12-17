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

@Service
@Transactional(readOnly = true)
public class CategoryServiceProdImpl implements CategoryService {
    private final CategoryDao categoryDao;

    public CategoryServiceProdImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryCreateDTO dto) {
        Category category = mapToEntity(dto);
        Category saved = categoryDao.save(category);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryCreateDTO dto) {
        try {
            Category toBeUpdated = categoryDao.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            toBeUpdated.setType(dto.getType());
            return mapToResponse(toBeUpdated);
        }
        catch (CategoryNotFoundException e) {
            throw new CategoryNotFoundException("Category was not updated.");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryDao.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        categoryDao.deleteById(category.getId());
    }

    @Override
    public CategoryResponseDTO getCategory(Long id) {
        Category foundCategory = categoryDao.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        return mapToResponse(foundCategory);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> foundCategories = categoryDao.findAll();
        List<CategoryResponseDTO> categoryResponseDTOs = new ArrayList<>();

        for (Category category : foundCategories) {
            categoryResponseDTOs.add(mapToResponse(category));
        }

        return categoryResponseDTOs;
    }

    private Category mapToEntity(CategoryCreateDTO dto) {
        return new Category(generateCategoryId(), dto.getType());
    }

    private CategoryResponseDTO mapToResponse(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getCategoryId(),
                category.getType()
        );
    }

    private String generateCategoryId() {
        return "CTG-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
