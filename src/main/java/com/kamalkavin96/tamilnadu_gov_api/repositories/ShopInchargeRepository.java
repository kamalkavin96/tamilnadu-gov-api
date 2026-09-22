package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopIncharge;

public interface ShopInchargeRepository extends JpaRepository<ShopIncharge, Long> {
    Optional<ShopIncharge> findByPhoneNumber(String phoneNumber);
}
