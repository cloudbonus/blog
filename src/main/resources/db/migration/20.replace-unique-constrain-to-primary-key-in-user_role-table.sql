ALTER TABLE blogging_platform.user_role
DROP CONSTRAINT IF EXISTS user_role_unique;

ALTER TABLE blogging_platform.user_role
ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);