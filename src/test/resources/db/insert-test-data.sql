INSERT INTO blogging_platform.role (role_name)
VALUES
    ('ROLE_USER');

INSERT INTO blogging_platform."user" (login, password, email, created_at, last_login)
VALUES
    ('kvossing0', '123', 'vpenzer0@icio.us', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO blogging_platform."user" (login, password, email, created_at, last_login)
VALUES
    ('gmaccook1', 'lY3<OP4Y', 'rpucker1@statcounter.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SET @user1_id = (SELECT user_id
                 FROM
                     blogging_platform."user"
                 WHERE
                     login = 'kvossing0');
SET @user2_id = (SELECT user_id
                 FROM
                     blogging_platform."user"
                 WHERE
                     login = 'gmaccook1');

SET @role_id = (SELECT role_id
                FROM
                    blogging_platform.role
                WHERE
                    role_name = 'ROLE_USER');

INSERT INTO blogging_platform.user_role (user_id, role_id)
VALUES
    (@user1_id, @role_id);
INSERT INTO blogging_platform.user_role (user_id, role_id)
VALUES
    (@user2_id, @role_id);

INSERT INTO blogging_platform.user_details (user_id, firstname, surname, university_name, major_name, company_name,
                                            job_title)
VALUES
    (@user1_id, 'Karl', 'Doe', 'Harvard University', 'Computer Science', 'Google', 'Software Engineer');
INSERT INTO blogging_platform.user_details (user_id, firstname, surname, university_name, major_name, company_name,
                                            job_title)
VALUES
    (@user2_id, 'Alice', 'Johnson', 'MIT', 'Artificial Intelligence', 'Facebook', 'AI Researcher');

INSERT INTO blogging_platform.post (user_id, title, content, published_at)
VALUES
    (@user1_id, 'First Post', 'This is the content of the first post.', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at)
VALUES
    (@user1_id, 'Second Post', 'This is the content of the second post.', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.post (user_id, title, content, published_at)
VALUES
    (@user2_id, 'Third Post', 'This is the content of the third post.', CURRENT_TIMESTAMP);

INSERT INTO blogging_platform.tag (tag_name)
VALUES
    ('news');
INSERT INTO blogging_platform.tag (tag_name)
VALUES
    ('education');

SET @tag1_id = (SELECT tag_id
                FROM
                    blogging_platform.tag
                WHERE
                    tag_name = 'news');
SET @tag2_id = (SELECT tag_id
                FROM
                    blogging_platform.tag
                WHERE
                    tag_name = 'education');

SET @post1_id = (SELECT post_id
                 FROM
                     blogging_platform.post
                 WHERE
                     title = 'First Post');
SET @post2_id = (SELECT post_id
                 FROM
                     blogging_platform.post
                 WHERE
                     title = 'Second Post');
SET @post3_id = (SELECT post_id
                 FROM
                     blogging_platform.post
                 WHERE
                     title = 'Third Post');

INSERT INTO blogging_platform.post_tag (post_id, tag_id)
VALUES
    (@post1_id, @tag1_id);
INSERT INTO blogging_platform.post_tag (post_id, tag_id)
VALUES
    (@post2_id, @tag1_id);
INSERT INTO blogging_platform.post_tag (post_id, tag_id)
VALUES
    (@post3_id, @tag1_id);
INSERT INTO blogging_platform.post_tag (post_id, tag_id)
VALUES
    (@post3_id, @tag2_id);

INSERT INTO blogging_platform.comment (post_id, user_id, content, published_at)
VALUES
    (@post1_id, @user1_id, 'This is the content of the first comment.', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.comment (post_id, user_id, content, published_at)
VALUES
    (@post2_id, @user1_id, 'This is the content of the second comment.', CURRENT_TIMESTAMP);
INSERT INTO blogging_platform.comment (post_id, user_id, content, published_at)
VALUES
    (@post3_id, @user2_id, 'This is the content of the third comment.', CURRENT_TIMESTAMP);