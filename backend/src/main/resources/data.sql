-- 레벨 데이터 추가
INSERT INTO course_levels (name, sort_order, active, description, created_at, updated_at)
SELECT '입문', 1, true, '프로그램에 처음 입문하는 초보자를 위한 수준', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM course_levels WHERE name = '입문');

INSERT INTO course_levels (name, sort_order, active, description, created_at, updated_at)
SELECT '초급', 2, true, '기본 개념에 익숙한 초급자를 위한 수준', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM course_levels WHERE name = '초급');

INSERT INTO course_levels (name, sort_order, active, description, created_at, updated_at)
SELECT '중급', 3, true, '개념과 활용에 익숙한 중급자를 위한 수준', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM course_levels WHERE name = '중급');

INSERT INTO course_levels (name, sort_order, active, description, created_at, updated_at)
SELECT '고급', 4, true, '심화 내용을 학습할 수 있는 고급자를 위한 수준', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM course_levels WHERE name = '고급'); 