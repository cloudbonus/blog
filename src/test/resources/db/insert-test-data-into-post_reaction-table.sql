DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    post1_id bigint;
    reaction1_id bigint; BEGIN SELECT id INTO user1_id FROM blog.user WHERE username = ''user'';
SELECT id INTO user2_id FROM blog.user WHERE username = ''student'';

SELECT id INTO post1_id FROM blog.post WHERE id = 1;

SELECT id INTO reaction1_id FROM blog.reaction WHERE name = ''LIKE'';

INSERT INTO blog.post_reaction (post_id, user_id, reaction_id) VALUES (post1_id, user1_id, reaction1_id), (post1_id, user2_id, reaction1_id); END' LANGUAGE plpgsql;