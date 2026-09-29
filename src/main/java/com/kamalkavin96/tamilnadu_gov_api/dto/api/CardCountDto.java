package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class CardCountDto {

    private Long AAYCards8;
    private Long noCommodityCards5;
    private Long PoliceCards3;
    private Long RiceCards1;
    private Long SugarCards2;
    

}
