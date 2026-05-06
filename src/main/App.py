from fastapi import FastAPI

app = FastAPI()

# model = joblib.load("model.pkl")

def similarity(image_graph):
    # similarity_score = model.predict(image_array)
    similarity_score = "example_similarity_score"
    return similarity_score

def predict(image_graph):
    # prediction = model.predict(image_array)
    prediction = "example_result"
    return prediction

@app.post("/predict")
async def predict_image(image):
    similarity_score = similarity(image)
    prediction = predict(image)
    
    return {
        "similarity": similarity_score,
        "prediction": prediction
    }