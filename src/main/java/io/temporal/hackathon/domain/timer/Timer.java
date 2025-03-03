package io.temporal.hackathon.domain.timer;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;

public class Timer {
    private final Logger logger = Workflow.getLogger(Timer.class);

    private long sleepTime;
    private boolean sleepTimeUpdated;

    public void sleep(long sleepTime) {
        logger.info("Sleep until: {}", Instant.ofEpochMilli(sleepTime));
        this.sleepTime = sleepTime;
        while (true) {
            sleepTimeUpdated = false;
            var sleepInterval = Duration.ofMillis(this.sleepTime - Workflow.currentTimeMillis());
            logger.info("Going to sleep for {}", sleepInterval);
            if (!Workflow.await(sleepInterval, () -> sleepTimeUpdated)) {
                break;
            }
        }
        logger.info("Sleep until completed");
    }

    public void updateWakeUpTime(long updateSleepTime) {
        logger.info("Update sleep time: {}", Instant.ofEpochMilli(updateSleepTime));
        this.sleepTime = updateSleepTime - Workflow.currentTimeMillis();
        this.sleepTimeUpdated = true;
    }

    public long getSleepTime() {
        return sleepTime;
    }
}
