package com.kamalkavin96.tamilnadu_gov_api.configuration.tnpds;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpHeadersConfiguration {

    public static final String BASE_URL = "https://portalwebservice.tnpds.gov.in";

    public HttpRequest.Builder apply(HttpRequest.Builder builder) {
        return builder
                .header("Accept", "application/json, text/plain, */*")
                .header("User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/153.0.0.0 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Origin", "https://www.tnpds.gov.in")
                .header("Referer", "https://www.tnpds.gov.in/");
    }

    public String makeGetRequest(String endPoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        String url = BASE_URL + endPoint;
        log.info(url);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        this.apply(builder);
        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String makePostRequest(
            String endPoint,
            String requestBody) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        String url = BASE_URL + endPoint;

        log.info("POST {}", url);
        log.info("Request Body: {}", requestBody);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(
                        HttpRequest.BodyPublishers.ofString(requestBody));

        this.apply(builder);

        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        log.info("Response Status: {}", response.statusCode());

        String responseBody = response.body();

        log.info("Response Body: {}",responseBody);

        return responseBody;
    }

}
