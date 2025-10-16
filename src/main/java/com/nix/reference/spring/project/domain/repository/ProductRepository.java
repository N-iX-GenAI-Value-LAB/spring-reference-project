package com.nix.reference.spring.project.domain.repository;

import com.nix.reference.spring.project.domain.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
