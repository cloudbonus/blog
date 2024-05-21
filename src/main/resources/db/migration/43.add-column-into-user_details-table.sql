ALTER TABLE blogging_platform.user_details
ADD COLUMN state text NOT NULL DEFAULT 'RESERVED', ADD COLUMN state_context text;