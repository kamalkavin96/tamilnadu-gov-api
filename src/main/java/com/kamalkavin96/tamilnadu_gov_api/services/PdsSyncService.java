package com.kamalkavin96.tamilnadu_gov_api.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalkavin96.tamilnadu_gov_api.clients.tnpds.PDSReportClient;
import com.kamalkavin96.tamilnadu_gov_api.repositories.BeneficiaryRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.DistrictRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.StateRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.TalukRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.VillageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdsSyncService {

    private final PDSReportClient pdsReportClient;

    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final TalukRepository talukRepository;
    private final VillageRepository villageRepository;
    private final ShopRepository shopRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    private final StateService stateService;
    private final DistrictService districtService;
    private final TalukService talukService;
    private final VillageService villageService;
    private final ShopService shopService;
    private final BeneficiaryService beneficiaryService;


    public void syncStates() throws IOException, InterruptedException{
        
        
        log.info("Fetching state PDS report...");

        String response = pdsReportClient.getStateListReport();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonObject = objectMapper.readTree(response);

        // State Information
        Long noOfDistrict = jsonObject.get("noOfDistrict").asLong();
        Long noOfAadhaarRegistered = jsonObject.get("noOfAadhaarRegistered").asLong();
        Long noOfFpsStore = jsonObject.get("noOfFpsStore").asLong();
        Long noOfMembers = jsonObject.get("noOfMembers").asLong();
        Long noOfCards = jsonObject.get("noOfCards").asLong();
        Long noOfTaluk = jsonObject.get("noOfTaluk").asLong();
        Long noOfMobileNumberRegistered = jsonObject.get("noOfMobileNumberRegistered").asLong();

        JsonNode districtList = jsonObject.get("stateLevelStatsReportDtoList");

        // District information
        districtList.elements().forEachRemaining(element->{
            String districtName = element.get("districtName").toString();
            Long districtId = element.get("districtId").asLong();

            try {
                String districtResponse = pdsReportClient.getDistrictReport(districtId, districtName);

                JsonNode districtObject= objectMapper.readTree(districtResponse);

                Long districtNoOfAadhaarRegistered = districtObject.get("noOfAadhaarRegistered").asLong();
                Long districtNoOfFpsStore = districtObject.get("noOfFpsStore").asLong();
                Long districtNoOfMembers = districtObject.get("noOfMembers").asLong();
                Long districtNoOfCards = districtObject.get("noOfCards").asLong();
                Long districtNoOfTaluk = districtObject.get("noOfTaluk").asLong();
                Long districtNoOfMobileNumberRegistered = districtObject.get("noOfMobileNumberRegistered").asLong();


                JsonNode talukList = districtObject.get("stateLevelStatsReportDtoList");

                talukList.elements().forEachRemaining(talukElement->{
                    String talukName = talukElement.get("talukName").toString();
                    Long talukId = talukElement.get("talukId").asLong();

                    try {
                        String talukResponse = pdsReportClient.getTalukReport(talukId);

                        JsonNode talukObject = objectMapper.readTree(talukResponse);

                        Long talukNoOfAadhaarRegistered = talukObject.get("noOfAadhaarRegistered").asLong();
                        Long talukNoOfFpsStore = talukObject.get("noOfFpsStore").asLong();
                        Long talukNoOfMembers = talukObject.get("noOfMembers").asLong();
                        Long talukNoOfCards = talukObject.get("noOfCards").asLong();
                        Long talukNoOfMobileNumberRegistered = talukObject.get("noOfMobileNumberRegistered").asLong();


                        JsonNode shopList = talukObject.get("stateLevelStatsReportDtoList");

                        shopList.elements().forEachRemaining(ShopElement->{
                            String shopCode = ShopElement.get("fpsCode").toString();
                            Long shopId = ShopElement.get("fpsId").asLong();

                            String fpsIncharge = ShopElement.get("fpsIncharge").toString();
                            String fpsContactNo = ShopElement.get("fpsContactNo").toString();


                            try {
                                String shopResponse = pdsReportClient.getFpsLocationDetails(shopCode, shopId);
                                JsonNode shopObject = objectMapper.readTree(shopResponse);

                                Long shopNoOfAadhaarRegistered = shopObject.get("noOfAadhaarRegistered").asLong();
                                Long shopNoOfMembers = shopObject.get("noOfMembers").asLong();
                                Long shopNoOfCards = shopObject.get("noOfCards").asLong();
                                Long shopNoOfMobileNumberRegistered = shopObject.get("noOfMobileNumberRegistered").asLong();

                                JsonNode fpsStoreDto = shopObject.get("noOfMobileNumberRegistered");

                                String shopName = fpsStoreDto.get("name").toString();

                                String addressLine1 = fpsStoreDto.get("addressLine1").toString();
                                String addressLine2 = fpsStoreDto.get("addressLine2").toString();
                                String addressLine3 = fpsStoreDto.get("addressLine3").toString();

                                Double latitude = fpsStoreDto.get("latitude").asDouble();
                                Double longitude = fpsStoreDto.get("longitude").asDouble();

                                String village = fpsStoreDto.get("village").toString();
                                Long villageId = fpsStoreDto.get("villageId").asLong();

                                JsonNode posOperatingHoursDto = fpsStoreDto.get("posOperatingHoursDto");

                                String firstSessionOpeningTime = posOperatingHoursDto.get("firstSessionOpeningTime").toString();
                                String firstSessionClosingTime = posOperatingHoursDto.get("firstSessionClosingTime").toString();
                                String secondSessionOpeningTime = posOperatingHoursDto.get("secondSessionOpeningTime").toString();
                                String secondSessionClosingTime = posOperatingHoursDto.get("secondSessionClosingTime").toString();







                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }

                        });


                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                });


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        });

        



    };




    // syncDistricts();
    // syncTaluks();
    // syncVillages();
    // syncShops();

}
