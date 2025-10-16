package com.nix.reference.spring.project.dto;

import java.io.Serializable;

/**
 * A DTO (Data Transfer Object) used to serialize / deserialize {@link com.nix.reference.spring.project.domain.model.Product} objects
 *
 * @author bogdan.solga
 */
public record ProductDTO(int id, String name, double price) implements Serializable {

}
