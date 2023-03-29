package com.devon.remotejenkins.service;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Service
public class JobNumber {

    public ResponseEntity<ArrayList<Integer>> fetchJObNUmber(String name){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8585/job/"+name+"/api/json?tree=builds[number]";
        String auth = "ajayabhinandan:Mar@2023#123";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String tokenResponse = stringResponseEntity.getBody();

        JSONObject obj = new JSONObject(tokenResponse);
        JSONArray jsonArray= (JSONArray) obj.get("builds");
        ArrayList<Integer> jobNumberList= new ArrayList<>();

        for(int i=0;i<jsonArray.length();i++){
            JSONObject o = (JSONObject) jsonArray.get(i);
            jobNumberList.add((Integer) o.get("number"));
        }

        return new ResponseEntity<>(jobNumberList, HttpStatus.OK);
    }
}
