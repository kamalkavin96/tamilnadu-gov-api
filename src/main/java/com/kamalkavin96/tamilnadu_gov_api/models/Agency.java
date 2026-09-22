package com.kamalkavin96.tamilnadu_gov_api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agency")
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // REMOVED: sourceId field has been deleted entirely

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    // Type of agency (e.g., "Cooperative", "TNCSC", "Self-Help Group")
    @Column(name = "type", length = 50)
    private String type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShopInfo> shopsManaged = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
