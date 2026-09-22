package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    Optional<Beneficiary> findByTnPdsId(Long tnPdsId);
    Optional<Beneficiary> findByUfcNumber(String ufcNumber);
    boolean existsByTnPdsId(Long tnPdsId);
    boolean existsByUfcNumber(String ufcNumber);
}
