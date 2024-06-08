CREATE TABLE IF NOT EXISTS blog.post_reaction (
    id          bigserial PRIMARY KEY,
    post_id     bigint REFERENCES blog.post(id) ON DELETE CASCADE,
    user_id     bigint REFERENCES blog.user(id) ON DELETE SET NULL,
    reaction_id bigint NOT NULL REFERENCES blog.reaction(id) ON DELETE CASCADE,
    CONSTRAINT post_reaction_post_user_key UNIQUE (post_id, user_id)
);

CREATE INDEX IF NOT EXISTS post_reaction_user_idx ON blog.post_reaction(user_id);