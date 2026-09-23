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
            String districtName = element.get("districtName").asText();
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
                    String talukName = talukElement.get("talukName").asText();
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
                            String shopCode = ShopElement.get("fpsCode").asText();
                            Long shopId = ShopElement.get("fpsId").asLong();

                            String fpsIncharge = ShopElement.get("fpsIncharge").asText();
                            String fpsContactNo = ShopElement.get("fpsContactNo").asText();


                            try {
                                String shopResponse = pdsReportClient.getFpsLocationDetails(shopCode, shopId);
                                JsonNode shopObject = objectMapper.readTree(shopResponse);

                                Long shopNoOfAadhaarRegistered = shopObject.get("noOfAadhaarNoRegistered").asLong();
                                Long shopNumberOfBeneficiaries = shopObject.get("numberOfBeneficiaries").asLong();
                                Long shopNumberOfCards = shopObject.get("numberOfCards").asLong();
                                Long shopNoOfMobileNumberRegistered = shopObject.get("noOfMobileNoRegistered").asLong();

                                JsonNode fpsStoreDto = shopObject.get("fpsStoreDto");

                                String shopName = fpsStoreDto.get("name").asText();

                                String addressLine1 = fpsStoreDto.get("addressLine1").asText();
                                String addressLine2 = fpsStoreDto.get("addressLine2").asText();
                                String addressLine3 = fpsStoreDto.get("addressLine3").asText();

                                Integer pincode = Integer.parseInt(addressLine2);

                                Double latitude = fpsStoreDto.get("latitude").asDouble();
                                Double longitude = fpsStoreDto.get("longitude").asDouble();

                                String village = fpsStoreDto.get("village").asText();
                                Long villageId = fpsStoreDto.get("villageId").asLong();

                                String agencyName = fpsStoreDto.get("agencyName").asText();

                                JsonNode posOperatingHoursDto = shopObject.get("posOperatingHoursDto");

                                String firstSessionOpeningTime = posOperatingHoursDto.get("firstSessionOpeningTime").asText();
                                String firstSessionClosingTime = posOperatingHoursDto.get("firstSessionClosingTime").asText();
                                String secondSessionOpeningTime = posOperatingHoursDto.get("secondSessionOpeningTime").asText();
                                String secondSessionClosingTime = posOperatingHoursDto.get("secondSessionClosingTime").asText();




                                // Beneficiry Information

                                String cardCountResponse = pdsReportClient.getCardCounts(shopId, "english");
                                JsonNode cardCountObject = objectMapper.readTree(cardCountResponse);

                                JsonNode cardContent = cardCountObject.get("content").get(0);

                                Integer riceCards1Count = cardContent.get("Rice Cards_1").asInt();
                                Integer aayCards8Count = cardContent.get("AAY Cards_8").asInt();
                                Integer sugarCards2Count = cardContent.get("Sugar Cards_2").asInt();
                                Integer noCommodityCards5Count = cardContent.get("No Commodity Cards_5").asInt();
                                Integer policeCards3Count = cardContent.get("Police Cards_3").asInt();

                                
                                List<List<Integer>> allCardCountList = List.of(
                                    List.of(riceCards1Count, 1),
                                    List.of(aayCards8Count, 8),
                                    List.of(sugarCards2Count, 2),
                                    List.of(noCommodityCards5Count, 5),
                                    List.of(policeCards3Count, 3)
                                    );

                                //  Last Worked ---------------

                                String shopBillList = pdsReportClient.getBillList(0, 10000, shopId);

                                for (List<Integer> cardCount : allCardCountList) {
                                    Integer count = cardCount.get(0);
                                    Integer cardTypeGroupId = cardCount.get(1);
                                

                                for (int i = 0; i < count; i++) {
                                    
                                    String beneficiryResponse = pdsReportClient.getBenefs(i, 1, shopId, cardTypeGroupId);


                                    JsonNode bneficiryObject = objectMapper.readTree(beneficiryResponse);

                                    JsonNode beneficiaryList = bneficiryObject.get("beneficiaryList");

                                    beneficiaryList.elements().forEachRemaining(beneficiryElement->{

                                        String beneficiryName = beneficiryElement.get("name").asText();
                                        String beneficiryLocalName = beneficiryElement.get("localName").asText();
                                        Long beneficiryId = beneficiryElement.get("id").asLong();
                                        String beneficiryGender = beneficiryElement.get("gender").asText();

                                        Long ufcNumber = beneficiryElement.get("ufc").asLong();
                                        String oldRationNumber = beneficiryElement.get("oldRationNumber").asText();
                                        String beneficiryVillage = beneficiryElement.get("village").asText();
                                        Long beneficiryVillageId = beneficiryElement.get("villageId").asLong();

                                        // String encryptedUfc = riceBeneficiryElement.get("ucf").asLong();


                                        JsonNode beneficiaryAddressDto = beneficiryElement.get("beneficiaryAddressDto");

                                        String beneficiaryAddressLine1 = beneficiaryAddressDto.get("addressLine1").asText();
                                        String beneficiaryAddressLine2 = beneficiaryAddressDto.get("addressLine2").asText();
                                        String beneficiaryAddressLine3 = beneficiaryAddressDto.get("addressLine3").asText();
                                        Integer pinCode = beneficiaryAddressDto.get("pincode").asInt();

                                        String fatherOrSpouseName = beneficiaryAddressDto.get("fatherOrSpouseName").asText();
                                        String familyHeadName = beneficiaryAddressDto.get("familyHeadName").asText();





                                        JsonNode cardTypeDto = beneficiryElement.get("cardTypeDto");
                                        Integer cardId = cardTypeDto.get("id").asInt();
                                        String cardDescription = cardTypeDto.get("description").asText();
                                        String cardLocalDescription = cardTypeDto.get("ldescription").asText();


                                        JsonNode cardTypeGroupDto = cardTypeDto.get("cardTypeGroupDto");

                                        String cardGroupName = cardTypeGroupDto.get("groupName").asText();
                                        String cardLocalGroupName = cardTypeGroupDto.get("lgroupName").asText();


                                    });

                                }
                            }




                                

                                String aayBeneficiryResponse = pdsReportClient.getBenefs(0, 1, shopId, 8);

                                String sugarBeneficiryResponse = pdsReportClient.getBenefs(0, 1, shopId, 2);

                                String noCommodityBeneficiryResponse = pdsReportClient.getBenefs(0, 1, shopId, 5);

                                String policBeneficiryResponse = pdsReportClient.getBenefs(0, 1, shopId, 3);



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
