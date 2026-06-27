package ralf2oo2.betterf3.tracking;

import java.util.Map;

public record AllocationTrackerSummary(
        Map<String, Double> ratesByThread,
        double totalAllocationRate
) {}
