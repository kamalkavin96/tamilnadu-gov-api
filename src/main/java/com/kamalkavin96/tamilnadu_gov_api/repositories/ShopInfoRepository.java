package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopInfo;

public interface ShopInfoRepository extends JpaRepository<ShopInfo, Long> {
    Optional<ShopInfo> findByShopId(Long shopId);
    Optional<ShopInfo> findByShopInchargeId(Long shopInchargeId);
    Optional<ShopInfo> findByAgencyId(Long agencyId);
}
