package com.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/api")
public class ApiGatewayController
{
    @GetMapping("/test")
    public String test(){
        throw new RuntimeException("Message");
    }
}
