#main.py
from fastapi import FastAPI
import pickle
import pandas as pd

app = FastAPI()

#get the model
with open("src/main/models/svm_model.pkl", 'rb') as f:
    similarity_model = pickle.load(f)

#get the data (image as a graph) from the csv
def get_data(filename: str):
    data = pd.read_csv(filename)
    return data

#get the similarity score from the model
def get_similarity(image_graph):
    similarity_score = similarity_model.decision_function(image_graph)
    
    return similarity_score[0]

def classify(image_graph):
    # prediction = model.predict(image_array)
    prediction = "example_result"
    return prediction

@app.get("/health")
def health_check():
    try:
        return {"status": "ok", "db": "connected"}
    except Exception as e:
        return f"error: {e}"

@app.get("/Greeting")
async def greet():
    return ("message : Hi. Welcome to the Image Similarity and Prediction API!")

@app.post("/Name_Greeting")
async def name_greet(name: str):
    return f"Hi {name}, Welcome to the Image Similarity and Prediction API"

@app.get("/Predict")
async def predict_image():
    try:
        image_graph = get_data('data/image_data.csv')
        similarity = get_similarity(image_graph)
        return str(similarity)

    except Exception as e:
        return {"error": str(e)}
    