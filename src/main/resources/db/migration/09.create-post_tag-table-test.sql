CREATE TABLE IF NOT EXISTS blogging_platform.post_tag (
    post_id bigint NOT NULL,
    tag_id  bigint NOT NULL
);

ALTER TABLE blogging_platform.post_tag
ADD CONSTRAINT post_tag_pkey PRIMARY KEY (post_id, tag_id);

ALTER TABLE IF EXISTS blogging_platform.post_tag
ADD CONSTRAINT post_tag_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.post_tag
ADD CONSTRAINT post_tag_tag_fkey FOREIGN KEY (tag_id) REFERENCES blogging_platform.tag(tag_id) ON DELETE CASCADE;

