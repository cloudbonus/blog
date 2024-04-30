CREATE TABLE IF NOT EXISTS blogging_platform."order" (
    order_id   bigint AUTO_INCREMENT,
    post_id    bigint UNIQUE,
    user_id    bigint,
    ordered_at timestamp    NOT NULL,
    message    varchar(255),
    status     varchar(255) NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform."order"
ADD CONSTRAINT order_pkey PRIMARY KEY (order_id);

ALTER TABLE IF EXISTS blogging_platform."order"
ADD CONSTRAINT order_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE SET NULL;

ALTER TABLE IF EXISTS blogging_platform."order"
ADD CONSTRAINT order_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform."user"(user_id) ON DELETE SET NULL;
