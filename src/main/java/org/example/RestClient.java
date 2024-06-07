package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;

import static java.net.http.HttpRequest.BodyPublishers.noBody;

public class RestClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private Map<String, String> headers;

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.headers = new HashMap<>();
    }

    public RestClient setBasicAuth(String username, String password) {
        String valueToEncode = username + ":" + password;
        headers.put("Authorization", "Basic " + Base64.getEncoder().withoutPadding().encodeToString(valueToEncode.getBytes()));
        return this;
    }

    public ApiResponse get(String path) throws IOException, InterruptedException {
        return doCall("GET", path, null);
    }

    public <R> ApiResponse post(String path, R requestBody) throws IOException, InterruptedException {
        return doCall("POST", path, requestBody);
    }

    public <R> ApiResponse patch(String path, R requestBody) throws IOException, InterruptedException {
        return doCall("PATCH", path, requestBody);
    }

    public <R> ApiResponse put(String path, R requestBody) throws IOException, InterruptedException {
        return doCall("PUT", path, requestBody);
    }

    public ApiResponse delete(String path) throws IOException, InterruptedException {
        return doCall("DELETE", path, null);
    }

    public <R> ApiResponse doCall(String method, String path, R requestBody) throws IOException, InterruptedException {

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path));

        if (method.equals("GET") || method.equals("DELETE")) {
            builder.method(method, noBody());
        } else {
            String json = DefaultMapper.getInstance().writeValueAsString(requestBody);
            builder.method(method, HttpRequest.BodyPublishers.ofString(json))
                    .header("Content-Type", "application/json");
        }

        if (!headers.isEmpty()) {
            builder.headers(mapToStringArray(headers));
        }

        HttpRequest request = builder.build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

        return ApiResponse.builder()
                .statusCode(response.statusCode())
                .body(response.body())
                .build();
    }

    private String[] mapToStringArray(Map<String, String> map) {
        List<String> resultList = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            resultList.add(entry.getKey());
            resultList.add(entry.getValue());
        }
        return resultList.toArray(new String[0]);
    }
}