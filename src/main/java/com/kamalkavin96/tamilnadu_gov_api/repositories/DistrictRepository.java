package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.District;

public interface DistrictRepository extends JpaRepository<District, Long> {
    Optional<District> findBySourceId(String sourceId);
    List<District> findByStateId(Long stateId);
    boolean existsBySourceId(String sourceId);
}
