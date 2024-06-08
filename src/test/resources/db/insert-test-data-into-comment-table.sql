DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    post1_id bigint;
    post2_id bigint;
    post3_id bigint; BEGIN SELECT id INTO user1_id FROM blog.user WHERE username = ''kvossing0'';
SELECT id INTO user2_id FROM blog.user WHERE username = ''gmaccook1'';

SELECT id INTO post1_id FROM blog.post WHERE id = 1;
SELECT id INTO post2_id FROM blog.post WHERE id = 2;
SELECT id INTO post3_id FROM blog.post WHERE id = 3;

INSERT INTO blog.comment (post_id, user_id, content, created_at) VALUES (post1_id, user1_id, ''1 content'', CURRENT_TIMESTAMP), (post2_id, user1_id, ''2 content'', CURRENT_TIMESTAMP), (post3_id, user2_id, ''3 content'', CURRENT_TIMESTAMP); END' LANGUAGE plpgsql;