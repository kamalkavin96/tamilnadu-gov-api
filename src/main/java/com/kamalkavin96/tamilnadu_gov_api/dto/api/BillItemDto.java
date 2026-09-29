package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class BillItemDto {

    private Double cost;
    private String lname;
    private String lproductUnit;
    private String name;
    private String product_unit;
    private String quantity;


}
