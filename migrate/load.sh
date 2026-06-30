#!/bin/sh
# 일회용 데이터 마이그레이션 로더 (검증 강화판).
# RDS 덤프를 PaaS 내부망의 mysql 로 적재하고, theme 테이블 행 수로 적재를 검증한다.
# 검증 실패 시 진단 정보를 출력하고 exit 1 -> (bts-backend 가 완료를 대기하므로) 배포가 실패하며
# 컨테이너 로그가 노출된다. 덤프가 DROP TABLE IF EXISTS 라 매 실행이 안전하게 재적재된다.
set -e

: "${MYSQL_HOST:=mysql}"
: "${MYSQL_PORT:=3306}"
DUMP_FILE="${DUMP_FILE:-/dump/bts_dump.sql}"

if [ -z "$MYSQL_ROOT_PASSWORD" ] || [ -z "$MYSQL_DATABASE" ]; then
  echo "[migrate] ERROR: MYSQL_ROOT_PASSWORD / MYSQL_DATABASE 가 설정되지 않았습니다."
  exit 1
fi

# MySQL8 caching_sha2_password 대비 (JDBC allowPublicKeyRetrieval=true 상당)
MYSQL_AUTH="-h $MYSQL_HOST -P $MYSQL_PORT -u root -p$MYSQL_ROOT_PASSWORD --get-server-public-key"
mysql_root() { mysql $MYSQL_AUTH "$@"; }

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

echo "[migrate] root 인증 테스트..."
if ! mysql_root -N -B -e "SELECT 1;" >/dev/null; then
  echo "[migrate] ERROR: root 인증 실패."
  exit 1
fi

# 어느 mysql 인스턴스/데이터 디렉터리에 연결됐는지 진단 출력 (볼륨 격리 추적용)
echo "[migrate] === 진단 ==="
mysql_root -e "SELECT @@hostname AS hostname, @@version AS version, @@datadir AS datadir, CURRENT_USER() AS user;" 2>&1 || true
echo "[migrate] 적재 전 ${MYSQL_DATABASE} 테이블 수: $(mysql_root -N -B -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='${MYSQL_DATABASE}';" 2>/dev/null || echo '?')"

if [ ! -f "$DUMP_FILE" ]; then
  echo "[migrate] ERROR: 덤프 파일이 없습니다: $DUMP_FILE"
  exit 1
fi
echo "[migrate] 덤프 크기: $(wc -c < "$DUMP_FILE") bytes"

echo "[migrate] 덤프 적재 시작..."
if ! mysql_root "$MYSQL_DATABASE" < "$DUMP_FILE"; then
  echo "[migrate] ERROR: 덤프 적재 실패 (위 mysql 에러 참고)."
  exit 1
fi

# 적재 검증: theme 테이블 행 수 확인
THEME_ROWS=$(mysql_root -N -B -e "SELECT COUNT(*) FROM \`${MYSQL_DATABASE}\`.theme;" 2>/dev/null || echo "ERR")
TABLE_CNT=$(mysql_root -N -B -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='${MYSQL_DATABASE}';" 2>/dev/null || echo "ERR")
echo "[migrate] 적재 후 테이블 수: ${TABLE_CNT}, theme 행 수: ${THEME_ROWS}"

if [ "$THEME_ROWS" = "ERR" ] || [ "$THEME_ROWS" = "0" ] || [ -z "$THEME_ROWS" ]; then
  echo "[migrate] ERROR: 적재 검증 실패 — theme 테이블에 데이터가 없습니다."
  echo "[migrate] 현재 ${MYSQL_DATABASE} 테이블 목록:"
  mysql_root -e "SHOW TABLES IN \`${MYSQL_DATABASE}\`;" 2>&1 || true
  exit 1
fi

# 마이그레이션 플래그(참고용)
mysql_root "$MYSQL_DATABASE" -e \
  "CREATE TABLE IF NOT EXISTS __migration_flag (id INT PRIMARY KEY, applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP); \
   INSERT IGNORE INTO __migration_flag (id) VALUES (1);" 2>/dev/null || true

echo "[migrate] 완료. (테이블 ${TABLE_CNT}개, theme ${THEME_ROWS}행)"
exit 0
