DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.columns
            WHERE table_schema = 'blogging_platform' AND table_name = 'comment' AND column_name = 'user_id' AND
                  is_nullable = 'NO'
                  ) THEN ALTER TABLE blogging_platform.comment
        ALTER COLUMN user_id DROP NOT NULL; ALTER TABLE blogging_platform.comment
        DROP CONSTRAINT comment_post_fkey; ALTER TABLE blogging_platform.comment
        ADD CONSTRAINT comment_post_fkey FOREIGN KEY (post_id) REFERENCES blogging_platform.post(post_id) ON DELETE CASCADE;
        END IF;
    END
$$;
