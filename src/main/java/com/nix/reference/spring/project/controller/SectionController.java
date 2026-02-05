package com.nix.reference.spring.project.controller;

import com.nix.reference.spring.project.dto.SectionDTO;
import com.nix.reference.spring.project.service.SectionService;
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
        path = API_PREFIX + "/section"
)
public class SectionController {

    private final SectionService sectionService;

    @Autowired
    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SectionDTO sectionDTO) {
        sectionService.create(sectionDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public SectionDTO getSection(@PathVariable final int id) {
        return sectionService.get(id);
    }

    @GetMapping
    public List<SectionDTO> getAll() {
        return sectionService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final int id) {
        sectionService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
