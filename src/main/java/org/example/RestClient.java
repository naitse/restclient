package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class RestClient {
    private final Gson gson;
    private final HttpClient httpClient;
    private final String baseUrl;

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = DefaultGson.getInstance();
    }

    public ApiResponse get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

        return ApiResponse.builder()
                .statusCode(response.statusCode())
                .body(response.body())
                .build();
    }

    public <R> ApiResponse post(String path, R requestBody) throws IOException, InterruptedException {
        String json = gson.toJson(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

        return ApiResponse.builder()
                .statusCode(response.statusCode())
                .body(response.body())
                .build();
    }
}