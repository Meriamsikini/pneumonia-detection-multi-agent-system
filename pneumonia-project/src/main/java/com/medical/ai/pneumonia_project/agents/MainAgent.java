package com.medical.ai.pneumonia_project.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.Behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.concurrent.ConcurrentHashMap;

public class MainAgent extends Agent {
    private static String lastResult;
    private static MainAgent instance;
    private static ConcurrentHashMap<String, String> resultMap = new ConcurrentHashMap<>();

    @Override
    protected void setup() {
        instance = this;
        System.out.println("🔗 MainAgent (Gateway) ready");
    }

    public static void setLastResult(String result) {
        lastResult = result;
    }

    public static String getLastResult() {
        return lastResult;
    }

    // 🔹 Stocker le résultat avec un ID de requête
    public static void setResultForRequest(String requestId, String result) {
        resultMap.put(requestId, result);
        lastResult = result; // Garder aussi la dernière valeur globale
    }

    // 🔹 Récupérer le résultat pour une requête spécifique
    public static String getLastResult(String requestId) {
        return resultMap.get(requestId);
    }

    // 🔹 Envoyer un message au DataAgent avec l'ID de requête
    public static void sendToDataAgent(String imagePath, String requestId) {
        if (instance == null) {
            System.out.println("❌ MainAgent not initialized");
            return;
        }

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("dataAgent", AID.ISLOCALNAME));
        msg.setContent(imagePath + "|" + requestId); // Format: imagePath|requestId
        instance.send(msg);
    }

    // 🔹 Méthode legacy pour compatibilité
    public static void sendToDataAgent(String imagePath) {
        sendToDataAgent(imagePath, "default");
    }

}
