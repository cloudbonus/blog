DO '
DECLARE
    user1_id bigint;
    post1_id bigint;
    post2_id bigint;
    post3_id bigint;
    post4_id bigint; BEGIN SELECT id INTO user1_id FROM blog.user WHERE username = ''company'';

SELECT id INTO post1_id FROM blog.post WHERE id = 4;
SELECT id INTO post2_id FROM blog.post WHERE id = 5;
SELECT id INTO post3_id FROM blog.post WHERE id = 6;
SELECT id INTO post4_id FROM blog.post WHERE id = 7;

INSERT INTO blog.order(post_id, user_id, created_at, state) VALUES (post1_id, user1_id, CURRENT_TIMESTAMP, ''NEW''), (post2_id, user1_id, CURRENT_TIMESTAMP, ''NEW''), (post3_id, user1_id, CURRENT_TIMESTAMP, ''NEW''), (post4_id, user1_id, CURRENT_TIMESTAMP, ''COMPLETED''); END' LANGUAGE plpgsql;