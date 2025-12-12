import os
import requests
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI()

# 환경 변수에서 키 가져오기
GEMINI_API_KEY = os.environ.get("GEMINI_API_KEY")
# GMS 프록시 URL
GMS_URL = "https://gms.ssafy.io/gmsapi/generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent"

class SummaryRequest(BaseModel):
    text: str

@app.post("/summary")
async def generate_summary(request: SummaryRequest):
    if not GEMINI_API_KEY:
        raise HTTPException(status_code=500, detail="GEMINI_API_KEY is not configured")

    headers = {
        "Content-Type": "application/json"
    }
    
    # GMS API 호출 URL (쿼리 파라미터로 키 전달)
    url = f"{GMS_URL}?key={GEMINI_API_KEY}"
    
    prompt = f"다음 내용을 3줄로 요약해줘:\n\n{request.text}"
    
    payload = {
        "contents": [
            {
                "parts": [
                    {"text": prompt}
                ]
            }
        ]
    }

    try:
        response = requests.post(url, headers=headers, json=payload)
        response.raise_for_status() # 4xx, 5xx 에러 시 예외 발생
        
        data = response.json()
        
        # 응답 파싱
        if "candidates" in data and data["candidates"]:
            summary_text = data["candidates"][0]["content"]["parts"][0]["text"]
            return {"summary": summary_text}
        else:
            return {"summary": "요약 실패: 적절한 응답을 받지 못했습니다."}
            
    except requests.exceptions.RequestException as e:
        print(f"Error calling GMS API: {e}")
        # GMS 에러 메시지가 있다면 포함
        error_detail = str(e)
        if e.response is not None:
            error_detail += f" Response: {e.response.text}"
        raise HTTPException(status_code=500, detail=f"AI Service Error: {error_detail}")
