package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopInfo;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopInfoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopInfoService {

    private final ShopInfoRepository repository;

    public List<ShopInfo> findAll() {
        return repository.findAll();
    }

    public ShopInfo findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ShopInfo", "id", id));
    }

    @Transactional
    public ShopInfo create(ShopInfo entity) {
        return repository.save(entity);
    }

    @Transactional
    public ShopInfo update(Long id, ShopInfo entity) {
        ShopInfo existing = findById(id);

        entity.setId(existing.getId());

        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ShopInfo", "id", id);
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
