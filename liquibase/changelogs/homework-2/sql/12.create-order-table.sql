CREATE TABLE IF NOT EXISTS blogging_platform.order (
    order_id   bigint GENERATED BY DEFAULT AS IDENTITY,
    post_id    bigint      NOT NULL UNIQUE,
    user_id    bigint      NOT NULL,
    ordered_at timestamptz NOT NULL,
    message    varchar(255),
    status     varchar(50) NOT NULL
);

ALTER TABLE IF EXISTS blogging_platform.order
ADD CONSTRAINT order_pk PRIMARY KEY (order_id);

ALTER TABLE IF EXISTS blogging_platform.order
ADD CONSTRAINT order_post_fk FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id);

ALTER TABLE IF EXISTS blogging_platform.order
ADD CONSTRAINT order_user_fk FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id);