CREATE TABLE IF NOT EXISTS blogging_platform.user_details (
    user_id         bigint,
    firstname       varchar(50),
    surname         varchar(50),
    university_name varchar(50),
    major_name      varchar(50),
    company_name    varchar(50),
    job_title       varchar(50)
);

ALTER TABLE IF EXISTS blogging_platform.user_details
ADD CONSTRAINT user_details_pk PRIMARY KEY (user_id);

ALTER TABLE IF EXISTS blogging_platform.user_details
ADD CONSTRAINT user_details_fk FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id) ON DELETE CASCADE;