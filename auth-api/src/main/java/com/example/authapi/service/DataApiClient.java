package com.example.authapi.service;

import com.example.authapi.dto.AuthDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataApiClient {
    private final RestTemplate rt = new RestTemplate();
    private final String url;
    private final String token;

    public DataApiClient(@Value("${app.data-api-url}") String url,
                         @Value("${app.internal-token}") String token) {
        this.url = url;
        this.token = token;
    }

    public String transform(String text) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", token);

        var rest = rt.postForEntity(url, new HttpEntity<>(new AuthDto.TransformRequest(text), headers), AuthDto.TransformResponse.class);

        if (!rest.getStatusCode().is2xxSuccessful() || rest.getBody() == null) throw  new RuntimeException("Tramsform failed");
        return rest.getBody().result();
    }
}
