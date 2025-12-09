package se.yrgo.event_service.dtos;

public class CategoryResponseDTO {
    private Long id;
    private String categoryId;
    private String type;

    public CategoryResponseDTO() {}

    public CategoryResponseDTO(Long id, String categoryId, String type) {
        this.id = id;
        this.categoryId = categoryId;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
