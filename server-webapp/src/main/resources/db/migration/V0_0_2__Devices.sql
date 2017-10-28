CREATE TABLE devices (
    id                      BIGINT PRIMARY KEY NOT NULL,
    temperature             DECIMAL(5, 2),
    last_temperature_update TIMESTAMP,
    state                   VARCHAR(64)        NOT NULL
);

CREATE TABLE device_registrations (
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    device_id  BIGINT                NOT NULL,
    user_id    BIGINT                NOT NULL,
    nickname   VARCHAR(256),
    created_at TIMESTAMP             NOT NULL,
    active     BOOLEAN               NOT NULL,
    FOREIGN KEY (device_id) REFERENCES devices (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX device_registrations_user_id_nickname_unique_index
    ON device_registrations (user_id, nickname);

