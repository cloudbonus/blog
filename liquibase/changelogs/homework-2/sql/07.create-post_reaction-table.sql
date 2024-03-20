CREATE TABLE IF NOT EXISTS blogging_platform.post_reaction (
    post_id       bigint,
    user_id       bigint      NOT NULL,
    reaction_type varchar(50) NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_pk PRIMARY KEY (post_id);

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_post_fk FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_user_fk FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id) ON DELETE CASCADE;