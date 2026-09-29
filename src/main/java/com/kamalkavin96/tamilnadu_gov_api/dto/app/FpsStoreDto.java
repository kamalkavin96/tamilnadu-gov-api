package com.kamalkavin96.tamilnadu_gov_api.dto.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FpsStoreDto {

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String agencyName;
    private String closingTime;
    private String openingTime;
    private String code;
    private String contactPerson;
    private String phoneNumber;
    private String district;
    private String ldistrict;
    private String fpsCategory;
    private String fpsType;
    private String godownName;
    private String latitude;
    private String longitude;
    private String taluk;
    private String ltaluk;
    private Long villageId;
    private String village;
    private String lvillage;
    private String name;

}
