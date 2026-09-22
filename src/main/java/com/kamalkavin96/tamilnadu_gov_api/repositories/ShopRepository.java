package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findBySourceId(String sourceId);
    List<Shop> findByVillageId(Long villageId);
    boolean existsBySourceId(String sourceId);
}
