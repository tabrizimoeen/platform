package org.platform.repair.security;


public class TenantContext {

    private static final ThreadLocal<Long> SHOP_ID =
            new ThreadLocal<>();

    public static void set(Long shopId) {
        SHOP_ID.set(shopId);
    }

    public static Long get() {
        return SHOP_ID.get();
    }

    public static void clear() {
        SHOP_ID.remove();
    }
}
