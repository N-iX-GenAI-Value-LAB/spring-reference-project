package com.nix.reference.spring.project.repository;

import com.nix.reference.spring.project.domain.model.Section;
import org.springframework.data.repository.CrudRepository;

public interface SectionRepository extends CrudRepository<Section, Integer> {
}
