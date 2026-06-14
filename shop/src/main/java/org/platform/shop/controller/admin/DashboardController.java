package org.platform.shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.dashboard.DashboardResponse;
import org.platform.shop.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard() {

        return dashboardService
                .getDashboard();
    }
}