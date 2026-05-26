package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.repository.RepairRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.platform.repair.enums.RepairStatus.*;
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class DashboardController {
    private final RepairRepository repairRepository;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        Map<String, Object> res = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        res.put("totalToday", repairRepository.countToday(start, end));
        res.put("received", repairRepository.countByStatus(RECEIVED));
        res.put("inRepair", repairRepository.countByStatus(IN_REPAIR));
        res.put("ready", repairRepository.countByStatus(READY));

        return res;
    }
}
