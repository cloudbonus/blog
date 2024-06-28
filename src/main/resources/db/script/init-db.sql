INSERT INTO blog.user (username, password, email, created_at, updated_at)
VALUES
    ('kvossing0', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'vpenzer0@icio.us',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('gmaccook1', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'rpucker1@statcounter.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'admin@gmail.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('user', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'user@gmail.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('company', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'company@gmail.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('student', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'student@gmail.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO blog.user_role (user_id, role_id)
VALUES
    (1, 2),
    (2, 2),
    (3, 1),
    (4, 2),
    (5, 4),
    (6, 3);

INSERT INTO blog.user_info (id, firstname, surname, university, major, company, job)
VALUES
    (5, 'Karl', 'Doe', 'Harvard University', 'Computer Science', 'Google', 'Software Engineer'),
    (6, 'Alice', 'Johnson', 'MIT', 'Artificial Intelligence', 'Facebook', 'AI Researcher');

INSERT INTO blog.post (user_id, title, content, created_at)
VALUES
    (6, '1 post', '1 content', CURRENT_TIMESTAMP),
    (6, '2 post', '2 content', CURRENT_TIMESTAMP),
    (6, '3 post', '3 content', CURRENT_TIMESTAMP),
    (5, '4 post', '4 content', CURRENT_TIMESTAMP),
    (5, '5 post', '5 content', CURRENT_TIMESTAMP),
    (5, '6 post', '6 content', CURRENT_TIMESTAMP);

INSERT INTO blog.post_tag (post_id, tag_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 1),
    (3, 2);

INSERT INTO blog.comment (post_id, user_id, content, created_at)
VALUES
    (1, 1, '1 content', CURRENT_TIMESTAMP),
    (2, 1, '2 content', CURRENT_TIMESTAMP),
    (3, 2, '3 content', CURRENT_TIMESTAMP);

INSERT INTO blog.order(post_id, user_id, created_at, state)
VALUES
    (4, 5, CURRENT_TIMESTAMP, 'NEW'),
    (5, 5, CURRENT_TIMESTAMP, 'NEW'),
    (6, 5, CURRENT_TIMESTAMP, 'COMMITED');

INSERT INTO blog.comment_reaction (comment_id, user_id, reaction_id)
VALUES
    (1, 1, 1),
    (2, 2, 1);

INSERT INTO blog.post_reaction (post_id, user_id, reaction_id)
VALUES
    (1, 1, 1),
    (1, 2, 2);