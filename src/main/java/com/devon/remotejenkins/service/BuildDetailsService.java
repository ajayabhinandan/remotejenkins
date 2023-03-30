package com.devon.remotejenkins.service;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;


@Service
public class BuildDetailsService {

    @Value("${jenkins.username}")
    private String userName;
    @Value("${jenkins.password}")
    private String password;
    @Value("${jenkins.url}")
    private  String jenkinsBaseURL;
    public String fetchJobDetails(String buildName, Integer buildNumber){
        RestTemplate restTemplate = new RestTemplate();
        String url = jenkinsBaseURL+"/job/"+buildName+"/"+buildNumber+"/api/json?pretty=true";
        String auth = userName+":"+password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String response=stringResponseEntity.getBody();
        JSONObject obj = new JSONObject(response);
        String status=obj.getString("result");
        System.out.println(status);
        return status;

    }

}
