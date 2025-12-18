package se.yrgo.event_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import se.yrgo.event_service.dataaccess.CategoryDao;
import se.yrgo.event_service.domain.Category;
import se.yrgo.event_service.dtos.CategoryCreateDTO;
import se.yrgo.event_service.dtos.CategoryResponseDTO;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-tests CategoryServiceProdImpl. Since we have repository-interaction, I've mocked it with mockito.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings
class CategoryServiceTest {

    @Mock
    CategoryDao categoryDao;

    @InjectMocks
    CategoryServiceProdImpl categoryServiceProdImpl;

    @Test
    void testCreateCategory() {
        CategoryCreateDTO dto = new CategoryCreateDTO("Music");
        Category category = mapToEntity(dto);

        when(categoryDao.save(any(Category.class)))
                .thenReturn(new Category("CAT-001", "Music"));
        assertThat(categoryServiceProdImpl.createCategory(dto)).isNotNull();
        assertThat(categoryServiceProdImpl.createCategory(dto).categoryId()).isEqualTo("CAT-001");
        assertThat(categoryServiceProdImpl.createCategory(dto).type()).isEqualTo("Music");
    }

    @Test
    void testUpdateCategory() {
        Category existing = new Category("CAT-001", "Music");
        CategoryCreateDTO dto = new CategoryCreateDTO("Sports");

        when(categoryDao.findById(1L))
                .thenReturn(Optional.of(existing));

        CategoryResponseDTO result =
                categoryServiceProdImpl.updateCategory(1L, dto);

        assertThat(result).isNotNull();
        assertThat(result.type()).isEqualTo("Sports");
        assertThat(existing.getType()).isEqualTo("Sports");
    }


    @Test
    void testDeleteCategory() {

        Category category = new Category("CAT-001", "Music");

        when(categoryDao.findById(1L))
                .thenReturn(Optional.of(category));

        categoryServiceProdImpl.deleteCategory(1L);

        verify(categoryDao).deleteById(category.getId());
    }

    @Test
    void testGetCategory() {
        Category category = new Category("CAT-001", "Music");

        when(categoryDao.findById(1L)).thenReturn(Optional.of(category));

        assertThat(categoryServiceProdImpl.getCategory(1L)).isNotNull();
        assertThat(categoryServiceProdImpl.getCategory(1L).type()).isEqualTo("Music");
    }

    @Test
    void testGetAllCategories() {
        Category category = new Category("CAT-001", "Music");
        List<Category> categories = List.of(category);
        when(categoryDao.findAll()).thenReturn(categories);

        assertThat(categoryServiceProdImpl.getAllCategories()).isNotNull();
        assertThat(categoryServiceProdImpl.getAllCategories().size()).isEqualTo(1);
        assertThat(categoryServiceProdImpl.getAllCategories().get(0)).isEqualTo(mapToResponse(category));
    }

    private Category mapToEntity(CategoryCreateDTO dto) {
        return new Category("CAT-001", dto.type());
    }

    private CategoryResponseDTO mapToResponse(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getCategoryId(),
                category.getType()
        );
    }
}