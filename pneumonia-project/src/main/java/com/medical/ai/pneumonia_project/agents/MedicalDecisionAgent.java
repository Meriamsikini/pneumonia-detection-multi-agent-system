package com.medical.ai.pneumonia_project.agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class MedicalDecisionAgent extends Agent {

    private static final String JSON_PATH = "data/test_predictions.json";

    @Override
    protected void setup() {
        System.out.println("🧠 MedicalDecisionAgent started");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    try {
                        String content = msg.getContent();
                        String[] parts = content.split("\\|");
                        String imagePath = parts[0];
                        String requestId = parts.length > 1 ? parts[1] : "default";
                        
                        File image = new File(imagePath);
                        JSONObject result = callFlaskAPI(image);

                        // 🔹 Enregistrement du résultat
                        saveResult(image.getName(), result);

                        // 🔹 Mise à jour du MainAgent avec l'ID de requête
                        MainAgent.setResultForRequest(requestId, result.toString());

                        // 🔹 Forward au ValidationAgent avec l'ID
                        ACLMessage toValidation = new ACLMessage(ACLMessage.INFORM);
                        toValidation.addReceiver(new AID("validationAgent", AID.ISLOCALNAME));
                        toValidation.setContent(result.toString() + "|" + requestId);
                        send(toValidation);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else block();
            }
        });
    }

    private JSONObject callFlaskAPI(File file) throws Exception {
        String boundary = Long.toHexString(System.currentTimeMillis());
        URL url = new URL("http://localhost:5000/predict");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream out = conn.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true)) {

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                  .append(file.getName()).append("\"\r\n\r\n").flush();

            Files.copy(file.toPath(), out);
            out.flush();
            writer.append("\r\n--").append(boundary).append("--").flush();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return new JSONObject(in.readLine());
    }

    private void saveResult(String imageId, JSONObject result) throws IOException {
        File file = new File(JSON_PATH);
        file.getParentFile().mkdirs();

        JSONArray array = file.exists()
                ? new JSONArray(Files.readString(file.toPath()))
                : new JSONArray();

        JSONObject obj = new JSONObject();
        obj.put("image_id", imageId);
        obj.put("prediction", result.getString("prediction"));
        obj.put("confidence", result.getDouble("confidence"));
        obj.put("agent_task", "medical_image_analysis");
        obj.put("model", "CNN_Pneumonia_v1");
        obj.put("timestamp", System.currentTimeMillis());

        array.put(obj);
        Files.writeString(file.toPath(), array.toString(2));
    }
}
