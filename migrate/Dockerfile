# RDS -> PaaS MySQL 일회용 마이그레이션 이미지.
# 운영 데이터(bts_dump.sql)를 포함하므로 반드시 GHCR private 패키지로 push 하고,
# 마이그레이션 검증 후 즉시 삭제할 것.
FROM mysql:8.0

# 빌드 전에 migrate/ 디렉터리에 RDS 덤프(bts_dump.sql)를 위치시켜야 한다.
COPY bts_dump.sql /dump/bts_dump.sql
COPY load.sh /usr/local/bin/load.sh
RUN chmod +x /usr/local/bin/load.sh

ENTRYPOINT ["/usr/local/bin/load.sh"]
