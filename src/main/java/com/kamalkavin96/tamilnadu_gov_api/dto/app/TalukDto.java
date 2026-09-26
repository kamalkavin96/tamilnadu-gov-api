package com.kamalkavin96.tamilnadu_gov_api.dto.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class TalukDto {

    private Long talukId;
    private String talukName;
    private String ltalukName;
    

    private Long noOfAadhaarRegistered;
    private Long noOfMobileNumberRegistered;
    private Long numberOfBeneficiaries;
    private Long numberOfCards;
    private Long numberOfFps;

}
