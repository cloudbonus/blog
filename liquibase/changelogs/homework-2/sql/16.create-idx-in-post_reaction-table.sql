DROP INDEX IF EXISTS idx_post_reaction_user_id;
CREATE INDEX idx_post_reaction_user_id ON blogging_platform.post_reaction(user_id);
