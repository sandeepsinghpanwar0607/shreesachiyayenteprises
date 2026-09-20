package com.SSE.Website.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    @PreAuthorize("hasRole('ADMIN')")
    public String protectedTest() {

        return "ADMIN access working";

    }
}