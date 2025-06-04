CREATE TABLE IF NOT EXISTS notifications (
     id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
     event_user_id BIGINT NOT NULL,
     title VARCHAR(255) NOT NULL,
     message TEXT,
     read BOOLEAN DEFAULT FALSE,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_notification_event_user
         FOREIGN KEY (event_user_id)
             REFERENCES event_users(id)
             ON DELETE CASCADE
);