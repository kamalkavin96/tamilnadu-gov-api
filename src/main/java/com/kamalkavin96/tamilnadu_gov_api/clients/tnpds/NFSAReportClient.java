package com.kamalkavin96.tamilnadu_gov_api.clients.tnpds;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;

import com.kamalkavin96.tamilnadu_gov_api.configuration.tnpds.HttpHeadersConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NFSAReportClient {

    private final HttpHeadersConfiguration headersConfiguration;

    public String getDashboardCounts() throws IOException, InterruptedException {
        return headersConfiguration.makeGetRequest("/publicportal/rediscache/getHomePageDashboardCounts");
    }

    public String getStateList() throws IOException, InterruptedException {
        return headersConfiguration.makeGetRequest("/nfsa/reports/state");
    }

    public String getDistrictList(Integer districtNum) throws IOException, InterruptedException {
        return headersConfiguration.makeGetRequest("/nfsa/reports/district/" + districtNum);
    }

    public String getTalukList(Integer districtNum, Integer talukNum) throws IOException, InterruptedException {
        return headersConfiguration.makeGetRequest("/nfsa/reports/taluk/" + districtNum + "/" + talukNum);
    }

    public String getBeneficiaryList(Integer shopNumber) throws IOException, InterruptedException {
        String requestBody = String.format("""
                    {
                        "fpsId": "%s"
                    }
                """, shopNumber);
        return headersConfiguration.makePostRequest("/nfsa/reports/beneficiary/fetch", requestBody);
    }

}