ALTER TABLE blogging_platform."order"
RENAME COLUMN status TO state;

ALTER TABLE blogging_platform."order"
ADD COLUMN state_context text;

ALTER TABLE blogging_platform."order"
DROP COLUMN message;