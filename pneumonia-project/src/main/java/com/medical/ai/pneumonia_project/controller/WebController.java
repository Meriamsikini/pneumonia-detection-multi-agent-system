package com.medical.ai.pneumonia_project.controller;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.medical.ai.pneumonia_project.agents.MainAgent;

import org.springframework.ui.Model;

import java.io.File;
import java.util.UUID;

import org.json.JSONObject;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/predict")
    public String predict(@RequestParam("image") MultipartFile image, Model model) throws Exception {
        String projectDir = System.getProperty("user.dir");
        File uploadDir = new File(projectDir, "uploads");
        if (!uploadDir.exists()) uploadDir.mkdirs();

        File dest = new File(uploadDir, image.getOriginalFilename());
        image.transferTo(dest);

        // Générer un ID unique pour cette requête
        String requestId = UUID.randomUUID().toString();
        
        // Envoyer au système multi-agents avec l'ID
        MainAgent.sendToDataAgent(dest.getAbsolutePath(), requestId);

        // Attendre un peu que le traitement se termine (max 5 secondes)
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 5000) {
            String result = MainAgent.getLastResult(requestId);
            if (result != null) {
                model.addAttribute("result", result);
                return "result";
            }
            Thread.sleep(100);
        }

        // Si timeout, afficher un message d'erreur
        model.addAttribute("result", "{\"prediction\":\"PROCESSING\",\"confidence\":0.0,\"error\":\"Processing timeout\"}");
        return "result";
    }

    @GetMapping("/result")
    public String result(Model model) {
        String result = MainAgent.getLastResult();
        if (result == null) {
            result = "{\"prediction\":\"UNKNOWN\",\"confidence\":0.0}";
        }
        model.addAttribute("result", result);
        return "result";
    }

}

