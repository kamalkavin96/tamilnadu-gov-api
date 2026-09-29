package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class BeneficiaryReportDto {

    private BeneficiaryDetailsDto beneficiryDetails;
    private BeneficiaryAddressDto beneficiaryAddress;
    private BeneficiaryCardTypeDto beneficiaryCardType;


}
