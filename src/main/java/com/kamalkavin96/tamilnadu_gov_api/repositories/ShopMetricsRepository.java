package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopMetrics;

public interface ShopMetricsRepository extends JpaRepository<ShopMetrics, Long> {
    Optional<ShopMetrics> findByShopId(Long shopId);
    boolean existsByShopId(Long shopId);
}
