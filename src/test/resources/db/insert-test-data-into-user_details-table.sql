DO '
DECLARE
    user1_id bigint;
    user2_id bigint; BEGIN SELECT user_id INTO user1_id FROM blogging_platform.user WHERE login = ''kvossing0'';
SELECT user_id INTO user2_id FROM blogging_platform.user WHERE login = ''gmaccook1'';

INSERT INTO blogging_platform.user_details (user_id, firstname, surname, university_name, major_name,
    company_name, job_title) VALUES (user1_id, ''Karl'', ''Doe'', ''Harvard University'', ''Computer Science'', ''Google'', ''Software Engineer'');

INSERT INTO blogging_platform.user_details (user_id, firstname, surname, university_name, major_name,
    company_name, job_title) VALUES (user2_id, ''Alice'', ''Johnson'', ''MIT'', ''Artificial Intelligence'', ''Facebook'', ''AI Researcher''); END
' LANGUAGE plpgsql;
