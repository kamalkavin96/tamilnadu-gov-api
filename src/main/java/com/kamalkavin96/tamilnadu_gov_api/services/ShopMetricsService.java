package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopMetrics;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopMetricsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopMetricsService {

    private final ShopMetricsRepository repository;

    public List<ShopMetrics> findAll() {
        return repository.findAll();
    }

    public ShopMetrics findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ShopMetrics", "id", id));
    }

    @Transactional
    public ShopMetrics create(ShopMetrics entity) {
        return repository.save(entity);
    }

    @Transactional
    public ShopMetrics update(Long id, ShopMetrics entity) {
        ShopMetrics existing = findById(id);

        entity.setId(existing.getId());

        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ShopMetrics", "id", id);
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
