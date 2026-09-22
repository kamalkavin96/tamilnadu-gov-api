package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.DistrictMetrics;

public interface DistrictMetricsRepository extends JpaRepository<DistrictMetrics, Long> {
    Optional<DistrictMetrics> findByDistrictId(Long districtId);
    boolean existsByDistrictId(Long districtId);
}
