package com.kamalkavin96.tamilnadu_gov_api.dto.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class BeneficiaryDetailsDto {

    private String district;
    private String ldistrict;
    private Long districtId;
    private String gender;
    private Long id;
    private Boolean isFamilyHeadAadharNumberRegistered;
    private Boolean isMobileNumberRegistered;
    private String localName;
    private String name;
    private String talukId;
    private String taluk;
    private String ltaluk;
    private String village;
    private Long villageId;
    private String lvillage;
    private Integer numOfAdults;
    private Integer numOfChild;
    private Integer numOfCylinder;
    private String oldRationNumber;
    private String stateId;
    private String ufc;

}
