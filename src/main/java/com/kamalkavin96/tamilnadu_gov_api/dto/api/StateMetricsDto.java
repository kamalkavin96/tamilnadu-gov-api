package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class StateMetricsDto {

    private Long noOfDistrict;
    private Long noOfTaluk;
    private Long noOfFpsStore;
    private Long noOfCards;
    private Long noOfMembers;
    private Long noOfAadhaarRegistered;
    private Long noOfMobileNumberRegistered;
    
}
