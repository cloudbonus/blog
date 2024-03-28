DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'post_reaction_pk' AND table_name = 'post_reaction' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.post_reaction RENAME CONSTRAINT post_reaction_pk TO post_reaction_pkey';
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
            WHERE constraint_name = 'post_reaction_post_fk' AND table_name = 'post_reaction' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.post_reaction RENAME CONSTRAINT post_reaction_post_fk TO post_reaction_post_fkey';
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
            WHERE constraint_name = 'post_reaction_user_fk' AND table_name = 'post_reaction' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.post_reaction RENAME CONSTRAINT post_reaction_user_fk TO post_reaction_user_fkey';
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
            WHERE constraint_name = 'post_reaction_unique' AND table_name = 'post_reaction' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.post_reaction RENAME CONSTRAINT post_reaction_unique TO post_reaction_post_user_key';
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
            WHERE c.relname = 'idx_post_reaction_user_id' AND n.nspname = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER INDEX blogging_platform.idx_post_reaction_user_id RENAME TO post_reaction_user_idx';
        END IF;
    END
$$;

