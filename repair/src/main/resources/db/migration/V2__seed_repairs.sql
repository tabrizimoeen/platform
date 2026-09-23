-- داده نمونه برای امتحان جریان نسخه اولیه.
-- کاربر: moeen  رمز: password
INSERT INTO repairs.repair_shop (id, name, owner_name, phone, created_at)
VALUES (1, 'تعمیرات موبایل تبریزی', 'معین تبریزی', '02100000000', now());

INSERT INTO repairs.users (id, username, password, full_name, role, active, shop_id)
VALUES (1, 'moeen', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'معین تبریزی', 'OWNER', true, 1);

INSERT INTO repairs.customers (id, name, phone, shop_id, created_at)
VALUES (1, 'علی کریمی', '09120000001', 1, now());

INSERT INTO repairs.repair_orders
(id, customer_id, shop_id, device_model, problem_description, status, estimated_cost, imei, tracking_code, created_at)
VALUES (1, 1, 1, 'iPhone 13', 'شکستگی صفحه', 'RECEIVED', 1500000, '356789123456789', 'A1B2C3D4', now());

INSERT INTO repairs.repair_logs (repair_id, shop_id, message, status, created_at)
VALUES (1, 1, 'دستگاه تحویل گرفته شد', 'RECEIVED', now());

INSERT INTO repairs.invoices (id, repair_order_id, amount, paid, shop_id, created_at)
VALUES (1, 1, 1500000, false, 1, now());

SELECT setval(pg_get_serial_sequence('repairs.repair_shop', 'id'), (SELECT MAX(id) FROM repairs.repair_shop));
SELECT setval(pg_get_serial_sequence('repairs.users', 'id'), (SELECT MAX(id) FROM repairs.users));
SELECT setval(pg_get_serial_sequence('repairs.customers', 'id'), (SELECT MAX(id) FROM repairs.customers));
SELECT setval(pg_get_serial_sequence('repairs.repair_orders', 'id'), (SELECT MAX(id) FROM repairs.repair_orders));
SELECT setval(pg_get_serial_sequence('repairs.repair_logs', 'id'), (SELECT MAX(id) FROM repairs.repair_logs));
SELECT setval(pg_get_serial_sequence('repairs.invoices', 'id'), (SELECT MAX(id) FROM repairs.invoices));
