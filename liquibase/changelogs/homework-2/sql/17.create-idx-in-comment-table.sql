DROP INDEX IF EXISTS idx_comment_post_id;
CREATE INDEX idx_comment_post_id ON blogging_platform.comment(post_id);

DROP INDEX IF EXISTS idx_comment_user_id;
CREATE INDEX idx_comment_user_id ON blogging_platform.comment(user_id);