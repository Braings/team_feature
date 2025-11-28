import google.generativeai as genai
import os
import pandas as pd
from dotenv import load_dotenv

def get_exercise_recommendation(target_gender, target_height, target_weight, target_age):
    # --- 1. API 키 및 모델 설정 ---
    load_dotenv()
    api_key = os.getenv("GOOGLE_API_KEY")
    if not api_key:
        return "오류: GOOGLE_API_KEY를 .env 파일에서 찾을 수 없습니다."

    try:
        genai.configure(api_key=api_key)
        model = genai.GenerativeModel('gemini-2.0-flash')
    except Exception as e:
        return f"API 설정 중 오류: {e}"

    # --- 연령대 매핑 ---
    if target_age < 19:
        target_age_group = '청소년'
    elif target_age < 65:
        target_age_group = '성인'
    else:
        target_age_group = '어르신'

    question = f"키 {target_height}cm, 몸무게 {target_weight}kg인 {target_age}세({target_age_group}) {target_gender}의 추천 운동을 알려줘."
    
    # --- 3. 로컬 CSV에서 관련 데이터 '검색' (RAG의 Retrieve 단계) ---
    csv_file_path = 'EX-HALF.csv'
    example_exercises_string = ""
    average_stats_string = ""
    
    try:
        # ⭐️ 최적화: 필요한 열만 로드하고 데이터 타입 지정
        # 007: 악력, 012: 앉아윗몸앞으로굽히기, 019: 교차윗몸일으키기, 022: 제자리멀리뛰기
        required_cols = [
            'AGRDE_FLAG_NM', 'SEXDSTN_FLAG_CD', 
            'MESURE_IEM_001_VALUE', 'MESURE_IEM_002_VALUE', 'MVM_PRSCRPTN_CN',
            'MESURE_IEM_007_VALUE', 'MESURE_IEM_012_VALUE', 'MESURE_IEM_019_VALUE', 'MESURE_IEM_022_VALUE'
        ]
        dtype_dict = {
            'AGRDE_FLAG_NM': 'string',
            'SEXDSTN_FLAG_CD': 'string',
            'MESURE_IEM_001_VALUE': 'float32',
            'MESURE_IEM_002_VALUE': 'float32',
            'MVM_PRSCRPTN_CN': 'string',
            'MESURE_IEM_007_VALUE': 'float32',
            'MESURE_IEM_012_VALUE': 'float32',
            'MESURE_IEM_019_VALUE': 'float32',
            'MESURE_IEM_022_VALUE': 'float32'
        }

        df = pd.read_csv(csv_file_path, usecols=required_cols, dtype=dtype_dict)

        # 필터링에 필요한 열에 NaN 값이 없는 행만 선택 (운동 처방 및 기본 신체 정보)
        df = df.dropna(subset=['AGRDE_FLAG_NM', 'SEXDSTN_FLAG_CD', 'MESURE_IEM_001_VALUE', 'MESURE_IEM_002_VALUE', 'MVM_PRSCRPTN_CN'])

        # --- 평균 운동 능력 계산 ---
        # 동일 연령대 및 성별 그룹 필터링
        group_df = df[
            (df['AGRDE_FLAG_NM'] == target_age_group) &
            (df['SEXDSTN_FLAG_CD'] == target_gender)
        ]

        if not group_df.empty:
            avg_grip = group_df['MESURE_IEM_007_VALUE'].mean()
            avg_flex = group_df['MESURE_IEM_012_VALUE'].mean()
            avg_situp = group_df['MESURE_IEM_019_VALUE'].mean()
            avg_jump = group_df['MESURE_IEM_022_VALUE'].mean()

            average_stats_string = f"""
            [동일 연령대({target_age_group}) 및 성별({target_gender}) 평균 운동 수행 능력]
            - 악력: {avg_grip:.1f} kg
            - 앉아윗몸앞으로굽히기 (유연성): {avg_flex:.1f} cm
            - 교차윗몸일으키기 (근지구력): {avg_situp:.1f} 회
            - 제자리멀리뛰기 (순발력): {avg_jump:.1f} cm
            """
        else:
            average_stats_string = "해당 연령대 및 성별의 평균 운동 데이터를 찾을 수 없습니다."

        # --- '유사한' 사용자 필터링 (질문 조건 기준) ---
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
        else:
            # 중복 제거 및 문자열로 결합 (API에 컨텍스트로 제공)
            unique_exercises = list(set(example_exercises_list))
            example_exercises_string = "\n\n".join(unique_exercises)

    except FileNotFoundError:
        return f"오류: '{csv_file_path}' 파일을 찾을 수 없습니다."
    except KeyError as e:
        return f"오류: CSV에 필요한 열({e})이 없습니다. 열 이름을 확인하세요."
    
    # --- 4. 프롬프트 구성 (RAG의 Augment 단계) ---
    prompt = f"""
    사용자 정보: {question}
    
    {average_stats_string}

    다음은 내 CSV 데이터에서 찾은, 사용자의 요청과 유사한 신체 조건을 가진 사람들의 실제 운동 추천 내역입니다.
    
    ---[참고 데이터: 유사 사례 운동 추천]----
    {example_exercises_string}
    
    위 정보를 바탕으로 사용자에게 적합한 운동을 추천해주세요.
    특히, 위에서 제공된 '평균 운동 수행 능력'을 언급하며 사용자가 목표로 삼을 수 있는 수치를 제시해주세요.
    만약 유사한 운동 데이터가 없다면, 일반적인 운동을 추천하되, 사용자 정보에 맞춰 조언해주세요.
    """

    # --- 5. API 호출 및 응답 ---
    try:
        # print("\n데이터에 기반해 AI 응답을 생성하고 있습니다...") # API 모드에서는 로그 생략 가능
        response = model.generate_content(prompt)
        return response.text

    except Exception as e:
        return f"API 호출 중 오류 발생: {e}"

if __name__ == "__main__":
    # 테스트용 코드
    try:
        g = input("성별(M/F): ").strip().upper()
        h = float(input("키(cm): ").strip())
        w = float(input("몸무게(kg): ").strip())
        a = int(input("나이(만): ").strip())
        print(get_exercise_recommendation(g, h, w, a))
    except ValueError:
        print("입력 오류: 숫자를 입력해주세요.")