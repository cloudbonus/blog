DO '
DECLARE
    post1_id bigint;
    post2_id bigint;
    post3_id bigint;
    tag1_id bigint;
    tag2_id bigint; BEGIN SELECT id INTO post1_id FROM blog.post WHERE id = 1;
SELECT id INTO post2_id FROM blog.post WHERE id = 2;
SELECT id INTO post3_id FROM blog.post WHERE id = 3;

SELECT id INTO tag1_id FROM blog.tag WHERE name = ''NEWS'';
SELECT id INTO tag2_id FROM blog.tag WHERE name = ''EDUCATION'';

INSERT INTO blog.post_tag (post_id, tag_id) VALUES (post1_id, tag1_id), (post2_id, tag1_id), (post3_id, tag1_id), (post3_id, tag2_id); END' LANGUAGE plpgsql;