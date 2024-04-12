
DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'user_pk' AND table_name = 'user' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.user RENAME CONSTRAINT user_pk TO user_pkey';
        END IF;
    END
$$;
