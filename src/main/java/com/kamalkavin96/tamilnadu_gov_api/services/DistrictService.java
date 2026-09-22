package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.District;
import com.kamalkavin96.tamilnadu_gov_api.repositories.DistrictRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DistrictService {

    private final DistrictRepository repository;

    public List<District> findAll() {
        return repository.findAll();
    }

    public District findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("District", "id", id));
    }

    @Transactional
    public District create(District entity) {
        return repository.save(entity);
    }

    @Transactional
    public District update(Long id, District entity) {
        District existing = findById(id);

        entity.setId(existing.getId());

        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("District", "id", id);
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
