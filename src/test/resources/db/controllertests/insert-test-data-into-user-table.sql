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
    role4_id bigint;BEGIN INSERT INTO blogging_platform.user (login, password, email, created_at, last_login) VALUES (''kvossing0'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''vpenzer0@icio.us'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''gmaccook1'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''rpucker1@statcounter.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''admin'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''admin@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''user'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''user@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''company'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''company@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''student'', ''{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe'', ''student@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT user_id INTO user1_id FROM blogging_platform.user WHERE login = ''kvossing0'';
SELECT user_id INTO user2_id FROM blogging_platform.user WHERE login = ''gmaccook1'';
SELECT user_id INTO user3_id FROM blogging_platform.user WHERE login = ''admin'';
SELECT user_id INTO user4_id FROM blogging_platform.user WHERE login = ''user'';
SELECT user_id INTO user5_id FROM blogging_platform.user WHERE login = ''student'';
SELECT user_id INTO user6_id FROM blogging_platform.user WHERE login = ''company'';
SELECT role_id INTO role1_id FROM blogging_platform.role WHERE role_name = ''ROLE_USER'';
SELECT role_id INTO role2_id FROM blogging_platform.role WHERE role_name = ''ROLE_ADMIN'';
SELECT role_id INTO role3_id FROM blogging_platform.role WHERE role_name = ''ROLE_STUDENT'';
SELECT role_id INTO role4_id FROM blogging_platform.role WHERE role_name = ''ROLE_COMPANY'';

INSERT INTO blogging_platform.user_role (user_id, role_id) VALUES (user1_id, role1_id), (user2_id, role1_id), (user3_id, role2_id), (user4_id, role1_id), (user5_id, role3_id), (user6_id, role4_id); END
' LANGUAGE plpgsql;
