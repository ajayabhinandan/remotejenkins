package com.devon.remotejenkins.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.commons.codec.binary.Base64;

@Service
public class JenkinsService {
    @Value("${jenkins.username}")
    private String userName;
    @Value("${jenkins.password}")
    private String password;
    @Value("${jenkins.url}")
    private  String jenkinsBaseURL;


    @Autowired
    private  RestTemplate restTemplate;

    public ResponseEntity<String> triggerbuild(String token,String jobName) {
        String url = jenkinsBaseURL+"/job/"+jobName+ "/build?token="+token;
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
        String auth = userName+":"+password;
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
        String url= jenkinsBaseURL+"/crumbIssuer/api/json";
        String auth = userName+":"+password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
}
