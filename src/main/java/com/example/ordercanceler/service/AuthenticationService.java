package com.example.ordercanceler.service;

import com.example.ordercanceler.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${auth.service.validate-endpoint}")
    private String validateEndpoint;

    public UserPrincipal validateToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = authServiceUrl + validateEndpoint;
            log.debug("Validating token with auth service at: {}", url);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> userData = response.getBody();
                log.debug("Token validation successful for user: {}", userData.get("username"));

                return UserPrincipal.builder()
                        .id((String) userData.get("id"))
                        .username((String) userData.get("username"))
                        .email((String) userData.get("email"))
                        .roles((List<String>) userData.get("roles"))
                        .build();
            }
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
        }
        return null;
    }
}
