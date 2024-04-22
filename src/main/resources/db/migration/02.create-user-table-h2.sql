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
