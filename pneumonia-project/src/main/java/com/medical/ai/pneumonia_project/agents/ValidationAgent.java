package com.medical.ai.pneumonia_project.agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ValidationAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("✅ ValidationAgent ready");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    
                    // Parse the result JSON - find last | to separate result from requestId
                    int lastPipeIndex = content.lastIndexOf("|");
                    String resultJson;
                    String requestId = "default";
                    
                    if (lastPipeIndex > 0) {
                        resultJson = content.substring(0, lastPipeIndex);
                        requestId = content.substring(lastPipeIndex + 1);
                    } else {
                        resultJson = content;
                    }

                    try {
                        JSONObject result = new JSONObject(resultJson);

                        boolean isHighRisk = result.getString("prediction").equals("PNEUMONIA")
                                && result.getDouble("confidence") >= 0.85;

                        if (isHighRisk) {
                            result.put("alertMessage", "High risk pneumonia detected");
                            MainAgent.setResultForRequest(requestId, result.toString());

                            ACLMessage alert = new ACLMessage(ACLMessage.INFORM);
                            alert.addReceiver(new AID("alertAgent", AID.ISLOCALNAME));
                            alert.setContent("High risk pneumonia detected - Confidence: " + 
                                           (result.getDouble("confidence") * 100) + "%");
                            send(alert);
                        }
                    } catch (Exception e) {
                        System.err.println("❌ ValidationAgent parsing error: " + e.getMessage());
                    }
                } else {
                    block();
                }
            }
        });
    }
}







