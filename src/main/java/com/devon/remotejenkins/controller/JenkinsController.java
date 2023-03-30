package com.devon.remotejenkins.controller;


import com.devon.remotejenkins.dto.JenkinsJob;
import com.devon.remotejenkins.service.*;
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

    @Autowired
    BuildDetailsService buildDetailsService;

    @PutMapping("/build")
    public ResponseEntity<String> remoteTrigger(@RequestParam("token") String token,
                                                @RequestParam("jobName") String jobName){
        service.triggerbuild(token,jobName);
        return new ResponseEntity<>("Trigger Successful", HttpStatus.CREATED);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ArrayList<JenkinsJob>> getJobs(){
        return jobService.fetchJobs();
    }

    @GetMapping("/console")
    public ResponseEntity<String> getConsoleOutput(@RequestParam("buildName") String buildName,
                                @RequestParam("buildNumber") Integer buildNumber){
        return consoleOutput.fetchConsoleOutput(buildName,buildNumber);
    }

    @GetMapping("/jobnumber")
    public ResponseEntity<ArrayList<Integer>> getJobNumber(@RequestParam("name") String name){
        return jobNumber.fetchJObNUmber(name);
    }

    @GetMapping("/builddetails")
    public ResponseEntity<String> getJobDetails(@RequestParam("buildName") String buildName,
                                                @RequestParam("buildNumber") Integer buildNumber){
        return new ResponseEntity<>(buildDetailsService.fetchJobDetails(buildName, buildNumber), HttpStatus.OK);
    }

}
