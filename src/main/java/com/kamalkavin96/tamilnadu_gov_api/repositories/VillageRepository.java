package com.kamalkavin96.tamilnadu_gov_api.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.Village;

public interface VillageRepository extends JpaRepository<Village, Long> {

    Optional<Village> findBySourceId(Long sourceId);
    List<Village> findByTalukId(Long talukId);
    boolean existsBySourceId(Long sourceId);
}