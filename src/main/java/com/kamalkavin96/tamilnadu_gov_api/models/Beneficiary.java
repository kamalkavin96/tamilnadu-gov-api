package com.kamalkavin96.tamilnadu_gov_api.models;

import java.time.LocalDateTime;
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
@Table(name = "beneficiary")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Core Identifiers & Security Tokens ---
    @Column(name = "tn_pds_id", unique = true, nullable = false)
    private Long tnPdsId; // Main JSON "id" (e.g., 14578719 or 1566387)

    @Column(name = "ufc_number", length = 50, nullable = false, unique = true)
    private String ufcNumber; // Unique Family Code -> "ufc" (e.g., "333401378776")

    @Column(name = "encrypted_ufc", length = 255)
    private String encryptedUfc; // "encryptedUfc" (can be null)

    @Column(name = "old_ration_number", length = 50)
    private String oldRationNumber; // "oldRationNumber" (e.g., "23G0117483")

    @Column(name = "a_register_number", length = 50)
    private String aRegisterNumber; // "aregisterNumber" (can be null)

    // --- Demographics & Family Head Details ---
    @Column(name = "name", length = 150)
    private String name; // "name" (e.g., "LAKSHMI E")

    @Column(name = "local_name", length = 150)
    private String localName; // "localName" (e.g., "லட்சுமி E")

    @Column(name = "family_head_name", length = 150)
    private String familyHeadName; // beneficiaryAddressDto.familyHeadName

    @Column(name = "father_or_spouse_name", length = 150)
    private String fatherOrSpouseName; // beneficiaryAddressDto.fatherOrSpouseName (e.g., "VARADARAJAN")

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "mobile_number", length = 15)
    private String mobileNumber; // "mobileNumber" (can be null)

    @Column(name = "family_head_aadhar_encrypted", length = 255)
    private String familyHeadAadharEncrypted; // "familyHeadAadharNumber"

    // --- Residential Address Details ---
    @Column(name = "address_line_1", length = 255)
    private String addressLine1;

    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @Column(name = "address_line_3", length = 255)
    private String addressLine3;

    @Column(name = "pin_code", length = 10)
    private Integer pinCode; // beneficiaryAddressDto.pincode (e.g., "602024")

    // --- Status Flags & Logical Indicators ---
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // "active"

    @Column(name = "is_mobile_registered")
    private Boolean isMobileRegistered = false; // "isMobileNumberRegistered"

    @Column(name = "is_aadhar_registered")
    private Boolean isAadharRegistered = false; // "isFamilyHeadAadharNumberRegistered"

    // --- Ration Family Metrics ---
    @Column(name = "num_of_adults")
    private Integer numOfAdults = 0; // "numOfAdults"

    @Column(name = "num_of_child")
    private Integer numOfChild = 0; // "numOfChild"

    @Column(name = "num_of_cylinder")
    private Integer numOfCylinder = 0; // "numOfCylinder"

    // --- Relational Schema Mappings (3NF Optimization) ---
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_type_id")
    private CardType cardType; // Linked to the new CardType metadata table

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "residential_village_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Village residentialVillage; // The actual geographic village where they reside

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_shop_id")
    private Shop assignedShop; // The Fair Price Shop (FPS) where they collect commodities

    // --- Auditing System & External Timeline Logs ---
    @Column(name = "activated_at")
    private LocalDateTime activatedAt; // "activatedTime"

    @Column(name = "source_created_at")
    private LocalDateTime sourceCreatedAt; // main payload "createdDate" or address "createdDate"

    @Column(name = "source_modified_at")
    private LocalDateTime sourceModifiedAt; // main payload "modifiedDate"

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
