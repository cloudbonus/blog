DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    user3_id bigint;
    user4_id bigint;
    user5_id bigint;
    user6_id bigint;
    role1_id bigint;
    role2_id bigint;
    role3_id bigint;
    role4_id bigint; BEGIN INSERT INTO blog.user (username, password, email, created_at, updated_at) VALUES (''kvossing0'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''vpenzer0@icio.us'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''gmaccook1'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''rpucker1@statcounter.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''admin'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''admin@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''user'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''user@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''company'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''company@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''student'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''student@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT id INTO user1_id FROM blog.user WHERE username = ''kvossing0'';
SELECT id INTO user2_id FROM blog.user WHERE username = ''gmaccook1'';
SELECT id INTO user3_id FROM blog.user WHERE username = ''admin'';
SELECT id INTO user4_id FROM blog.user WHERE username = ''user'';
SELECT id INTO user5_id FROM blog.user WHERE username = ''student'';
SELECT id INTO user6_id FROM blog.user WHERE username = ''company'';

SELECT id INTO role1_id FROM blog.role WHERE name = ''ROLE_USER'';
SELECT id INTO role2_id FROM blog.role WHERE name = ''ROLE_ADMIN'';
SELECT id INTO role3_id FROM blog.role WHERE name = ''ROLE_STUDENT'';
SELECT id INTO role4_id FROM blog.role WHERE name = ''ROLE_COMPANY'';

INSERT INTO blog.user_role (user_id, role_id) VALUES (user1_id, role1_id), (user2_id, role1_id), (user3_id, role2_id), (user4_id, role1_id), (user5_id, role3_id), (user6_id, role4_id); END' LANGUAGE plpgsql;
