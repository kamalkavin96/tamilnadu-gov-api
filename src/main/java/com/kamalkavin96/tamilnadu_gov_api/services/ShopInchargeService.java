package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopIncharge;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopInchargeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopInchargeService {

    private final ShopInchargeRepository repository;

    public List<ShopIncharge> findAll() {
        return repository.findAll();
    }

    public ShopIncharge findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ShopIncharge", "id", id));
    }

    @Transactional
    public ShopIncharge create(ShopIncharge entity) {
        return repository.save(entity);
    }

    @Transactional
    public ShopIncharge update(Long id, ShopIncharge entity) {
        ShopIncharge existing = findById(id);

        entity.setId(existing.getId());

        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ShopIncharge", "id", id);
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
