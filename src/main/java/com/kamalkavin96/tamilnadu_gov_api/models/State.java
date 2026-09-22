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

@Data
@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Table(name = "state")
public class State {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", unique = true, nullable = false, length = 50)
    private String sourceId;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<District> districts = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper method to keep bidirectional sync safe
    public void addDistrict(District district) {
        districts.add(district);
        district.setState(this);
    }

    public void removeDistrict(District district) {
        districts.remove(district);
        district.setState(null);
    }
}
