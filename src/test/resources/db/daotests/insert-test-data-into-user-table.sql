INSERT INTO blogging_platform.role (role_name)
VALUES
    ('ROLE_USER');

INSERT INTO blogging_platform."user" (login, password, email, created_at, last_login)
VALUES
    ('kvossing0', '123', 'vpenzer0@icio.us', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO blogging_platform."user" (login, password, email, created_at, last_login)
VALUES
    ('gmaccook1', 'lY3<OP4Y', 'rpucker1@statcounter.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SET @user1_id = (SELECT user_id FROM blogging_platform."user" WHERE LOGIN = 'kvossing0');
SET @user2_id = (SELECT user_id FROM blogging_platform."user" WHERE LOGIN = 'gmaccook1');

SET @role_id = (SELECT role_id FROM blogging_platform.role WHERE role_name = 'ROLE_USER');

INSERT INTO blogging_platform.user_role (user_id, role_id)
VALUES
    (@user1_id, @role_id);
INSERT INTO blogging_platform.user_role (user_id, role_id)
VALUES
    (@user2_id, @role_id);