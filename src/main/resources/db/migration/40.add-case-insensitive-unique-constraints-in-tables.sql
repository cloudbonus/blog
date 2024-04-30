ALTER TABLE blogging_platform.user DROP CONSTRAINT user_login_key;
ALTER TABLE blogging_platform.user DROP CONSTRAINT user_email_key;

ALTER TABLE blogging_platform.role DROP CONSTRAINT role_role_name_key;

ALTER TABLE blogging_platform.tag DROP CONSTRAINT tag_tag_name_key;

CREATE UNIQUE INDEX user_login_key ON blogging_platform.user(LOWER(login));
CREATE UNIQUE INDEX user_email_key ON blogging_platform.user(LOWER(email));

CREATE UNIQUE INDEX role_role_name_key ON blogging_platform.role(LOWER(role_name));

CREATE UNIQUE INDEX post_reaction_reaction_type_key ON blogging_platform.post_reaction(LOWER(reaction_type));

CREATE UNIQUE INDEX tag_tag_name_key ON blogging_platform.tag(LOWER(tag_name));

CREATE UNIQUE INDEX comment_reaction_reaction_type_key ON blogging_platform.comment_reaction(LOWER(reaction_type));

CREATE UNIQUE INDEX order_status_key ON blogging_platform.order(LOWER(status));