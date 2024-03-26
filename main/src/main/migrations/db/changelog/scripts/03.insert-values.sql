--liquibase formatted sql

--changeset haurylau:18
INSERT INTO blogging_platform.role (role_name)
VALUES
    ('ADMIN'),
    ('STAFF'),
    ('USER'),
    ('STUDENT'),
    ('TECH_COMPANY_REPRESENTATIVE');

--changeset haurylau:19
INSERT INTO blogging_platform.tag (tag_name)
VALUES
    ('internship'),
    ('world'),
    ('education'),
    ('news');
