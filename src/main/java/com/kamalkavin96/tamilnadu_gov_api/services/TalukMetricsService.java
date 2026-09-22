package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.models.TalukMetrics;
import com.kamalkavin96.tamilnadu_gov_api.repositories.TalukMetricsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TalukMetricsService {

    private final TalukMetricsRepository repository;

    public List<TalukMetrics> findAll() {
        return repository.findAll();
    }

    public TalukMetrics findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TalukMetrics not found with id: " + id));
    }

    @Transactional
    public TalukMetrics create(TalukMetrics entity) {
        return repository.save(entity);
    }

    @Transactional
    public TalukMetrics update(Long id, TalukMetrics entity) {
        TalukMetrics existing = findById(id);
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("TalukMetrics not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
