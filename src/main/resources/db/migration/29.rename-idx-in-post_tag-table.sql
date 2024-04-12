DO
$$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM
                information_schema.table_constraints
            WHERE constraint_name = 'post_tag_post_fk' AND table_name = 'post_tag' AND
                  table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.post_tag RENAME CONSTRAINT post_tag_post_fk TO post_tag_post_fkey';
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
            WHERE constraint_name = 'post_tag_tag_fk' AND table_name = 'post_tag' AND table_schema = 'blogging_platform'
                  ) THEN
            EXECUTE 'ALTER TABLE blogging_platform.post_tag RENAME CONSTRAINT post_tag_tag_fk TO post_tag_tag_fkey';
        END IF;
    END
$$;
