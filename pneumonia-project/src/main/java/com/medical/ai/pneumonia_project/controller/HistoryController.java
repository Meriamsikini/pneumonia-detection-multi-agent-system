package com.medical.ai.pneumonia_project.controller;

import org.json.JSONArray;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HistoryController {

    @GetMapping("/history")
    public String history() {
        return "history";
    }
    	@GetMapping("/history/data")
    	@ResponseBody
    	public List<Map<String, Object>> getHistoryData() {
    	    List<Map<String, Object>> data = new ArrayList<>();

    	    File testFile = new File("data/test_predictions.json");
    	    if (!testFile.exists()) {
    	        System.out.println("📋 No test data file yet: " + testFile.getAbsolutePath());
    	        return data;
    	    }

    	    try {
    	        String content = Files.readString(testFile.toPath());
    	        JSONArray array = new JSONArray(content);

    	        for (int i = 0; i < array.length(); i++) {
    	            JSONObject obj = array.getJSONObject(i);

    	            Map<String, Object> row = new HashMap<>();

    	            double confidence = obj.getDouble("confidence");
    	            String prediction = obj.getString("prediction");

    	            String status = confidence >= 0.8 ? "Valid"
    	                    : confidence >= 0.6 ? "Uncertain" : "Low";

    	            String decision = "No_action";
    	            if ("PNEUMONIA".equals(prediction) && confidence >= 0.85) {
    	                decision = "Alert";
    	            } else if ("PNEUMONIA".equals(prediction) && confidence >= 0.6) {
    	                decision = "Follow_up";
    	            }

    	            row.put("image_id", obj.getString("image_id"));
    	            row.put("prediction", prediction);
    	            row.put("confidence", confidence);
    	            row.put("model", obj.getString("model"));
    	            row.put("timestamp", obj.getLong("timestamp"));
    	            row.put("status", status);
    	            row.put("decision", decision);

    	            data.add(row);
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }

    	    return data;
    	}
    }