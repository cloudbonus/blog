DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.columns
            WHERE table_schema = 'blogging_platform' AND table_name = 'order' AND column_name = 'user_id' AND
                  is_nullable = 'NO'
                  ) THEN ALTER TABLE blogging_platform.order
        ALTER COLUMN user_id DROP NOT NULL; ALTER TABLE blogging_platform.order
        ALTER COLUMN post_id DROP NOT NULL; ALTER TABLE blogging_platform.order
        DROP CONSTRAINT order_post_fkey; ALTER TABLE blogging_platform.order
        ADD CONSTRAINT order_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE SET NULL; ALTER TABLE blogging_platform.order
        DROP CONSTRAINT order_user_fkey; ALTER TABLE blogging_platform.order
        ADD CONSTRAINT order_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id) ON DELETE SET NULL;
        END IF;
    END
$$;
