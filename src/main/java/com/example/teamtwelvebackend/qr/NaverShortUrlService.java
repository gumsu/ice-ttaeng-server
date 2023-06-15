package com.example.teamtwelvebackend.qr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverShortUrlService {

    private static final String NAVER_SHORT_API_URL = "https://openapi.naver.com/v1/util/shorturl?url=";
    private static final String NAVER_CLIENT_ID = "X-Naver-Client-Id";
    private static final String NAVER_CLIENT_SECRET = "X-Naver-Client-Secret";

    @Value("${NAVER_CLIENT_ID}")
    private String clientId;
    @Value("${NAVER_CLIENT_SECRET}")
    private String clientSecret;

    public NaverShortUrlDto naverShortUrlApi(String url) {
        String apiURL = NAVER_SHORT_API_URL + url;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(NAVER_CLIENT_ID, clientId);
        headers.add(NAVER_CLIENT_SECRET, clientSecret);

        String requestBody = apiURL;
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverShortUrlDto> response = restTemplate.exchange(apiURL, HttpMethod.GET, httpEntity, NaverShortUrlDto.class);

        return response.getBody();
    }
}
