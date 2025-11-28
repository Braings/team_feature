from flask import Flask, request, jsonify
from ask_csv import get_exercise_recommendation

app = Flask(__name__)

@app.route('/recommend', methods=['POST'])
def recommend():
    try:
        data = request.get_json()
        if not data:
            return jsonify({"error": "No JSON data provided"}), 400

        gender = data.get('gender')
        height = data.get('height')
        weight = data.get('weight')
        age = data.get('age')

        if not all([gender, height, weight, age]):
            return jsonify({"error": "Missing required fields: gender, height, weight, age"}), 400

        # 데이터 타입 변환 (필요 시)
        try:
            height = float(height)
            weight = float(weight)
            age = int(age)
        except ValueError:
            return jsonify({"error": "Height, weight, and age must be numbers"}), 400

        recommendation = get_exercise_recommendation(gender, height, weight, age)
        
        return jsonify({"recommendation": recommendation})

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
