DO '
DECLARE
    user1_id bigint;
    user2_id bigint;
    role1_id bigint; BEGIN INSERT INTO blogging_platform.user (login, password, email, created_at, last_login) VALUES (''kvossing0'', ''123'', ''vpenzer0@icio.us'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (''gmaccook1'', ''lY3<OP4Y'', ''rpucker1@statcounter.com'', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT user_id INTO user1_id FROM blogging_platform.user WHERE login = ''kvossing0'';
SELECT user_id INTO user2_id FROM blogging_platform.user WHERE login = ''gmaccook1'';
SELECT role_id INTO role1_id FROM blogging_platform.role WHERE role_name = ''ROLE_USER'';

INSERT INTO blogging_platform.user_role (user_id, role_id) VALUES (user1_id, role1_id), (user2_id, role1_id); END
' LANGUAGE plpgsql;
