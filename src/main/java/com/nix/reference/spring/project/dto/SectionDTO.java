package com.nix.reference.spring.project.dto;

import java.io.Serializable;

/**
 * A DTO (Data Transfer Object) used to serialize / deserialize {@link com.nix.reference.spring.project.domain.model.Section} objects
 *
 * @author bogdan.solga
 */
public record SectionDTO(int id, String name, Integer storeId) implements Serializable {

}
