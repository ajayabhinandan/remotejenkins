package com.devon.remotejenkins.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/github")
public class GitHubDetailsController {


    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public void getAllRepos(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> stringResponseEntity = restTemplate
                .exchange("https://api.github.com/users/shivanbgtsi/repos", HttpMethod.GET, entity, String.class);
        String response=stringResponseEntity.getBody();
        System.out.println(response);
    }

}
