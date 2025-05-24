#!/bin/bash

# 사용자 정보
EMAIL="linxer@naver.com"
USERNAME=$(git config user.name)
REPO_URL="https://github.com/dikafryo/iteagle.git"

# 1. Git 초기화 (없으면)
if [ ! -d ".git" ]; then
    echo "[+] 초기화 중..."
    git init
    git config user.email "$EMAIL"
    git config user.name "${USERNAME:-AutoUploader}"
    git remote add origin "$REPO_URL"
fi

# 2. GitHub에서 최신 pull
echo "[+] 원격 저장소에서 pull..."
git pull origin main --allow-unrelated-histories

# 3. 모든 변경사항 추가
echo "[+] 변경사항 스테이징..."
git add .

# 4. 커밋 (변경 없으면 무시)
if git diff --cached --quiet; then
    echo "[=] 커밋할 변경사항이 없습니다."
else
    git commit -m "Auto-merge local and remote content"
fi

# 5. push
echo "[+] 원격 저장소로 push 중..."
git push origin main

echo "[✔] 완료되었습니다."
