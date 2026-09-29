package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class BeneficiaryAddressDto {
    
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private Long districtId;
    private String districtName;
    private String familyHeadName;
    private String fatherOrSpouseName;
    private String fpsDistrictId;
    private Long fpsTalukId;
    private Long fpsVillageId;
    private String ldistrictName;
    private String ltalukName;
    private String lvillageName;
    private String pincode;
    private Long talukId;
    private String talukName;
    private Long villageId;
    private String villageName;


}
