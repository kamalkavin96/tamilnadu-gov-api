package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductDto {

    private String code;
    private String name;
    private String lproductName;
    
}
