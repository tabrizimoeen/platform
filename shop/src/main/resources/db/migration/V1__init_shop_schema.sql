CREATE SCHEMA IF NOT EXISTS shops;

CREATE TABLE shops.users
(
    id            bigserial PRIMARY KEY,
    created_at    timestamp    NOT NULL,
    updated_at    timestamp    NOT NULL,
    first_name    varchar(255) NOT NULL,
    last_name     varchar(255) NOT NULL,
    mobile        varchar(255) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    role          varchar(255) NOT NULL
);

CREATE INDEX idx_user_mobile ON shops.users (mobile);

CREATE TABLE shops.categories
(
    id          bigserial PRIMARY KEY,
    created_at  timestamp    NOT NULL,
    updated_at  timestamp    NOT NULL,
    name        varchar(255) NOT NULL,
    slug        varchar(255) NOT NULL UNIQUE,
    description varchar(255)
);

CREATE TABLE shops.products
(
    id          bigserial PRIMARY KEY,
    created_at  timestamp      NOT NULL,
    updated_at  timestamp      NOT NULL,
    category_id bigint         NOT NULL REFERENCES shops.categories (id),
    name        varchar(255)   NOT NULL,
    slug        varchar(255)   NOT NULL UNIQUE,
    description text,
    price       numeric(19, 2) NOT NULL,
    inventory   integer        NOT NULL,
    active      boolean        NOT NULL DEFAULT true,
    status      varchar(255)
);

CREATE INDEX idx_product_category ON shops.products (category_id);
CREATE INDEX idx_product_slug ON shops.products (slug);

CREATE TABLE shops.product_images
(
    id         bigserial PRIMARY KEY,
    created_at timestamp    NOT NULL,
    updated_at timestamp    NOT NULL,
    product_id bigint       NOT NULL REFERENCES shops.products (id),
    image_url  varchar(255) NOT NULL,
    sort_order integer      NOT NULL DEFAULT 0
);

CREATE TABLE shops.addresses
(
    id            bigserial PRIMARY KEY,
    created_at    timestamp    NOT NULL,
    updated_at    timestamp    NOT NULL,
    user_id       bigint       NOT NULL REFERENCES shops.users (id),
    receiver_name varchar(255) NOT NULL,
    mobile        varchar(255) NOT NULL,
    province      varchar(255),
    city          varchar(255),
    address       text         NOT NULL,
    postal_code   varchar(255)
);

CREATE TABLE shops.orders
(
    id          bigserial PRIMARY KEY,
    created_at  timestamp      NOT NULL,
    updated_at  timestamp      NOT NULL,
    user_id     bigint         NOT NULL REFERENCES shops.users (id),
    address_id  bigint         NOT NULL REFERENCES shops.addresses (id),
    status      varchar(255)   NOT NULL,
    total_price numeric(19, 2) NOT NULL
);

CREATE INDEX idx_order_user ON shops.orders (user_id);

CREATE TABLE shops.order_items
(
    id           bigserial PRIMARY KEY,
    created_at   timestamp      NOT NULL,
    updated_at   timestamp      NOT NULL,
    order_id     bigint         NOT NULL REFERENCES shops.orders (id),
    product_id   bigint         NOT NULL REFERENCES shops.products (id),
    product_name varchar(255)   NOT NULL,
    unit_price   numeric(19, 2) NOT NULL,
    quantity     integer        NOT NULL,
    total_price  numeric(19, 2) NOT NULL
);

CREATE TABLE shops.payments
(
    id         bigserial PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    order_id   bigint    NOT NULL REFERENCES shops.orders (id),
    authority  varchar(255) UNIQUE,
    ref_id     varchar(255) UNIQUE,
    status     varchar(255),
    paid_at    timestamp
);

CREATE TABLE shops.carts
(
    id         bigserial PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    user_id    bigint REFERENCES shops.users (id)
);

CREATE TABLE shops.cart_items
(
    id         bigserial PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    cart_id    bigint REFERENCES shops.carts (id),
    product_id bigint REFERENCES shops.products (id),
    quantity   integer
);
