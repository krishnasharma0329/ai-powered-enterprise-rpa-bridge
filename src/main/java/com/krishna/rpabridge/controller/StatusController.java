
package com.krishna.rpabridge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class StatusController {

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        return Map.of(
                "application", "AI-Powered Enterprise RPA Bridge",
                "status", "UP",
                "message", "Application is running successfully"
        );
    }
}
