from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
import numpy as np
from flask import Flask, request, jsonify
import os
from io import BytesIO
app = Flask(__name__)
# model = load_model("cnn_pneumonia_model.h5")


# Chemin du script actuel
current_dir = os.path.dirname(__file__)
model_path = os.path.join(current_dir, "cnn_pneumonia_model.h5")

model = load_model(model_path)


def preprocess(img_path):
    img = image.load_img(img_path, target_size=(224, 224))  # adapter à ton modèle
    img_array = image.img_to_array(img)/255.0
    img_array = np.expand_dims(img_array, axis=0)
    return img_array

@app.route('/predict', methods=['POST'])
def predict():
    
    file = request.files['file']
    img = preprocess(BytesIO(file.read()))

    
    pred = model.predict(img)[0][0]
    label = "PNEUMONIA" if pred > 0.5 else "NORMAL"
    return jsonify({"prediction": label, "confidence": float(pred)})

if __name__ == "__main__":
    app.run(port=5000)
