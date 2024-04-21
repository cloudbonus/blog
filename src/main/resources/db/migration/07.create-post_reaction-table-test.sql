CREATE TABLE IF NOT EXISTS blogging_platform.post_reaction (
    post_id       bigint       NOT NULL,
    user_id       bigint,
    reaction_type varchar(255) NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_pkey PRIMARY KEY (post_id);

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE SET NULL ;

ALTER TABLE blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_post_user_key UNIQUE (post_id, user_id);
