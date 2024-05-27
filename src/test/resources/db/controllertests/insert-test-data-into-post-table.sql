DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    user3_id bigint;
    user4_id bigint;
    BEGIN SELECT user_id INTO user1_id FROM blogging_platform.user WHERE login = ''kvossing0'';
SELECT user_id INTO user2_id FROM blogging_platform.user WHERE login = ''gmaccook1''; SELECT user_id INTO user3_id FROM blogging_platform.user WHERE login = ''student'';
SELECT user_id INTO user4_id FROM blogging_platform.user WHERE login = ''company'';


INSERT INTO blogging_platform.post (user_id, title, content, published_at) VALUES (user1_id, ''First Post'', ''This is the content of the first post.'', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at) VALUES (user1_id, ''Second Post'', ''This is the content of the second post.'', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at) VALUES (user2_id, ''Third Post'', ''This is the content of the third post.'', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at) VALUES (user3_id, ''Fourth  Post'', ''This is the content of the fourth post.'', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at) VALUES (user4_id, ''5 Post'', ''This is the content of the third post.'', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at) VALUES (user4_id, ''6 Post'', ''This is the content of the third post.'', CURRENT_TIMESTAMP); END

' LANGUAGE plpgsql;