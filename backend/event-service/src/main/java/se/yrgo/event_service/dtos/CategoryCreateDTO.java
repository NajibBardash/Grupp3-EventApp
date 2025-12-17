package se.yrgo.event_service.dtos;

public class CategoryCreateDTO {
    private String type;

    public CategoryCreateDTO() {}

    public CategoryCreateDTO(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
