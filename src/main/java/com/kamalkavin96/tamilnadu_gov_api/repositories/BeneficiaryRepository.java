package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kamalkavin96.tamilnadu_gov_api.models.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    Optional<Beneficiary> findByTnPdsId(Long tnPdsId);
    Optional<Beneficiary> findByUfcNumber(String ufcNumber);
    boolean existsByTnPdsId(Long tnPdsId);
    boolean existsByUfcNumber(String ufcNumber);

    @Query(value = "SELECT assigned_shop_id FROM beneficiary order by id DESC limit 1", nativeQuery= true)
    Long findLastBeneficryShopId();
}
