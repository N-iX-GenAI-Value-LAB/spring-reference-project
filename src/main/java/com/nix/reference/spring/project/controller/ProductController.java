package com.nix.reference.spring.project.controller;

import com.nix.reference.spring.project.aop.profiling.ExecutionTimeProfiling;
import com.nix.reference.spring.project.aop.profiling.MemoryProfiling;
import com.nix.reference.spring.project.dto.ProductDTO;
import com.nix.reference.spring.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nix.reference.spring.project.controller.ProductController.API_PREFIX;

/**
 * A Spring {@link RestController} used to showcase the modeling of a REST controller for CRUD operations
 *
 * @author bogdan.solga
 */
@RestController
@RequestMapping(
        path = API_PREFIX + "/product"
)
public class ProductController {

    public static final String API_PREFIX = "/v1/api";

    private final ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO) {
        productService.save(productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable final int id) {
        return productService.get(id);
    }

    @MemoryProfiling
    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAll();
    }

    @ExecutionTimeProfiling
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final int id, @RequestBody ProductDTO productDTO) {
        productService.update(id, productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final int id) {
        productService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
