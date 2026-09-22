package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.DistrictMetrics;
import com.kamalkavin96.tamilnadu_gov_api.repositories.DistrictMetricsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DistrictMetricsService {

    private final DistrictMetricsRepository repository;

    public List<DistrictMetrics> findAll() {
        return repository.findAll();
    }

    public DistrictMetrics findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("DistrictMetrics", "id", id));
    }

    @Transactional
    public DistrictMetrics create(DistrictMetrics entity) {
        return repository.save(entity);
    }

    @Transactional
    public DistrictMetrics update(Long id, DistrictMetrics entity) {
        DistrictMetrics existing = findById(id);

        entity.setId(existing.getId());

        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DistrictMetrics", "id", id);
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
