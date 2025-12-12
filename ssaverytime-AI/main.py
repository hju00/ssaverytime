import os
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage, SystemMessage

app = FastAPI()

# 환경 변수에서 키 가져오기 (GMS 통합 키)
GEMINI_API_KEY = os.environ.get("GEMINI_API_KEY")

# GMS OpenAI Proxy URL
GMS_OPENAI_BASE_URL = "https://gms.ssafy.io/gmsapi/api.openai.com/v1"

class SummaryRequest(BaseModel):
    text: str

@app.post("/summary")
async def generate_summary(request: SummaryRequest):
    if not GEMINI_API_KEY:
        raise HTTPException(status_code=500, detail="API Key is not configured")

    try:
        # LangChain ChatOpenAI 초기화
        chat = ChatOpenAI(
            model="gpt-4o-mini",
            api_key=GEMINI_API_KEY,
            base_url=GMS_OPENAI_BASE_URL,
            temperature=0.5
        )

        messages = [
            SystemMessage(content="너는 게시판 게시글을 요약해주는 AI 어시스턴트야. 다음 게시글의 핵심 내용을 파악해서 3줄 이내의 한국어로 깔끔하게 요약해줘. 불필요한 인사는 생략하고 요약문만 출력해."),
            HumanMessage(content=request.text)
        ]

        response = chat.invoke(messages)
        
        return {"summary": response.content}

    except Exception as e:
        print(f"AI Service Error: {e}")
        raise HTTPException(status_code=500, detail=f"AI Error: {str(e)}")