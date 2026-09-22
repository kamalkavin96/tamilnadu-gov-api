package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.models.Village;
import com.kamalkavin96.tamilnadu_gov_api.repositories.VillageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VillageService {

    private final VillageRepository repository;

    public List<Village> findAll() {
        return repository.findAll();
    }

    public Village findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Village not found with id: " + id));
    }

    @Transactional
    public Village create(Village entity) {
        return repository.save(entity);
    }

    @Transactional
    public Village update(Long id, Village entity) {
        Village existing = findById(id);
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Village not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
