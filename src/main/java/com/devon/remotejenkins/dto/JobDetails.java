package com.devon.remotejenkins.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDetails {

    private String status;
    private String jobDetails;



}
