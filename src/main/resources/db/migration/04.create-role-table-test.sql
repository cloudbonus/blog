CREATE TABLE IF NOT EXISTS blogging_platform.role (
    role_id   bigint AUTO_INCREMENT,
    role_name varchar(255) NOT NULL UNIQUE
);

ALTER TABLE IF EXISTS blogging_platform.role
ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);
