package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.DashboardResponse;
import org.platform.repair.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse get() {
        return dashboardService.getDashboard();
    }
}