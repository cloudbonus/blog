CREATE TABLE IF NOT EXISTS blog.post (
    id         bigserial PRIMARY KEY,
    user_id    bigint      REFERENCES blog.user(id) ON DELETE SET NULL,
    title      text        NOT NULL,
    content    text        NOT NULL,
    created_at timestamptz NOT NULL
);

CREATE INDEX IF NOT EXISTS post_user_idx ON blog.post(user_id);