package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.models.Shop;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository repository;

    public List<Shop> findAll() {
        return repository.findAll();
    }

    public Shop findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found with id: " + id));
    }

    @Transactional
    public Shop create(Shop entity) {
        return repository.save(entity);
    }

    @Transactional
    public Shop update(Long id, Shop entity) {
        Shop existing = findById(id);
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Shop not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
