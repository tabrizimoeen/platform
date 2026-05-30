package org.platform.repair.enums;

public enum RepairStatus {

    RECEIVED("دریافت شده"),
    DIAGNOSED("عیب‌یابی شده"),
    WAITING_PARTS("در انتظار قطعه"),
    IN_REPAIR("در حال تعمیر"),
    READY("آماده تحویل"),
    DELIVERED("تحویل داده شده"),
    CANCELLED("لغو شده");

    private final String labelFa;

    RepairStatus(String labelFa) {
        this.labelFa = labelFa;
    }

    public String getLabelFa() {
        return labelFa;
    }
}