DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'user_details_pk' AND table_name = 'user_details' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.user_details RENAME CONSTRAINT user_details_pk TO user_details_pkey';
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'user_details_fk' AND table_name = 'user_details' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.user_details RENAME CONSTRAINT user_details_fk TO user_details_user_fkey';
        END IF;
    END
$$;