package com.medical.ai.pneumonia_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AgentsController {

    @GetMapping("/agents")
    public String agentsStatus() {
        return "agents";
    }
}
