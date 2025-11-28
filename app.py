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
        age_group = data.get('age_group')

        if not all([gender, height, weight, age_group]):
            return jsonify({"error": "Missing required fields: gender, height, weight, age_group"}), 400

        # 데이터 타입 변환 (필요 시)
        try:
            height = float(height)
            weight = float(weight)
        except ValueError:
            return jsonify({"error": "Height and weight must be numbers"}), 400

        recommendation = get_exercise_recommendation(gender, height, weight, age_group)
        
        return jsonify({"recommendation": recommendation})

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
