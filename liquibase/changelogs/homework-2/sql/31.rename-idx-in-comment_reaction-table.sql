DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'comment_reaction_pk' AND table_name = 'comment_reaction' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.comment_reaction RENAME CONSTRAINT comment_reaction_pk TO comment_reaction_pkey';
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
            WHERE
                constraint_name = 'comment_reaction_comment_fk' AND table_name = 'comment_reaction' AND
                table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.comment_reaction RENAME CONSTRAINT comment_reaction_comment_fk TO comment_reaction_comment_fkey';
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
            WHERE constraint_name = 'comment_reaction_user_fk' AND table_name = 'comment_reaction' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.comment_reaction RENAME CONSTRAINT comment_reaction_user_fk TO comment_reaction_user_fkey';
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
            WHERE constraint_name = 'comment_reaction_unique' AND table_name = 'comment_reaction' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.comment_reaction RENAME CONSTRAINT comment_reaction_unique TO comment_reaction_comment_user_key';
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
            WHERE c.relname = 'idx_comment_reaction_user_id' AND n.nspname = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER INDEX blogging_platform.idx_comment_reaction_user_id RENAME TO comment_reaction_user_idx';
        END IF;
    END
$$;
