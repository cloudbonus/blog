CREATE TABLE IF NOT EXISTS blog.tag (
    id   bigserial PRIMARY KEY,
    name text NOT NULL
);

CREATE UNIQUE INDEX tag_name_key ON blog.tag(UPPER(name));
