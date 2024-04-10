DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'post_pk' AND table_name = 'post' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.post RENAME CONSTRAINT post_pk TO post_pkey';
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
            WHERE constraint_name = 'post_fk' AND table_name = 'post' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.post RENAME CONSTRAINT post_fk TO post_user_fkey';
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                pg_class AS c
                JOIN pg_namespace AS n ON n.oid = c.relnamespace
            WHERE c.relname = 'idx_post_user_id' AND n.nspname = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER INDEX blogging_platform.idx_post_user_id RENAME TO post_user_idx';
        END IF;
    END
$$;

