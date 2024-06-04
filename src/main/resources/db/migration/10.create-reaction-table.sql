CREATE TABLE IF NOT EXISTS blog.reaction (
    id   bigserial PRIMARY KEY,
    name text NOT NULL
);

CREATE UNIQUE INDEX reaction_reaction_name_key ON blog.reaction(UPPER(name));