CREATE TABLE IF NOT EXISTS events (
      id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
      title VARCHAR(200) NOT NULL,
      description VARCHAR(4048),
      event_datetime TIMESTAMP NOT NULL,
      location VARCHAR(255),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      category VARCHAR(255),
      organizer_id BIGINT NOT NULL,
      CONSTRAINT fk_events_organizer FOREIGN KEY (organizer_id) REFERENCES organizers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS organizers (
      id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
      user_id BIGINT NOT NULL UNIQUE,
      organizer_login VARCHAR(150),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT fk_organizers_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participants (
      id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
      user_id BIGINT NOT NULL,
      event_id BIGINT NOT NULL,
      status VARCHAR(255) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT fk_participant_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
      CONSTRAINT fk_participant_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
      CONSTRAINT uq_participant_user_event UNIQUE (user_id, event_id)
);



