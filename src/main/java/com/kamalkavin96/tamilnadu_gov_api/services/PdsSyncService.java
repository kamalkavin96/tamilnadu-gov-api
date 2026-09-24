package com.kamalkavin96.tamilnadu_gov_api.services;

import com.kamalkavin96.tamilnadu_gov_api.repositories.StateMetricsRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalkavin96.tamilnadu_gov_api.clients.tnpds.PDSReportClient;
import com.kamalkavin96.tamilnadu_gov_api.models.District;
import com.kamalkavin96.tamilnadu_gov_api.models.DistrictMetrics;
import com.kamalkavin96.tamilnadu_gov_api.models.State;
import com.kamalkavin96.tamilnadu_gov_api.models.StateMetrics;
import com.kamalkavin96.tamilnadu_gov_api.models.Taluk;
import com.kamalkavin96.tamilnadu_gov_api.models.TalukMetrics;
import com.kamalkavin96.tamilnadu_gov_api.repositories.BeneficiaryRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.DistrictMetricsRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.DistrictRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.StateRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.TalukMetricsRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.TalukRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.VillageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdsSyncService {

        private final StateMetricsRepository stateMetricsRepository;

        private final PDSReportClient pdsReportClient;

        private final StateRepository stateRepository;
        private final DistrictRepository districtRepository;
        private final TalukRepository talukRepository;
        private final VillageRepository villageRepository;
        private final ShopRepository shopRepository;
        private final BeneficiaryRepository beneficiaryRepository;
        private final DistrictMetricsRepository districtMetricsRepository;
        private final TalukMetricsRepository talukMetricsRepository;

        private final StateService stateService;
        private final DistrictService districtService;
        private final TalukService talukService;
        private final VillageService villageService;
        private final ShopService shopService;
        private final BeneficiaryService beneficiaryService;

        private final ObjectMapper objectMapper = new ObjectMapper();

        public void dataSync() throws IOException, InterruptedException {

                log.info("Fetching state PDS report...");

                String response = pdsReportClient.getStateListReport();
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
                districtList.elements().forEachRemaining(element -> {
                        String districtName = element.get("districtName").asText();
                        Long districtId = element.get("districtId").asLong();

                        try {
                                String districtResponse = pdsReportClient.getDistrictReport(districtId, districtName);

                                JsonNode districtObject = objectMapper.readTree(districtResponse);

                                Long districtNoOfAadhaarRegistered = districtObject.get("noOfAadhaarRegistered")
                                                .asLong();
                                Long districtNoOfFpsStore = districtObject.get("noOfFpsStore").asLong();
                                Long districtNoOfMembers = districtObject.get("noOfMembers").asLong();
                                Long districtNoOfCards = districtObject.get("noOfCards").asLong();
                                Long districtNoOfTaluk = districtObject.get("noOfTaluk").asLong();
                                Long districtNoOfMobileNumberRegistered = districtObject
                                                .get("noOfMobileNumberRegistered").asLong();

                                JsonNode talukList = districtObject.get("stateLevelStatsReportDtoList");

                                talukList.elements().forEachRemaining(talukElement -> {
                                        String talukName = talukElement.get("talukName").asText();
                                        Long talukId = talukElement.get("talukId").asLong();

                                        try {
                                                String talukResponse = pdsReportClient.getTalukReport(talukId);

                                                JsonNode talukObject = objectMapper.readTree(talukResponse);

                                                Long talukNoOfAadhaarRegistered = talukObject
                                                                .get("noOfAadhaarRegistered").asLong();
                                                Long talukNoOfFpsStore = talukObject.get("noOfFpsStore").asLong();
                                                Long talukNoOfMembers = talukObject.get("noOfMembers").asLong();
                                                Long talukNoOfCards = talukObject.get("noOfCards").asLong();
                                                Long talukNoOfMobileNumberRegistered = talukObject
                                                                .get("noOfMobileNumberRegistered").asLong();

                                                JsonNode shopList = talukObject.get("stateLevelStatsReportDtoList");

                                                shopList.elements().forEachRemaining(ShopElement -> {
                                                        String shopCode = ShopElement.get("fpsCode").asText();
                                                        Long shopId = ShopElement.get("fpsId").asLong();

                                                        String fpsIncharge = ShopElement.get("fpsIncharge").asText();
                                                        String fpsContactNo = ShopElement.get("fpsContactNo").asText();

                                                        try {
                                                                String shopResponse = pdsReportClient
                                                                                .getFpsLocationDetails(shopCode,
                                                                                                shopId);
                                                                JsonNode shopObject = objectMapper
                                                                                .readTree(shopResponse);

                                                                Long shopNoOfAadhaarRegistered = shopObject
                                                                                .get("noOfAadhaarNoRegistered")
                                                                                .asLong();
                                                                Long shopNumberOfBeneficiaries = shopObject
                                                                                .get("numberOfBeneficiaries").asLong();
                                                                Long shopNumberOfCards = shopObject.get("numberOfCards")
                                                                                .asLong();
                                                                Long shopNoOfMobileNumberRegistered = shopObject
                                                                                .get("noOfMobileNoRegistered").asLong();

                                                                JsonNode fpsStoreDto = shopObject.get("fpsStoreDto");

                                                                String shopName = fpsStoreDto.get("name").asText();

                                                                String addressLine1 = fpsStoreDto.get("addressLine1")
                                                                                .asText();
                                                                String addressLine2 = fpsStoreDto.get("addressLine2")
                                                                                .asText();
                                                                String addressLine3 = fpsStoreDto.get("addressLine3")
                                                                                .asText();

                                                                Integer pincode = Integer.parseInt(addressLine2);

                                                                Double latitude = fpsStoreDto.get("latitude")
                                                                                .asDouble();
                                                                Double longitude = fpsStoreDto.get("longitude")
                                                                                .asDouble();

                                                                String village = fpsStoreDto.get("village").asText();
                                                                Long villageId = fpsStoreDto.get("villageId").asLong();

                                                                String agencyName = fpsStoreDto.get("agencyName")
                                                                                .asText();

                                                                JsonNode posOperatingHoursDto = shopObject
                                                                                .get("posOperatingHoursDto");

                                                                String firstSessionOpeningTime = posOperatingHoursDto
                                                                                .get("firstSessionOpeningTime")
                                                                                .asText();
                                                                String firstSessionClosingTime = posOperatingHoursDto
                                                                                .get("firstSessionClosingTime")
                                                                                .asText();
                                                                String secondSessionOpeningTime = posOperatingHoursDto
                                                                                .get("secondSessionOpeningTime")
                                                                                .asText();
                                                                String secondSessionClosingTime = posOperatingHoursDto
                                                                                .get("secondSessionClosingTime")
                                                                                .asText();

                                                                // Beneficiry Information

                                                                String cardCountResponse = pdsReportClient
                                                                                .getCardCounts(shopId, "english");
                                                                JsonNode cardCountObject = objectMapper
                                                                                .readTree(cardCountResponse);

                                                                JsonNode cardContent = cardCountObject.get("content")
                                                                                .get(0);

                                                                Integer riceCards1Count = cardContent
                                                                                .get("Rice Cards_1").asInt();
                                                                Integer aayCards8Count = cardContent.get("AAY Cards_8")
                                                                                .asInt();
                                                                Integer sugarCards2Count = cardContent
                                                                                .get("Sugar Cards_2").asInt();
                                                                Integer noCommodityCards5Count = cardContent
                                                                                .get("No Commodity Cards_5").asInt();
                                                                Integer policeCards3Count = cardContent
                                                                                .get("Police Cards_3").asInt();

                                                                List<List<Integer>> allCardCountList = List.of(
                                                                                List.of(riceCards1Count, 1),
                                                                                List.of(aayCards8Count, 8),
                                                                                List.of(sugarCards2Count, 2),
                                                                                List.of(noCommodityCards5Count, 5),
                                                                                List.of(policeCards3Count, 3));

                                                                // Last Worked ---------------

                                                                String shopBillListResponse = pdsReportClient
                                                                                .getBillList(0, 10000, shopId);
                                                                JsonNode shopBillListObject = objectMapper
                                                                                .readTree(shopBillListResponse);

                                                                Integer totalRecords = shopBillListObject
                                                                                .get("totalRecords").asInt();
                                                                JsonNode billDtoList = shopBillListObject
                                                                                .get("billDtoList");

                                                                Map<String, JsonNode> beneficeryAdditionaData = new HashMap<>();

                                                                billDtoList.elements().forEachRemaining(
                                                                                billDtoListElement -> {

                                                                                        Long mobileNum = billDtoListElement
                                                                                                        .get("mobileNumber")
                                                                                                        .asLong();
                                                                                        String encryptedUfc = billDtoListElement
                                                                                                        .get("encryptedUfc")
                                                                                                        .asText();
                                                                                        String familyHeadAadharNumber = billDtoListElement
                                                                                                        .get("familyHeadAadharNumber")
                                                                                                        .asText();

                                                                                        beneficeryAdditionaData.put(
                                                                                                        billDtoListElement
                                                                                                                        .get("ufc")
                                                                                                                        .asText(),
                                                                                                        talukList);

                                                                                });

                                                                System.exit(0);

                                                                for (List<Integer> cardCount : allCardCountList) {
                                                                        Integer count = cardCount.get(0);
                                                                        Integer cardTypeGroupId = cardCount.get(1);

                                                                        log.info("Count: {}", count);

                                                                        for (int i = 0; i < count; i++) {

                                                                                String beneficiryResponse = pdsReportClient
                                                                                                .getBenefs(i, 1, shopId,
                                                                                                                cardTypeGroupId);

                                                                                JsonNode bneficiryObject = objectMapper
                                                                                                .readTree(beneficiryResponse);

                                                                                JsonNode beneficiaryList = bneficiryObject
                                                                                                .get("beneficiaryList");

                                                                                beneficiaryList.elements()
                                                                                                .forEachRemaining(
                                                                                                                beneficiryElement -> {

                                                                                                                        String beneficiryName = beneficiryElement
                                                                                                                                        .get("name")
                                                                                                                                        .asText();
                                                                                                                        String beneficiryLocalName = beneficiryElement
                                                                                                                                        .get("localName")
                                                                                                                                        .asText();
                                                                                                                        Long beneficiryId = beneficiryElement
                                                                                                                                        .get("id")
                                                                                                                                        .asLong();
                                                                                                                        String beneficiryGender = beneficiryElement
                                                                                                                                        .get("gender")
                                                                                                                                        .asText();

                                                                                                                        Long ufcNumber = beneficiryElement
                                                                                                                                        .get("ufc")
                                                                                                                                        .asLong();
                                                                                                                        String oldRationNumber = beneficiryElement
                                                                                                                                        .get("oldRationNumber")
                                                                                                                                        .asText();
                                                                                                                        String beneficiryVillage = beneficiryElement
                                                                                                                                        .get("village")
                                                                                                                                        .asText();
                                                                                                                        Long beneficiryVillageId = beneficiryElement
                                                                                                                                        .get("villageId")
                                                                                                                                        .asLong();

                                                                                                                        Integer numOfAdults = beneficiryElement
                                                                                                                                        .get("numOfAdults")
                                                                                                                                        .asInt();
                                                                                                                        Integer numOfChild = beneficiryElement
                                                                                                                                        .get("numOfChild")
                                                                                                                                        .asInt();
                                                                                                                        Integer numOfCylinder = beneficiryElement
                                                                                                                                        .get("numOfCylinder")
                                                                                                                                        .asInt();

                                                                                                                        // String
                                                                                                                        // encryptedUfc
                                                                                                                        // =
                                                                                                                        // riceBeneficiryElement.get("ucf").asLong();

                                                                                                                        JsonNode beneficiaryAddressDto = beneficiryElement
                                                                                                                                        .get("beneficiaryAddressDto");

                                                                                                                        if (!beneficiaryAddressDto
                                                                                                                                        .isNull()) {
                                                                                                                                String beneficiaryAddressLine1 = beneficiaryAddressDto
                                                                                                                                                .get("addressLine1")
                                                                                                                                                .asText();
                                                                                                                                String beneficiaryAddressLine2 = beneficiaryAddressDto
                                                                                                                                                .get("addressLine2")
                                                                                                                                                .asText();
                                                                                                                                String beneficiaryAddressLine3 = beneficiaryAddressDto
                                                                                                                                                .get("addressLine3")
                                                                                                                                                .asText();
                                                                                                                                Integer pinCode = beneficiaryAddressDto
                                                                                                                                                .get("pincode")
                                                                                                                                                .asInt();
                                                                                                                                String fatherOrSpouseName = beneficiaryAddressDto
                                                                                                                                                .get("fatherOrSpouseName")
                                                                                                                                                .asText();
                                                                                                                                String familyHeadName = beneficiaryAddressDto
                                                                                                                                                .get("familyHeadName")
                                                                                                                                                .asText();
                                                                                                                        }

                                                                                                                        JsonNode cardTypeDto = beneficiryElement
                                                                                                                                        .get("cardTypeDto");
                                                                                                                        Integer cardId = cardTypeDto
                                                                                                                                        .get("id")
                                                                                                                                        .asInt();
                                                                                                                        String cardDescription = cardTypeDto
                                                                                                                                        .get("description")
                                                                                                                                        .asText();
                                                                                                                        String cardLocalDescription = cardTypeDto
                                                                                                                                        .get("ldescription")
                                                                                                                                        .asText();
                                                                                                                        JsonNode cardTypeGroupDto = cardTypeDto
                                                                                                                                        .get("cardTypeGroupDto");
                                                                                                                        String cardGroupName = cardTypeGroupDto
                                                                                                                                        .get("groupName")
                                                                                                                                        .asText();
                                                                                                                        String cardLocalGroupName = cardTypeGroupDto
                                                                                                                                        .get("lgroupName")
                                                                                                                                        .asText();

                                                                                                                });

                                                                        }
                                                                }

                                                                String aayBeneficiryResponse = pdsReportClient
                                                                                .getBenefs(0, 1, shopId, 8);
                                                                String sugarBeneficiryResponse = pdsReportClient
                                                                                .getBenefs(0, 1, shopId, 2);
                                                                String noCommodityBeneficiryResponse = pdsReportClient
                                                                                .getBenefs(0, 1, shopId, 5);
                                                                String policBeneficiryResponse = pdsReportClient
                                                                                .getBenefs(0, 1, shopId, 3);

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

        }

        public void syncStates() throws IOException, InterruptedException {

                final String sourceId = "1";
                final String stateName = "Tamilnadu";
                final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

                log.info("Starting state synchronization. sourceId={}, stateName={}",
                                sourceId,
                                stateName);

                Optional<State> existingState = stateRepository.findBySourceId(sourceId);
                State state;

                if (existingState.isPresent()) {
                        state = existingState.get();
                        log.info(
                                        "State found. id={}, sourceId={}, name={}",
                                        state.getId(),
                                        state.getSourceId(),
                                        state.getName());

                } else {

                        state = new State();
                        state.setSourceId(sourceId);
                        state.setName(stateName);
                        state.setCreatedAt(now);
                        log.info(
                                        "State not found. Creating new state. sourceId={}, name={}",
                                        sourceId,
                                        stateName);
                }

                state.setSourceId(sourceId);
                state.setName(stateName);
                state.setUpdatedAt(now);

                State stateSaved = stateRepository.save(state);
                log.info(
                                "State saved successfully. id={}, sourceId={}, name={}",
                                stateSaved.getId(),
                                stateSaved.getSourceId(),
                                stateSaved.getName());

                log.info(
                                "Fetching state report from TNPDS. stateId={}, sourceId={}",
                                stateSaved.getId(),
                                sourceId);

                String response = pdsReportClient.getStateListReport();

                log.info(
                                "State report received successfully. stateId={}",
                                stateSaved.getId());

                JsonNode jsonObject = objectMapper.readTree(response);

                Long noOfDistrict = jsonObject.get("noOfDistrict").asLong();
                Long noOfAadhaarRegistered = jsonObject.get("noOfAadhaarRegistered").asLong();
                Long noOfFpsStore = jsonObject.get("noOfFpsStore").asLong();
                Long noOfMembers = jsonObject.get("noOfMembers").asLong();
                Long noOfCards = jsonObject.get("noOfCards").asLong();
                Long noOfTaluk = jsonObject.get("noOfTaluk").asLong();
                Long noOfMobileNumberRegistered = jsonObject.get("noOfMobileNumberRegistered").asLong();

                log.info(
                                "State report parsed. " +
                                                "districts={}, taluks={}, fpsStores={}, members={}, " +
                                                "cards={}, aadhaarRegistered={}, mobileRegistered={}",
                                noOfDistrict,
                                noOfTaluk,
                                noOfFpsStore,
                                noOfMembers,
                                noOfCards,
                                noOfAadhaarRegistered,
                                noOfMobileNumberRegistered);

                Optional<StateMetrics> existingMetrics = stateMetricsRepository.findByStateId(stateSaved.getId());
                StateMetrics stateMetrics;

                if (existingMetrics.isPresent()) {
                        stateMetrics = existingMetrics.get();
                        log.info(
                                        "State metrics found. metricsId={}, stateId={}",
                                        stateMetrics.getId(),
                                        stateSaved.getId());
                } else {
                        stateMetrics = new StateMetrics();
                        stateMetrics.setState(stateSaved);
                        log.info(
                                        "State metrics not found. Creating new metrics. stateId={}",
                                        stateSaved.getId());
                }

                stateMetrics.setState(stateSaved);
                stateMetrics.setBeneficiariesCount(noOfMembers);
                stateMetrics.setFamilyCardCount(noOfCards);
                stateMetrics.setFairPriceShopCount(noOfFpsStore);
                stateMetrics.setTalukCount(noOfTaluk);
                stateMetrics.setDistrictCount(noOfDistrict);
                stateMetrics.setAadharRegCount(noOfAadhaarRegistered);
                stateMetrics.setMobileRegCount(noOfMobileNumberRegistered);

                StateMetrics savedMetrics = stateMetricsRepository.save(stateMetrics);

                log.info(
                                "State metrics saved successfully. " +
                                                "metricsId={}, stateId={}, beneficiaries={}, cards={}, " +
                                                "districts={}, taluks={}, fpsStores={}",
                                savedMetrics.getId(),
                                stateSaved.getId(),
                                savedMetrics.getBeneficiariesCount(),
                                savedMetrics.getFamilyCardCount(),
                                savedMetrics.getDistrictCount(),
                                savedMetrics.getTalukCount(),
                                savedMetrics.getFairPriceShopCount());

                log.info("State synchronization completed successfully. stateId={}, sourceId={}",
                                stateSaved.getId(),
                                sourceId);

        }

        public void syncDistricts() throws IOException, InterruptedException {

                final String stateSourceId = "1";
                final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

                log.info("Starting district synchronization. stateSourceId={}", stateSourceId);

                State state = stateRepository.findBySourceId(stateSourceId)
                                .orElseThrow(() -> new IllegalStateException(
                                                "State not found for sourceId=" + stateSourceId));

                log.info(
                                "Parent state found. stateId={}, sourceId={}, name={}",
                                state.getId(),
                                state.getSourceId(),
                                state.getName());

                String response = pdsReportClient.getStateListReport();

                JsonNode jsonObject = objectMapper.readTree(response);

                JsonNode districtList = jsonObject.get("stateLevelStatsReportDtoList");

                if (districtList == null || !districtList.isArray()) {
                        log.warn("No district list found in state report");
                        return;
                }

                for (JsonNode districtElement : districtList) {

                        String districtName = districtElement.get("districtName").asText();

                        Long districtSourceId = districtElement.get("districtId").asLong();

                        log.info(
                                        "Processing district. sourceId={}, name={}",
                                        districtSourceId,
                                        districtName);

                        District district = districtRepository
                                        .findBySourceId(String.valueOf(districtSourceId))
                                        .orElseGet(District::new);

                        district.setSourceId(String.valueOf(districtSourceId));
                        district.setName(districtName);
                        district.setState(state);

                        District savedDistrict = districtRepository.save(district);

                        log.info(
                                        "District saved. id={}, sourceId={}, name={}",
                                        savedDistrict.getId(),
                                        savedDistrict.getSourceId(),
                                        savedDistrict.getName());

                        String districtResponse = pdsReportClient.getDistrictReport(
                                        districtSourceId,
                                        districtName);

                        JsonNode districtObject = objectMapper.readTree(districtResponse);

                        Long noOfAadhaarRegistered = districtObject
                                        .get("noOfAadhaarRegistered")
                                        .asLong();

                        Long noOfFpsStore = districtObject
                                        .get("noOfFpsStore")
                                        .asLong();

                        Long noOfMembers = districtObject
                                        .get("noOfMembers")
                                        .asLong();

                        Long noOfCards = districtObject
                                        .get("noOfCards")
                                        .asLong();

                        Long noOfTaluk = districtObject
                                        .get("noOfTaluk")
                                        .asLong();

                        Long noOfMobileNumberRegistered = districtObject
                                        .get("noOfMobileNumberRegistered")
                                        .asLong();

                        log.info(
                                        "District report parsed. districtId={}, " +
                                                        "taluks={}, fpsStores={}, members={}, " +
                                                        "cards={}, aadhaarRegistered={}, " +
                                                        "mobileRegistered={}",
                                        savedDistrict.getId(),
                                        noOfTaluk,
                                        noOfFpsStore,
                                        noOfMembers,
                                        noOfCards,
                                        noOfAadhaarRegistered,
                                        noOfMobileNumberRegistered);

                        DistrictMetrics districtMetrics = districtMetricsRepository
                                        .findByDistrictId(savedDistrict.getId())
                                        .orElseGet(DistrictMetrics::new);

                        districtMetrics.setDistrict(savedDistrict);

                        districtMetrics.setTalukCount(noOfTaluk);
                        districtMetrics.setFairPriceShopCount(noOfFpsStore);
                        districtMetrics.setFamilyCardCount(noOfCards);
                        districtMetrics.setBeneficiariesCount(noOfMembers);
                        districtMetrics.setAadharRegCount(noOfAadhaarRegistered);
                        districtMetrics.setMobileRegCount(
                                        noOfMobileNumberRegistered);

                        DistrictMetrics savedMetrics = districtMetricsRepository.save(districtMetrics);

                        log.info(
                                        "District metrics saved. metricsId={}, " +
                                                        "districtId={}, taluks={}, fpsStores={}, " +
                                                        "cards={}, beneficiaries={}",
                                        savedMetrics.getId(),
                                        savedDistrict.getId(),
                                        savedMetrics.getTalukCount(),
                                        savedMetrics.getFairPriceShopCount(),
                                        savedMetrics.getFamilyCardCount(),
                                        savedMetrics.getBeneficiariesCount());
                }

                log.info(
                                "District synchronization completed. stateId={}",
                                state.getId());
        }

        public void syncTaluks() throws IOException, InterruptedException {

                final String stateSourceId = "1";

                State state = stateRepository.findBySourceId(stateSourceId)
                                .orElseThrow(() -> new IllegalStateException(
                                                "State not found for sourceId: " + stateSourceId));

                List<District> districts = districtRepository.findByStateId(state.getId());
                if (districts.isEmpty()) {
                        log.warn("No districts found for state: {}", state.getName());
                        return;
                }

                for (District district : districts) {

                        String districtSourceId = district.getSourceId();
                        String districtName = district.getName();

                        log.info("Syncing taluks for district: {} ({})", districtName, districtSourceId);

                        String districtReportResponse = pdsReportClient.getDistrictReport(Long.valueOf(districtSourceId), districtName);
                        JsonNode districtReportObject = objectMapper.readTree(districtReportResponse);
                        JsonNode talukList = districtReportObject.path("stateLevelStatsReportDtoList");

                        if (!talukList.isArray()) {
                                log.warn("No taluk data found for district: {}", districtName);
                                continue;
                        }

                        for (JsonNode talukNode : talukList) {

                                String talukSourceId = talukNode.path("talukId").asText();
                                String talukName = talukNode.path("talukName").asText();

                                Taluk taluk = talukRepository.findBySourceId(talukSourceId).orElseGet(Taluk::new);

                                taluk.setSourceId(talukSourceId);
                                taluk.setName(talukName);
                                taluk.setDistrict(district);

                                taluk = talukRepository.save(taluk);

                                TalukMetrics metrics = talukMetricsRepository.findByTalukId(taluk.getId()).orElseGet(TalukMetrics::new);

                                metrics.setTaluk(taluk);
                                metrics.setFairPriceShopCount(talukNode.path("numberOfFps").asLong());
                                metrics.setFamilyCardCount(talukNode.path("numberOfCards").asLong());
                                metrics.setBeneficiariesCount(talukNode.path("numberOfBeneficiaries").asLong());
                                metrics.setAadharRegCount(talukNode.path("noOfAadhaarRegistered").asLong());
                                metrics.setMobileRegCount(talukNode.path("noOfMobileNumberRegistered").asLong());
                                talukMetricsRepository.save(metrics);

                                log.info("Taluk synced: {} ({}) for district: {}", talukName, talukSourceId, districtName);
                        }
                }
        }

        // syncDistricts();
        // syncTaluks();
        // syncVillages();
        // syncShops();

}
