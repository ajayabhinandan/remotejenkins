package com.devon.remotejenkins.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.commons.codec.binary.Base64;

@Service
public class JenkinsService {

    public ResponseEntity<String> triggerbuild(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8585/job/Hello World/build?token="+token;
        ResponseEntity<String> stringResponseEntity = crumbAuthentication();
        String tokenResponse = stringResponseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

// Convert the JSON string to a Java object
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(tokenResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String param = jsonNode.get("crumbRequestField").asText();
        String value = jsonNode.get("crumb").asText();
        String auth = "ajayabhinandan:Mar@2023#123";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set(param, value);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return exchange;
    }
    private ResponseEntity<String> crumbAuthentication(){
        RestTemplate restTemplate = new RestTemplate();
        String url="http://localhost:8585/crumbIssuer/api/json";
        String auth = "ajayabhinandan:Mar@2023#123";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
}
