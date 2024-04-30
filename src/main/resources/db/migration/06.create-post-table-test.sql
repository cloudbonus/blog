CREATE TABLE IF NOT EXISTS blogging_platform.post (
    post_id      bigint AUTO_INCREMENT,
    user_id      bigint,
    title        varchar(255) NOT NULL,
    content      varchar(255) NOT NULL,
    published_at timestamp    NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.post
ADD CONSTRAINT post_pkey PRIMARY KEY (post_id);

ALTER TABLE IF EXISTS blogging_platform.post
ADD CONSTRAINT post_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE SET NULL;
