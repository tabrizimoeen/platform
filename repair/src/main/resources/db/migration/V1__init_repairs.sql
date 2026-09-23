CREATE SCHEMA IF NOT EXISTS repairs;

CREATE TABLE repairs.repair_shop
(
    id         bigserial PRIMARY KEY,
    name       varchar(255),
    owner_name varchar(255),
    phone      varchar(255),
    created_at timestamp
);

CREATE TABLE repairs.users
(
    id         bigserial PRIMARY KEY,
    username   varchar(255) NOT NULL UNIQUE,
    password   varchar(255),
    full_name  varchar(255),
    role       varchar(255),
    active     boolean,
    shop_id    bigint       NOT NULL REFERENCES repairs.repair_shop (id),
    created_at timestamp
);

CREATE TABLE repairs.customers
(
    id         bigserial PRIMARY KEY,
    name       varchar(255),
    phone      varchar(255),
    shop_id    bigint NOT NULL REFERENCES repairs.repair_shop (id),
    created_at timestamp
);

CREATE UNIQUE INDEX uk_customer_phone_shop
    ON repairs.customers (shop_id, phone);

CREATE TABLE repairs.repair_orders
(
    id                  bigserial PRIMARY KEY,
    customer_id         bigint NOT NULL REFERENCES repairs.customers (id),
    shop_id             bigint NOT NULL REFERENCES repairs.repair_shop (id),
    device_model        varchar(255),
    problem_description text,
    status              varchar(255),
    estimated_cost      numeric(19, 2),
    final_cost          numeric(19, 2),
    imei                varchar(50),
    tracking_code       varchar(16) NOT NULL UNIQUE,
    created_at          timestamp
);

CREATE INDEX idx_repair_customer ON repairs.repair_orders (customer_id);
CREATE INDEX idx_repair_shop ON repairs.repair_orders (shop_id);
CREATE INDEX idx_repair_imei ON repairs.repair_orders (imei);
CREATE INDEX idx_repair_status ON repairs.repair_orders (status);

CREATE TABLE repairs.repair_logs
(
    id         bigserial PRIMARY KEY,
    repair_id  bigint NOT NULL REFERENCES repairs.repair_orders (id),
    message    varchar(255),
    status     varchar(255),
    shop_id    bigint NOT NULL REFERENCES repairs.repair_shop (id),
    created_at timestamp
);

CREATE INDEX idx_repair_logs_repair ON repairs.repair_logs (repair_id);

CREATE TABLE repairs.invoices
(
    id              bigserial PRIMARY KEY,
    repair_order_id bigint UNIQUE REFERENCES repairs.repair_orders (id),
    amount          numeric(19, 2),
    paid            boolean,
    shop_id         bigint NOT NULL REFERENCES repairs.repair_shop (id),
    created_at      timestamp
);
