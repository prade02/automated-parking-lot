package com.automated.parkinglot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/test")
public class TestController {

    @GetMapping
    public Map<String, Object> getTestMessage() {
        return Map.of("message", "API works");
    }
}
