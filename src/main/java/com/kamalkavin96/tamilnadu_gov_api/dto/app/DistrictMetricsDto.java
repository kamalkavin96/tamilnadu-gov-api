package com.kamalkavin96.tamilnadu_gov_api.dto.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class DistrictMetricsDto {

    private Long noOfAadhaarRegistered;
    private Long noOfCards;
    private Long noOfFpsStore;
    private Long noOfMembers;
    private Long noOfMobileNumberRegistered;
    private Long noOfGodowns;
    
}
