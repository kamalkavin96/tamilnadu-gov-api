package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.CardType;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {
    Optional<CardType> findBySourceId(Long sourceId);
    boolean existsBySourceId(Long sourceId);
}
