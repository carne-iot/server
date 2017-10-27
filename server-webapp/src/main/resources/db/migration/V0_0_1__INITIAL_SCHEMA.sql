CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY    NOT NULL,
    full_name       VARCHAR(64)              NOT NULL,
    birth_date      DATE                     NOT NULL,
    username        VARCHAR(64)              NOT NULL,
    email           VARCHAR(64)              NOT NULL,
    hashed_password VARCHAR                  NOT NULL
);

CREATE UNIQUE INDEX users_username_unique_index
    ON users (username);

CREATE UNIQUE INDEX users_email_unique_index
    ON users (email);

CREATE TABLE sessions (
    id      BIGSERIAL PRIMARY KEY    NOT NULL,
    user_id INTEGER                  NOT NULL,
    jti     BIGINT                   NOT NULL,
    valid   BOOLEAN DEFAULT TRUE     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX sessions_user_id_jti_unique_index
    ON sessions (user_id, jti);

CREATE TABLE user_roles (
    user_id INTEGER     NOT NULL,
    role    VARCHAR(64) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);