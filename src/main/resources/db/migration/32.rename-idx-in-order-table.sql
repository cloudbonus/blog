DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'order_pk' AND table_name = 'order' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.order RENAME CONSTRAINT order_pk TO order_pkey';
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
            WHERE constraint_name = 'order_post_fk' AND table_name = 'order' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.order RENAME CONSTRAINT order_post_fk TO order_post_fkey';
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
            WHERE constraint_name = 'order_user_fk' AND table_name = 'order' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.order RENAME CONSTRAINT order_user_fk TO order_user_fkey';
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
            WHERE c.relname = 'idx_order_user_id' AND n.nspname = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER INDEX blogging_platform.idx_order_user_id RENAME TO order_user_idx';
        END IF;
    END
$$;
