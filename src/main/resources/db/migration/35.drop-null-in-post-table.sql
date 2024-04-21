DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.columns
            WHERE table_schema = 'blogging_platform' AND table_name = 'post' AND column_name = 'user_id' AND
                  is_nullable = 'NO'
                  ) THEN ALTER TABLE blogging_platform.post
        ALTER COLUMN user_id DROP NOT NULL;
        END IF;
    END
$$;