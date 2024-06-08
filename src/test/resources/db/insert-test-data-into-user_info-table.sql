DO '
DECLARE
    user1_id bigint;
    user2_id bigint; BEGIN SELECT id INTO user1_id FROM blog.user WHERE username = ''kvossing0'';
SELECT id INTO user2_id FROM blog.user WHERE username = ''gmaccook1'';

INSERT INTO blog.user_info (id, firstname, surname, university, major, company, job) VALUES (user1_id, ''Karl'', ''Doe'', ''Harvard University'', ''Computer Science'', ''Google'', ''Software Engineer''), (user2_id, ''Alice'', ''Johnson'', ''MIT'', ''Artificial Intelligence'', ''Facebook'', ''AI Researcher''); END' LANGUAGE plpgsql;
