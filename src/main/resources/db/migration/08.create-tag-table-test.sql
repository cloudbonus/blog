CREATE TABLE IF NOT EXISTS blogging_platform.tag (
    tag_id   bigint AUTO_INCREMENT,
    tag_name varchar(255) NOT NULL UNIQUE
);

ALTER TABLE IF EXISTS blogging_platform.tag
ADD CONSTRAINT tag_pkey PRIMARY KEY (tag_id);