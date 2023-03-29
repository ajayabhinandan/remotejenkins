package com.devon.remotejenkins.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
public class ConsoleOutput {

    @Value("${jenkins.username}")
    private String userName;
    @Value("${jenkins.password}")
    private String password;
    @Value("${jenkins.url}")
    private  String jenkinsBaseURL;


    @Autowired
    private  RestTemplate restTemplate;

    public ResponseEntity<String> fetchConsoleOutput(String jobName,Integer buildNumber) {
        String url = jenkinsBaseURL+"/job/"+jobName+"/"+buildNumber+"/consoleText";
        String auth = userName+":"+password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String response=stringResponseEntity.getBody();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
