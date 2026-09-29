package com.kamalkavin96.tamilnadu_gov_api.dto.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class StateReportDto {
    private StateMetricsDto stateMetrics;
    private List<DistrictDto> districtList;
}
