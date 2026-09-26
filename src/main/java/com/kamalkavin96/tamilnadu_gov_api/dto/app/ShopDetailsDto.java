package com.kamalkavin96.tamilnadu_gov_api.dto.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ShopDetailsDto {

    private Long id;
    private String name;
    

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String taluk;
    private String ltaluk;
    private String village;
    private String lvillage;
    private Long villageId;
    private String district;
    private String ldistrict;

    private String agencyName;
    private String godownName;

    private String latitude;
    private String longitude;

    private String fpsCategory;
    private String fpsType;




}
