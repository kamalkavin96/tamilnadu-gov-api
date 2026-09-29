package com.kamalkavin96.tamilnadu_gov_api.dto.app;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FpsLocationDetailsReportDto {

    private Long closedServiceReqTktCnt;
    private Long  closedTktCount;
    private Long  openServiceReqTktCnt;
    private Long  openTktCount;

    private Long  noOfAadhaarNoRegistered;
    private Long  noOfMobileNoRegistered;
    private Long  numberOfBeneficiaries;
    private Long  numberOfCards;

    private FpsStoreDto fpsStoreDto;
    private PosOperatingHoursDto posOperatingHoursDto;
    private List<ProductCollectionDto> productDtoCollection;

}
