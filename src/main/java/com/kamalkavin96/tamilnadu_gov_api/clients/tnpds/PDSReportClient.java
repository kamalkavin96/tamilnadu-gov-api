package com.kamalkavin96.tamilnadu_gov_api.clients.tnpds;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.kamalkavin96.tamilnadu_gov_api.configuration.tnpds.HttpHeadersConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PDSReportClient {

    private final HttpHeadersConfiguration headersConfiguration;

    /**
     * Get state-level PDS report.
     */
    public String getStateListReport()
            throws IOException, InterruptedException {

        String requestBody = """
                {}
                """;

        return headersConfiguration.makePostRequest(
                "/portalreport/stateReport",
                requestBody
        );
    }

    /**
     * Get district-level PDS report.
     */
    public String getDistrictReport(Long districtId, String districtName)
            throws IOException, InterruptedException {

        String requestBody = String.format("""
                {
                    "districtId": %d,
                    "districtName": "%s"
                }
                """, districtId, districtName);

        return headersConfiguration.makePostRequest(
                "/portalreport/districtReport",
                requestBody
        );
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
                requestBody
        );
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
                requestBody
        );
    }

    /**
     * Get beneficiaries.
     */
    public String getBenefs(
            Integer page,
            Integer size,
            Long sfpsCode,
            Integer cardTypeGroupId
    ) throws IOException, InterruptedException {

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
                cardTypeGroupId
        );

        return headersConfiguration.makePostRequest(
                "/portalreport/getbenefs",
                requestBody
        );
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
                requestBody
        );
    }

    /**
     * Get FPS bill list.
     */
    public String getBillList(
            Integer page,
            Integer size,
            Long fpsId
    ) throws IOException, InterruptedException {

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
                size
        );

        return headersConfiguration.makePostRequest(
                "/report/getBillList",
                requestBody
        );
    }
}