package com.medical.ai.pneumonia_project.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Controller
public class StatisticsController {

    @GetMapping("/statistics")
    public String stats(Model model) throws Exception {

        JSONArray array = new JSONArray(
                Files.readString(Path.of("data/chest-x-ray_predictions.json")));

        long total = array.length();
        
        double pneumoniaCount = 0;
        double pneumoniaConfidenceSum = 0;
        double normalCount = 0;
        double normalConfidenceSum = 0;

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String prediction = obj.getString("prediction");
            double confidence = obj.getDouble("confidence");

            if ("PNEUMONIA".equals(prediction)) {
                pneumoniaCount++;
                pneumoniaConfidenceSum += confidence;
            } else if ("NORMAL".equals(prediction)) {
                normalCount++;
                normalConfidenceSum += confidence;
            }
        }

        double avgPneumoniaConfidence = pneumoniaCount > 0 ? pneumoniaConfidenceSum / pneumoniaCount : 0.0;
        double avgNormalConfidence = normalCount > 0 ? normalConfidenceSum / normalCount : 0.0;

        model.addAttribute("total", total);
        model.addAttribute("pneumonia", (long) pneumoniaCount);
        model.addAttribute("normal", (long) normalCount);
     
        model.addAttribute("avgPneumoniaConfidence", avgPneumoniaConfidence);
        model.addAttribute("avgNormalConfidence", avgNormalConfidence);



        return "statistics";
    }
}
