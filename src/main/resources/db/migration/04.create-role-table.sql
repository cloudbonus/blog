CREATE TABLE IF NOT EXISTS blog.role (
    id   bigserial PRIMARY KEY,
    name text NOT NULL
);

CREATE UNIQUE INDEX role_role_name_key ON blog.role(UPPER(name));
