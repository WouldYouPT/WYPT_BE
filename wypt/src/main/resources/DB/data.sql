USE WYPT;

INSERT INTO customer (kakao_user_id, name, created_at) VALUES
    ('kakao_user_1', '홍길동', NOW()),
    ('kakao_user_2', '김영희', NOW()),
    ('kakao_user_3', '이철수', NOW());

INSERT INTO trainer (name, specialty, created_at) VALUES
    ('트레이너1', '근력 강화', NOW()),
    ('트레이너2', '요가',       NOW()),
    ('트레이너3', '필라테스',   NOW());

INSERT INTO chat_room (customer_id, trainer_id, created_at) VALUES
    (1, 1, NOW()),
    (2, 2, NOW()),
    (3, 3, NOW());
