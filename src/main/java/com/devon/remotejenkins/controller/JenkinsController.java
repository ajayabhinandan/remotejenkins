package com.devon.remotejenkins.controller;


import com.devon.remotejenkins.JenkinsJob;
import com.devon.remotejenkins.service.ConsoleOutput;
import com.devon.remotejenkins.service.JenkinsJobService;
import com.devon.remotejenkins.service.JenkinsService;
import com.devon.remotejenkins.service.JobNumber;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class JenkinsController {
    @Autowired
    private JenkinsService service;
    @Autowired
    private JenkinsJobService jobService;
    @Autowired
    ConsoleOutput consoleOutput;
    @Autowired
    JobNumber jobNumber;

    @PutMapping("/build")
    public ResponseEntity<String> remoteTrigger(@RequestParam("token") String token){
        service.triggerbuild(token);
        return new ResponseEntity<>("Trigger Successful", HttpStatus.CREATED);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ArrayList<JenkinsJob>> getJobs(){
        return jobService.fetchJobs();
    }

    @GetMapping("/console")
    public ResponseEntity<String> getConsoleOutput(){
        return consoleOutput.fetchConsoleOutput();
    }

    @GetMapping("/jobnumber")
    public ResponseEntity<ArrayList<Integer>> getJobNumber(@RequestParam("name") String name){
        return jobNumber.fetchJObNUmber(name);
    }

}
