CREATE TABLE IF NOT EXISTS blog.user_info (
    id            bigint PRIMARY KEY REFERENCES blog.user(id) ON DELETE CASCADE,
    firstname     text,
    surname       text,
    university    text,
    major         text,
    company       text,
    job           text,
    state         text,
    state_context text
);