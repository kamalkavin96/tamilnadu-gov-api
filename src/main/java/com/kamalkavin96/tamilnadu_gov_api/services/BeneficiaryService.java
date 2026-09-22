package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.Beneficiary;
import com.kamalkavin96.tamilnadu_gov_api.repositories.BeneficiaryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BeneficiaryService {

    private final BeneficiaryRepository repository;

    public List<Beneficiary> findAll() {
        return repository.findAll();
    }

    public Beneficiary findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Beneficiary", "id", id));
    }

    @Transactional
    public Beneficiary create(Beneficiary entity) {
        return repository.save(entity);
    }

    @Transactional
    public Beneficiary update(Long id, Beneficiary entity) {
        Beneficiary existing = findById(id);

        entity.setId(existing.getId());

        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Beneficiary", "id", id);
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
