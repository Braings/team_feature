# single_turn.py
import google.generativeai as genai
import os
from dotenv import load_dotenv

load_dotenv()
genai.configure(api_key=os.getenv("GOOGLE_API_KEY"))
model = genai.GenerativeModel('gemini-2.0-flash') 
response = model.generate_content("1+1을 수행하는 C++코드 작성")
print(response.text)