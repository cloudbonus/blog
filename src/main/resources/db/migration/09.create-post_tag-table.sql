CREATE TABLE IF NOT EXISTS blog.post_tag (
    post_id bigint REFERENCES blog.post(id) ON DELETE CASCADE,
    tag_id  bigint REFERENCES blog.tag(id) ON DELETE CASCADE,
    CONSTRAINT post_tag_pkey PRIMARY KEY (post_id, tag_id)
);