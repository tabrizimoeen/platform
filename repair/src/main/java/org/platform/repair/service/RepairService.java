package org.platform.repair.service;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.CreateRepairRequest;
import org.platform.repair.dto.NotificationMessage;
import org.platform.repair.dto.TrackingResponse;
import org.platform.repair.entity.Customer;
import org.platform.repair.entity.RepairLog;
import org.platform.repair.entity.RepairOrder;
import org.platform.repair.entity.RepairShop;
import org.platform.repair.enums.NotificationType;
import org.platform.repair.enums.RepairStatus;
import org.platform.repair.repository.CustomerRepository;
import org.platform.repair.repository.RepairLogRepository;
import org.platform.repair.repository.RepairRepository;
import org.platform.repair.repository.RepairShopRepository;
import org.platform.repair.security.TenantContext;
import org.platform.repair.service.notification.NotificationDispatcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RepairService {

    private static final Logger log = LoggerFactory.getLogger(RepairService.class);
    private static final List<RepairStatus> CLOSED = List.of(
            RepairStatus.DELIVERED,
            RepairStatus.CANCELLED
    );

    private final RepairRepository repairRepository;
    private final RepairLogRepository logRepository;
    private final RepairShopRepository repairShopRepository;
    private final CustomerRepository customerRepository;
    private final NotificationDispatcherService notificationDispatcherService;

    public RepairOrder create(CreateRepairRequest req) {
        if (req.getCustomerName() == null || req.getCustomerName().isBlank()) {
            throw new RuntimeException("نام مشتری الزامی است");
        }
        if (req.getPhone() == null || req.getPhone().isBlank()) {
            throw new RuntimeException("شماره مشتری الزامی است");
        }
        if (req.getDeviceModel() == null || req.getDeviceModel().isBlank()) {
            throw new RuntimeException("مدل دستگاه الزامی است");
        }

        Long shopId = requireShopId();
        RepairShop shop = repairShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("تعمیرگاه پیدا نشد"));

        String phone = req.getPhone().trim();
        String imei = normalizeImei(req.getImei());
        assertImeiAvailable(shopId, imei);

        Customer customer = customerRepository.findByPhoneAndShopId(phone, shopId)
                .orElseGet(() -> {
                    Customer created = new Customer();
                    created.setPhone(phone);
                    created.setShop(shop);
                    return created;
                });
        customer.setName(req.getCustomerName().trim());
        customer = customerRepository.save(customer);

        RepairOrder repair = new RepairOrder();
        repair.setCustomer(customer);
        repair.setShop(shop);
        repair.setDeviceModel(req.getDeviceModel().trim());
        repair.setProblemDescription(req.getProblemDescription());
        repair.setEstimatedCost(req.getEstimatedCost());
        repair.setImei(imei);
        repair.setStatus(RepairStatus.RECEIVED);
        repair.setTrackingCode(newTrackingCode());

        RepairOrder saved = repairRepository.save(repair);
        addLog(saved.getId(), "دستگاه تحویل گرفته شد", RepairStatus.RECEIVED.name());
        return saved;
    }

    public Page<RepairOrder> findAll(Pageable page) {
        return repairRepository.findByShopIdOrderByIdDesc(requireShopId(), page);
    }

    public RepairOrder updateStatus(Long id, RepairStatus status) {
        if (status == null) {
            throw new RuntimeException("وضعیت الزامی است");
        }

        Long shopId = requireShopId();
        RepairOrder order = repairRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("سفارش تعمیر پیدا نشد"));

        if (order.getStatus() == status) {
            return order;
        }
        if (!RepairStatusValidator.canMove(order.getStatus(), status)) {
            throw new IllegalStateException("تغییر وضعیت انتخاب‌شده مجاز نیست");
        }

        order.setStatus(status);
        RepairOrder saved = repairRepository.save(order);
        addLog(id, "وضعیت به " + status.getLabelFa() + " تغییر کرد", status.name());
        notifyCustomer(saved, status);
        return saved;
    }

    public RepairOrder findById(Long id) {
        return repairRepository.findByIdAndShopId(id, requireShopId())
                .orElseThrow(() -> new RuntimeException("سفارش تعمیر پیدا نشد"));
    }

    @Transactional(readOnly = true)
    public TrackingResponse track(Long repairId) {
        RepairOrder repair = repairRepository.findByIdAndShopId(repairId, requireShopId())
                .orElseThrow(() -> new RuntimeException("سفارش تعمیر پیدا نشد"));
        return toTracking(repair);
    }

    @Transactional(readOnly = true)
    public TrackingResponse trackByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new RuntimeException("کد پیگیری نامعتبر است");
        }
        RepairOrder repair = repairRepository.findByTrackingCodeIgnoreCase(code.trim())
                .orElseThrow(() -> new RuntimeException("سفارش تعمیر پیدا نشد"));
        return toTracking(repair);
    }

    public RepairOrder createOrGetCustomerAndCreateRepair(
            String customerName,
            String phone,
            String deviceModel,
            String problemDescription,
            BigDecimal estimatedCost,
            String imei
    ) {
        CreateRepairRequest request = new CreateRepairRequest();
        request.setCustomerName(customerName);
        request.setPhone(phone);
        request.setDeviceModel(deviceModel);
        request.setProblemDescription(problemDescription);
        request.setEstimatedCost(estimatedCost);
        request.setImei(imei);
        return create(request);
    }

    public RepairOrder findByImei(String imei) {
        return repairRepository.findByImeiAndShopId(imei, requireShopId())
                .orElseThrow(() -> new RuntimeException("سفارش تعمیر پیدا نشد"));
    }

    public List<RepairOrder> searchByImei(String imei) {
        return repairRepository.findTop10ByShopIdAndImeiContainingIgnoreCaseOrderByIdDesc(
                requireShopId(),
                imei
        );
    }

    private void notifyCustomer(RepairOrder order, RepairStatus status) {
        String phone = order.getCustomer() == null ? null : order.getCustomer().getPhone();
        if (phone == null || phone.isBlank()) {
            return;
        }
        try {
            notificationDispatcherService.send(
                    NotificationType.SMS,
                    new NotificationMessage(
                            phone,
                            "وضعیت تعمیر",
                            order.getCustomer().getName()
                                    + " عزیز، وضعیت دستگاه "
                                    + order.getDeviceModel()
                                    + " به «"
                                    + status.getLabelFa()
                                    + "» تغییر کرد. کد پیگیری: "
                                    + order.getTrackingCode(),
                            Map.of()
                    )
            );
        } catch (Exception ex) {
            log.warn("اعلان وضعیت ارسال نشد: {}", ex.getMessage());
        }
    }

    private void addLog(Long repairId, String message, String status) {
        RepairShop shop = repairShopRepository.findById(requireShopId()).orElseThrow();
        logRepository.save(RepairLog.builder()
                .repairId(repairId)
                .message(message)
                .status(status)
                .shop(shop)
                .createdAt(java.time.LocalDateTime.now())
                .build());
    }

    private void assertImeiAvailable(Long shopId, String imei) {
        if (imei == null) {
            return;
        }
        if (repairRepository.existsByShopIdAndImeiAndStatusNotIn(shopId, imei, CLOSED)) {
            throw new RuntimeException("این IMEI روی یک سفارش باز دیگر ثبت شده است");
        }
    }

    private String newTrackingCode() {
        for (int attempt = 0; attempt < 5; attempt++) {
            String code = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            if (!repairRepository.existsByTrackingCode(code)) {
                return code;
            }
        }
        throw new RuntimeException("ساخت کد پیگیری ممکن نشد");
    }

    private TrackingResponse toTracking(RepairOrder repair) {
        return new TrackingResponse(
                repair.getId(),
                repair.getTrackingCode(),
                repair.getCustomer().getName(),
                repair.getDeviceModel(),
                repair.getStatus().name(),
                repair.getStatus().getLabelFa(),
                repair.getEstimatedCost()
        );
    }

    private Long requireShopId() {
        Long shopId = TenantContext.get();
        if (shopId == null) {
            throw new RuntimeException("تعمیرگاه کاربر مشخص نیست");
        }
        return shopId;
    }

    private String normalizeImei(String imei) {
        if (imei == null || imei.isBlank()) {
            return null;
        }
        return imei.trim();
    }
}
