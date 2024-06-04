DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    comment1_id bigint;
    reaction1_id bigint; BEGIN SELECT id INTO user1_id FROM blog.user WHERE username = ''user'';
SELECT id INTO user2_id FROM blog.user WHERE username = ''student'';

SELECT id INTO comment1_id FROM blog.comment WHERE id = 1;

SELECT id INTO reaction1_id FROM blog.reaction WHERE name = ''LIKE'';

INSERT INTO blog.comment_reaction (comment_id, user_id, reaction_id) VALUES (comment1_id, user1_id, reaction1_id), (comment1_id, user2_id, reaction1_id); END' LANGUAGE plpgsql;