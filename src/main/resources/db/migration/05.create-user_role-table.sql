CREATE TABLE IF NOT EXISTS blog.user_role (
    user_id bigint REFERENCES blog.user(id) ON DELETE CASCADE,
    role_id bigint REFERENCES blog.role(id) ON DELETE CASCADE,
    CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id)
);