ALTER TABLE blogging_platform.post_tag
DROP CONSTRAINT IF EXISTS post_tag_unique;

ALTER TABLE blogging_platform.post_tag
ADD CONSTRAINT post_tag_pkey PRIMARY KEY (post_id, tag_id);