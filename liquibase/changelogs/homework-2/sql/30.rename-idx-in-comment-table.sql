DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'comment_pk' AND table_name = 'comment' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.comment RENAME CONSTRAINT comment_pk TO comment_pkey';
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
            WHERE constraint_name = 'comment_post_fk' AND table_name = 'comment' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.comment RENAME CONSTRAINT comment_post_fk TO comment_post_fkey';
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
            WHERE constraint_name = 'comment_user_fk' AND table_name = 'comment' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.comment RENAME CONSTRAINT comment_user_fk TO comment_user_fkey';
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
            WHERE c.relname = 'idx_comment_post_id' AND n.nspname = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER INDEX blogging_platform.idx_comment_post_id RENAME TO comment_post_idx';
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
            WHERE c.relname = 'idx_comment_user_id' AND n.nspname = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER INDEX blogging_platform.idx_comment_user_id RENAME TO comment_user_idx';
        END IF;
    END
$$;
