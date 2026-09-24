package com.kamalkavin96.tamilnadu_gov_api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "taluk")
public class Taluk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", unique = true, nullable = false, length = 50)
    private String sourceId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private District district;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
        mappedBy = "taluk",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Shop> shops = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
