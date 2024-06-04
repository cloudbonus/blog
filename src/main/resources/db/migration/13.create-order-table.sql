CREATE TABLE IF NOT EXISTS blog.order (
    id            bigserial PRIMARY KEY,
    post_id       bigint UNIQUE REFERENCES blog.post(id) ON DELETE SET NULL,
    user_id       bigint        REFERENCES blog.user(id) ON DELETE SET NULL,
    created_at    timestamptz   NOT NULL,
    state         text          NOT NULL,
    state_context text
);

CREATE INDEX IF NOT EXISTS order_user_idx ON blog.order(user_id);