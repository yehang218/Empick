INSERT INTO `member`
(
  `password`,
  `employee_number`,
  `name`,
  `birth`,
  `phone`,
  `picture_url`,
  `email`,
  `resign_at`,
  `address`,
  `vacation_count`,
  `hire_at`,
  `created_member_id`,
  `status`,
  `department_id`,
  `position_id`,
  `job_id`,
  `rank_id`
)
VALUES
    (
      'password123',               -- 비밀번호 (예시)
      44,                          -- 사번
      '홍길동',                    -- 사원 이름 (예시)
      '1990-01-01',               -- 생년월일
      '010-1234-5678',            -- 전화번호
      'https://s3.example.com/profile/44.jpg',  -- 사진 경로 (예시)
      'hong.gildong@example.com', -- 이메일
      '2025-06-01 18:00:00',
      '서울시 강남구 역삼동',      -- 주소
      10,                         -- 휴가 일수 (예시)
      NOW(),                      -- 입사일 (현재 시각)
      1,                          -- 입사처리자 (예시: 1번 사원)
      1,                          -- 계정 상태 (활성)
      1,                          -- 부서 ID (예시)
      1,                          -- 직책 ID (예시)
      1,                          -- 직무 ID (예시)
      1                           -- 직급 ID (예시)
    );