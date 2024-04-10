DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'tag_pk' AND table_name = 'tag' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.tag RENAME CONSTRAINT tag_pk TO tag_pkey';
        END IF;
    END
$$;