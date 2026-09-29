package com.kamalkavin96.tamilnadu_gov_api.dto.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class BeneficiaryCardTypeDto {

    private BeneficiaryCardTypeGroupDto cardTypeGroupDto;
    private String description;
    private String ldescription;
    private String type;
    private Long id;
}   
