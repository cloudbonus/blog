CREATE INDEX IF NOT EXISTS post_user_idx ON blogging_platform.post(user_id);

CREATE INDEX IF NOT EXISTS post_reaction_user_idx ON blogging_platform.post_reaction(user_id);

CREATE INDEX IF NOT EXISTS comment_post_idx ON blogging_platform.comment(post_id);
CREATE INDEX IF NOT EXISTS comment_user_idx ON blogging_platform.comment(user_id);

CREATE INDEX IF NOT EXISTS comment_reaction_user_idx ON blogging_platform.comment_reaction(user_id);

CREATE INDEX IF NOT EXISTS order_user_idx ON blogging_platform."order"(user_id);