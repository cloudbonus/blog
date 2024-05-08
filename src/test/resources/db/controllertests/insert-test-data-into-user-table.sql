DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    user3_id bigint;
    user4_id bigint;
    role1_id bigint;
    role2_id bigint; BEGIN INSERT INTO blogging_platform.user (login, password, email, created_at, last_login) VALUES (''kvossing0'', ''{bcrypt}$2a$10$VmOQPaFUF3G8y56iQ3OoGObgcGVaP5NJ9W8ifOVI6dduCyYYeo1gi'', ''vpenzer0@icio.us'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''gmaccook1'', ''{bcrypt}$2a$10$VmOQPaFUF3G8y56iQ3OoGObgcGVaP5NJ9W8ifOVI6dduCyYYeo1gi'', ''rpucker1@statcounter.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''admin'', ''{bcrypt}$2a$10$VmOQPaFUF3G8y56iQ3OoGObgcGVaP5NJ9W8ifOVI6dduCyYYeo1gi'', ''admin@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''user'', ''{bcrypt}$2a$10$VmOQPaFUF3G8y56iQ3OoGObgcGVaP5NJ9W8ifOVI6dduCyYYeo1gi'', ''user@gmail.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT user_id INTO user1_id FROM blogging_platform.user WHERE login = ''kvossing0'';
SELECT user_id INTO user2_id FROM blogging_platform.user WHERE login = ''gmaccook1'';
SELECT user_id INTO user3_id FROM blogging_platform.user WHERE login = ''admin'';
SELECT user_id INTO user4_id FROM blogging_platform.user WHERE login = ''user'';
SELECT role_id INTO role1_id FROM blogging_platform.role WHERE role_name = ''ROLE_USER'';
SELECT role_id INTO role2_id FROM blogging_platform.role WHERE role_name = ''ROLE_ADMIN'';

INSERT INTO blogging_platform.user_role (user_id, role_id) VALUES (user1_id, role1_id), (user2_id, role1_id), (user3_id, role2_id), (user4_id, role1_id); END
' LANGUAGE plpgsql;
