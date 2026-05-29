package org.platform.repair.controller;

import lombok.RequiredArgsConstructor;
import org.platform.repair.dto.TrackingResponse;
import org.platform.repair.service.RepairService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class TrackingController {

    private final RepairService repairService;

    @GetMapping("/track/{id}")
    public TrackingResponse track(
            @PathVariable Long id
    ) {
        return repairService.track(id);
    }
}