CREATE TABLE IF NOT EXISTS blogging_platform.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

ALTER TABLE blogging_platform.user_role
ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);

ALTER TABLE IF EXISTS blogging_platform.user_role
ADD CONSTRAINT user_role_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.user_role
ADD CONSTRAINT user_role_role_fkey FOREIGN KEY (role_id) REFERENCES blogging_platform.role(role_id) ON DELETE CASCADE;
