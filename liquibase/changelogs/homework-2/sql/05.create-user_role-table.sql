CREATE TABLE IF NOT EXISTS blogging_platform.user_role (
    user_id bigint,
    role_id bigint
);

ALTER TABLE IF EXISTS blogging_platform.user_role
ADD CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id);

ALTER TABLE IF EXISTS blogging_platform.user_role
ADD CONSTRAINT user_details_user_fk FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.user_role
ADD CONSTRAINT user_details_role_fk FOREIGN KEY (role_id) REFERENCES blogging_platform.role(role_id) ON DELETE CASCADE;
