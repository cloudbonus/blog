CREATE TABLE IF NOT EXISTS blogging_platform.comment (
    comment_id   bigint GENERATED BY DEFAULT AS IDENTITY,
    post_id      bigint      NOT NULL,
    user_id      bigint      NOT NULL,
    content      text        NOT NULL,
    published_at timestamptz NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_pk PRIMARY KEY (comment_id);

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_post_fk FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE SET NULL;

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_user_fk FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id) ON DELETE SET NULL;