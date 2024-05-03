INSERT INTO blogging_platform.comment (post_id, user_id, content, published_at)
VALUES
    (@post1_id, @user1_id, 'This is the content of the first comment.', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.comment (post_id, user_id, content, published_at)
VALUES
    (@post2_id, @user1_id, 'This is the content of the second comment.', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.comment (post_id, user_id, content, published_at)
VALUES
    (@post3_id, @user2_id, 'This is the content of the third comment.', CURRENT_TIMESTAMP);