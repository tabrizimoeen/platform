package org.platform.repair.service;

import org.platform.repair.enums.RepairStatus;

import java.util.Map;
import java.util.Set;

public final class RepairStatusValidator {

    private static final Map<RepairStatus, Set<RepairStatus>> ALLOWED =
            Map.of(

                    RepairStatus.RECEIVED,
                    Set.of(
                            RepairStatus.DIAGNOSED,
                            RepairStatus.CANCELLED
                    ),

                    RepairStatus.DIAGNOSED,
                    Set.of(
                            RepairStatus.WAITING_PARTS,
                            RepairStatus.IN_REPAIR,
                            RepairStatus.CANCELLED
                    ),

                    RepairStatus.WAITING_PARTS,
                    Set.of(
                            RepairStatus.IN_REPAIR,
                            RepairStatus.CANCELLED
                    ),

                    RepairStatus.IN_REPAIR,
                    Set.of(
                            RepairStatus.READY,
                            RepairStatus.CANCELLED
                    ),

                    RepairStatus.READY,
                    Set.of(
                            RepairStatus.DELIVERED
                    ),

                    RepairStatus.DELIVERED,
                    Set.of(),

                    RepairStatus.CANCELLED,
                    Set.of()
            );

    private RepairStatusValidator() {
    }

    public static boolean canMove(
            RepairStatus current,
            RepairStatus next
    ) {
        return ALLOWED
                .getOrDefault(current, Set.of())
                .contains(next);
    }
}