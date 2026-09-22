package com.kamalkavin96.tamilnadu_gov_api.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Table(name = "shop_metrics")
public class ShopMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // OWNING SIDE: Ensures each individual shop maps uniquely to exactly one row of metrics
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", referencedColumnName = "id", unique = true, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Shop shop;

    @Column(name = "family_card_count", nullable = false)
    private Long familyCardCount = 0L;

    @Column(name = "beneficiaries_count", nullable = false)
    private Long beneficiariesCount = 0L;

    @Column(name = "aadhar_reg_count", nullable = false)
    private Long aadharRegCount = 0L;

    @Column(name = "mobile_reg_count", nullable = false)
    private Long mobileRegCount = 0L;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
