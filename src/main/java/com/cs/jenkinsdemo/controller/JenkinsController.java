package com.cs.jenkinsdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JenkinsController {

    @GetMapping("/jenkins-url")
    public String getData(){
        return "Hello from Jenkins";
    }


}
