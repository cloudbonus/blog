INSERT INTO blog.comment (post_id, user_id, content, created_at)
VALUES
    (1, 1, '1 content', CURRENT_TIMESTAMP),
    (2, 1, '2 content', CURRENT_TIMESTAMP),
    (3, 2, '3 content', CURRENT_TIMESTAMP);