package com.kamalkavin96.tamilnadu_gov_api.services;

import com.kamalkavin96.tamilnadu_gov_api.repositories.StateMetricsRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalkavin96.tamilnadu_gov_api.clients.tnpds.PDSReportClient;
import com.kamalkavin96.tamilnadu_gov_api.dto.api.BeneficiaryReportDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.api.BillDetailReportDto;
import com.kamalkavin96.tamilnadu_gov_api.dto.api.FpsLocationDetailsReportDto;
import com.kamalkavin96.tamilnadu_gov_api.models.Agency;
import com.kamalkavin96.tamilnadu_gov_api.models.Beneficiary;
import com.kamalkavin96.tamilnadu_gov_api.models.CardType;
import com.kamalkavin96.tamilnadu_gov_api.models.District;
import com.kamalkavin96.tamilnadu_gov_api.models.DistrictMetrics;
import com.kamalkavin96.tamilnadu_gov_api.models.Shop;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopIncharge;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopInfo;
import com.kamalkavin96.tamilnadu_gov_api.models.ShopMetrics;
import com.kamalkavin96.tamilnadu_gov_api.models.State;
import com.kamalkavin96.tamilnadu_gov_api.models.StateMetrics;
import com.kamalkavin96.tamilnadu_gov_api.models.Taluk;
import com.kamalkavin96.tamilnadu_gov_api.models.TalukMetrics;
import com.kamalkavin96.tamilnadu_gov_api.models.Village;
import com.kamalkavin96.tamilnadu_gov_api.repositories.AgencyRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.BeneficiaryRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.DistrictMetricsRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.DistrictRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopInchargeRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopInfoRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopMetricsRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.ShopRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.StateRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.TalukMetricsRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.TalukRepository;
import com.kamalkavin96.tamilnadu_gov_api.repositories.VillageRepository;

import io.swagger.v3.core.util.Json;
import jakarta.transaction.Transactional;
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
        private final ShopInfoRepository shopInfoRepository;
        private final ShopInchargeRepository shopInchargeRepository;
        private final ShopMetricsRepository shopMetricsRepository;
        private final AgencyRepository agencyRepository;

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

                Optional<State> existingState = stateRepository.findBySourceId(Long.parseLong(sourceId));
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
                        state.setSourceId(Long.parseLong(sourceId));
                        state.setName(stateName);
                        state.setCreatedAt(now);
                        log.info(
                                        "State not found. Creating new state. sourceId={}, name={}",
                                        sourceId,
                                        stateName);
                }

                state.setSourceId(Long.parseLong(sourceId));
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

                State state = stateRepository.findBySourceId(Long.parseLong(stateSourceId))
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

                State state = stateRepository.findBySourceId(Long.parseLong(stateSourceId))
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

                        String districtReportResponse = pdsReportClient
                                        .getDistrictReport(Long.valueOf(districtSourceId), districtName);
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

                                TalukMetrics metrics = talukMetricsRepository.findByTalukId(taluk.getId())
                                                .orElseGet(TalukMetrics::new);

                                metrics.setTaluk(taluk);
                                metrics.setFairPriceShopCount(talukNode.path("numberOfFps").asLong());
                                metrics.setFamilyCardCount(talukNode.path("numberOfCards").asLong());
                                metrics.setBeneficiariesCount(talukNode.path("numberOfBeneficiaries").asLong());
                                metrics.setAadharRegCount(talukNode.path("noOfAadhaarRegistered").asLong());
                                metrics.setMobileRegCount(talukNode.path("noOfMobileNumberRegistered").asLong());
                                talukMetricsRepository.save(metrics);

                                log.info("Taluk synced: {} ({}) for district: {}", talukName, talukSourceId,
                                                districtName);
                        }
                }
        }

        public void syncShops() throws IOException, InterruptedException {

                final String stateSourceId = "1";
                State state = stateRepository.findBySourceId(Long.parseLong(stateSourceId))
                                .orElseThrow(() -> new IllegalStateException(
                                                "State not found for sourceId: " + stateSourceId));

                List<District> districts = districtRepository.findByStateId(state.getId());

                if (districts.isEmpty()) {
                        log.warn("No districts found for state: {}", state.getName());
                        return;
                }

                int totalShops = 0;
                int createdShops = 0;
                int updatedShops = 0;
                int skippedShops = 0;

                for (District district : districts) {

                        log.info("Processing district. id={}, sourceId={}, name={}",
                                        district.getId(), district.getSourceId(), district.getName());
                        List<Taluk> taluks = talukRepository.findByDistrictId(district.getId());

                        if (taluks.isEmpty()) {
                                log.warn("No taluks found for district: {}",
                                                district.getName());
                                continue;
                        }

                        

                        for (Taluk taluk : taluks) {

                                // if (taluk.getId()!=97) {
                                //         continue;
                                // }

                                String talukSourceId = taluk.getSourceId();
                                String talukName = taluk.getName();

                                log.info("Fetching shops for taluk. talukId={}, talukName={}",
                                                talukSourceId, talukName);

                                String talukResponse = pdsReportClient.getTalukReport(Long.valueOf(talukSourceId));
                                JsonNode talukObject = objectMapper.readTree(talukResponse);
                                JsonNode shopList = talukObject.path("stateLevelStatsReportDtoList");

                                if (!shopList.isArray()) {
                                        log.warn("No shop list found for taluk. talukId={}, talukName={}",
                                                        talukSourceId, talukName);
                                        continue;
                                }

                                List<Shop> shopListBulk = new ArrayList<>();

                                for (JsonNode shopNode : shopList) {

                                        totalShops++;
                                        String shopSourceId = shopNode.path("fpsId").asText(null);
                                        String shopCode = shopNode.path("fpsCode").asText(null);
                                        String shopName = shopNode.path("fpsName").asText(null);

                                        if (shopSourceId == null || shopSourceId.isBlank()
                                                        || "null".equalsIgnoreCase(shopSourceId)) {

                                                skippedShops++;

                                                log.warn("Skipping shop because fpsId is missing. talukId={}, talukName={}",
                                                                talukSourceId, talukName);

                                                continue;
                                        }

                                        if (shopCode == null || shopCode.isBlank()
                                                        || "null".equalsIgnoreCase(shopCode)) {

                                                skippedShops++;
                                                log.warn("Skipping shop because fpsCode is missing. fpsId={}, talukId={}, talukName={}",
                                                                shopSourceId, talukSourceId, talukName);
                                                continue;
                                        }

                                        if (shopName == null || shopName.isBlank()
                                                        || "null".equalsIgnoreCase(shopName)) {
                                                skippedShops++;
                                                log.warn("Skipping shop because fpsName is missing. fpsId={}, fpsCode={}, taluk={}",
                                                                shopSourceId, shopCode, talukName);
                                                continue;
                                        }

                                        Optional<Shop> existingShop = shopRepository.findBySourceId(shopSourceId);
                                        Shop shop;

                                        if (existingShop.isPresent()) {

                                                shop = existingShop.get();
                                                updatedShops++;
                                                log.debug("Existing shop found. id={}, sourceId={}",
                                                                shop.getId(), shop.getSourceId());

                                        } else {

                                                shop = new Shop();
                                                createdShops++;
                                                log.debug("Creating new shop. sourceId={}, shopCode={}",
                                                                shopSourceId, shopCode);
                                        }

                                        shop.setSourceId(shopSourceId);
                                        shop.setShopCode(shopCode);
                                        shop.setShopname(shopName);
                                        shop.setTaluk(taluk);


                                        shopListBulk.add(shop);
                                        log.info("added");
                                        // Shop savedShop = shopRepository.save(shop);

                                        // log.info("Shop synchronized. id={}, sourceId={}, shopCode={}, shopName={}, talukId={}, talukName={}",
                                        //                 savedShop.getId(), savedShop.getSourceId(),
                                        //                 savedShop.getShopCode(),
                                        //                 savedShop.getShopname(), taluk.getId(), taluk.getName());
                                }

                                List<Shop> savedShopList = shopRepository.saveAll(shopListBulk);

                                log.info("bulk save completed: {}", savedShopList.toString());


                        }
                }

                log.info("============================================================");
                log.info("Shop synchronization completed");
                log.info("State      : {} ({})", state.getName(), state.getId());
                log.info("Total      : {}", totalShops);
                log.info("Created    : {}", createdShops);
                log.info("Updated    : {}", updatedShops);
                log.info("Skipped    : {}", skippedShops);
                log.info("============================================================");
        }

        public void syncShopInfo() throws IOException, InterruptedException {

                log.info("============================================================");
                log.info("Starting ShopInfo synchronization");
                log.info("============================================================");

                List<Shop> shops = shopRepository.findAll();

                if (shops.isEmpty()) {
                        log.warn("No shops found. Please run syncShops() first.");
                        return;
                }

                int totalShops = shops.size();
                int processedShops = 0;
                int skippedShops = 0;

                int createdVillages = 0;
                int updatedVillages = 0;

                int createdShopInfos = 0;
                int updatedShopInfos = 0;

                int createdShopIncharges = 0;
                int updatedShopIncharges = 0;

                for (Shop shop : shops) {

                        if (shop.getId()<10000) {
                                continue;
                        }

                        String shopSourceId = shop.getSourceId();
                        String shopCode = shop.getShopCode();

                        if (shopSourceId == null || shopSourceId.isBlank()) {
                                skippedShops++;

                                log.warn(
                                                "Skipping shop. sourceId is missing. shopId={}",
                                                shop.getId());

                                continue;
                        }

                        if (shopCode == null || shopCode.isBlank()) {
                                skippedShops++;

                                log.warn(
                                                "Skipping shop. shopCode is missing. shopId={}, sourceId={}",
                                                shop.getId(),
                                                shopSourceId);

                                continue;
                        }

                        Long fpsId;

                        try {
                                fpsId = Long.valueOf(shopSourceId);
                        } catch (NumberFormatException e) {

                                skippedShops++;

                                log.warn(
                                                "Invalid FPS ID. shopId={}, sourceId={}",
                                                shop.getId(),
                                                shopSourceId);

                                continue;
                        }

                        log.info(
                                        "Processing shop. shopId={}, fpsId={}, shopCode={}, shopName={}",
                                        shop.getId(),
                                        fpsId,
                                        shopCode,
                                        shop.getShopname());

                        /*
                         * ============================================================
                         * 1. GET FPS LOCATION DETAILS
                         * ============================================================
                         */

                        String response = pdsReportClient.getFpsLocationDetails(
                                        shopCode,
                                        fpsId);

                        JsonNode shopObject = objectMapper.readTree(response);

                        JsonNode fpsStoreDto = shopObject.path("fpsStoreDto");
                        JsonNode posOperatingHoursDto = shopObject.path("posOperatingHoursDto");

                        // =====================================================
                        // AGENCY
                        // =====================================================

                        String agencyName = getTextValue(fpsStoreDto, "agencyName");

                        Agency agency = null;

                        if (agencyName != null && !agencyName.isBlank()) {

                                if (agencyRepository.existsByName(agencyName)) {

                                        agency = agencyRepository
                                                        .findByName(agencyName)
                                                        .orElseThrow(() -> new IllegalStateException(
                                                                        "Agency exists but could not be found: "
                                                                                        + agencyName));

                                } else {

                                        agency = new Agency();
                                        agency.setName(agencyName);

                                        agency = agencyRepository.save(agency);
                                }
                        }

                        if (fpsStoreDto.isMissingNode()
                                        || fpsStoreDto.isNull()) {

                                skippedShops++;

                                log.warn(
                                                "fpsStoreDto not found. shopId={}, fpsId={}, shopCode={}",
                                                shop.getId(),
                                                fpsId,
                                                shopCode);

                                continue;
                        }

                        /*
                         * ============================================================
                         * 2. SHOP INCHARGE
                         * ============================================================
                         *
                         * These fields come from the main shop response:
                         *
                         * fpsIncharge
                         * fpsContactNo
                         */

                        String fpsIncharge = getTextValue(fpsStoreDto, "contactPerson");

                        String fpsContactNo = getTextValue(fpsStoreDto, "phoneNumber");

                        ShopIncharge shopIncharge = null;

                        if (fpsContactNo != null && !fpsContactNo.isBlank()) {

                                Optional<ShopIncharge> existingIncharge = shopInchargeRepository
                                                .findByPhoneNumber(fpsContactNo);

                                boolean inchargeExists = existingIncharge.isPresent();

                                if (inchargeExists) {

                                        shopIncharge = existingIncharge.get();

                                } else {

                                        shopIncharge = new ShopIncharge();
                                }

                                /*
                                 * Name is mandatory in ShopIncharge.
                                 */
                                if (fpsIncharge != null
                                                && !fpsIncharge.isBlank()) {

                                        shopIncharge.setName(fpsIncharge);

                                } else if (!inchargeExists) {

                                        shopIncharge.setName("Unknown");
                                }

                                shopIncharge.setPhoneNumber(fpsContactNo);

                                ShopIncharge savedShopIncharge = shopInchargeRepository.save(shopIncharge);

                                if (inchargeExists) {
                                        updatedShopIncharges++;
                                } else {
                                        createdShopIncharges++;
                                }

                                shopIncharge = savedShopIncharge;

                                log.info(
                                                "ShopIncharge synchronized. id={}, name={}, phone={}",
                                                shopIncharge.getId(),
                                                shopIncharge.getName(),
                                                shopIncharge.getPhoneNumber());
                        } else {

                                log.warn(
                                                "ShopIncharge not available. shopId={}, fpsId={}, shopCode={}",
                                                shop.getId(),
                                                fpsId,
                                                shopCode);
                        }

                        /*
                         * ============================================================
                         * 3. VILLAGE
                         * ============================================================
                         */

                        Long villageSourceId = getLongValue(fpsStoreDto, "villageId");

                        String villageName = getTextValue(fpsStoreDto, "village");

                        if (villageSourceId == null) {

                                skippedShops++;

                                log.warn(
                                                "Village ID missing. shopId={}, fpsId={}, village={}",
                                                shop.getId(),
                                                fpsId,
                                                villageName);

                                continue;
                        }

                        if (villageName == null || villageName.isBlank()) {

                                skippedShops++;

                                log.warn(
                                                "Village name missing. villageSourceId={}, shopId={}",
                                                villageSourceId,
                                                shop.getId());

                                continue;
                        }

                        /*
                         * Find existing village or create new one.
                         */
                        Optional<Village> existingVillage = villageRepository.findBySourceId(
                                        villageSourceId);

                        Village village;
                        boolean villageExists;

                        if (existingVillage.isPresent()) {

                                village = existingVillage.get();
                                villageExists = true;

                        } else {

                                village = new Village();
                                villageExists = false;
                        }

                        village.setSourceId(villageSourceId);
                        village.setName(villageName);

                        /*
                         * Village belongs to the same Taluk as Shop.
                         */
                        village.setTaluk(shop.getTaluk());

                        Village savedVillage = villageRepository.save(village);

                        if (villageExists) {
                                updatedVillages++;
                        } else {
                                createdVillages++;
                        }

                        /*
                         * ============================================================
                         * 4. SHOP INFO
                         * ============================================================
                         */

                        Optional<ShopInfo> existingShopInfo = shopInfoRepository.findByShopId(
                                        shop.getId());

                        ShopInfo shopInfo;
                        boolean shopInfoExists;

                        if (existingShopInfo.isPresent()) {

                                shopInfo = existingShopInfo.get();
                                shopInfoExists = true;

                        } else {

                                shopInfo = new ShopInfo();
                                shopInfoExists = false;
                        }

                        /*
                         * Shop relationship
                         */
                        shopInfo.setShop(shop);

                        /*
                         * Village relationship
                         */
                        shopInfo.setVillage(savedVillage);

                        shopInfo.setAgency(agency);

                        /*
                         * Shop Incharge relationship
                         */
                        if (shopIncharge != null) {
                                shopInfo.setShopIncharge(shopIncharge);
                        }

                        /*
                         * ============================================================
                         * 5. ADDRESS
                         * ============================================================
                         */

                        String addressLine1 = getTextValue(
                                        fpsStoreDto,
                                        "addressLine1");

                        String addressLine2 = getTextValue(
                                        fpsStoreDto,
                                        "addressLine2");

                        String addressLine3 = getTextValue(
                                        fpsStoreDto,
                                        "addressLine3");

                        Integer pinCode = getIntegerValue(
                                        fpsStoreDto,
                                        "pincode");

                        /*
                         * Fallback if pincode is returned in addressLine2.
                         */
                        if (pinCode == null && addressLine2 != null) {
                                pinCode = parseInteger(addressLine2);
                        }

                        /*
                         * ShopInfo has nullable=false for these fields.
                         */
                        // if (addressLine1 == null
                        //                 || addressLine1.isBlank()) {

                        //         addressLine1 = "Unknown";
                        // }

                        if (pinCode == null) {
                                pinCode = 0;
                        }

                        shopInfo.setAddressLine1(addressLine1);
                        shopInfo.setAddressLine2(addressLine2);
                        shopInfo.setAddressLine3(addressLine3);
                        shopInfo.setPinCode(pinCode);

                        /*
                         * ============================================================
                         * 6. LATITUDE / LONGITUDE
                         * ============================================================
                         */

                        BigDecimal latitude = getBigDecimalValue(
                                        fpsStoreDto,
                                        "latitude");

                        BigDecimal longitude = getBigDecimalValue(
                                        fpsStoreDto,
                                        "longitude");

                        shopInfo.setLatitude(latitude);
                        shopInfo.setLongitude(longitude);




                        String firstSessionOpeningTime = posOperatingHoursDto.get("firstSessionOpeningTime").asText();
                        String firstSessionClosingTime = posOperatingHoursDto.get("firstSessionClosingTime").asText();
                        String secondSessionOpeningTime = posOperatingHoursDto.get("secondSessionOpeningTime").asText();
                        String secondSessionClosingTime = posOperatingHoursDto.get("secondSessionClosingTime").asText();

                        

                        shopInfo.setFirstSessionOpen(parseLocalTime(firstSessionOpeningTime));
                        shopInfo.setFirstSessionClose(parseLocalTime(firstSessionClosingTime));
                        shopInfo.setSecondSessionOpen(parseLocalTime(secondSessionOpeningTime));
                        shopInfo.setSecondSessionClose(parseLocalTime(secondSessionClosingTime));

                        /*
                         * ============================================================
                         * 7. SAVE SHOP INFO
                         * ============================================================
                         */

                        ShopInfo savedShopInfo = shopInfoRepository.save(shopInfo);

                        // =====================================================
                        // SHOP METRICS
                        // =====================================================

                        ShopMetrics shopMetrics = shopMetricsRepository
                                        .findByShopId(shop.getId())
                                        .orElseGet(ShopMetrics::new);

                        shopMetrics.setShop(shop);

                        shopMetrics.setFamilyCardCount(
                                        getLongValue(shopObject, "numberOfCards") != null
                                                        ? getLongValue(shopObject, "numberOfCards")
                                                        : 0L);

                        shopMetrics.setBeneficiariesCount(
                                        getLongValue(shopObject, "numberOfBeneficiaries") != null
                                                        ? getLongValue(shopObject, "numberOfBeneficiaries")
                                                        : 0L);

                        shopMetrics.setAadharRegCount(
                                        getLongValue(shopObject, "noOfAadhaarNoRegistered") != null
                                                        ? getLongValue(shopObject, "noOfAadhaarNoRegistered")
                                                        : 0L);

                        shopMetrics.setMobileRegCount(
                                        getLongValue(shopObject, "noOfMobileNoRegistered") != null
                                                        ? getLongValue(shopObject, "noOfMobileNoRegistered")
                                                        : 0L);

                        shopMetricsRepository.save(shopMetrics);

                        if (shopInfoExists) {
                                updatedShopInfos++;
                        } else {
                                createdShopInfos++;
                        }

                        processedShops++;

                        log.info(
                                        "ShopInfo synchronized. " +
                                                        "shopInfoId={}, shopId={}, villageId={}, inchargeId={}",
                                        savedShopInfo.getId(),
                                        shop.getId(),
                                        savedVillage.getId(),
                                        shopIncharge != null
                                                        ? shopIncharge.getId()
                                                        : null);
                }

                log.info("============================================================");
                log.info("ShopInfo synchronization completed");
                log.info("Total shops        : {}", totalShops);
                log.info("Processed          : {}", processedShops);
                log.info("Skipped            : {}", skippedShops);
                log.info("Villages created   : {}", createdVillages);
                log.info("Villages updated   : {}", updatedVillages);
                log.info("ShopInfo created   : {}", createdShopInfos);
                log.info("ShopInfo updated   : {}", updatedShopInfos);
                log.info("Incharge created   : {}", createdShopIncharges);
                log.info("Incharge updated   : {}", updatedShopIncharges);
                log.info("============================================================");
        }

        public void syncBeneficiry() {

                log.info("============================================================");
                log.info("Starting Beneficiary synchronization");
                log.info("============================================================");

                List<Shop> shops = shopRepository.findAll();

                if (shops.isEmpty()) {
                        log.warn("No shops found. Please run syncShops() first.");
                        return;
                }

                int totalShops = shops.size();
                int processedShops = 0;
                int skippedShops = 0;

                int createdBeneficiaries = 0;
                int updatedBeneficiaries = 0;

                for (Shop shop : shops) {

                        String shopSourceId = shop.getSourceId();

                        if (shopSourceId == null || shopSourceId.isBlank()) {
                                skippedShops++;
                                log.warn("Skipping shop because sourceId is missing. shopId={}", shop.getId());
                                continue;
                        }

                        Long shopId;
                        try {
                                shopId = Long.valueOf(shopSourceId);
                        } catch (NumberFormatException e) {
                                skippedShops++;
                                log.warn("Invalid FPS ID. shopId={}, sourceId={}", shop.getId(), shopSourceId);
                                continue;
                        }

                        log.info("Processing beneficiaries for shop. shopId={}, fpsId={}, shopCode={}",
                                        shop.getId(), shopId, shop.getShopCode());

                        try {

                                // =====================================================
                                // 1. GET CARD COUNTS
                                // =====================================================

                                String cardCountResponse = pdsReportClient.getCardCounts(shopId, "english");
                                JsonNode cardCountObject = objectMapper.readTree(cardCountResponse);
                                JsonNode content = cardCountObject.path("content");

                                if (!content.isArray() || content.isEmpty()) {
                                        log.warn("No card count data found. shopId={}, fpsId={}",
                                                        shop.getId(), shopId);
                                        continue;
                                }

                                JsonNode cardContent = content.get(0);
                                Integer riceCards1Count = cardContent.path("Rice Cards_1").asInt(0);
                                Integer aayCards8Count = cardContent.path("AAY Cards_8").asInt(0);
                                Integer sugarCards2Count = cardContent.path("Sugar Cards_2").asInt(0);
                                Integer noCommodityCards5Count = cardContent.path("No Commodity Cards_5").asInt(0);
                                Integer policeCards3Count = cardContent.path("Police Cards_3").asInt(0);

                                List<List<Integer>> allCardCountList = List.of(
                                                List.of(riceCards1Count, 1),
                                                List.of(aayCards8Count, 8),
                                                List.of(sugarCards2Count, 2),
                                                List.of(noCommodityCards5Count, 5),
                                                List.of(policeCards3Count, 3));

                                // =====================================================
                                // 2. GET ADDITIONAL BENEFICIARY DATA
                                // =====================================================

                                Map<String, JsonNode> beneficiaryAdditionalData = new HashMap<>();
                                String shopBillListResponse = pdsReportClient.getBillList(0, 10000, shopId);
                                JsonNode shopBillListObject = objectMapper.readTree(shopBillListResponse);
                                JsonNode billDtoList = shopBillListObject.path("billDtoList");

                                if (billDtoList.isArray()) {
                                        for (JsonNode billNode : billDtoList) {
                                                JsonNode beneficiaryDtoNode = billNode.get("beneficiaryDto");
                                                String ufc = getTextValue(beneficiaryDtoNode, "ufc");
                                                if (ufc == null) {
                                                        continue;
                                                }
                                                beneficiaryAdditionalData.put(ufc, beneficiaryDtoNode);
                                        }
                                }

                                log.info("Additional beneficiary data loaded. shopId={}, count={}",
                                                shop.getId(), beneficiaryAdditionalData.size());

                                // =====================================================
                                // 3. GET BENEFICIARIES
                                // =====================================================

                                for (List<Integer> cardCount : allCardCountList) {
                                        Integer count = cardCount.get(0);
                                        Integer cardTypeGroupId = cardCount.get(1);

                                        log.info("Processing card group. shopId={}, cardTypeGroupId={}, count={}",
                                                        shop.getId(), cardTypeGroupId, count);

                                        for (int page = 0; page < count; page++) {

                                                String beneficiaryResponse = pdsReportClient.getBenefs(page, 1, shopId, cardTypeGroupId);
                                                JsonNode beneficiaryObject = objectMapper.readTree(beneficiaryResponse);
                                                JsonNode beneficiaryList = beneficiaryObject.path("beneficiaryList");

                                                if (!beneficiaryList.isArray() || beneficiaryList.isEmpty()) {

                                                        log.debug("No beneficiary returned. shopId={}, page={}, cardTypeGroupId={}",
                                                                        shop.getId(), page, cardTypeGroupId);
                                                        continue;
                                                }

                                                // Because page size is 1
                                                JsonNode beneficiaryNode = beneficiaryList.get(0);

                                                // =================================================
                                                // 4. CORE BENEFICIARY DATA
                                                // =================================================

                                                Long tnPdsId = getLongValue(beneficiaryNode, "id");
                                                String ufcNumber = getTextValue(beneficiaryNode, "ufc");

                                                if (tnPdsId == null || ufcNumber == null) {

                                                        log.warn("Skipping beneficiary because ID/UFC is missing. shopId={}, page={}, cardTypeGroupId={}",
                                                                        shop.getId(), page, cardTypeGroupId);
                                                        continue;
                                                }

                                                String name = getTextValue(beneficiaryNode, "name");
                                                String localName = getTextValue(beneficiaryNode, "localName");
                                                String gender = getTextValue(beneficiaryNode, "gender");
                                                String oldRationNumber = getTextValue(beneficiaryNode, "oldRationNumber");
                                                Long villageSourceId = getLongValue(beneficiaryNode, "villageId");
                                                String villageName = getTextValue(beneficiaryNode, "village");
                                                Integer numOfAdults = getIntegerValue(beneficiaryNode,"numOfAdults");
                                                Integer numOfChild = getIntegerValue(beneficiaryNode,"numOfChild");
                                                Integer numOfCylinder = getIntegerValue(beneficiaryNode,"numOfCylinder");

                                                Boolean isFamilyHeadAadharNumberRegistered = beneficiaryNode.get("isFamilyHeadAadharNumberRegistered").asBoolean();
                                                Boolean isMobileNumberRegistered = beneficiaryNode.get("isMobileNumberRegistered").asBoolean();
                                                

                                                // =================================================
                                                // 5. BENEFICIARY ADDRESS
                                                // =================================================

                                                JsonNode addressNode = beneficiaryNode.path("beneficiaryAddressDto");

                                                String addressLine1 = null;
                                                String addressLine2 = null;
                                                String addressLine3 = null;
                                                Integer pinCode = null;
                                                String fatherOrSpouseName = null;
                                                String familyHeadName = null;

                                                if (!addressNode.isMissingNode() && !addressNode.isNull()) {
                                                        addressLine1 = getTextValue(addressNode, "addressLine1");
                                                        addressLine2 = getTextValue(addressNode,"addressLine2");
                                                        addressLine3 = getTextValue(addressNode,"addressLine3");
                                                        pinCode = getIntegerValue(addressNode,"pincode");
                                                        fatherOrSpouseName = getTextValue(addressNode,"fatherOrSpouseName");
                                                        familyHeadName = getTextValue(addressNode,"familyHeadName");
                                                }

                                                // =================================================
                                                // 6. CARD TYPE
                                                // =================================================

                                                JsonNode cardTypeNode = beneficiaryNode.path("cardTypeDto");
                                                CardType cardType = null;

                                                if (!cardTypeNode.isMissingNode() && !cardTypeNode.isNull()) {

                                                        Integer cardTypeId = getIntegerValue(cardTypeNode,"id");
                                                        String description = getTextValue(cardTypeNode,"description");
                                                        String localDescription = getTextValue(cardTypeNode,"ldescription");

                                                }

                                                // =================================================
                                                // 7. ADDITIONAL DATA FROM BILL API
                                                // =================================================

                                                JsonNode additionalData = beneficiaryAdditionalData.get(ufcNumber);

                                                String mobileNumber = null;
                                                String encryptedUfc = null;
                                                String familyHeadAadharEncrypted = null;

                                                if (additionalData != null) {

                                                        mobileNumber = getTextValue(additionalData, "mobileNumber");
                                                        encryptedUfc = getTextValue(additionalData, "encryptedUfc");
                                                        familyHeadAadharEncrypted = getTextValue(additionalData, "familyHeadAadharNumber");
                                                }

                                                // =================================================
                                                // 8. VILLAGE
                                                // =================================================

                                                Village residentialVillage = null;
                                                if (villageSourceId != null) {
                                                        residentialVillage = villageRepository
                                                                        .findBySourceId(villageSourceId)
                                                                        .orElse(null);
                                                        if (residentialVillage == null) {
                                                                log.warn("Village not found for beneficiary. " +
                                                                                                "beneficiaryId={}, ufc={}, villageId={}, villageName={}",
                                                                                tnPdsId, ufcNumber,
                                                                                villageSourceId, villageName);
                                                        }
                                                }

                                                // =================================================
                                                // 9. UPSERT BENEFICIARY
                                                // =================================================

                                                Optional<Beneficiary> existingBeneficiary = beneficiaryRepository
                                                                .findByTnPdsId(tnPdsId);

                                                Beneficiary beneficiary;
                                                boolean beneficiaryExists;

                                                if (existingBeneficiary.isPresent()) {

                                                        beneficiary = existingBeneficiary.get();
                                                        beneficiaryExists = true;

                                                } else {

                                                        beneficiary = new Beneficiary();
                                                        beneficiaryExists = false;
                                                }

                                                beneficiary.setTnPdsId(tnPdsId);
                                                beneficiary.setUfcNumber(ufcNumber);
                                                beneficiary.setEncryptedUfc(encryptedUfc);
                                                beneficiary.setOldRationNumber(oldRationNumber);
                                                beneficiary.setName(name);
                                                beneficiary.setLocalName(localName);
                                                beneficiary.setFamilyHeadName(familyHeadName);
                                                beneficiary.setFatherOrSpouseName(fatherOrSpouseName);
                                                beneficiary.setGender(gender);
                                                beneficiary.setMobileNumber(mobileNumber);
                                                beneficiary.setFamilyHeadAadharEncrypted(familyHeadAadharEncrypted);
                                                beneficiary.setAddressLine1(addressLine1);
                                                beneficiary.setAddressLine2(addressLine2);
                                                beneficiary.setAddressLine3(addressLine3);
                                                beneficiary.setPinCode(pinCode);
                                                beneficiary.setNumOfAdults(numOfAdults != null ? numOfAdults : 0);
                                                beneficiary.setNumOfChild(numOfChild != null ? numOfChild : 0);
                                                beneficiary.setNumOfCylinder(numOfCylinder != null ? numOfCylinder : 0);
                                                beneficiary.setIsActive(true);
                                                beneficiary.setIsMobileRegistered(isMobileNumberRegistered);
                                                beneficiary.setIsAadharRegistered(isFamilyHeadAadharNumberRegistered);

                                                beneficiary.setResidentialVillage(residentialVillage);
                                                beneficiary.setAssignedShop(shop);

                                                // Set cardType here once CardType lookup is implemented.

                                                Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
                                                if (beneficiaryExists) {
                                                        updatedBeneficiaries++;
                                                } else {
                                                        createdBeneficiaries++;
                                                }
                                                log.debug("Beneficiary synchronized. id={}, tnPdsId={}, ufc={}, shopId={}, villageId={}",
                                                                savedBeneficiary.getId(),savedBeneficiary.getTnPdsId(),
                                                                savedBeneficiary.getUfcNumber(),shop.getId(),
                                                                residentialVillage != null ? residentialVillage.getId() : null);
                                        }
                                }

                                processedShops++;

                                log.info("Beneficiary synchronization completed for shop. shopId={}, fpsId={}",
                                                shop.getId(),shopId);

                        } catch (IOException | InterruptedException e) {

                                log.error(
                                                "Failed to synchronize beneficiaries. shopId={}, fpsId={}",
                                                shop.getId(),
                                                shopId,
                                                e);
                        }
                }

                log.info("============================================================");
                log.info("Beneficiary synchronization completed");
                log.info("Total shops        : {}", totalShops);
                log.info("Processed shops    : {}", processedShops);
                log.info("Skipped shops      : {}", skippedShops);
                log.info("Created            : {}", createdBeneficiaries);
                log.info("Updated            : {}", updatedBeneficiaries);
                log.info("============================================================");
        }

        private LocalTime parseLocalTime(String value) {

                if (value == null || value.isBlank()) {
                        return null;
                }

                try {
                        return LocalTime.parse(value.trim());
                } catch (DateTimeParseException e) {
                        log.warn("Unable to parse time: {}", value);
                        return null;
                }
        }

        private BigDecimal getBigDecimalValue(
                        JsonNode node,
                        String fieldName) {

                String value = getTextValue(node, fieldName);

                if (value == null) {
                        return null;
                }

                try {
                        return new BigDecimal(value);
                } catch (NumberFormatException e) {
                        return null;
                }
        }

        private Integer parseInteger(String value) {

                if (value == null || value.isBlank()) {
                        return null;
                }

                try {
                        return Integer.valueOf(value.trim());
                } catch (NumberFormatException e) {
                        return null;
                }
        }

        private Integer getIntegerValue(JsonNode node, String fieldName) {

                JsonNode value = node.path(fieldName);

                if (value.isMissingNode() || value.isNull()) {
                        return null;
                }

                if (value.isNumber()) {
                        return value.asInt();
                }

                String text = value.asText();

                if (text == null || text.isBlank() || "null".equalsIgnoreCase(text)) {
                        return null;
                }

                try {
                        return Integer.valueOf(text.trim());
                } catch (NumberFormatException e) {
                        return null;
                }
        }

        private String getTextValue(JsonNode node, String fieldName) {

                JsonNode value = node.path(fieldName);
                if (value.isMissingNode() || value.isNull()) {
                        return null;
                }
                String text = value.asText();
                if (text == null || text.isBlank() || "null".equalsIgnoreCase(text)) {
                        return null;
                }
                return text.trim();
        }

        private Long getLongValue(JsonNode node, String fieldName) {

                JsonNode value = node.path(fieldName);
                if (value.isMissingNode() || value.isNull()) {
                        return null;
                }
                if (value.isNumber()) {
                        return value.asLong();
                }
                String text = value.asText();
                if (text == null || text.isBlank() || "null".equalsIgnoreCase(text)) {
                        return null;
                }

                try {
                        return Long.valueOf(text.trim());
                } catch (NumberFormatException e) {
                        return null;
                }
        }



        public void testMethod() throws IOException, InterruptedException{
                List<BillDetailReportDto> data = pdsReportClient.getBillListObj(0, 10, 31650l);
                System.out.println(data);
        }
}
