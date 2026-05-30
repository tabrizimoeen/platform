create table if not exists repairs.repair_shop (
    id          bigserial primary key,
    name        varchar(100) not null,
    owner_name  varchar(100),
    phone       varchar(20),
    created_at  timestamp default now()
);
create table if not exists repairs.users (
    id          bigserial primary key,
    username    varchar(255) not null,
    password    varchar(255) not null,
    role        varchar(50) default 'STAFF',
    shop_id     bigint not null references repairs.repair_shop(id),
    created_at  timestamp default now()
);
create table if not exists repairs.customers (
    id          bigserial primary key,
    name        varchar(255) not null,
    phone       varchar(255) not null,
    shop_id     bigint not null references repairs.repair_shop(id),
    created_at  timestamp default now(),
    updated_at  timestamp default now()
);

create index idx_customers_phone on repairs.customers(phone);

create unique index uk_customer_phone_shop
on repairs.customers(phone, shop_id);

create table if not exists repairs.repair_orders (
    id                  bigserial primary key,
    customer_id         bigint not null references repairs.customers(id) on delete cascade,
    shop_id             bigint not null references repairs.repair_shop(id) on delete cascade,

    device_model        varchar(255) not null,
    imei                varchar(255),
    problem_description text,

    status              varchar(50) default 'RECEIVED' not null,

    estimated_cost      numeric(10,2),
    final_cost          numeric(10,2),

    created_at          timestamp default now(),
    updated_at          timestamp default now()
);

create index idx_repair_customer on repairs.repair_orders(customer_id);
create index idx_repair_status on repairs.repair_orders(status);
create index idx_repair_imei on repairs.repair_orders(imei);



create table if not exists repairs.repair_logs (
    id          bigserial primary key,
    repair_id   bigint not null references repairs.repair_orders(id) on delete cascade,
    shop_id     bigint not null references repairs.repair_shop(id),

    message     varchar(255) not null,
    status      varchar(50),

    created_at  timestamp default now()
);

create table if not exists repairs.invoices (
    id              bigserial primary key,
    repair_order_id bigint unique references repairs.repair_orders(id),

    amount          numeric(10,2),
    paid            boolean default false,

    shop_id         bigint not null references repairs.repair_shop(id),
    created_at      timestamp default now()
);