package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.Taluk;
import com.kamalkavin96.tamilnadu_gov_api.repositories.TalukRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TalukService {

    private final TalukRepository repository;

    public List<Taluk> findAll() {
        return repository.findAll();
    }

    public Taluk findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Taluk", "id", id));
    }

    @Transactional
    public Taluk create(Taluk entity) {
        return repository.save(entity);
    }

    @Transactional
    public Taluk update(Long id, Taluk entity) {
        Taluk existing = findById(id);

        entity.setId(existing.getId());

        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Taluk", "id", id);
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
