DO '
DECLARE
    user1_id bigint;
    post1_id bigint;
    post2_id bigint; BEGIN SELECT user_id INTO user1_id FROM blogging_platform.user WHERE login = ''company'';
SELECT post_id INTO post1_id FROM blogging_platform.post WHERE title = ''5 Post'';
SELECT post_id INTO post2_id FROM blogging_platform.post WHERE title = ''6 Post'';

INSERT INTO blogging_platform.order(post_id, user_id, ordered_at, state) VALUES (post1_id, user1_id, CURRENT_TIMESTAMP, ''COMPLETED'');
INSERT INTO blogging_platform.order(post_id, user_id, ordered_at, state) VALUES (post2_id, user1_id, CURRENT_TIMESTAMP, ''NEW''); END

' LANGUAGE plpgsql;