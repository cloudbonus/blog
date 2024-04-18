CREATE SCHEMA IF NOT EXISTS blogging_platform;

CREATE TABLE IF NOT EXISTS blogging_platform."user" (
    user_id    bigint AUTO_INCREMENT,
    login      varchar(255) NOT NULL,
    password   varchar(255) NOT NULL,
    email      varchar(255) NOT NULL,
    created_at timestamp    NOT NULL,
    last_login timestamp
);

ALTER TABLE IF EXISTS blogging_platform."user"
ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);

ALTER TABLE IF EXISTS blogging_platform."user"
ADD CONSTRAINT login_unique UNIQUE (login);

ALTER TABLE IF EXISTS blogging_platform."user"
ADD CONSTRAINT email_unique UNIQUE (email);

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

CREATE TABLE IF NOT EXISTS blogging_platform.role (
    role_id   bigint AUTO_INCREMENT,
    role_name varchar(255) NOT NULL UNIQUE
);

ALTER TABLE IF EXISTS blogging_platform.role
ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);

CREATE TABLE IF NOT EXISTS blogging_platform.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

ALTER TABLE blogging_platform.user_role
ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);

ALTER TABLE IF EXISTS blogging_platform.user_role
ADD CONSTRAINT user_role_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.user_role
ADD CONSTRAINT user_role_role_fkey FOREIGN KEY (role_id) REFERENCES blogging_platform.role(role_id) ON DELETE CASCADE;


CREATE TABLE IF NOT EXISTS blogging_platform.post (
    post_id      bigint AUTO_INCREMENT NOT NULL,
    user_id      bigint                NOT NULL,
    title        varchar(255)          NOT NULL,
    content      varchar(255)          NOT NULL,
    published_at timestamp             NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.post
ADD CONSTRAINT post_pkey PRIMARY KEY (post_id);

ALTER TABLE IF EXISTS blogging_platform.post
ADD CONSTRAINT post_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS blogging_platform.post_reaction (
    post_id       bigint       NOT NULL,
    user_id       bigint       NOT NULL,
    reaction_type varchar(255) NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_pkey PRIMARY KEY (post_id);

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE CASCADE;


ALTER TABLE blogging_platform.post_reaction
ADD CONSTRAINT post_reaction_post_user_key UNIQUE (post_id, user_id);

CREATE TABLE IF NOT EXISTS blogging_platform.tag (
    tag_id   bigint AUTO_INCREMENT,
    tag_name varchar(255) NOT NULL UNIQUE
);

ALTER TABLE IF EXISTS blogging_platform.tag
ADD CONSTRAINT tag_pkey PRIMARY KEY (tag_id);

CREATE TABLE IF NOT EXISTS blogging_platform.post_tag (
    post_id bigint NOT NULL,
    tag_id  bigint NOT NULL
);

ALTER TABLE blogging_platform.post_tag
ADD CONSTRAINT post_tag_pkey PRIMARY KEY (post_id, tag_id);

ALTER TABLE IF EXISTS blogging_platform.post_tag
ADD CONSTRAINT post_tag_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.post_tag
ADD CONSTRAINT post_tag_tag_fkey FOREIGN KEY (tag_id) REFERENCES blogging_platform.tag(tag_id) ON DELETE CASCADE;


CREATE TABLE IF NOT EXISTS blogging_platform.comment (
    comment_id   bigint AUTO_INCREMENT,
    post_id      bigint       NOT NULL,
    user_id      bigint       NOT NULL,
    content      varchar(255) NOT NULL,
    published_at timestamp    NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_pkey PRIMARY KEY (comment_id);

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE SET NULL;

ALTER TABLE IF EXISTS blogging_platform.comment
ADD CONSTRAINT comment_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE SET NULL;


CREATE TABLE IF NOT EXISTS blogging_platform.comment_reaction (
    comment_id    bigint       NOT NULL,
    user_id       bigint       NOT NULL,
    reaction_type varchar(255) NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_pkey PRIMARY KEY (comment_id);

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_comment_fkey FOREIGN KEY (comment_id) REFERENCES blogging_platform.comment(comment_id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE CASCADE;

ALTER TABLE blogging_platform.comment_reaction
ADD CONSTRAINT comment_reaction_comment_user_key UNIQUE (comment_id, user_id);

CREATE TABLE IF NOT EXISTS blogging_platform."order" (
    order_id   bigint AUTO_INCREMENT,
    post_id    bigint       NOT NULL UNIQUE,
    user_id    bigint       NOT NULL,
    ordered_at timestamp    NOT NULL,
    message    varchar(255),
    status     varchar(255) NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform."order"
ADD CONSTRAINT order_pkey PRIMARY KEY (order_id);

ALTER TABLE IF EXISTS blogging_platform."order"
ADD CONSTRAINT order_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id);

ALTER TABLE IF EXISTS blogging_platform."order"
ADD CONSTRAINT order_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id);


CREATE INDEX IF NOT EXISTS post_user_idx ON blogging_platform.post(user_id);

CREATE INDEX IF NOT EXISTS post_reaction_user_idx ON blogging_platform.post_reaction(user_id);

CREATE INDEX IF NOT EXISTS comment_post_idx ON blogging_platform.comment(post_id);
CREATE INDEX IF NOT EXISTS comment_user_idx ON blogging_platform.comment(user_id);

CREATE INDEX IF NOT EXISTS comment_reaction_user_idx ON blogging_platform.comment_reaction(user_id);

CREATE INDEX IF NOT EXISTS order_user_idx ON blogging_platform."order"(user_id);