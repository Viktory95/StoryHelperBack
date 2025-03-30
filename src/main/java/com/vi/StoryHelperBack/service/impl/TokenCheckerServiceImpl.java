package com.vi.StoryHelperBack.service.impl;

import com.vi.StoryHelperBack.service.TokenCheckerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.net.URI;

@Service
@Slf4j
public class TokenCheckerServiceImpl implements TokenCheckerService {
    @Value("${auth.check.url}")
    private String url;

    @Override
    public boolean checkToken(String token, String username) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(URI.create(url+"?token=" + token + "&username=" + username));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.addHeader(HttpHeaders.ACCEPT_CHARSET, "utf-8");

        HttpResponse response;
        try {
            response = (HttpResponse) httpClient.execute(request);
            return Boolean.parseBoolean(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
