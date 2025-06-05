CREATE DATABASE IF NOT EXISTS wouldyoupt DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wouldyoupt;

-- 트레이너 계정
CREATE TABLE Trainer (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(30)  NOT NULL,
    email        VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    kakao_id     VARCHAR(30),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 리프레시 토큰
CREATE TABLE RefreshToken (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id  BIGINT NOT NULL UNIQUE,
    token       VARCHAR(500) NOT NULL,
    expiry_date DATETIME NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 트레이너가 관리하는 PT 회원
CREATE TABLE Member (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id      BIGINT NOT NULL,
    name            VARCHAR(30) NOT NULL,
    phone_number    VARCHAR(20),
    kakao_id        VARCHAR(30) NOT NULL UNIQUE,
    age             INT,
    height_cm       DECIMAL(5,1),
    weight_kg       DECIMAL(5,1),
    body_fat_pct    DECIMAL(5,1),
    muscle_mass_kg  DECIMAL(5,1),
    bmi             DECIMAL(5,1),
    goal            VARCHAR(200),
    feature         VARCHAR(500),
    pt_count        INT DEFAULT 0,
    used_pt         INT DEFAULT 0,
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 이거는 트레이너의 주간(고정, 반복적인) 근무시간
CREATE TABLE TrainerAvailableTime (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id   BIGINT NOT NULL,
    work_day      TINYINT NOT NULL,          -- 1=월, ..., 6=토, 7=일
    start_time   TIME NOT NULL,
    end_time     TIME NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE TrainerUnavailableTime (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id   BIGINT NOT NULL,
    date         DATE NOT NULL,
    start_time   TIME NOT NULL,
    end_time     TIME NOT NULL,
    reason       VARCHAR(200),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE
) ENGINE=InnoDB;


CREATE TABLE ScheduleRequest (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    trainer_id   BIGINT NOT NULL,
    date         DATE NOT NULL,
    start_time   TIME NOT NULL,
    end_time     TIME NOT NULL,
    status       BOOLEAN DEFAULT FALSE,      # false=pending, true=rejected. Accepted는 Schedule이 생성되며 삭제하기에
    requested_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id)  REFERENCES Member(id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE
) ENGINE=InnoDB;


-- PT 수업 예약 스케줄
CREATE TABLE Schedule (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id   BIGINT NOT NULL,
    member_id    BIGINT NOT NULL,
    title        VARCHAR(50),
    date         DATE NOT NULL,
    start_time   TIME NOT NULL,
    end_time     TIME NOT NULL,
    status       VARCHAR(20),
    is_next      BOOLEAN DEFAULT FALSE,
    feed_back    BOOLEAN DEFAULT FALSE,
    notes        VARCHAR(500),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES Member(id)  ON DELETE CASCADE
) ENGINE=InnoDB;


-- 트레이너가 등록한 운동 목록
CREATE TABLE Exercise (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id  BIGINT NOT NULL,
    ex_name     VARCHAR(50) NOT NULL,
    category    VARCHAR(50),       # 상체, 하체, 유산소, 스트레칭 등
    description VARCHAR(300),
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 수업에 사용된 운동 목록
CREATE TABLE ScheduleExercise (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    schedule_id   BIGINT NOT NULL,
    exercise_id   BIGINT NOT NULL,
    reps          VARCHAR(20),
    sets          VARCHAR(20),
    weight        VARCHAR(20),
    FOREIGN KEY (schedule_id) REFERENCES Schedule(id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES Exercise(id) ON DELETE CASCADE
) ENGINE=InnoDB;


-- 트레이너와 회원 간 채팅 메시지
CREATE TABLE Message (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id  BIGINT NOT NULL,
    member_id   BIGINT NOT NULL,
    sender      VARCHAR(20) NOT NULL,   -- trainer / member
    content     TEXT NOT NULL,
    direction   VARCHAR(20) NOT NULL,   -- in / out
    status      VARCHAR(20),            -- pending / sent / read / fail
    sent_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id)  REFERENCES Member(id)  ON DELETE CASCADE
) ENGINE=InnoDB;

-- 카카오 채널 웹훅 이벤트 로그
CREATE TABLE KakaoWebhookLog (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    type       VARCHAR(50) NOT NULL,
    payload    JSON NOT NULL,
    recv_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Member(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 알림 (트레이너용)
CREATE TABLE Notification (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id  BIGINT NOT NULL,
    content     TEXT NOT NULL,
    is_read     BOOLEAN DEFAULT FALSE,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trainer_id) REFERENCES Trainer(id) ON DELETE CASCADE
) ENGINE=InnoDB;


-- 인덱스 설정
#CREATE INDEX idx_member_trainer       ON Member(trainer_id);
#CREATE INDEX idx_schedule_trainer     ON Schedule(trainer_id);
#CREATE INDEX idx_schedule_member      ON Schedule(member_id);
#CREATE INDEX idx_message_member       ON Message(member_id);
#CREATE INDEX idx_kakao_member         ON KakaoWebhookLog(member_id);
#CREATE INDEX idx_notification_trainer ON Notification(trainer_id);
#CREATE INDEX idx_exercise_trainer     ON Exercise(trainer_id);

