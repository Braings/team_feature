import requests
import json

url = "http://127.0.0.1:5000/recommend"
data = {
    "gender": "M",
    "height": 180,
    "weight": 75,
    "age_group": "성인"
}

try:
    response = requests.post(url, json=data)
    print(f"Status Code: {response.status_code}")
    print("Response JSON:")
    print(json.dumps(response.json(), indent=2, ensure_ascii=False))
except Exception as e:
    print(f"Error: {e}")
