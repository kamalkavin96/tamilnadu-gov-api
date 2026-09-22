package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.State;

public interface StateRepository extends JpaRepository<State, Long> {
    Optional<State> findBySourceId(String sourceId);
    Optional<State> findByName(String name);
    boolean existsBySourceId(String sourceId);
    boolean existsByName(String name);
}
