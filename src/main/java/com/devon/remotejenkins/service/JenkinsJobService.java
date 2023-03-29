package com.devon.remotejenkins.service;

import com.devon.remotejenkins.dto.JenkinsJob;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Service
public class JenkinsJobService {

    @Value("${jenkins.username}")
    private String userName;
    @Value("${jenkins.password}")
    private String password;
    @Value("${jenkins.url}")
    private  String jenkinsBaseURL;

    @Autowired
    private  RestTemplate restTemplate;

    public ResponseEntity<ArrayList<JenkinsJob>> fetchJobs() {

        String url = jenkinsBaseURL+"/api/json?pretty=true";
        String auth = userName+":"+password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String tokenResponse = stringResponseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject obj = new JSONObject(tokenResponse);
        String response=obj.get("jobs").toString();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        ArrayList<JenkinsJob> jobList;
            try {
                jobList = objectMapper.readValue(response, typeFactory.constructCollectionType(ArrayList.class, JenkinsJob.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


        return new ResponseEntity<>(jobList, HttpStatus.OK);
    }
}
