# 🫁 Pneumonia Detection System

**CNN-based Medical Image Analysis with Multi-Agent Architecture (JADE + Spring Boot + Flask)**

---

## 📌 Project Overview

This project is an **end-to-end intelligent system for pneumonia detection** from chest X-ray images.
It combines **Deep Learning (CNN)**, a **Python inference API**, and a **Java multi-agent system (JADE)** orchestrated within a **Spring Boot web application**.

The system allows a user to upload a chest X-ray image via a web interface, processes it through multiple agents, and returns a medical prediction (**PNEUMONIA** or **NORMAL**) with a confidence score.

---

## 🧠 Machine Learning Model

* **Dataset**:
  Kaggle – *Chest X-Ray Images (Pneumonia)*
  [https://www.kaggle.com/datasets/paultimothymooney/chest-xray-pneumonia](https://www.kaggle.com/datasets/paultimothymooney/chest-xray-pneumonia)

* **Model**:
  Convolutional Neural Network (CNN) built with **TensorFlow / Keras**

* **Input size**: `224 × 224 × 3`

* **Output**: Binary classification (PNEUMONIA / NORMAL)

* **Performance**:

  * Training accuracy ≈ **98%**
  * Validation accuracy fluctuates due to dataset imbalance and limited validation size

* **Trained model download**: [Modèle CNN Pneumonia (Google Drive)](https://drive.google.com/file/d/1DKKZagH73Qv8atL6lqE-wHWf_WsUE7cX/view?usp=drive_link)

* **Notebook**:

  * `cnn_pneumonia_training.ipynb` – model training, evaluation, and testing

---

## 🏗️ System Architecture

```
User
 │
 ▼
Spring Boot Web UI (Thymeleaf)
 │
 ▼
MainAgent (JADE)
 │
 ▼
DataAgent ──► MedicalDecisionAgent ──► Python Flask API
 │                                   │
 ▼                                   ▼
ValidationAgent                 CNN Model
 │
 ▼
AlertAgent (high-risk cases)
```

---

## 📁 Project Structure

```
pneumonia-project/
├─ cnn_pneumonia_training.ipynb
├─ python_api/
│  └─ app.py
└─ pneumonia-project/
   ├─ pom.xml
   ├─ libs/
   │  ├─ jade.jar
   │  └─ commons-codec-1.18.0.jar
   ├─ data/
   │  ├─ test_predictions.json
   │  └─ chest-x-ray.json
   │
   ├─ uploads/
   └─ src/
      └─ main/
         ├─ java/com/medical/ai/pneumonia_project/
         │  ├─ agents/
         │  └─ controller/
         └─ resources/
            ├─ templates/
            └─ static/css/
```

---

## 🧩 Technologies Used

### 🔹 AI & Data

* TensorFlow / Keras
* NumPy
* Scikit-learn
* Pillow

### 🔹 Backend

* Python (Flask API)
* Java 17
* Spring Boot
* JADE (Multi-Agent System)

### 🔹 Frontend

* Thymeleaf
* HTML / CSS

### 🔹 Tools

* Maven
* Git / GitHub

---

## 🚀 How to Run the Project

### 1️⃣ Run the Python Inference API

```bash
cd python_api
python -m venv venv
venv\Scripts\activate   # Windows
pip install flask tensorflow pillow numpy
python predict.py
```

API runs on:
`http://localhost:5000/predict`

---

### 2️⃣ Run the Spring Boot Application

```bash
cd pneumonia-project
mvn clean package
mvn spring-boot:run
```

Web app runs on:
`http://localhost:8080`

---

## 🔌 API Endpoint

### POST `/predict`

* **Input**: image file (`multipart/form-data`)
* **Output**:

```json
{
  "prediction": "PNEUMONIA",
  "confidence": 0.94
}
```

---

## 📊 Data & Logs

* `uploads/` → user-uploaded X-ray images
* `data/test_predictions.json` → prediction history
* `data/chest-x-ray_predictions.json` → dataset predictions (statistics)

---

## 🧪 Testing

```bash
mvn test
```

---

## ⚠️ Important Notes

* Ensure **Flask API is running before using the web interface**
* JADE is included via `libs/jade.jar`
* CNN model is loaded once at API startup for performance
* System supports asynchronous agent communication

---

## 🎓 Academic Value

This project demonstrates:

* Medical image classification using deep learning
* Integration of AI models into real-world systems
* Multi-agent system design (JADE)
* Distributed architecture (Java ↔ Python)
* Full-stack AI application development

---
![Java](https://img.shields.io/badge/Java-17-blue)
![TensorFlow](https://img.shields.io/badge/TensorFlow-2.x-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green)


## 👤 Author

**Meriam Sikini**
Master’s Student – Big Data, AI & Advanced Applications
📍 Morocco
