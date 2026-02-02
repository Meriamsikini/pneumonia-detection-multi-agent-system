package com.medical.ai.pneumonia_project.agents;

	import jade.core.Agent;
	import jade.core.AID;
	import jade.core.behaviours.CyclicBehaviour;
	import jade.lang.acl.ACLMessage;

	import java.io.File;

	public class DataAgent extends Agent {

	    @Override
	    protected void setup() {
	        System.out.println("📂 DataAgent ready");

	        addBehaviour(new CyclicBehaviour() {
	            @Override
	            public void action() {
	                ACLMessage msg = receive();

	                if (msg != null) {
	                    String content = msg.getContent();
	                    String[] parts = content.split("\\|");
	                    String path = parts[0];
	                    String requestId = parts.length > 1 ? parts[1] : "default";
	                    
	                    File file = new File(path);

	                    if (file.exists() && isValidFormat(file.getName())) {
	                        ACLMessage forward = new ACLMessage(ACLMessage.INFORM);
	                        forward.addReceiver(new AID("medicalDecisionAgent", AID.ISLOCALNAME));
	                        forward.setContent(path + "|" + requestId); // Passer l'ID
	                        send(forward);
	                    } else {
	                        System.out.println("❌ Invalid image: " + path);
	                    }
	                } else {
	                    block();
	                }
	            }
	        });
	    }

	    private boolean isValidFormat(String name) {
	        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
	    }
	}
