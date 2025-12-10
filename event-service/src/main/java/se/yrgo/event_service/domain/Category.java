package se.yrgo.event_service.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryId;
    private String type;

    @OneToMany(mappedBy = "category")
    private List<Event> events = new ArrayList<>();

    public Category() {}

    public Category(String categoryId, String type) {
        this.categoryId = categoryId;
        this.type = type;
    }

    public Long getId() {
        return id;
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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
        event.setCategory(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Category category)) return false;
        return Objects.equals(id, category.id) && Objects.equals(categoryId, category.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryId='" + categoryId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
