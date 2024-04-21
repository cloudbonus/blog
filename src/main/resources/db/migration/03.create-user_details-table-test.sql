CREATE TABLE IF NOT EXISTS blogging_platform.user_details (
    user_id         bigint NOT NULL,
    firstname       varchar(255),
    surname         varchar(255),
    university_name varchar(255),
    major_name      varchar(255),
    company_name    varchar(255),
    job_title       varchar(255)
);

ALTER TABLE IF EXISTS blogging_platform.user_details
ADD CONSTRAINT user_details_pkey PRIMARY KEY (user_id);

ALTER TABLE IF EXISTS blogging_platform.user_details
ADD CONSTRAINT user_details_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE CASCADE;
