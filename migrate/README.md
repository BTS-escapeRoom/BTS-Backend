# RDS → PaaS MySQL 데이터 마이그레이션 (일회용)

PaaS는 GitHub Actions 배포로만 조작 가능하고, MySQL 컨테이너는 외부로 포트가 열려
있지 않다. 따라서 RDS 덤프를 담은 일회용 이미지를 GHCR(private)에 올린 뒤, 배포
시 같은 내부망에서 `mysql` 컨테이너로 적재한다.

`/health` 가 DB 비의존이라 이 사이드카의 성패는 배포(롤백) 성패와 무관하다.
실패하면 그냥 다시 트리거하면 된다(멱등).

## 1. 로컬에서 RDS 덤프

```bash
cd migrate

mysqldump \
  -h bangtal-boys-database.clmmy2kggqxt.ap-northeast-2.rds.amazonaws.com \
  -u <RDS_USER> -p \
  --single-transaction --routines --triggers --events \
  --set-gtid-purged=OFF --no-tablespaces \
  bts \
  | sed -E 's/DEFINER=`[^`]+`@`[^`]+`//g' \
  > bts_dump.sql
```

- `--no-tablespaces` : PROCESS 권한 없이 덤프
- `sed ... DEFINER` : RDS 사용자에 묶인 DEFINER 절 제거(컨테이너에 그 계정이 없어 import 실패 방지)
- `bts_dump.sql` 은 운영 데이터이므로 **절대 커밋 금지** (.gitignore 에 등록됨)

## 2. 마이그레이션 이미지 빌드 & GHCR(private) push

```bash
cd migrate

# GHCR 로그인 (write:packages 권한 토큰)
echo "$GHCR_TOKEN" | docker login ghcr.io -u <github-username> --password-stdin

docker build -t ghcr.io/bts-escaperoom/bts-migrate:latest .
docker push ghcr.io/bts-escaperoom/bts-migrate:latest
```

push 후 GitHub > Packages 에서 `bts-migrate` 가 **private** 인지 확인할 것.

## 3. 마이그레이션 배포 실행 (수동)

GitHub Actions > "Build and Deploy" > Run workflow:

- `run_migration` = `true`
- `migration_image` = `ghcr.io/bts-escaperoom/bts-migrate:latest` (기본값)

이때만 compose 에 `db-migrate` 사이드카가 포함된다. 평소 push 배포에는 포함되지 않는다.

배포 로그에서 `[migrate] 완료.` 를 확인한다.

## 4. 검증

앱이 실제 데이터를 반환하는지 확인(예: 목록 API 호출). 정상이면:

## 5. 정리 (중요)

- GHCR 에서 `bts-migrate` 패키지 **삭제** (운영 데이터가 레지스트리에 남지 않도록)
- 로컬 `migrate/bts_dump.sql` 삭제
- 이후 배포는 `run_migration` 없이(평소 push) 진행 → 사이드카 미포함

멱등 가드(`__migration_flag`) 덕분에 실수로 다시 실행해도 재적재되지 않는다.
