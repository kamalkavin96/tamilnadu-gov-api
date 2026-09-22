package com.kamalkavin96.tamilnadu_gov_api.controller;

import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamalkavin96.tamilnadu_gov_api.clients.tnpds.NFSAReportClient;
import com.kamalkavin96.tamilnadu_gov_api.clients.tnpds.PDSReportClient;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

// @Profile("api")
// @RestController
// @RequestMapping("/api/v1/fare-price-shop")
@RequiredArgsConstructor
public class FarePriceShopController {

    private final NFSAReportClient farePriceShopClient;
    private final PDSReportClient pdsReportClient;

    @GetMapping(value = "/dashboard-count")
    public ResponseEntity<String> getDashboardCount() throws IOException, InterruptedException {

        String response = farePriceShopClient.getDashboardCounts();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/state-list")
    public ResponseEntity<String> getStateList() throws IOException, InterruptedException {
        String response = farePriceShopClient.getStateList();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/district-list")
    public ResponseEntity<String> getDistructList(@RequestParam Integer districtNumber)
            throws IOException, InterruptedException {
        String response = farePriceShopClient.getDistrictList(districtNumber);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/taluk-list")
    public ResponseEntity<String> gettalukList(
            @RequestParam Integer districtNumber,
            @RequestParam Integer talukNumber)
            throws IOException, InterruptedException {
        String response = farePriceShopClient.getTalukList(districtNumber, talukNumber);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/beneficiary-list")
    public ResponseEntity<String> getBeneficiaryList(
            @RequestParam Integer shopNumber)
            throws IOException, InterruptedException {
        String response = farePriceShopClient.getBeneficiaryList(shopNumber);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @GetMapping("test")
    public String getMethodName() throws IOException, InterruptedException {
        return  "";
    }
    

}