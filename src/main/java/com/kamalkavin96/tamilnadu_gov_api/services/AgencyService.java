package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.models.Agency;
import com.kamalkavin96.tamilnadu_gov_api.repositories.AgencyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgencyService {

    private final AgencyRepository repository;

    public List<Agency> findAll() {
        return repository.findAll();
    }

    public Agency findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agency not found with id: " + id));
    }

    @Transactional
    public Agency create(Agency entity) {
        return repository.save(entity);
    }

    @Transactional
    public Agency update(Long id, Agency entity) {
        Agency existing = findById(id);
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Agency not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
