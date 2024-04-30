DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    post1_id bigint;
    post2_id bigint;
    post3_id bigint;
    tag1_id bigint;
    tag2_id bigint; BEGIN SELECT user_id INTO user1_id FROM blogging_platform.user WHERE login = ''kvossing0'';
SELECT user_id INTO user2_id FROM blogging_platform.user WHERE login = ''gmaccook1'';

SELECT post_id INTO post1_id FROM blogging_platform.post WHERE title = ''First Post'';
SELECT post_id INTO post2_id FROM blogging_platform.post WHERE title = ''Second Post'';
SELECT post_id INTO post3_id FROM blogging_platform.post WHERE title = ''Third Post'';

SELECT tag_id INTO tag1_id FROM blogging_platform.tag WHERE tag_name = ''news'';
SELECT tag_id INTO tag2_id FROM blogging_platform.tag WHERE tag_name = ''education'';

INSERT INTO blogging_platform.post_tag (post_id, tag_id) VALUES (post1_id, tag1_id), (post2_id, tag1_id), (post3_id, tag1_id), (post3_id, tag2_id); END
' LANGUAGE plpgsql;