-- 사용자 초기화 데이터
INSERT INTO users (email, password, name, role, enabled, created_at, updated_at)
VALUES 
('admin@example.com', '$2a$10$RVd4gtBHZkhFkZdTqrlE9.S6QHO66nOOCbRkKLuSUfVvW7LR7F82W', '관리자', 'ROLE_ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user@example.com', '$2a$10$RVd4gtBHZkhFkZdTqrlE9.S6QHO66nOOCbRkKLuSUfVvW7LR7F82W', '일반사용자', 'ROLE_USER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('instructor@example.com', '$2a$10$RVd4gtBHZkhFkZdTqrlE9.S6QHO66nOOCbRkKLuSUfVvW7LR7F82W', '강사', 'ROLE_INSTRUCTOR', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
