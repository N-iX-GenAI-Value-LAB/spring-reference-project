package com.nix.reference.spring.project.domain.repository;

import com.nix.reference.spring.project.domain.model.Store;
import org.springframework.data.repository.CrudRepository;

public interface StoreRepository extends CrudRepository<Store, Integer> {
}
