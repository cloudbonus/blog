INSERT INTO blogging_platform.post (user_id, title, content, published_at)
VALUES
    (@user1_id, 'First Post', 'This is the content of the first post.', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at)
VALUES
    (@user1_id, 'Second Post', 'This is the content of the second post.', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at)
VALUES
    (@user2_id, 'Third Post', 'This is the content of the third post.', CURRENT_TIMESTAMP);

SET @post1_id = (SELECT post_id FROM blogging_platform.post WHERE title = 'First Post');
SET @post2_id = (SELECT post_id FROM blogging_platform.post WHERE title = 'Second Post');
SET @post3_id = (SELECT post_id FROM blogging_platform.post WHERE title = 'Third Post');