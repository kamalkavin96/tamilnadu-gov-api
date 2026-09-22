package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.TalukMetrics;

public interface TalukMetricsRepository extends JpaRepository<TalukMetrics, Long> {
    Optional<TalukMetrics> findByTalukId(Long talukId);
    boolean existsByTalukId(Long talukId);
}
