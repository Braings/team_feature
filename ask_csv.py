import google.generativeai as genai
import os
import pandas as pd  # Pandas 라이브러리 임포트
from dotenv import load_dotenv

def get_exercise_recommendation():
    # --- 1. API 키 및 모델 설정 ---
    load_dotenv()
    api_key = os.getenv("GOOGLE_API_KEY")
    if not api_key:
        print("오류: GOOGLE_API_KEY를 .env 파일에서 찾을 수 없습니다.")
        return

    try:
        genai.configure(api_key=api_key)
        model = genai.GenerativeModel('gemini-2.0-flash')
    except Exception as e:
        print(f"API 설정 중 오류: {e}")
        return

    # --- 2. 사용자 요청 정의 ---
    target_height = 180
    target_weight = 60
    target_age_group = '성인'
    target_gender = 'M'
    
    question = f"키 {target_height}cm, 몸무게 {target_weight}kg인 {target_age_group} {target_gender}의 추천 운동을 알려줘."
    print(f"요청: {question}\n")

    # --- 3. 로컬 CSV에서 관련 데이터 '검색' (RAG의 Retrieve 단계) ---
    csv_file_path = 'EX-HALF.csv'
    example_exercises_string = ""
    
    try:
        df = pd.read_csv(csv_file_path)

        # ⭐️ 중요: 사용할 열을 숫자형으로 변환 (오류 발생 시 NaN으로)
        # 키: MESURE_IEM_001_VALUE, 체중: MESURE_IEM_002_VALUE
        df['MESURE_IEM_001_VALUE'] = pd.to_numeric(df['MESURE_IEM_001_VALUE'], errors='coerce')
        df['MESURE_IEM_002_VALUE'] = pd.to_numeric(df['MESURE_IEM_002_VALUE'], errors='coerce')

        # 필터링에 필요한 열에 NaN 값이 없는 행만 선택
        required_cols = ['AGRDE_FLAG_NM', 'SEXDSTN_FLAG_CD', 'MESURE_IEM_001_VALUE', 'MESURE_IEM_002_VALUE', 'MVM_PRSCRPTN_CN']
        df = df.dropna(subset=required_cols)

        # '유사한' 사용자 필터링 (질문 조건 기준)
        height_min, height_max = target_height - 5, target_height + 5  # 키 ±5cm
        weight_min, weight_max = target_weight - 5, target_weight + 5  # 몸무게 ±5kg

        filtered_df = df[
            (df['AGRDE_FLAG_NM'] == target_age_group) &
            (df['SEXDSTN_FLAG_CD'] == target_gender) &
            (df['MESURE_IEM_001_VALUE'].between(height_min, height_max)) &
            (df['MESURE_IEM_002_VALUE'].between(weight_min, weight_max))
        ]

        # 'MVM_PRSCRPTN_CN' (추천운동) 열의 내용을 리스트로 추출
        example_exercises_list = filtered_df['MVM_PRSCRPTN_CN'].tolist()
        
        if not example_exercises_list:
            example_exercises_string = "유사한 사용자를 CSV에서 찾지 못했습니다."
            print("CSV에서 유사한 데이터를 찾지 못했습니다. API가 일반적인 추천을 시도합니다.")
        else:
            # 중복 제거 및 문자열로 결합 (API에 컨텍스트로 제공)
            unique_exercises = list(set(example_exercises_list))
            example_exercises_string = "\n\n".join(unique_exercises)
            print(f"--- CSV에서 찾은 {len(unique_exercises)}개의 유사 사례 운동 루틴 ---")
            print(example_exercises_string)
            print("--------------------------------------")


    except FileNotFoundError:
        print(f"오류: '{csv_file_path}' 파일을 찾을 수 없습니다.")
        return
    except KeyError as e:
        print(f"오류: CSV에 필요한 열({e})이 없습니다. 열 이름을 확인하세요.")
        return
    except Exception as e:
        print(f"CSV 처리 중 오류: {e}")
        return

    # --- 4. API에 '검색된 정보'와 함께 질문 (RAG의 Generate 단계) ---
    
    prompt = f"""
    당신은 헬스케어 데이터 분석가입니다.
    
    다음은 내 CSV 데이터에서 찾은, 사용자의 요청과 유사한 신체 조건을 가진 사람들의 실제 운동 추천 내역입니다.
    
    ---[참고 데이터: 유사 사례 운동 추천]----
    {example_exercises_string}
    ---[참고 데이터 끝]----
    
    이 참고 데이터를 바탕으로, 다음 사용자를 위한 맞춤형 운동 루틴을 (준비운동, 본운동, 마무리운동으로 나누어) 새로 생성해주세요.
    
    사용자 요청: "{question}"
    
    (만약 참고 데이터가 "찾지 못했습니다"라면, 해당 신체조건에 맞는 일반적인 건강 정보를 바탕으로 추천해주세요.)
    """

    # --- 5. API 호출 및 응답 ---
    try:
        print("\n... API에 맞춤형 추천 요청 중 ...")
        response = model.generate_content(prompt)
        
        print("\n[Gemini 맞춤형 응답]")
        print(response.text)

    except Exception as e:
        print(f"API 호출 중 오류 발생: {e}")

if __name__ == "__main__":
    get_exercise_recommendation()