package com.kamalkavin96.tamilnadu_gov_api.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data 
@AllArgsConstructor 
@NoArgsConstructor
@Entity
@Table(name = "shop_info")
public class ShopInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", referencedColumnName = "id", unique = true, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Shop shop;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_incharge_id")
    private ShopIncharge shopIncharge;

    // REPLACED: agencyName string is now a formal relationship field
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(name = "address_line_1", nullable = false, length = 255)
    private String addressLine1;

    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @Column(name = "address_line_3", length = 255)
    private String addressLine3;

    @Column(name = "pin_code", nullable = false)
    private Integer pinCode;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "first_session_open")
    private LocalTime firstSessionOpen;

    @Column(name = "first_session_close")
    private LocalTime firstSessionClose;

    @Column(name = "second_session_open")
    private LocalTime secondSessionOpen;

    @Column(name = "second_session_close")
    private LocalTime secondSessionClose;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
