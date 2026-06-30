#!/bin/sh
# 일회용 데이터 마이그레이션 로더.
# PaaS 내부망의 mysql 컨테이너가 뜰 때까지 기다린 뒤 RDS 덤프를 적재한다.
# 멱등하게 동작한다: __migration_flag 가 이미 있으면 건너뛴다.
# /health 가 DB 비의존이라 이 스크립트의 성패는 배포(롤백) 성패와 무관하다.
set -e

: "${MYSQL_HOST:=mysql}"
: "${MYSQL_PORT:=3306}"
DUMP_FILE="${DUMP_FILE:-/dump/bts_dump.sql}"

if [ -z "$MYSQL_ROOT_PASSWORD" ] || [ -z "$MYSQL_DATABASE" ]; then
  echo "[migrate] ERROR: MYSQL_ROOT_PASSWORD / MYSQL_DATABASE 가 설정되지 않았습니다."
  exit 1
fi

# MySQL 8 기본 인증(caching_sha2_password)은 비암호화 연결에서 서버 공개키 취득이
# 필요하다. JDBC 의 allowPublicKeyRetrieval=true 에 해당하는 CLI 옵션이
# --get-server-public-key 다. 이게 없으면 root 인증이 실패한다.
MYSQL_AUTH="-h $MYSQL_HOST -P $MYSQL_PORT -u root -p$MYSQL_ROOT_PASSWORD --get-server-public-key"

mysql_root() {
  mysql $MYSQL_AUTH "$@"
}

echo "[migrate] MySQL(${MYSQL_HOST}:${MYSQL_PORT}) 기동 대기..."
i=0
until mysqladmin $MYSQL_AUTH ping --silent 2>/dev/null; do
  i=$((i + 1))
  if [ "$i" -gt 60 ]; then
    echo "[migrate] ERROR: 120초 내에 MySQL 에 연결하지 못했습니다."
    exit 1
  fi
  sleep 2
done
echo "[migrate] MySQL 서버 응답 확인(ping)."

# 실제 root 인증이 되는지 명시적으로 검증(인증 실패 원인을 로그로 노출).
echo "[migrate] root 인증 테스트..."
if ! mysql_root -N -B -e "SELECT 1;" >/dev/null; then
  echo "[migrate] ERROR: root 인증 실패. (위 mysql 에러 참고 — 비밀번호/계정 호스트 권한 확인)"
  exit 1
fi
echo "[migrate] root 인증 성공."

# 멱등 가드: 이미 적재 완료되었으면 건너뛴다.
DONE=$(mysql_root -N -B -e \
  "SELECT COUNT(*) FROM information_schema.tables \
   WHERE table_schema='${MYSQL_DATABASE}' AND table_name='__migration_flag';" 2>/dev/null || echo 0)
if [ "$DONE" = "1" ]; then
  echo "[migrate] 이미 마이그레이션 완료된 DB 입니다. 건너뜁니다."
  exit 0
fi

if [ ! -f "$DUMP_FILE" ]; then
  echo "[migrate] ERROR: 덤프 파일이 없습니다: $DUMP_FILE"
  exit 1
fi
echo "[migrate] 덤프 파일 크기: $(wc -c < "$DUMP_FILE") bytes"

echo "[migrate] 덤프 적재 시작: $DUMP_FILE"
# set -e 로 조용히 죽지 않도록 명시적으로 에러를 잡아 출력한다.
if ! mysql_root "$MYSQL_DATABASE" < "$DUMP_FILE"; then
  echo "[migrate] ERROR: 덤프 적재 실패. (위 mysql 에러 메시지 참고)"
  exit 1
fi

TABLES=$(mysql_root -N -B -e \
  "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='${MYSQL_DATABASE}';")
echo "[migrate] 적재 후 ${MYSQL_DATABASE} 테이블 수: ${TABLES}"

echo "[migrate] 마이그레이션 플래그 기록..."
mysql_root "$MYSQL_DATABASE" -e \
  "CREATE TABLE IF NOT EXISTS __migration_flag (id INT PRIMARY KEY, applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP); \
   INSERT INTO __migration_flag (id) VALUES (1);"

echo "[migrate] 완료. (테이블 ${TABLES}개)"
exit 0
