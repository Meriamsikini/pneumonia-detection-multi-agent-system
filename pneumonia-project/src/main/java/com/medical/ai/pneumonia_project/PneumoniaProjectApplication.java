package com.medical.ai.pneumonia_project;

import com.medical.ai.pneumonia_project.agents.AlertAgent;
import com.medical.ai.pneumonia_project.agents.DataAgent;
import com.medical.ai.pneumonia_project.agents.MainAgent;
import com.medical.ai.pneumonia_project.agents.MedicalDecisionAgent;
import com.medical.ai.pneumonia_project.agents.ValidationAgent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PneumoniaProjectApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PneumoniaProjectApplication.class, args);

        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        AgentContainer container = rt.createMainContainer(p);

        container.createNewAgent("mainAgent", MainAgent.class.getName(), null).start();
        container.createNewAgent("dataAgent", DataAgent.class.getName(), null).start();
        container.createNewAgent("medicalDecisionAgent", MedicalDecisionAgent.class.getName(), null).start();
        container.createNewAgent("validationAgent", ValidationAgent.class.getName(), null).start();
        container.createNewAgent("alertAgent", AlertAgent.class.getName(), null).start();
    }
}
