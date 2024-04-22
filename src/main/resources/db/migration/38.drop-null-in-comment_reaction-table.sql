DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.columns
            WHERE table_schema = 'blogging_platform' AND table_name = 'comment_reaction' AND column_name = 'user_id' AND
                  is_nullable = 'NO'
                  ) THEN ALTER TABLE blogging_platform.comment_reaction
        ALTER COLUMN user_id DROP NOT NULL; ALTER TABLE blogging_platform.comment_reaction
        DROP CONSTRAINT comment_reaction_user_fkey; ALTER TABLE blogging_platform.comment_reaction
        ADD CONSTRAINT comment_reaction_user_fkey FOREIGN KEY (user_id) REFERENCES blogging_platform.user(user_id) ON DELETE SET NULL;
        END IF;
    END
$$;
