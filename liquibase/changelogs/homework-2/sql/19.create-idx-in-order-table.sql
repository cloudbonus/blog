DROP INDEX IF EXISTS idx_order_user_id;
CREATE INDEX IF NOT EXISTS idx_order_user_id ON blogging_platform.order(user_id);