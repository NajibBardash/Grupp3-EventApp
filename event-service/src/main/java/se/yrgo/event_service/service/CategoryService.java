package se.yrgo.event_service.service;

import se.yrgo.event_service.dtos.CategoryCreateDTO;
import se.yrgo.event_service.dtos.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryCreateDTO dto);
    CategoryResponseDTO updateCategory(Long id, CategoryCreateDTO dto);
    void deleteCategory(Long id);
    CategoryResponseDTO getCategory(Long id);
    List<CategoryResponseDTO> getAllCategories();
}
