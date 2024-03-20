CREATE TABLE IF NOT EXISTS blogging_platform.user_details (
    user_id         bigint,
    firstname       text,
    surname         text,
    university_name text,
    major_name      text,
    company_name    text,
    job_title       text
);

ALTER TABLE IF EXISTS blogging_platform.user_details
ADD CONSTRAINT user_details_pk PRIMARY KEY (user_id);

ALTER TABLE IF EXISTS blogging_platform.user_details
ADD CONSTRAINT user_details_fk FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id) ON DELETE CASCADE;