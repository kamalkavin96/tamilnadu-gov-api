package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ShopMetricsDto {

    private Long noOfAadhaarNoRegistered;
    private Long noOfMobileNoRegistered;
    private Long numberOfBeneficiaries;
    private Long numberOfCards;
    private String onlineStatus;
    private Long openServiceReqTktCnt;
    private Long openTktCount;


}
