package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class BillDetailReportDto {

    private Double amount;
    private String billRefId;
    private String channel;
    private Long id;
    private String mode;
    private String transactionId;
    
    private BeneficiaryDetails2Dto beneficiaryDto;
    private List<BillItemDto> billItemDto;

}
