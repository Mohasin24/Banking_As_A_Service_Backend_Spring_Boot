package com.reporting_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reporting")
public class ReportingController
{
    @GetMapping("/test")
    public String test(){
        return "ReportingController controller";
    }
}