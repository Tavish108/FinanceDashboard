package com.finance.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "04. Role Testing", description = "Role-based API testing")
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "4.1 Public API")
    @GetMapping("/public")
    public String publicApi() {
        return "Public endpoint ✅";
    }

    @Operation(summary = "4.2 Analyst Access")
    @GetMapping("/analyst")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public String analystApi() {
        return "Analyst + Admin 📊";
    }

    @Operation(summary = "4.3 Admin Access")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi() {
        return "Admin only ";
    }
}