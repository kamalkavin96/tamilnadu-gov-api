package com.kamalkavin96.tamilnadu_gov_api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "district")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", unique = true, nullable = false, length = 50)
    private String sourceId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "state_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private State state;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Taluk> taluks = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public void addTaluk(Taluk taluk) {
        taluks.add(taluk);
        taluk.setDistrict(this);
    }

    public void removeTaluk(Taluk taluk) {
        taluks.remove(taluk);
        taluk.setDistrict(null);
    }

}
