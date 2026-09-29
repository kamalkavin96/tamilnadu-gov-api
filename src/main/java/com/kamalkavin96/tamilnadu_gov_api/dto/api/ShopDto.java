package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDto {

    private Long fpsId;
    private String fpsCode;
    private String fpsName;

    private Long noOfAadhaarRegistered;
    private Long noOfMobileNumberRegistered;
    private Long numberOfBeneficiaries;
    private Long numberOfCards;

}
