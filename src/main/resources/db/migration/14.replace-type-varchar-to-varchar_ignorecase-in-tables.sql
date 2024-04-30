ALTER TABLE IF EXISTS blogging_platform."user"
ALTER COLUMN login TYPE varchar_ignorecase(255);
ALTER TABLE IF EXISTS blogging_platform."user"
ALTER COLUMN email TYPE varchar_ignorecase(255);

ALTER TABLE IF EXISTS blogging_platform.role
ALTER COLUMN role_name TYPE varchar_ignorecase(255);

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ALTER COLUMN reaction_type TYPE varchar_ignorecase(255);

ALTER TABLE IF EXISTS blogging_platform.tag
ALTER COLUMN tag_name TYPE varchar_ignorecase(255);

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ALTER COLUMN reaction_type TYPE varchar_ignorecase(255);

ALTER TABLE IF EXISTS blogging_platform."order"
ALTER COLUMN status TYPE varchar_ignorecase(255);