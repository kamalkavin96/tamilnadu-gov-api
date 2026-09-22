package com.kamalkavin96.tamilnadu_gov_api.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card_type")
public class CardType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", unique = true, nullable = false)
    private Long sourceId; // Maps to cardTypeDto.id (e.g., 1)

    @Column(name = "type_code", length = 10)
    private String typeCode; // Maps to cardTypeDto.type (e.g., "G")

    @Column(name = "description", length = 100)
    private String description; // Maps to cardTypeDto.description (e.g., "Rice Card")

    @Column(name = "local_description", length = 100)
    private String localDescription; // Maps to cardTypeDto.ldescription (e.g., "அரிசி அட்டை")

    @Column(name = "group_name", length = 100)
    private String groupName; // Maps to cardTypeGroupDto.groupName (e.g., "Green cards")

    @Column(name = "local_group_name", length = 100)
    private String localGroupName; // Maps to cardTypeGroupDto.lgroupName (e.g., "பச்சை அட்டை")

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
