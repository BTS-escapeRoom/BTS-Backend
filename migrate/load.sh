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

mysql_root() {
  mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u root -p"$MYSQL_ROOT_PASSWORD" "$@"
}

echo "[migrate] MySQL(${MYSQL_HOST}:${MYSQL_PORT}) 기동 대기..."
i=0
until mysqladmin ping -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u root -p"$MYSQL_ROOT_PASSWORD" --silent 2>/dev/null; do
  i=$((i + 1))
  if [ "$i" -gt 60 ]; then
    echo "[migrate] ERROR: 120초 내에 MySQL 에 연결하지 못했습니다."
    exit 1
  fi
  sleep 2
done
echo "[migrate] MySQL 연결 확인."

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

echo "[migrate] 덤프 적재 시작: $DUMP_FILE"
mysql_root "$MYSQL_DATABASE" < "$DUMP_FILE"

echo "[migrate] 마이그레이션 플래그 기록..."
mysql_root "$MYSQL_DATABASE" -e \
  "CREATE TABLE IF NOT EXISTS __migration_flag (id INT PRIMARY KEY, applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP); \
   INSERT INTO __migration_flag (id) VALUES (1);"

echo "[migrate] 완료."
exit 0
