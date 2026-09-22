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
@Table(name = "state_metrics")
public class StateMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // OWNING SIDE: One state can only have exactly one metric row
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "state_id", referencedColumnName = "id", unique = true, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private State state;

    @Column(name = "district_count", nullable = false)
    private Long districtCount = 0L;

    @Column(name = "taluk_count", nullable = false)
    private Long talukCount = 0L;

    @Column(name = "fair_price_shop_count", nullable = false)
    private Long fairPriceShopCount = 0L;

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
