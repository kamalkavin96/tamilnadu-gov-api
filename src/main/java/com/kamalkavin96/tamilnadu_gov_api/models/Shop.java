package com.kamalkavin96.tamilnadu_gov_api.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", unique = true, nullable = false, length = 50)
    private String sourceId;

    @Column(name = "shop_code", nullable = false, length = 150, unique = true)
    private String shopCode;

    @Column(name = "shop_name", nullable = false, length = 150)
    private String shopname;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "taluk_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Taluk taluk;
    

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
