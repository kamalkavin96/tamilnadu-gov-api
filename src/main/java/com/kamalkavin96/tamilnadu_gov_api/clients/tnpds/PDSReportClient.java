package com.kamalkavin96.tamilnadu_gov_api.clients.tnpds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.apache.bcel.generic.Type;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalkavin96.tamilnadu_gov_api.configuration.tnpds.HttpHeadersConfiguration;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.BeneficiaryAddressDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.BeneficiaryCardTypeDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.BeneficiaryDetailsDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.BeneficiaryReportDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.BillDetailReportDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.CardCountDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.DistrictDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.DistrictMetricsDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.DistrictReportDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.FpsLocationDetailsReportDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.ProductDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.ShopDetailsDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.ShopDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.ShopInchargeDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.ShopMetricsDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.ShopOperatingHoursDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.ShopReportDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.StateMetricsDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.StateReportDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.TalukDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.TalukMetricsDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.app.TalukReportDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PDSReportClient {

        private final HttpHeadersConfiguration headersConfiguration;

        ObjectMapper objectMapper = new ObjectMapper();

        public String getStateListReport()
                        throws IOException, InterruptedException {

                String requestBody = """
                                {}
                                """;

                return headersConfiguration.makePostRequest(
                                "/portalreport/stateReport",
                                requestBody);
        }

        

        /**
         * Get district-level PDS report.
         */
        public String getDistrictReport(Long districtId, String districtName) throws IOException, InterruptedException {

                String requestBody = String.format("{\"districtId\": %d, \"districtName\": \"%s\"}", districtId,
                                districtName);
                return headersConfiguration.makePostRequest("/portalreport/districtReport", requestBody);
        }

        /**
         * Get taluk-level PDS report.
         */
        public String getTalukReport(Long talukId)
                        throws IOException, InterruptedException {

                String requestBody = String.format("""
                                {
                                    "talukId": %d
                                }
                                """, talukId);

                return headersConfiguration.makePostRequest(
                                "/portalreport/talukReport",
                                requestBody);
        }

        /**
         * Get card counts for an FPS.
         */
        public String getCardCounts(Long sfpsCode, String language)
                        throws IOException, InterruptedException {

                String requestBody = String.format("""
                                {
                                    "sfpscode": %d,
                                    "language": "%s"
                                }
                                """, sfpsCode, language);

                return headersConfiguration.makePostRequest(
                                "/portalreport/cardcounts",
                                requestBody);
        }

        /**
         * Get beneficiaries.
         */
        public String getBenefs(
                        Integer page,
                        Integer size,
                        Long sfpsCode,
                        Integer cardTypeGroupId) throws IOException, InterruptedException {

                String requestBody = String.format("""
                                {
                                    "page": %d,
                                    "size": %d,
                                    "sfpscode": %d,
                                    "cardTypeGroupId": %d
                                }
                                """,
                                page,
                                size,
                                sfpsCode,
                                cardTypeGroupId);

                return headersConfiguration.makePostRequest(
                                "/portalreport/getbenefs",
                                requestBody);
        }

        /**
         * Get FPS location details.
         */
        public String getFpsLocationDetails(String code, Long id)
                        throws IOException, InterruptedException {

                String requestBody = String.format("""
                                {
                                    "code": "%s",
                                    "id": %d
                                }
                                """, code, id);

                return headersConfiguration.makePostRequest(
                                "/portal/fpslocationdetails",
                                requestBody);
        }

        /**
         * Get FPS bill list.
         */
        public String getBillList(
                        Integer page,
                        Integer size,
                        Long fpsId) throws IOException, InterruptedException {

                String requestBody = String.format("""
                                {
                                    "fpsId": %d,
                                    "paginationDto": {
                                        "page": %d,
                                        "size": %d
                                    }
                                }
                                """,
                                fpsId,
                                page,
                                size);

                return headersConfiguration.makePostRequest(
                                "/report/getBillList",
                                requestBody);
        }





        public StateReportDto getStateListReportObj() throws IOException, InterruptedException {

                String stateReportResponse = headersConfiguration.makePostRequest("/portalreport/stateReport", "{}");
                JsonNode stateReportNode = objectMapper.readTree(stateReportResponse);
                JsonNode districtListNode = stateReportNode.get("stateLevelStatsReportDtoList");
                return new StateReportDto(
                        objectMapper.convertValue(stateReportNode,new TypeReference<StateMetricsDto>() {}), 
                        objectMapper.convertValue(districtListNode,new TypeReference<List<DistrictDto>>() {})
                );

        }

        public DistrictReportDto getDistrictReportObj(Long districtId, String districtName) throws IOException, InterruptedException {

                String requestBody = String.format("{\"districtId\": %d, \"districtName\": \"%s\"}", districtId, districtName);
                String districtReportResponse = headersConfiguration.makePostRequest("/portalreport/districtReport", requestBody);
                JsonNode districtReportNode = objectMapper.readTree(districtReportResponse);
                JsonNode talukListNode = districtReportNode.get("stateLevelStatsReportDtoList");
                return new DistrictReportDto(
                        objectMapper.convertValue(districtReportNode, new TypeReference<DistrictMetricsDto>() {}),
                        objectMapper.convertValue(talukListNode, new TypeReference<List<TalukDto>>() {})
                );

        }

        public TalukReportDto getTalukReportObj(Long talukId) throws IOException, InterruptedException {

                String requestBody = String.format("{\"talukId\": %d}", talukId);
                String talukReportResponse = headersConfiguration.makePostRequest("/portalreport/talukReport", requestBody);
                JsonNode talukResponseNode = objectMapper.readTree(talukReportResponse);
                JsonNode shopListNode = talukResponseNode.get("stateLevelStatsReportDtoList");
                return new TalukReportDto(
                        objectMapper.convertValue(talukReportResponse, new TypeReference<TalukMetricsDto>() {}),
                        objectMapper.convertValue(shopListNode, new TypeReference<List<ShopDto>>() {})
                );

        }

        public ShopReportDto getShopReportObj(String code, Long id) throws IOException, InterruptedException {

                String requestBody = String.format("{\"code\": \"%s\",\"id\": %d}", code, id);
                String shopReportResponse =  headersConfiguration.makePostRequest("/portal/fpslocationdetails", requestBody);
                JsonNode shopReportNode = objectMapper.readTree(shopReportResponse);
                JsonNode shopDetailsNode = shopReportNode.get("fpsStoreDto");
                JsonNode shopOperatingHoursNode = shopReportNode.get("posOperatingHoursDto");
                JsonNode shopProductListNode = shopReportNode.get("productDtoCollection");

                return new ShopReportDto(
                        objectMapper.convertValue(shopReportNode, new TypeReference<ShopMetricsDto>() {}),
                        objectMapper.convertValue(shopDetailsNode, new TypeReference<ShopDetailsDto>() {}),
                        objectMapper.convertValue(shopDetailsNode, new TypeReference<ShopInchargeDto>() {}),
                        objectMapper.convertValue(shopOperatingHoursNode, new TypeReference<ShopOperatingHoursDto>() {}),
                        objectMapper.convertValue(shopProductListNode, new TypeReference<List<ProductDto>>() {})
                );

        }

        public CardCountDto getCardCountsObj(Long sfpsCode, String language) throws IOException, InterruptedException {

                String requestBody = String.format("{\"sfpscode\": %d,\"language\": \"%s\"}", sfpsCode, language);
                String cardCountResponse = headersConfiguration.makePostRequest("/portalreport/cardcounts",requestBody);
                JsonNode cardCountNode = objectMapper.readTree(cardCountResponse).get("content").get(0);
                
                return new CardCountDto(
                        cardCountNode.get("AAY Cards_8").asLong(), 
                        cardCountNode.get("No Commodity Cards_5").asLong(),
                        cardCountNode.get("Police Cards_3").asLong(),
                        cardCountNode.get("Rice Cards_1").asLong(),
                        cardCountNode.get("Sugar Cards_2").asLong()
                );

        }

        public BeneficiaryReportDto getBenefsObj( Integer page, Integer size, Long sfpsCode, Integer cardTypeGroupId) 
                throws IOException, InterruptedException {

                String requestBody = String.format("{\"page\": %d,\"size\": %d,\"sfpscode\": %d,\"cardTypeGroupId\": %d}",
                                page, size, sfpsCode, cardTypeGroupId);

                
                String beneficiaryResponse = headersConfiguration.makePostRequest("/portalreport/getbenefs", requestBody);
                JsonNode beneficaryNode = objectMapper.readTree(beneficiaryResponse).get("beneficiaryList").get(0);
                
                return new BeneficiaryReportDto(
                        objectMapper.convertValue(beneficaryNode, new TypeReference<BeneficiaryDetailsDto>() {}),
                        objectMapper.convertValue(beneficaryNode.get("beneficiaryAddressDto"), new TypeReference<BeneficiaryAddressDto>() {}),
                        objectMapper.convertValue(beneficaryNode.get("cardTypeDto"), new TypeReference<BeneficiaryCardTypeDto>() {})
                );
        }


        public FpsLocationDetailsReportDto getFpsLocationDetailsObj(String code, Long id) throws IOException, InterruptedException {

                String requestBody = String.format("{\"code\": \"%s\",\"id\": %d}", code, id);
                String fpsLocationDetailsResponse =  headersConfiguration.makePostRequest("/portal/fpslocationdetails",requestBody);
                JsonNode fpsLocationDetailsNode = objectMapper.readTree(fpsLocationDetailsResponse);

                return objectMapper.convertValue(fpsLocationDetailsNode, new TypeReference<FpsLocationDetailsReportDto>() {});

        }

        public List<BillDetailReportDto>  getBillListObj( Integer page, Integer size, Long fpsId) throws IOException, InterruptedException {

                String requestBody = String.format("{\"fpsId\": %d,\"paginationDto\": {\"page\": %d,\"size\": %d}}",
                                fpsId, page, size);
                String billListResponse = headersConfiguration.makePostRequest("/report/getBillList", requestBody);
                JsonNode billListNode = objectMapper.readTree(billListResponse);

                return objectMapper.convertValue(billListNode.get("billDtoList"), new TypeReference<List<BillDetailReportDto>>() {});

        }





}