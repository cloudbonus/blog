CREATE TABLE IF NOT EXISTS blogging_platform.comment_reaction (
    comment_id    bigint       NOT NULL,
    user_id       bigint,
    reaction_type varchar(255) NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_pkey PRIMARY KEY (comment_id);

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_comment_fkey FOREIGN KEY (comment_id) REFERENCES blogging_platform.comment(comment_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE SET NULL ;

ALTER TABLE blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_comment_user_key UNIQUE (comment_id, user_id);
