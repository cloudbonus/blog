CREATE TABLE IF NOT EXISTS blog.user (
    id         bigserial PRIMARY KEY,
    username   text        NOT NULL,
    password   text        NOT NULL,
    email      text        NOT NULL,
    created_at timestamptz NOT NULL,
    updated_at timestamptz
);

CREATE UNIQUE INDEX user_username_key ON blog.user(LOWER(username));
CREATE UNIQUE INDEX user_email_key ON blog.user(LOWER(email));