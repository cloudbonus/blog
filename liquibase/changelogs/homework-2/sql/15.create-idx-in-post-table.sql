DROP INDEX IF EXISTS idx_post_user_id;
CREATE INDEX idx_post_user_id ON blogging_platform.post(user_id);