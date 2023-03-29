package com.devon.remotejenkins.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
public class ConsoleOutput {


    public ResponseEntity<String> fetchConsoleOutput() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8585/job/Hello World/17/consoleText";
        String auth = "ajayabhinandan:Mar@2023#123";
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
