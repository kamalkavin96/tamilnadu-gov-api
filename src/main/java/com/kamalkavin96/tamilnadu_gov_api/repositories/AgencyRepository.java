package com.kamalkavin96.tamilnadu_gov_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kamalkavin96.tamilnadu_gov_api.models.Agency;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    boolean existsByName(String name);
}
