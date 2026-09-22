package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.StateMetrics;

public interface StateMetricsRepository extends JpaRepository<StateMetrics, Long> {
    Optional<StateMetrics> findByStateId(Long stateId);
    boolean existsByStateId(Long stateId);
}
