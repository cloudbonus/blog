CREATE TABLE IF NOT EXISTS blogging_platform.post_tag (
    post_id bigint,
    tag_id  bigint
);

ALTER TABLE IF EXISTS blogging_platform.post_tag
ADD CONSTRAINT post_tag_pk PRIMARY KEY (post_id, tag_id);

ALTER TABLE IF EXISTS blogging_platform.post_tag
ADD CONSTRAINT post_tag_post_fk FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.post_tag
ADD CONSTRAINT post_tag_tag_fk FOREIGN KEY (tag_id) REFERENCES blogging_platform.tag(tag_id) ON DELETE CASCADE;
