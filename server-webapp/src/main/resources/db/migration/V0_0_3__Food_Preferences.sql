CREATE TABLE food_preferences (
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    name        VARCHAR(256)          NOT NULL,
    temperature DECIMAL(5, 2)         NOT NULL,
    user_id     BIGINT                NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE UNIQUE INDEX food_preferences_name_user_id_unique_index
    ON food_preferences (name, user_id);