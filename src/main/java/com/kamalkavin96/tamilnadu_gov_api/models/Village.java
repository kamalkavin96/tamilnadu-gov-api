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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "village")
public class Village {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", unique = true, nullable = false, length = 50)
    private String sourceId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "taluk_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Taluk taluk;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Shop> shops = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Bidirectional synchronization helpers
    public void addShop(Shop shop) {
        shops.add(shop);
        shop.setVillage(this);
    }

    public void removeShop(Shop shop) {
        shops.remove(shop);
        shop.setVillage(null);
    }

}
