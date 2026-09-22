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
@Table(name = "shop_incharge")
public class ShopIncharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // String prevents issues with leading zeros or country code configurations
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
