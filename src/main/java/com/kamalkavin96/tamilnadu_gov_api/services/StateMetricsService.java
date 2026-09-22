package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.models.StateMetrics;
import com.kamalkavin96.tamilnadu_gov_api.repositories.StateMetricsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StateMetricsService {

    private final StateMetricsRepository repository;

    public List<StateMetrics> findAll() {
        return repository.findAll();
    }

    public StateMetrics findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StateMetrics not found with id: " + id));
    }

    @Transactional
    public StateMetrics create(StateMetrics entity) {
        return repository.save(entity);
    }

    @Transactional
    public StateMetrics update(Long id, StateMetrics entity) {
        StateMetrics existing = findById(id);
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("StateMetrics not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
