package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.DashboardResponse;
import org.platform.repair.enums.RepairStatus;
import org.platform.repair.repository.CustomerRepository;
import org.platform.repair.repository.InvoiceRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.security.TenantContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RepairRepository repairRepository;
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;

    public DashboardResponse getDashboard() {
        Long shopId = TenantContext.get();

        long totalRepairs = repairRepository.countByShopId(shopId);
        long totalCustomers = customerRepository.countByShopId(shopId);

        long pending = repairRepository.countByShopIdAndStatus(shopId, RepairStatus.RECEIVED);
        long inProgress = repairRepository.countByShopIdAndStatus(shopId, RepairStatus.IN_REPAIR);
        long delivered = repairRepository.countByShopIdAndStatus(shopId, RepairStatus.DELIVERED);
        long waitingParts =
                repairRepository.countByShopIdAndStatus(
                        shopId,
                        RepairStatus.WAITING_PARTS
                );

        long ready =
                repairRepository.countByShopIdAndStatus(
                        shopId,
                        RepairStatus.READY
                );
        LocalDate today = LocalDate.now();

        long todayRepairs =
                repairRepository.countToday(
                        shopId,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                );
        BigDecimal todayRevenue = invoiceRepository.sumTodayRevenue(shopId);
        BigDecimal totalRevenue = invoiceRepository.sumPaidInvoices(shopId);
        BigDecimal unpaidRevenue = invoiceRepository.sumUnpaidInvoices(shopId);

        Map<String, Long> statusCount = new HashMap<>();

        for (RepairStatus status : RepairStatus.values()) {
            statusCount.put(
                    status.name(),
                    repairRepository.countByShopIdAndStatus(shopId, status)
            );
        }

        return new DashboardResponse(
                totalRepairs,
                totalCustomers,

                todayRepairs,

                pending,
                waitingParts,
                inProgress,
                ready,
                delivered,

                todayRevenue,
                totalRevenue,
                unpaidRevenue,

                statusCount
        );
    }
}