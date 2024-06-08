CREATE TABLE IF NOT EXISTS blog.comment (
    id         bigserial PRIMARY KEY,
    post_id    bigint      NOT NULL REFERENCES blog.post(id) ON DELETE CASCADE,
    user_id    bigint      REFERENCES blog.user(id) ON DELETE SET NULL,
    content    text        NOT NULL,
    created_at timestamptz NOT NULL
);

CREATE INDEX IF NOT EXISTS comment_post_idx ON blog.comment(post_id);
CREATE INDEX IF NOT EXISTS comment_user_idx ON blog.comment(user_id);