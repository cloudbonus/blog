CREATE INDEX IF NOT EXISTS idx_comment_post_id ON blogging_platform.comment(post_id);
CREATE INDEX IF NOT EXISTS idx_comment_user_id ON blogging_platform.comment(user_id);