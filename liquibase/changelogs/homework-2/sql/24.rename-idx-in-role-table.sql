DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'role_pk' AND table_name = 'role' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.role RENAME CONSTRAINT role_pk TO role_pkey';
        END IF;
    END
$$;