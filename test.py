import sys
from transformers import AutoTokenizer, AutoModelForCausalLM

model_name = "HyperCLOVAX-SEED-Text-Instruct-1.5B"

hugging_face_token = "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" 
tokenizer = AutoTokenizer.from_pretrained(model_name, token=hugging_face_token)

model = AutoModelForCausalLM.from_pretrained("HyperCLOVAX-SEED-Text-Instruct-1.5B", token=hugging_face_token)
if len(sys.argv) < 2:
    print("Usage: python script.py <input_text>")
    sys.exit(1)

input_text = sys.argv[1] 

inputs = tokenizer(input_text, return_tensors="pt")

outputs = model.generate(**inputs, max_new_tokens=200)

print(tokenizer.decode(outputs[0], skip_special_tokens=True))
# 입력 텍스트를 텐서로 변환
#inputs = tokenizer(input_text, return_tensors="pt").to('cuda')

# 모델에 입력하여 출력 생성
#outputs = model.generate(**inputs, max_new_tokens=2000)

# 텐서를 문자열로 디코딩
#result = tokenizer.decode(outputs[0], skip_special_tokens=True)

#print(result)
