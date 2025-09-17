#!/bin/bash
set -e

echo "==== 1. 기본 패키지 업데이트 ===="
sudo apt update -y
sudo apt upgrade -y
sudo apt install -y ca-certificates curl gnupg lsb-release git

echo "==== 2. Docker 설치 ===="
# GPG key 등록
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

# Docker repository 등록
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Docker 설치
sudo apt update -y
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

echo "==== 3. Docker 권한 설정 ===="
sudo usermod -aG docker $USER

echo "==== 4. 애플리케이션 디렉토리 준비 ===="
mkdir -p ~/app

echo "==== 설치 완료 ===="
echo "👉 로그아웃 후 다시 로그인해야 docker 그룹 권한이 적용돼요!"
echo "👉 배포할 땐: cd ~/app && docker compose pull && docker compose up -d"
