CREATE TABLE IF NOT EXISTS blogging_platform.comment_reaction (
    comment_id    bigint,
    user_id       bigint NOT NULL,
    reaction_type text   NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_pk PRIMARY KEY (comment_id);

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_comment_fk FOREIGN KEY (comment_id) REFERENCES blogging_platform.comment(comment_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_user_fk FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id) ON DELETE CASCADE;

ALTER TABLE blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_unique UNIQUE (comment_id, user_id);