package ralf2oo2.betterf3.tracking;

import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedThread;
import jdk.jfr.consumer.RecordingStream;
import ralf2oo2.betterf3.Betterf3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AllocationTracker {
    private final Map<String, long[]> lastSnapshots = new ConcurrentHashMap<>();
    private final Map<String, Double> activeRates = new ConcurrentHashMap<>();
    private RecordingStream stream;

    public void start() {
        try {
            this.stream = new RecordingStream();
            this.stream.enable("jdk.ThreadAllocationStatistics").withPeriod(java.time.Duration.ofSeconds(1));
            this.stream.onEvent("jdk.ThreadAllocationStatistics", this::handleAllocationEvent);
            this.stream.startAsync();

        } catch (Exception e) {
            Betterf3.LOGGER.error("Something went wrong setting up the AllocationTracker");
            Betterf3.LOGGER.error(e);
        }
    }

    private void handleAllocationEvent(RecordedEvent event) {
        RecordedThread recordedThread = event.getThread("thread");
        String threadName = (recordedThread != null && recordedThread.getJavaName() != null) ? recordedThread.getJavaName() : "unknown";

        long totalBytes = event.getLong("allocated");
        long now = event.getEndTime().toEpochMilli();

        long[] last = lastSnapshots.get(threadName);
        if (last != null) {
            long prevBytes = last[0];
            long prevTime = last[1];

            double durationSec = (now - prevTime) / 1000.0;
            if (durationSec > 0) {
                long allocatedDiff = totalBytes - prevBytes;
                double mbPerSecond = (allocatedDiff / durationSec) / (1024.0 * 1024.0);

                activeRates.put(threadName, mbPerSecond);
            }
        }

        lastSnapshots.put(threadName, new long[]{totalBytes, now});
    }

    public AllocationTrackerSummary getLatestSummary() {
        Map<String, Double> ratesCopy = new HashMap<>(this.activeRates);
        double total = ratesCopy.values().stream().mapToDouble(Double::doubleValue).sum();

        return new AllocationTrackerSummary(ratesCopy, total);
    }
}
