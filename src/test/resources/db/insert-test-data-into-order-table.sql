INSERT INTO blog.order(post_id, user_id, created_at, state)
VALUES
    (4, 5, CURRENT_TIMESTAMP, 'NEW'),
    (5, 5, CURRENT_TIMESTAMP, 'NEW'),
    (6, 5, CURRENT_TIMESTAMP, 'COMMITED');