DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    post1_id bigint;
    post2_id bigint;
    post3_id bigint; BEGIN SELECT user_id INTO user1_id FROM blogging_platform.user WHERE login = ''kvossing0'';
SELECT user_id INTO user2_id FROM blogging_platform.user WHERE login = ''gmaccook1'';

SELECT post_id INTO post1_id FROM blogging_platform.post WHERE title = ''First Post'';
SELECT post_id INTO post2_id FROM blogging_platform.post WHERE title = ''Second Post'';
SELECT post_id INTO post3_id FROM blogging_platform.post WHERE title = ''Third Post'';

INSERT INTO blogging_platform.comment (post_id, user_id, content, published_at) VALUES (post1_id, user1_id, ''This is the content of the first comment.'', CURRENT_TIMESTAMP), (post2_id, user1_id, ''This is the content of the second comment.'', CURRENT_TIMESTAMP), (post3_id, user2_id, ''This is the content of the third comment.'', CURRENT_TIMESTAMP); END
' LANGUAGE plpgsql;