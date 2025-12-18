package se.yrgo.event_service.exceptions;

/**
 * Custom exception for when a category cannot be found
 */
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) { super(message); }
}
