package com.kamalkavin96.tamilnadu_gov_api.services;

import java.util.List;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.CardType;
import com.kamalkavin96.tamilnadu_gov_api.repositories.CardTypeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardTypeService {

    private final CardTypeRepository repository;

    public List<CardType> findAll() {
        return repository.findAll();
    }

    public CardType findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CardType", "id", id));
    }

    @Transactional
    public CardType create(CardType entity) {
        return repository.save(entity);
    }

    @Transactional
    public CardType update(Long id, CardType entity) {
        CardType existing = findById(id);

        entity.setId(existing.getId());

        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("CardType", "id", id);
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
