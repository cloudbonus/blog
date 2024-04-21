CREATE TABLE IF NOT EXISTS blogging_platform.comment (
    comment_id   bigint AUTO_INCREMENT,
    post_id      bigint       NOT NULL,
    user_id      bigint,
    content      varchar(255) NOT NULL,
    published_at timestamp    NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_pkey PRIMARY KEY (comment_id);

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE SET NULL;
