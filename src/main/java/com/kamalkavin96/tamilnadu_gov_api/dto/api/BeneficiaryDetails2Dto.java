package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class BeneficiaryDetails2Dto {

    private Boolean active;
    private String aregisterNumber;
    private String encryptedUfc;
    private String familyHeadAadharNumber;
    private String gender;
    private Long id;
    private String mobileNumber;
    private Integer numOfAdults;
    private Integer numOfChild;
    private Integer numOfCylinder;
    private String oldRationNumber;
    private String ufc;
    

}
