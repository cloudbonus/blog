INSERT INTO blogging_platform.tag (tag_name)
VALUES
    ('news');
INSERT INTO blogging_platform.tag (tag_name)
VALUES
    ('education');

SET @tag1_id = (SELECT tag_id FROM blogging_platform.tag WHERE tag_name = 'news');
SET @tag2_id = (SELECT tag_id FROM blogging_platform.tag WHERE tag_name = 'education');

INSERT INTO blogging_platform.post_tag (post_id, tag_id)
VALUES
    (@post1_id, @tag1_id);
INSERT INTO blogging_platform.post_tag (post_id, tag_id)
VALUES
    (@post2_id, @tag1_id);
INSERT INTO blogging_platform.post_tag (post_id, tag_id)
VALUES
    (@post3_id, @tag1_id);
INSERT INTO blogging_platform.post_tag (post_id, tag_id)
VALUES
    (@post3_id, @tag2_id);