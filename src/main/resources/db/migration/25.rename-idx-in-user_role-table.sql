DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'user_details_user_fk' AND table_name = 'user_role' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.user_role RENAME CONSTRAINT user_details_user_fk TO user_details_user_fkey';
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
            WHERE constraint_name = 'user_details_role_fk' AND table_name = 'user_role' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.user_role RENAME CONSTRAINT user_details_role_fk TO user_details_role_fkey';
        END IF;
    END
$$;