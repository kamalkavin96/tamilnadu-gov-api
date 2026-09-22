package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.Taluk;

public interface TalukRepository extends JpaRepository<Taluk, Long> {
    Optional<Taluk> findBySourceId(String sourceId);
    List<Taluk> findByDistrictId(Long districtId);
    boolean existsBySourceId(String sourceId);
}
