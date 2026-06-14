ALTER TABLE shops.products
    ADD COLUMN reserved_inventory INTEGER NOT NULL DEFAULT 0;