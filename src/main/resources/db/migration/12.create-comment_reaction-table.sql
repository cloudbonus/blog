CREATE TABLE IF NOT EXISTS blog.comment_reaction (
    id          bigserial PRIMARY KEY,
    comment_id  bigint REFERENCES blog.comment(id) ON DELETE CASCADE,
    user_id     bigint REFERENCES blog.user(id) ON DELETE SET NULL,
    reaction_id bigint NOT NULL REFERENCES blog.reaction(id) ON DELETE CASCADE,
    CONSTRAINT comment_reaction_comment_user_key UNIQUE (comment_id, user_id)
);

CREATE INDEX IF NOT EXISTS comment_reaction_user_idx ON blog.comment_reaction(user_id);