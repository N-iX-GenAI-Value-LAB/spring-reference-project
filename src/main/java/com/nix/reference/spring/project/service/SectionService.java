package com.nix.reference.spring.project.service;

import com.nix.reference.spring.project.domain.model.Product;
import com.nix.reference.spring.project.domain.model.Section;
import com.nix.reference.spring.project.domain.model.Store;
import com.nix.reference.spring.project.domain.repository.SectionRepository;
import com.nix.reference.spring.project.domain.repository.StoreRepository;
import com.nix.reference.spring.project.dto.SectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public SectionService(final SectionRepository sectionRepository,
                          final StoreRepository storeRepository) {
        this.sectionRepository = sectionRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional(
            readOnly = false,
            propagation = Propagation.REQUIRED
    )
    public SectionDTO create(final SectionDTO dto) {
        final Section section = new Section();
        section.setName(dto.name());

        if (dto.storeId() != null) {
            final Store store = storeRepository.findById(dto.storeId())
                    .orElseThrow(() -> new IllegalArgumentException("There is no store with the id " + dto.storeId()));
            section.setStore(store);
        }

        final Section saved = sectionRepository.save(section);
        return getSectionConverter().apply(saved);
    }

    @Transactional(
            readOnly = true,
            propagation = Propagation.SUPPORTS
    )
    public SectionDTO get(final int id) {
        final Section section = getByIdOrThrow(id);
        return getSectionConverter().apply(section);
    }

    @Transactional(
            readOnly = true,
            propagation = Propagation.SUPPORTS
    )
    public List<SectionDTO> getAll() {
        return StreamSupport.stream(sectionRepository.findAll().spliterator(), false)
                .map(getSectionConverter())
                .collect(Collectors.toList());
    }

    @Transactional(
            readOnly = false,
            propagation = Propagation.REQUIRED
    )
    public void delete(final int id) {
        sectionRepository.delete(getByIdOrThrow(id));
    }

    @Transactional
    public void createGoodiesSectionAndProducts() {
        final Section section = new Section();
        section.setName("Goodies");

        final LinkedHashSet<Product> products =
                IntStream.rangeClosed(1, 10)
                         .boxed()
                         .map(id -> new Product("The product with the ID " + id, section))
                         .collect(Collectors.toCollection(LinkedHashSet::new));
        section.setProducts(products);

        sectionRepository.save(section);
    }

    private Section getByIdOrThrow(int id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no section with the id " + id));
    }

    private Function<Section, SectionDTO> getSectionConverter() {
        return section -> new SectionDTO(
                section.getId(),
                section.getName(),
                section.getStore() != null ? section.getStore().getId() : null
        );
    }
}
