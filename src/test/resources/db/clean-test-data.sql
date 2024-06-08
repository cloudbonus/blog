TRUNCATE blog.user CASCADE;
TRUNCATE blog.post CASCADE;
TRUNCATE blog.order CASCADE;
TRUNCATE blog.comment CASCADE;
TRUNCATE blog.post_reaction CASCADE;
TRUNCATE blog.comment_reaction CASCADE;

ALTER SEQUENCE blog.user_id_seq RESTART WITH 1;
ALTER SEQUENCE blog.post_reaction_id_seq RESTART WITH 1;
ALTER SEQUENCE blog.post_id_seq RESTART WITH 1;
ALTER SEQUENCE blog.order_id_seq RESTART WITH 1;
ALTER SEQUENCE blog.comment_reaction_id_seq RESTART WITH 1;
ALTER SEQUENCE blog.comment_id_seq RESTART WITH 1;
