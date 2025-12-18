package se.yrgo.event_service.dtos;

/**
 * This class is a dto for categories as responses from the database
 * @param id as database-id of the category
 * @param categoryId of the category
 * @param type of category
 */
public record CategoryResponseDTO(Long id, String categoryId, String type) {}
