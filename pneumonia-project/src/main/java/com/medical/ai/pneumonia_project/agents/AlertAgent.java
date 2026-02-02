package com.medical.ai.pneumonia_project.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AlertAgent extends Agent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void setup() {
        System.out.println("🚨 AlertAgent active");

        addBehaviour(new CyclicBehaviour() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println("🚨 MEDICAL ALERT: " + msg.getContent());
                } else block();
            }
        });
    }
}