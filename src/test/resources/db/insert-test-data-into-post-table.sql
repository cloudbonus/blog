DO '
DECLARE
    user1_id bigint;
    user2_id bigint; BEGIN SELECT id INTO user1_id FROM blog.user WHERE username = ''student'';
SELECT id INTO user2_id FROM blog.user WHERE username = ''company'';

INSERT INTO blog.post (user_id, title, content, created_at) VALUES (user1_id, ''1 post'', ''1 content'', CURRENT_TIMESTAMP), (user1_id, ''2 post'', ''2 content'', CURRENT_TIMESTAMP), (user1_id, ''3 post'', ''3 content'', CURRENT_TIMESTAMP), (user2_id, ''4 post'', ''4 content'', CURRENT_TIMESTAMP), (user2_id, ''5 post'', ''5 content'', CURRENT_TIMESTAMP), (user2_id, ''6 post'', ''6 content'', CURRENT_TIMESTAMP), (user2_id, ''7 post'', ''7 content'', CURRENT_TIMESTAMP); END' LANGUAGE plpgsql;