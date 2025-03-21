package com.vi.StoryHelperBack.service.impl;

import com.vi.StoryHelperBack.service.TokenCheckerService;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;

public class TokenCheckerServiceImpl implements TokenCheckerService {
    @Value("${auth.service.check}")
    private String url;

    @Override
    public boolean checkToken(String token, String username) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(URI.create(url));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.addHeader(HttpHeaders.ACCEPT_CHARSET, "utf-8");
        request.setEntity(new StringEntity("{\"token\": \"" + token + "\", \"username\": \"" + username + "\"}", "utf-8"));

        HttpResponse response;
        try {
            response = (HttpResponse) httpClient.execute(request);
            return (boolean) response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
