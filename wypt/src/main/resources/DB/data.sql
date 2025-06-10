USE wouldyoupt;

-- 1) 샘플 트레이너 한 명 :contentReference[oaicite:5]{index=5}
INSERT INTO Trainer (name, email, password, phone_number, kakao_id)
VALUES
    ('트레이너1', 'trainer1@test.com', '$2a$10$VuPfJJPhJdkwMYV.aZIKjeFKxrYHBnqmfldMVDLyD9Y9yI/l4foGG', '010-1234-5678', NULL);

-- 2) 샘플 회원 한 명 (성문님 카카오톡 계정: 4294058556) :contentReference[oaicite:6]{index=6}
INSERT INTO Member (trainer_id, name, phone_number, kakao_id, pt_count, used_pt)
VALUES
    (1, '성문', '010-2401-3487', '4294058556', 0, 0);
