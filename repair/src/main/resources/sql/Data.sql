-- =========================
-- 🏪 فروشگاه‌ها (repair_shop)
-- =========================
INSERT INTO repairs.repair_shop (id, name, owner_name, phone, created_at)
VALUES
    (1, 'تعمیرات موبایل تبریزی', 'معین تبریزی', '+90-555-100-2001', now()),
    (2, 'مرکز تعمیرات استانبول', 'احمد ییلماز', '+90-555-100-2002', now());

-- =========================
-- 👤 کاربران (USERS)
-- =========================
INSERT INTO repairs.users (id, username, password, role, shop_id, created_at)
VALUES
    (1, 'moeen', '$2a$10$hashedpassword1', 'ADMIN', 1, now()),
    (2, 'tech1', '$2a$10$hashedpassword2', 'STAFF', 1, now()),
    (3, 'ahmad', '$2a$10$hashedpassword3', 'ADMIN', 2, now());

-- =========================
-- 👥 مشتری‌ها (CUSTOMERS)
-- =========================
INSERT INTO repairs.customers (id, name, phone, shop_id, created_at, updated_at)
VALUES
    (1, 'علی کریمی', '+90-555-111-0001', 1, now(), now()),
    (2, 'سارا محمدی', '+90-555-111-0002', 1, now(), now()),
    (3, 'جان اسمیت', '+90-555-111-0003', 2, now(), now()),
    (4, 'الیف دمیر', '+90-555-111-0004', 2, now(), now());

-- =========================
-- 📱 سفارش‌های تعمیر (REPAIR ORDERS)
-- =========================
INSERT INTO repairs.repair_orders
(id, customer_id, shop_id, device_model, imei, problem_description, status, estimated_cost, final_cost, created_at, updated_at)
VALUES
    (1, 1, 1, 'iPhone 13 Pro', '356789123456789', 'شکستگی صفحه نمایش و کار نکردن تاچ', 'RECEIVED', 150.00, NULL, now(), now()),
    (2, 2, 1, 'Samsung S21', '987654321098765', 'خالی شدن سریع باتری', 'IN_PROGRESS', 80.00, NULL, now(), now()),
    (3, 3, 2, 'Xiaomi Redmi Note 12', '456123789654123', 'مشکل درگاه شارژ', 'DONE', 40.00, 45.00, now(), now()),
    (4, 4, 2, 'iPhone 11', '741852963147852', 'آب‌خوردگی دستگاه', 'RECEIVED', 200.00, NULL, now(), now());

-- =========================
-- 📜 لاگ تعمیرات (REPAIR LOGS)
-- =========================
INSERT INTO repairs.repair_logs (repair_id, shop_id, message, status, created_at)
VALUES
    (1, 1, 'دستگاه تحویل گرفته شد', 'RECEIVED', now()),
    (1, 1, 'بررسی اولیه انجام شد', 'RECEIVED', now()),

    (2, 1, 'تست باتری آغاز شد', 'IN_PROGRESS', now()),

    (3, 2, 'درگاه شارژ با موفقیت تعویض شد', 'DONE', now()),
    (3, 2, 'کنترل نهایی انجام شد', 'DONE', now()),

    (4, 2, 'دستگاه در انتظار بررسی است', 'RECEIVED', now());

-- =========================
-- 🧾 فاکتورها (INVOICES)
-- =========================
INSERT INTO repairs.invoices (id, repair_order_id, amount, paid, shop_id, created_at)
VALUES
    (1, 1, 150.00, false, 1, now()),
    (2, 2, 80.00, false, 1, now()),
    (3, 3, 45.00, true, 2, now()),
    (4, 4, 200.00, false, 2, now());